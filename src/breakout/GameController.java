package breakout;
/*
* CLASSE GAMECONTROLLER , RESPONSABILE DEL CONTROLLO DEI COMOPONENTI DEL GIOCO
*/
public class GameController{
    // CAMPI DATI
    int reward, action, stateRL, newStateRL;
    boolean lost, human, aiTraining, aiPlay;
    private CollisionManager collisionManager;
    private RewardSystem rewardSystem;
    private GameState state;
    private RLManager rlManager;
    private StateEncoder encoder;
    private GameLoop loop;
    private static final int LENGTH = 1600;
    // COSTRUTTORE
    public GameController(GameState state) {
        System.out.println("Controller creato");
        this.state = state;
        this.encoder = new StateEncoder();
        this.collisionManager = new CollisionManager();
        this.rewardSystem = new RewardSystem();
        this.reward = 0;
        this.lost = false;
        this.human = false;
        this.aiTraining = false;
        this.aiPlay = false;
    }
    // IMPOSTA IL LOOP DI GIOCO
    public void setLoop(GameLoop loop) {
        this.loop = loop;
    }
    // IMPOSTA LA MODALITÀ UMANA
    public void setHuman() { 
        human = true;
    }
    // IMPOSTA LA MODALITÀ ARTIFICIALE IN ADDESTRAMENTO
    public void setAITraining() { 
        aiTraining = true;
    }
    // IMPOSTA LA MODALITÀ ARTIFICIALE IN GIOCO AUTONOMO
    public void setAIPlay() { 
        aiPlay = true; 
    }
    // IMPOSTA L'RLMANAGER
    public void setRLManager(RLManager rlManager) {
        this.rlManager = rlManager;
    }
    // GETTERS
    public GameState getState() {
        return state;
    }
    public void moveLeft() {
        state.getPaddle().moveSx();
    }
    public void moveRight() {
        state.getPaddle().moveDx(LENGTH);
    }
    // AGGIORNA IL GIOCO
    public void update(){

        Ball ball = state.getBall();
        Paddle paddle = state.getPaddle();
        Brick[][] bricks = state.getBricks();

        ball.move();
        ball.bounceWall(LENGTH);
        // GIOCA L'UMANO
        if(human){
            if(allBricks(bricks)){
                state.nextLevel();
                ball.increasesSpeed(state.getLevel());
            }
        }
        // AI TRAINING
        if(aiTraining){
            stateRL = encoder.encode(state);
            action = rlManager.chooseAction(stateRL);
            if(action == 1) 
                moveLeft();
            if(action == 2) 
                moveRight();

            reward=0;
            System.out.println("Reward iniziale");
            reward += handleCollisions(ball, paddle, bricks);
            if(allBricks(bricks)){
                state.nextLevel();
                ball.increasesSpeed(state.getLevel());
                reward += rewardSystem.levelReward();
            }
            newStateRL = encoder.encode(state);
            rlManager.updateLearning(stateRL, action, reward, newStateRL);
        }
        // AI PLAY
        if(aiPlay){
            stateRL = encoder.encode(state);
            action = rlManager.chooseBestAction(stateRL);

            if(action == 1) 
                moveLeft();
            if(action == 2) 
                moveRight();

            if(allBricks(bricks)){
                state.nextLevel();
                ball.increasesSpeed(state.getLevel());
            }
        }
        // GAME OVER
        if(ball.getY() > 800){
            lost = true;
        }
        if(lost){
            handleGameOver();
        }
    }
    // CONTROLLA COLLISIONI
    private int handleCollisions(Ball ball, Paddle paddle, Brick[][] bricks){
        // TRA PADDLE E BALL
        if(collisionManager.paddleCollision(ball, paddle)){
            ball.bouncePaddle(paddle);
            reward += rewardSystem.paddleReward();
            System.out.println("Reward collisione paddle e ball");
        }
        // TRA BALL E BRICK
        for (int i = 0; i < bricks.length; i++){
            for (int j = 0; j < bricks[i].length; j++){
                Brick brick = bricks[i][j];
                if(collisionManager.brickCollision(ball, brick)){
                    brick.destroy();
                    ball.reverseDirectionY();
                    state.increasesPoints(10);
                    reward+= rewardSystem.brickReward();
                    System.out.println("Reward collisione ball e brick");
                }
            }
        }
        if(allBricks(bricks)){
            state.nextLevel();
            ball.increasesSpeed(state.getLevel());
            reward += rewardSystem.levelReward();
            System.out.println("Reward disturtti tutti i mattocini");
        }
        return reward;
    }
    // CONTROLLA SE LA PARTITA È FINITA
    private void handleGameOver(){
        if(human){
            state.reset();
            if (loop != null) 
                loop.stop();
        }
        else if(aiTraining){
            reward += rewardSystem.deathPenalty();
            System.out.println("Penalità persa la partita");
            rlManager.updateLearning(stateRL, action, reward, stateRL);
            rlManager.endEpisode();
            state.reset();
        }
        else if(aiPlay){
            state.reset();
        }
        lost = false;
    }
    // CONTROLLA SE TUTTI I BRICKS SONO STATI DISRUTTTI
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