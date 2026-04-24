package breakout;
/*
* CLASSE CHE FUNGE DA INTEMEDIARIO TRA IL GAMESTATE 
* E IL RESTO DEL SISTEMA
*/
public class StateEncoder {
    // CODIFICA LO STATO IN UN NUMERO CHE LO RIASSUME E LO RITORNA 
    public int encode(GameState state) {
      
        
        Ball ball = state.getBall();
        Paddle paddle = state.getPaddle();

        // 1. posizione pallina (più fine)
        int col = (int)(ball.getX() / (1600.0 / 10));
        col = Math.max(0, Math.min(9, col));

        // 2. direzione verticale
        int directionY = ball.getDY() > 0 ? 1 : 0;

        // 3. velocità orizzontale
        double dx = ball.getDX();
        int velX;
        if (dx < -2) velX = 0;
        else if (dx < 0) velX = 1;
        else if (dx < 2) velX = 2;
        else velX = 3;

        // 4. relazione con paddle (più stabile)
        double centerBall = ball.getX() + 10;
        double centerPaddle = paddle.getX() + paddle.getLength() / 2;
        double diff = centerBall - centerPaddle;

        int rel;
        if (diff < -100) rel = 0;
        else if (diff < -30) rel = 1;
        else if (diff < 30) rel = 2;
        else if (diff < 100) rel = 3;
        else rel = 4;
      
        return col * (2 * 4 * 5)
             + directionY * (4 * 5)
             + velX * 5
             + rel;
    }
}


