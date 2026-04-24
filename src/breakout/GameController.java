package breakout;
/*
* Controller principale del gioco
*/
public class GameController {
    // CAMPI DATI
    private boolean aiAttiva = false;
    private GameState state;
    private static final int LENGTH = 1600;
    private static final int HEIGHT= 800;
    private RLManager rlManager;
    private StateEncoder encoder;
    // COSTRUTTORE
    public GameController(GameState state){
        System.out.println("Controller creato");
        this.state = state;
        this.encoder = new StateEncoder();
    }
    // IMPOSTA RLMANAGER
    public void setRLManager(RLManager rlManager){
        this.rlManager = rlManager;
    }
    // ATTIVA L'AI
    public void attivaAI(boolean attiva){
        aiAttiva = attiva;
    }
    // RITORNA SE I MATTONI SONO TUTTI DISTRUTTI
    private boolean allBricks(Brick[][] bricks) {
        for (int i = 0; i < bricks.length; i++)
            for (int j = 0; j < bricks[i].length; j++)
                if(!bricks[i][j].isDestroy())
                    return false;
            return true;
       }
    // MUOVE A SINISTRA L'ASTICELLA
    public void moveLeft() {
        state.getPaddle().moveSx();
    }
    // MUOVE A DESTRA L'ASTICELLA 
    public void moveRight(int length) {
        state.getPaddle().moveDx(length);
    }
    // RITORNA LO STATO CORRENTE 
    public GameState getState() {
        return state;
    }
    // METODO PER DIRE AL CONTROLLER DI AGGIORNARE LO STATO 
    // ALL'ENCODER CHIEDE LO STATO ATTUALE 
    // INIZIALIAZZA L'AZIONE A 0 ( FERMO ) E LA REWARD A 0
    // CONTROLLA SE L'AI È ATTIVA E SE RLMANAGER È DIVERSO DA NULL
    // E SE SI DICE A RLMANAGER DI SCEGLIERE L'AZIONE ( CHE A SUA VOLTA LO CHIEDE ALL'AGENTE )
    // IN OGNI CASO DICE ALLA PALLINA DI MUOVERSI E DI RIMBALZARE SUI MURI
    // CONTROLLA LO STATO DEI MATTONI E SE LA PALLINA TOCCA IL MATTONCINO 
    // LO DISTRUGGE E INVERTE LA DIREZIONE DELLA PALLINA E DICE ALLO STATO 
    // DI AUMENTARE IL PUNTEGGIO
    // CONTROLLA LA DISTANZA TRA PADDLE E PALLINA ( IN CASO L'AI SIA ATTIVA )
    // CONTROLLA SE LA PARTITA È FINITA 
    // SI FA DARE LO STATO DALL'ENCODER ( CHE PER IL CONTROLLER È IL NUOVO STATO 
    // , DOPO QUELLO SUCCESSO PRIMA E CONTROLLA SE L'AI È ATTIVA E RLMANAGER != NULL 
    // E SE SI , DICE ALL' RLMANAGER DI AGGIORNARE L'APPRENDIMENTO E INFINE CONTROLLA SE TUTTI I MATTONI SONO STATI DITRUTTI 
    // E SE SI DICE ALLO STATO DI CAMBIARE LIVELLO
    public void update(){

        Ball ball = state.getBall();
        Paddle paddle = state.getPaddle();
        Brick[][] bricks = state.getBricks();

        int stateRL = encoder.encode(state);
        int action = 0;
        int reward = 0;
       
        // ===== AI =====
        if(aiAttiva && rlManager != null){
            action = rlManager.chooseAction(stateRL);
            
            reward -= 2;  
            if(action == 1) 
                moveLeft();
            if(action == 2) 
                moveRight(1600);
        }

        // ===== UPDATE =====
        ball.move();
        ball.bounceWall(LENGTH);

        // ===== PADDLE =====
        if (ball.rettangle().intersects(paddle.rettangle())) {
            ball.bouncePaddle(paddle);
            reward += 25; // Reward se il paddle prende la pallina
        }

        // ===== MATTONI =====
        for (int i = 0; i < bricks.length; i++) {
            for (int j = 0; j < bricks[i].length; j++) {
                Brick brick = bricks[i][j];

                if (!brick.isDestroy() && ball.rettangle().intersects(brick.rettangle())) {
                    brick.destroy();
                    ball.reverseDirectionY();
                    state.increasesPoints(10);

                    reward += 10; // Se la pallina distrugge un mattoncino
                }
            }
        }

        // ===== DISTANZA PALLINA-PADDLE (GUIDA L’AI) =====
        double centerBall = ball.getX() + 10;
        double centerPaddle = paddle.getX() + paddle.getLength() / 2;
        double distance = Math.abs(centerBall - centerPaddle);

        reward -= distance * 0.005;

        // ===== GAME OVER =====
        boolean lost = false;

        if (ball.getY() > 800) {
            reward -= 250;   // Punizione se non prende la pallina e finisce la partita
            lost = true;
        }
        
        int newState = encoder.encode(state);

        // ===== Q UPDATE =====
        if (aiAttiva && rlManager != null) 
            rlManager.updateLearning(stateRL, action, reward, newState);
        

        // ===== FINE EPISODIO =====
        if (lost){
            if (aiAttiva && rlManager != null)
                 rlManager.endEpisode();

            state.reset();
          
        }

        // ===== LIVELLO =====
        if (allBricks(bricks)) {
            state.nextLevel();
            ball.increasesSpeed(state.getLevel());
            reward += 300 + state.getLevel() * 50;
        }
    }
}