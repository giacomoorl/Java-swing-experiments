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

    // ===== MODE MANAGEMENT =====
    public void setMode(Mode m) {
        this.mode = m;
    }

    public boolean isHuman() {
        return mode == Mode.HUMAN;
    }

    public boolean isAI() {
        return mode == Mode.AI_TRAINING || mode == Mode.AI_PLAY;
    }

    public Mode getMode() {
        return mode;
    }

    // ===== RL =====
    public void setRLManager(RLManager rlManager) {
        this.rlManager = rlManager;
    }

    // ===== MOVIMENTO PADDLE =====
    public void moveLeft() {
        state.getPaddle().moveSx();
    }

    public void moveRight(int limit) {
        state.getPaddle().moveDx(limit);
    }

    public GameState getState() {
        return state;
    }

    // ===== BRICKS CHECK =====
    private boolean allBricks(Brick[][] bricks) {
        for (Brick[] row : bricks)
            for (Brick b : row)
                if (!b.isDestroy())
                    return false;
        return true;
    }

    // ===== MAIN UPDATE =====
    public void update() {

        Ball ball = state.getBall();
        Paddle paddle = state.getPaddle();
        Brick[][] bricks = state.getBricks();

        int stateRL = encoder.encode(state);
        int action = 0;
        int reward = 0;

        // ================= AI CONTROL =================
        if (isAI() && rlManager != null) {

            action = rlManager.chooseAction(stateRL);

            if (action == 1)
                moveLeft();
            else if (action == 2)
                moveRight(LENGTH);
        }

        // ================= BALL =================
        ball.move();
        ball.bounceWall(LENGTH);

        // ================= PADDLE =================
        if (ball.rettangle().intersects(paddle.rettangle())) {
            ball.bouncePaddle(paddle);
            reward += 20;
        }

        // ================= BRICKS =================
        for (int i = 0; i < bricks.length; i++) {
            for (int j = 0; j < bricks[i].length; j++) {

                Brick brick = bricks[i][j];

                if (!brick.isDestroy() && ball.rettangle().intersects(brick.rettangle())) {
                    brick.destroy();
                    ball.reverseDirectionY();
                    state.increasesPoints(10);
                    reward += 20;
                }
            }
        }

        // ================= DISTANCE PENALTY =================
        double centerBall = ball.getX() + 10;
        double centerPaddle = paddle.getX() + paddle.getLength() / 2;
        double distance = Math.abs(centerBall - centerPaddle);

        reward -= distance * 0.002;

        // ================= GAME OVER =================
        boolean lost = false;

        if (ball.getY() > 800) {
            reward -= 50;
            lost = true;
        }

        int newState = encoder.encode(state);

        // ================= RL UPDATE =================
        if (isAI() && rlManager != null) {
            rlManager.updateLearning(stateRL, action, reward, newState);
        }

        // ================= EPISODE END =================
        if (lost) {
            if (isAI() && rlManager != null)
                rlManager.endEpisode();

            state.reset();
             if (isHuman()) {
               System.out.println("Sistemremo ");
            }
        }

        // ================= LEVEL =================
        if (allBricks(bricks)) {
            state.nextLevel();
            ball.increasesSpeed(state.getLevel());
            reward += 50;
        }
    }
}