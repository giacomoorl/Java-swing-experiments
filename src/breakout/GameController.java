package breakout;

public class GameController {

    private GameState state;
    private RLManager rlManager;
    private StateEncoder encoder;

    private static final int LENGTH = 1600;

    enum Mode {
        HUMAN,
        AI_TRAINING,
        AI_PLAY
    }

    private Mode mode = Mode.HUMAN;

    public GameController(GameState state) {
        System.out.println("Controller creato");
        this.state = state;
        this.encoder = new StateEncoder();
    }

    public void setMode(Mode m) {
        this.mode = m;
    }

    public boolean isHuman() {
        return mode == Mode.HUMAN;
    }

    public boolean isAITraining() {
        return mode == Mode.AI_TRAINING;
    }

    public boolean isAIPlay() {
        return mode == Mode.AI_PLAY;
    }

    public void setRLManager(RLManager rlManager) {
        this.rlManager = rlManager;
    }

    public void moveLeft() {
        state.getPaddle().moveSx();
    }

    public void moveRight(int limit) {
        state.getPaddle().moveDx(limit);
    }

    public GameState getState() {
        return state;
    }

    private boolean allBricks(Brick[][] bricks) {
        for (Brick[] row : bricks)
            for (Brick b : row)
                if (!b.isDestroy())
                    return false;
        return true;
    }

    public void update() {

        Ball ball = state.getBall();
        Paddle paddle = state.getPaddle();
        Brick[][] bricks = state.getBricks();

        int stateRL = encoder.encode(state);
        int action = 0;
        int reward = 0;

        // ?SCELTA AZIONE (AI)
        if (isAITraining()) {
            action = rlManager.chooseAction(stateRL);
        }
        else if(isAIPlay()){
                action = rlManager.chooseBestAction(stateRL); // NO random 
        }
        if(action == 1)
                moveLeft();
        else if (action == 2)
                moveRight(LENGTH);

        // ⚙️ LOGICA GIOCO (SEMPRE)
        ball.move();
        ball.bounceWall(LENGTH);

        // collisione paddle
        if (ball.getRettangle().intersects(paddle.getRettangle())) {
            ball.bouncePaddle(paddle);
            reward += 5;
        }

        // collisione mattoni
        for (int i = 0; i < bricks.length; i++) {
            for (int j = 0; j < bricks[i].length; j++) {
                Brick brick = bricks[i][j];

                if (!brick.isDestroy() && ball.getRettangle().intersects(brick.rettangle())) {
                    brick.destroy();
                    ball.reverseDirectionY();
                    state.increasesPoints(10);
                    reward += 20;
                }
            }
        }

        // distanza paddle-ball
        double centerBall = ball.getX() + ball.getDiameter() / 2.0;
        double centerPaddle = paddle.getX() + paddle.getLength() / 2;
        double distance = Math.abs(centerBall - centerPaddle);

        reward -= Math.min(5, distance * 0.01);

        // perdita
        boolean lost = false;
        if (ball.getY() > 800) {
            reward -= 200;
            lost = true;
        }

        int newState = encoder.encode(state);

        // 🧠 LEARNING SOLO IN TRAINING
        if (isAITraining()) {
            if (lost)
                rlManager.updateLearning(stateRL, action, reward, -1);
            else
                rlManager.updateLearning(stateRL, action, reward, newState);
        }

        // fine episodio
        if (lost) {
            if (isAITraining())
                rlManager.endEpisode();

            state.reset();
        }

        // livello completato
        if (allBricks(bricks)) {
            state.nextLevel();
            ball.increasesSpeed(state.getLevel());
            reward += 100;
        }
    }
}