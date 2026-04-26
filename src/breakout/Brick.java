package breakout;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Graphics;
/**
 * CLASSE BRICK
 */
public class Brick {
    // CAMPI DATI
    private int x, y;
    private int length, height;
    private Color color;
    private boolean destroy;
    // COSTRUTTORE 
    public Brick(int x, int y, int l, int h, Color c) {
        this.x = x;
        this.y = y;
        this.length = l;
        this.height = h;
        this.color = c;
        this.destroy = false;
    }
    // RIRTORNA SE IL MATTONCINO È DISTRUTTO O NO 
    public boolean isDestroy() { return destroy; }
    // DISTRUGGE IL MATTONCINO
    public void destroy() { destroy = true; }
    // RESTITUISCE IL RETTANGOLO DEL MATTONCINO
    // SERVE PER CONTROLLARE LE COLLISIONI (es. con la pallina)
    public Rectangle rettangle() {
        return new Rectangle(x, y, length, height);
    }
    // GETTERS 
    public int getX() { return x; }
    public int getY() { return y; }
    public int getLength() { return length; }
    public int getHeight() { return height; }
    public Color getColor() { return color; }
    // DISEGNA A SCHERMO IL MATTONCINO
    public void draw(Graphics g) {
        if (!destroy) {
            g.setColor(color);
            g.fillRect(x, y, length, height);
        }
    }
}