package breakout;

import java.awt.Color;
import java.awt.Rectangle;

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

    // METODO CHE RIRTORNA SE IL MATTONCINO È DISTRUTTO O NO 
    public boolean isDestroy() { return destroy; }
    // METODO CHE DISTRUGGE IL MATTONCINO
    public void destroy() { destroy = true; }

    // METODO CHE RESTITUISCE IL RETTANGOLO DEL MATTONCINO
    // SERVE PER CONTROLLARE LE COLLISIONI (es. con la pallina)
    public Rectangle rettangle() {
        return new Rectangle(x, y, length, height);
    }

    // METODI GETTERS 
    public int getX() { return x; }
    public int getY() { return y; }
    public int getLength() { return length; }
    public int getHeight() { return height; }
    public Color getColor() { return color; }

    // METODO CHE DICE AL MATTONCINO DI DISEGNARSI A SCHERMO
    public void draw(java.awt.Graphics g) {
        if (!destroy) {
            g.setColor(color);
            g.fillRect(x, y, length, height);
        }
    }
}