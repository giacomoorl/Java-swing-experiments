package breakout;
/*
* CLASSE GAMESTATE
*/
public class GameState {
    // CAMPI DATI 
    private Paddle paddle;
    private Ball ball;
    private Brick[][] bricks;
    private int points;
    private int level;
    private final int MaxLevel = 5;
    private final int numberOfLines = 5;
    private final int numberOfColumns = 10;
    // COSTRUTTORE
    public GameState() {
        System.out.println("Stato creato");
        // INIZIALIZZA IL GIOCO AL LIVELLO 1 E I POINTS A ZERO
        level = 1;
        points = 0;
        // INIZIALIZZA PADDLE E BALL E CREA I MATTONCINI
        paddle = new Paddle(750, 700);
        ball = new Ball(800, 670);
        createBricks();
    }
    // CREA I MATTONCINI
    private void createBricks() {
        bricks = new Brick[numberOfLines][numberOfColumns];
        java.awt.Color[] colori = {
            java.awt.Color.RED,
            java.awt.Color.ORANGE,
            java.awt.Color.YELLOW,
            java.awt.Color.GREEN,
            java.awt.Color.CYAN
        };
        int gap = 10;
        int totalGap = gap * (numberOfColumns + 1);
        int brickWidth = (1600 - totalGap) / numberOfColumns;
        int brickHeight = 30;
        int yStart = 50;
        for (int i = 0; i < numberOfLines; i++) {
            for (int j = 0; j < numberOfColumns; j++) {
                int x = gap + j * (brickWidth + gap);
                int y = yStart + i * (brickHeight + gap);
                bricks[i][j] = new Brick(x, y, brickWidth, brickHeight, colori[i]);
            }
        }
    }
    //RIPRISTINA IL GIOCO ALLO STATO INIZIALE
    public void reset() {
        paddle.setX(750);
        paddle.setY(700);
        ball.setPosition(800, 670);
        ball.setDX(3);
        ball.setDY(-3);
        createBricks();
        points = 0;
        level = 1;
    }
    // RITORNA IL NUMERO DI STATI
    public int numTotalState() {
        return 10 * 2 * 4 * 5; 
    }
    // GETTERS
    public Paddle getPaddle(){
        return paddle;
    }
    public Ball getBall(){
        return ball; 
    }
    public Brick[][] getBricks(){ 
        return bricks;
    }
    public int getPoints(){
        return points; 
    }
    public int getLevel(){
        return level; 
    }
    // AUMENTA IL PUNTEGGIO
    public void increasesPoints(int p){
        points += p; 
    }
    // PASSA AL LIVELLO DI GIOCO SUCCESSIVO
    public void nextLevel(){
        if (level < MaxLevel){
            level++;
            // RIPRSITINA POSIZIONI DI PADDLE E BALL
            paddle.setX(750);
            paddle.setY(700);
            ball.setPosition(800, 670);
            // CREA MATTONCINI
            createBricks();
            // AUMENTA VELOCITÀ BALL
            ball.increasesSpeed(level);
        }
    }
}