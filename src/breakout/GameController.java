package breakout;

public class GameController {

    int reward, action, stateRL, newStateRL;
    boolean lost, human, aiTraining, aiPlay;

    private CollisionManager collisionManager;
    private RewardSystem rewardSystem;
    private GameState state;
    private RLManager rlManager;
    private StateEncoder encoder;
    private GameLoop loop;

    private static final int LENGTH = 1600;

    public GameController(GameState state) {

        this.state = state;
        this.encoder = new StateEncoder();
        this.collisionManager = new CollisionManager();
        this.rewardSystem = new RewardSystem();

        reward = 0;
        lost = false;

        human = false;
        aiTraining = false;
        aiPlay = false;
    }

    public void setLoop(GameLoop loop) {
        this.loop = loop;
    }

    public void setHuman() { 
        human = true;
        aiTraining = false;
        aiPlay = false;
    }
    public void setAITraining() { 
        aiTraining = true;
        human = false;
        aiPlay=false;
    }
    public void setAIPlay() { 
        aiPlay = true; 
        aiTraining = false;
        human = false;
    }

    public void setRLManager(RLManager rlManager) {
        this.rlManager = rlManager;
    }

    public GameState getState() {
        return state;
    }

    public void moveLeft() {
        state.getPaddle().moveSx();
    }

    public void moveRight() {
        state.getPaddle().moveDx(LENGTH);
    }

    // =========================
    // LOOP PRINCIPALE
    // =========================
    public void update() {

        reward = 0;

        Ball ball = state.getBall();
        Paddle paddle = state.getPaddle();
        Brick[][] bricks = state.getBricks();

        ball.move();
        ball.bounceWall(LENGTH);
        
        if(human){
            if (allBricks(bricks)){
                state.nextLevel();
                ball.increasesSpeed(state.getLevel());
            }
        }

        // =========================
        // COLLISIONI + REWARD
        // =========================
        reward += handleCollisions(ball, paddle, bricks);

        // =========================
        // AI TRAINING
        // =========================
        if (aiTraining) {

            stateRL = encoder.encode(state);

            action = rlManager.chooseAction(stateRL);

            if (action == 1) moveLeft();
            if (action == 2) moveRight();

            if (allBricks(bricks)) {
                state.nextLevel();
                ball.increasesSpeed(state.getLevel());
                reward += rewardSystem.levelReward();
            }

            newStateRL = encoder.encode(state);

            rlManager.updateLearning(stateRL, action, reward, newStateRL);
        }

        // =========================
        // AI PLAY
        // =========================
        if (aiPlay) {

            stateRL = encoder.encode(state);

            action = rlManager.chooseBestAction(stateRL);

            if (action == 1) moveLeft();
            if (action == 2) moveRight();

            if (allBricks(bricks)) {
                state.nextLevel();
                ball.increasesSpeed(state.getLevel());
            }
        }

        // =========================
        // GAME OVER
        // =========================
        if (ball.getY() > 800) {
            lost = true;
        }

        if (lost) {
            handleGameOver();
        }
    }

    // =========================
    // COLLISIONI
    // =========================
    private int handleCollisions(Ball ball, Paddle paddle, Brick[][] bricks) {

        int r = 0;

        if (collisionManager.paddleCollision(ball, paddle)) {
            ball.bouncePaddle(paddle);
            r += rewardSystem.paddleReward();
        }

        for (int i = 0; i < bricks.length; i++) {
            for (int j = 0; j < bricks[i].length; j++) {

                Brick brick = bricks[i][j];

                if (collisionManager.brickCollision(ball, brick)) {
                    brick.destroy();
                    ball.reverseDirectionY();
                    state.increasesPoints(10);

                    r += rewardSystem.brickReward();
                }
            }
        }

        return r;
    }

    // =========================
    // GAME OVER 
    // =========================
    private void handleGameOver() {

        if (human) {
            state.reset();
            if (loop != null) loop.stop();
        }

        else if (aiTraining) {
            rlManager.endEpisode();
            state.reset();
        }

        else if (aiPlay) {
            state.reset();
        }

        lost = false;
    }

    // =========================
    // BRICKS CHECK
    // =========================
    private boolean allBricks(Brick[][] bricks) {

        for (int i = 0; i < bricks.length; i++) {
            for (int j = 0; j < bricks[i].length; j++) {

                if (!bricks[i][j].isDestroy()) {
                    return false;
                }
            }
        }
        return true;
    }
}