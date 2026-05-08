package breakout;

public class CollisionManager {
    public CollisionManager(){
          System.out.println("CollisionManager creato");
    }
    public boolean paddleCollision(Ball ball, Paddle paddle) {
        return ball.getY() + ball.getDiameter() >= paddle.getY() &&
               ball.getY() <= paddle.getY() + paddle.getHeight() &&
               ball.getX() + ball.getDiameter() >= paddle.getX() &&
               ball.getX() <= paddle.getX() + paddle.getLength();
    }

    public boolean brickCollision(Ball ball, Brick brick) {
        return !brick.isDestroy() &&
               ball.getX() + ball.getDiameter() >= brick.getX() &&
               ball.getX() <= brick.getX() + brick.getLength() &&
               ball.getY() + ball.getDiameter() >= brick.getY() &&
               ball.getY() <= brick.getY() + brick.getHeight();
    }
}