package breakout;

public class StateEncoder {
    
    private final int COLS = 7;
    private final int ROWS = 7;
    private final int WIDTH = 1600;
    private final int HEIGHT = 800; 

    public int encode(GameState state) {
        
        Ball ball = state.getBall();

        int col = (int)(ball.getX() / (WIDTH / COLS));
        int row = (int)(ball.getY() / (HEIGHT / ROWS));

        col = Math.max(0, Math.min(COLS - 1, col));
        row = Math.max(0, Math.min(ROWS - 1, row));

        return row * COLS + col;
    }
}