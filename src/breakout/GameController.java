package breakout;
/*
* CLASSE CONTROLLER
*/
public class GameController {
    // CAMPI DATI , RIFERIMENTI A STATE , RLMANAGER E ENCODER
    private GameState state;
    private RLManager rlManager;
    private StateEncoder encoder;
    // LARGHEZZA DEL PANNELLO DI GIOCO
    private static final int LENGTH = 1600;
    // MODALITÀ DI GIOCO , SE UMANA O AI_TRAINING O AI_PLAY
    enum Mode {
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
    // IMPOSTA MODALITÀ DI GIOCO
    public void setMode(Mode m) {
        this.mode = m;
    }
    // RITORNA SE È UMANO
    public boolean isHuman() {
        return mode == Mode.HUMAN;
    }
    // RITORNA SE È AI_TRAINING
    public boolean isAITraining() {
        return mode == Mode.AI_TRAINING;
    }
    // RITORNA SE È AI_PLAY
    public boolean isAIPlay() {
        return mode == Mode.AI_PLAY;
    }
    // RITORNA LA MODALITÀ
    public Mode getMode() {
        return mode;
    }
    // IMPOSTA  RLMANAGER 
    public void setRLManager(RLManager rlManager) {
        this.rlManager = rlManager;
    }
    // MUOVEPADDLE A SINISTRA
    public void moveLeft() {
        state.getPaddle().moveSx();
    }
    // MUOVEPADDLE A SINISTRA
    public void moveRight(int limit) {
        state.getPaddle().moveDx(limit);
    }
    // RITORNA LO STATO DEL GIOCO
    public GameState getState() {
        return state;
    }
    // RITORNA SE I MATTONI SONO TUTTI DISTRUTTI
    private boolean allBricks(Brick[][] bricks) {
        for (Brick[] row : bricks)
            for (Brick b : row)
                if (!b.isDestroy())
                    return false;
        return true;
    }
    // AGGIORNA IL GIOCO
    public void update() {
        // SI FA DARE BALL , PADDLE E MATTONCINI DALLO STATE
        Ball ball = state.getBall();
        Paddle paddle = state.getPaddle();
        Brick[][] bricks = state.getBricks();
        // SI FA DARE LO STATO DEL AGENTE
        int stateRL = encoder.encode(state);
        int action = 0;
        int reward = 0;
        // CONTROLLA SE È MODALITÀ AI
        if(isAITraining()) {
            // DICE ALL'RLMANAGER DI SCIEGLERE L'AZIONE
            action = rlManager.chooseAction(stateRL);
            if(action == 1)
                moveLeft();
            else 
                if(action == 2)
                moveRight(LENGTH);
        }
        // MUOVE BALL 
        ball.move();
        ball.bounceWall(LENGTH);
        // CONTROLLA SE PADDLE E BALL COLLIDONO
        if (ball.getRettangle().intersects(paddle.getRettangle())) {
            ball.bouncePaddle(paddle);
            reward += 20;
        }
        // SI OCCUPA DELLA COLLISIONE COI MATTONCINI E LA BALL
        for (int i = 0; i < bricks.length; i++) {
            for (int j = 0; j < bricks[i].length; j++) {
                Brick brick = bricks[i][j];
                if (!brick.isDestroy() && ball.getRettangle().intersects(brick.rettangle())) {
                    // DISTRUGGE MATTONCINO
                    brick.destroy();
                    // DICE A BALL DI INVERTIRE LA DIREZIONE
                    ball.reverseDirectionY();
                    // AUMENTA IL PUNTEGGIO
                    state.increasesPoints(10);
                    // AUMENTA LA REWARD
                    reward += 20;
                }
            }
        }
        // SI OCCUPA DI CONTROLLARE LA DISTANZA TRA PADDLE E BALL
        double centerBall = ball.getX() + 10;
        double centerPaddle = paddle.getX() + paddle.getLength() / 2;
        // CALCOLA LA DISTANZA
        double distance = Math.abs(centerBall - centerPaddle);
        // PICCOLA PENALITÀ PESATA DALLA DISTANZA
        reward -= distance * 0.002;
        // CONTROLLA SE È FINITA LA PARTITA
        boolean lost = false;
        if(ball.getY() > 800) {
            reward -= 50;
            lost = true;
        }
        // CONTROLLA IL NUOVO STATO
        int newState = encoder.encode(state);
        // CONTROLLA SE È MODALITÀ AI TRAINING
        // E SE SI DICE AL MANAGERAI DI AGGIORNARE L'APPRENDIMENTO
        if (isAITraining()){
            rlManager.updateLearning(stateRL, action, reward, newState);
        }
        // GESTISCE IL CASO DI FINE PARTITA AI
        if (lost){
            if (isAITraining())
                rlManager.endEpisode();
            state.reset();
            if (isHuman()) {
                System.out.println("Sistemremo ");
            }
        }
        // CONTROLLA SE TUTTI I MATTONCINI SONO DISTRUTTI 
        if (allBricks(bricks)){
            // SE SI , DICE ALLO STATO DI PASSARE AL PROSSIMO LIVELLO
            state.nextLevel();
            // ALLA BALL DI AUMENTARE LA VELOCITÀ
            ball.increasesSpeed(state.getLevel());
            // AUMENTA LA REWARD
            reward += 50;
        }
    }
}