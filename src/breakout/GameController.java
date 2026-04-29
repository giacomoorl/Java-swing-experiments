package breakout;
/*
* CLASSE GAMECONTROLLER , GESTISCE IL GIOCO 
*/
public class GameController{
    // CAMPI DATI 
    private GameState state;
    private RLManager rlManager;
    private StateEncoder encoder;
    private boolean gameOver;
    private static final int LENGTH = 1600;
    enum Mode{
        HUMAN,
        AI_TRAINING,
        AI_PLAY
    }
    private Mode mode = Mode.HUMAN;
    // COSTRUTTORE 
    public GameController(GameState state) {
        System.out.println("Controller creato");
        this.state = state;
        this.encoder = new StateEncoder();
    }
    // INIZIA UNA NUOVA PARTITA
    public void startNewGame() {
        gameOver = false;
        state.reset(); 
    }
    // IMPOSTA LA MODALITÀ DI GIOCO
    public void setMode(Mode m) {
        this.mode = m;
    }
    // CONTROLLA SE STA GIOCANDO L'UMANO
    public boolean isHuman() {
        return mode == Mode.HUMAN;
    }
    // CONTROLLA SE L'AI SI STA ALLENANDO
    public boolean isAITraining() {
        return mode == Mode.AI_TRAINING;
    }
    // CONTROLLA SE STA GIOCANDO L'AI
    public boolean isAIPlay() {
        return mode == Mode.AI_PLAY;
    }
    // CONTROLLA SE LA PARTITA È FINITA
    public boolean isGameOver(){
        return gameOver;
    }
    // IMPOSTA L'RLMANAGER
    public void setRLManager(RLManager rlManager){
        System.out.println("RLManager impostato da Controller");
        this.rlManager = rlManager;
    }
    // MUOVE PADDLE A SINISTRA
    public void moveLeft() {
        state.getPaddle().moveSx();
    }
    // MUOVE PADDLE A DESTRA 
    public void moveRight(int limit) {
        state.getPaddle().moveDx(limit);
    }
    // SI FA DARE STATO
    public GameState getState() {
        return state;
    }
    // CONTROLLA SE I MATTONCINI SONO DISTRUTTI TUTTI
    private boolean allBricks(Brick[][] bricks) {
        for (Brick[] row : bricks)
            for (Brick b : row)
                if (!b.isDestroy())
                    return false;
        return true;
    }
    // AGGIORNA IL GIOCO
    public void update() {
        Ball ball = state.getBall();
        Paddle paddle = state.getPaddle();
        if (isHuman()) {
            paddle.setSpeed(25); 
        } 
        else if (isAITraining() || isAIPlay()) {
            paddle.increasesSpeed(state.getLevel());
        } 
     
        Brick[][] bricks = state.getBricks();

        int stateRL = encoder.encode(state);
        int action = 0;
        int reward = 0;
      
        // SCELTA AZIONE (AI)
        if (isAITraining()) {
            action = rlManager.chooseAction(stateRL);
        }
        else if(isAIPlay()){
                action = rlManager.chooseBestAction(stateRL); // NO random 
        }
        if(isAITraining() || isAIPlay()) {
            if (action == 1)
                moveLeft();
            else if(action == 2)
                moveRight(LENGTH);
        }
        // MUOVE BALL E CONTROLLA SE LA BALL COLLIDE WITH WALL
        ball.move();
        ball.bounceWall(LENGTH);

        // CONTROLLA LA COLLISIONE TRA PADDLE E BALL
        if (ball.getRettangle().intersects(paddle.getRettangle())) {
            ball.bouncePaddle(paddle);
            reward += 5;
        }
        // CALCOLA LA COLLISIONE WITH BRICKS
        for (int i = 0; i < bricks.length; i++) {
            for (int j = 0; j < bricks[i].length; j++) {
                Brick brick = bricks[i][j];

                if (!brick.isDestroy() && ball.getRettangle().intersects(brick.rettangle())) {
                    brick.destroy();
                    ball.reverseDirectionY();
                    state.increasesPoints(10);
                    reward += 60;
                }
            }
        }
        // CALCOLA LA DISTANZA TRA IL PADDLE E LA BALL
        double centerBall = ball.getX() + ball.getDiameter() / 2.0;
        double centerPaddle = paddle.getX() + paddle.getLength() / 2;
        double distance = Math.abs(centerBall - centerPaddle);
        // PUNIZIONE SE IL PADDLE E LA BALL SONO DISTANTI
        reward -= Math.min(5, distance * 0.01);
        // PUNIZIONE SE PERDE LA PARTITA L'AI
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
        // FINE PARTITA
        if (lost) {
            if (isHuman()){
                gameOver = true;
                state.reset();
                return;
            }
            if(isAITraining()){
                rlManager.endEpisode();
                state.reset();
                return;
            }
            if(isAIPlay()) {
                state.reset();
                return;
            }
        }
        // LIVELLO COMPLETATO
        if (allBricks(bricks)) {
            state.nextLevel();
            ball.increasesSpeed(state.getLevel());
            reward += 120;
        }
    }
}