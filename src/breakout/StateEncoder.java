package breakout;
/*
* CLASSE CHE FUNGE DA INTEMEDIARIO TRA IL GAMESTATE 
* E IL RESTO DEL SISTEMA
*/
public class StateEncoder {
    // CODIFICA LO STATO IN UN NUMERO CHE LO RIASSUME E LO RITORNA 
    public int encode(GameState state){
        Ball ball = state.getBall();
        Paddle paddle = state.getPaddle();
        // CALCOLA IN CHE RIGA È LA PALLA 
        int row = (int)(ball.getY() / (900.0 / 3));
        row = Math.max(0, Math.min(2, row));
        // CALCOLA IN CHE COLONNA È LA BALL
        int col = (int)(ball.getX() / (1600.0 / 6));
        col = Math.max(0, Math.min(5, col));
        // CALCOLA LA DIREZIONE DELLA BALL
        int directionY;
        if(ball.getDY() > 0)
            directionY=1;
        else
            directionY=0;
        // CALCOLA LA VELOCITÀ ORIZZONTALE DELLA BALL
        double dx = ball.getDX();
        int velX;
        if (dx < -2) 
            velX = 0;
        else if (dx < 0) 
            velX = 1;
        else if (dx < 2) 
            velX = 2;
        else 
            velX = 3;
        // CALCOLA LA DISTANZA TRA BALL E PADDLE
        double centerBall = ball.getX() + ball.getDiameter() / 2.0;
        double centerPaddle = paddle.getX() + paddle.getLength() / 2;
        double diff = centerBall - centerPaddle;

        int rel;
        if (diff < -100) 
            rel = 0;
        else if (diff < -30) 
            rel = 1;
        else if (diff < 30) 
            rel = 2;
        else if (diff < 100)
            rel = 3;
        else 
            rel = 4;
      
        return (((((col * 3 + row) * 2 + directionY) * 4 + velX) * 5 + rel));
    }
}


