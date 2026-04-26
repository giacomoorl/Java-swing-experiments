package breakout;

import java.awt.Rectangle;
/**
 * CLASSE ASTICELLA
 */
public class Paddle {
    // CAMPI DATI 
    private int x, y;          
    private final int length; 
    private final int height; 
    private double speed;      
    // COSTRUTTORE 
    public Paddle(int startX, int startY){
        this.x = startX;
        this.y = startY;
        this.length = 120;
        this.height = 15;
        this.speed = 25;
    }
    // MUOVE PADDLE A SINISTRA
    public void moveSx(){
        x -= speed;
        if (x < 0)
            x = 0;
    }
    // MUOVE PADDLE A DESTRA 
    public void moveDx(int limitDx){
        x += speed;
        if (x + length > limitDx) 
            x = limitDx - length;
    }
    // RETTANGOLO PER COLLISIONI 
    public Rectangle getRettangle() {
        return new Rectangle(x, y, length, height);
    }
    // GETTER 
    public int getX() { return x; }
    public int getY() { return y; }
    public int getLength() { return length; }
    public int getHeight() { return height; }
    public double getSpeed() { return speed; }
    // SETTER 
    public void setX(int x){
        this.x = x;
    }
    public void setY(int y){
        this.y = y; 
    }
    public void setSpeed(int v){ 
        this.speed = v;
    }
    public void increasesSpeed(int livello){
        speed = 25 + livello * 0.5; 
    }
    // DISEGNA L'ASTICELLA 
    public void draw(java.awt.Graphics g){
        g.setColor(java.awt.Color.BLUE);
        g.fillRect(x, y, length, height);
    }
}