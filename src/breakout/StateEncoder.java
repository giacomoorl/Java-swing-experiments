package breakout;

public class StateEncoder {

    public int encode(GameState state) {

        Ball ball = state.getBall();
        Paddle paddle = state.getPaddle();

        // 1) palla X in 8 zone
        int ballX = (int)(ball.getX() / 200);
        if (ballX < 0) ballX = 0;
        if (ballX > 7) ballX = 7;

        // 2) paddle in 4 zone
        int paddleX = (int)(paddle.getX() / 400);
        if (paddleX < 0) paddleX = 0;
        if (paddleX > 3) paddleX = 3;

        // 3) direzioni
        int dirX = ball.getDX() > 0 ? 1 : 0;
        int dirY = ball.getDY() > 0 ? 1 : 0;

        // 4) stato finale (~128 stati)
        return ballX
                + paddleX * 8
                + dirX * 32
                + dirY * 64;
    }
}