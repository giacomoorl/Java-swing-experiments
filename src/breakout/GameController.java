package breakout;
/*
* CLASSE GAMECONTROLLER , GESTISCE IL GIOCO 
*/
public class GameController{
    // CAMPI DATI 
    int reward , action, stateRL , newStateRL;
    boolean lost, human, aiTraining, aiPlay;
    private GameState state;
    private RLManager rlManager;
    private StateEncoder encoder;
    private GameLoop loop;
    private static final int LENGTH = 1600;
   
    public GameController(GameState state) {
        System.out.println("Controller creato");
        this.state = state;
        this.encoder = new StateEncoder();
        this.reward=0;
        this.stateRL=0;
        this.newStateRL=0;
        this.action=0;
        this.lost=false;
        this.aiPlay=false;
        this.aiTraining=false;
        this.human=false;
        
    }
    // IMPOSTA IL LOOP PER FERMARE IL TEMPO SE FINISCE LA PARTITA L'UTENTE UMANO
    public void setLoop(GameLoop loop){
        this.loop = loop;
    }
    // IMPOSTA IL GIOCATORE , SE UMANO O ARTIFICIALE E SE IN ADDESTRAMNETO 
    // O GIOCO VERO E PROPRIO
    public void setHuman(){
        human=true;
    }
    public void setAITraining(){
        aiTraining=true;
    }
    public void setAIPlay(){
        aiPlay=true;
    }
    // IMPOSTA L'RLMANAGER
    public void setRLManager(RLManager rlManager){
        System.out.println("RLManager impostato da Controller");
        this.rlManager = rlManager;
    }
    // MUOVE PADDLE A SINISTRA
    public void moveLeft(){
        state.getPaddle().moveSx();
    }
    // MUOVE PADDLE A DESTRA 
    public void moveRight(int limit){
        state.getPaddle().moveDx(limit);
    }
    // SI FA DARE STATO
    public GameState getState(){
        return state;
    }
    // CONTROLLA SE I MATTONCINI SONO DISTRUTTI TUTTI
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
    // AGGIORNA IL GIOCO
    public void update(){
       
        Ball ball = state.getBall();
        Paddle paddle = state.getPaddle();
        Brick[][] bricks = state.getBricks();
        // MUOVE LA BALL
        ball.move();
        // CONTROLLA SE LA BALL COLLIDE COI MURI DEL GIOCO
        ball.bounceWall(LENGTH);
        // INIZIO SEZIONE GIOCA UMANO
        if(human){
             for (int i = 0; i < bricks.length; i++) {
                for (int j = 0; j < bricks[i].length; j++) {
                    Brick brick = bricks[i][j];
                    if(!brick.isDestroy()){
                        if(ball.getX() + ball.getDiameter() >= brick.getX() &&
                            ball.getX() <= brick.getX() + brick.getLength() &&
                            ball.getY() + ball.getDiameter() >= brick.getY() &&
                            ball.getY() <= brick.getY() + brick.getHeight()) 
                        {
                            brick.destroy();
                            ball.reverseDirectionY();
                            state.increasesPoints(10);
                            reward += 40;
                            System.out.println("preso mattone gioco umano");
                            break; 
                        }
                    }
                }
            }
        }
        // FINE SEZIONE UMANO , INIZIO SEZIONE TRAINING AI
        if(aiTraining){
            stateRL = encoder.encode(state);
            System.out.println("reward iniziale "+reward);
            action = rlManager.chooseAction(stateRL);
            if(action == 1)
                moveLeft();
            if(action == 2)
                moveRight(LENGTH);
            for (int i = 0; i < bricks.length; i++) {
                for (int j = 0; j < bricks[i].length; j++) {
                    Brick brick = bricks[i][j];
                    if(!brick.isDestroy()){
                        if(ball.getX() + ball.getDiameter() >= brick.getX() &&
                            ball.getX() <= brick.getX() + brick.getLength() &&
                            ball.getY() + ball.getDiameter() >= brick.getY() &&
                            ball.getY() <= brick.getY() + brick.getHeight()) 
                        {
                            brick.destroy();
                            ball.reverseDirectionY();
                            state.increasesPoints(10);
                            reward += 40;
                            System.out.println("preso mattone addestrando AI" + reward);
                            break; 
                        }
                    }
                }
            }
            // CALCOLA NUOVO STATO 
            newStateRL = encoder.encode(state);
            // LIVELLO COMPLETATO
            if(allBricks(bricks)){
                state.nextLevel();
                ball.increasesSpeed(state.getLevel());
                reward += 50;
                System.out.println("distrutto tutto " + reward + "  ");
            }
            rlManager.updateLearning(stateRL, action, reward, newStateRL);
        }
        // FINE SEZIONE AI TRAINING , INIZIO SEZIONE GIOCA AI
        if(aiPlay){
            action = rlManager.chooseBestAction(stateRL); 
            if(action == 1)
                moveLeft();
            if(action == 2)
                moveRight(LENGTH);
             for (int i = 0; i < bricks.length; i++) {
                for (int j = 0; j < bricks[i].length; j++) {
                    Brick brick = bricks[i][j];
                    if(!brick.isDestroy()){
                        if(ball.getX() + ball.getDiameter() >= brick.getX() &&
                            ball.getX() <= brick.getX() + brick.getLength() &&
                            ball.getY() + ball.getDiameter() >= brick.getY() &&
                            ball.getY() <= brick.getY() + brick.getHeight()) 
                        {
                            brick.destroy();
                            ball.reverseDirectionY();
                            state.increasesPoints(10);
                            System.out.println("preso mattone gioca AI");
                            break; 
                        }
                    }
                }
            }
            if(allBricks(bricks)){
                state.nextLevel();
                ball.increasesSpeed(state.getLevel());
                System.out.println("distrutto tutto gioca AI");
            }
         }
        // CONTROLLA FINE PARTITA
         if(ball.getY() > 800)
            lost = true;
            
        // CHIUDE LA PARTITA IN BASE A CHI STA GIOCANDO
        if(lost){
            if(human){
                state.reset();
                loop.stop();
                lost = false;
                System.out.println("persa partita umano");
            }
            else if(aiTraining){
                reward -= 50;
                rlManager.endEpisode();
                state.reset();
                lost = false;
                System.out.println("persa partita aiTraining");
            }
            else if(aiPlay){
                state.reset();
                lost = false;
                System.out.println("persa partita aiPlay");
            }
        }
    }
}