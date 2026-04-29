package breakout;

import java.awt.Rectangle;
/**
 * CLASSE BALL
 */
public class Ball {
    // POSIZIONE 
    private double x, y;
    // VELOCITÀ 
    private double dx = 3;
    private double dy = -3;
    // DIMENSIONE
    private final int diameter = 20;
    // COSTRUTTORE 
    public Ball(int startX, int startY) {
        this.x = startX;
        this.y = startY;
    }
    // METODO PER DIRE ALLA PALLINA DI MUOVERSI
    public void move() {
        x += dx;
        y += dy;
    }
    // METODO PER DIRE ALLA PALLINA DI INVERTIRE LA DIREZIONE IN CASO DI COLLISIONI 
    // CON GLI ALTRI OGGETTI DEL GIOCO 
    public void reverseDirectionY() {
        dy = -dy;
    }
    // METODO CHE GESTISCE IL RIMBALZO DELLA PALLINA SULL'ASTICELLA
    // LA DIREZIONE CAMBIA IN BASE A DOVE COLPISCE (SINISTRA / CENTRO / DESTRA)
    // PIÙ COLPISCE AI LATI → PIÙ LA PALLINA VA IN ORIZZONTALE
    // IN POSIZIONEIMPATTA C'È IL RISULTATO DEL CALCOLO DI DOVE HA TOCCATO LA PALLINA
    // IL PADDLE , ( 0 A SINISTRA , 1 A DESTRA , 0.5 IN CENTRO ) 
    public void bouncePaddle(Paddle paddle) {
        double impactPosition = (x + diameter / 2 - paddle.getX()) / (double) paddle.getLength();
        if (impactPosition < 0) 
            impactPosition = 0;
        if (impactPosition > 1) 
            impactPosition = 1;

        dx = (impactPosition - 0.5) * 6;
        if (Math.abs(dx) < 0.5) {
            dx = dx < 0 ? -0.5 : 0.5;
        }
        dy = -Math.abs(dy); 
    }
    // DICE ALLA BALL COSA FARE IN CASO DI RIMBALZO SUI MURI
    public void bounceWall(int panelWidth) {
        if(x < 0) 
        { 
            x = 0;
            dx = -dx; 
        }
        else if(x + diameter > panelWidth) 
        { 
            x = panelWidth - diameter; 
            dx = -dx; 
        }
        if(y < 0) 
        {
            y = 0;
            dy = -dy; 
        }
    }
    // RITORNA LA COORDINATA ORIZZONTALE DELLA BALL
    public double getX() 
    { 
        return x; 
    }
    // RITORNA LA COORDINATA VERTICALE DELLA BALL
    public double getY() 
    { 
        return y; 
    }
     // RITORNA VELOCITÀ ORIZZONTALE DELLA BALL
    public double getDX() 
    {
        return dx;
    }
     // RITORNA VELOCITÀ VERTICALE DELLA BALL
    public double getDY() 
    {
        return dy; 
    }
    // RITORNA IL DIAMETRO DELLA BALL
    public int getDiameter()
    { 
        return diameter; 
    }
    // RITORNA LA VELOCITÀ ORIZZONTALE DELLA BALL
    public void setDX(double dx) 
    { 
        this.dx = dx; 
    }
    // IMPOSTA LA VELOCITÀ VERTICALE DELLA BALL
    public void setDY(double dy) 
    { 
        this.dy = dy; 
    }
    // IMPOSTA LA POSIZIONE DELLA PALLINA
    public void setPosition(double x, double y) 
    { 
        this.x = x; 
        this.y = y; 
    }
    // CREA E RESTITUISCE IL RETTANGOLO DELLA PALLINA
    // SERVE PER CONTROLLARE LE COLLISIONI (con mattoni e paddle)
    public Rectangle getRettangle() {
        return new java.awt.Rectangle((int)x, (int)y, diameter, diameter);
    }
    // DISEGNA LA BALL A SCHERMO
    public void draw(java.awt.Graphics g) {
        g.setColor(java.awt.Color.WHITE);
        g.fillOval((int)x, (int)y, diameter, diameter);
    }
    // AUMENTA LA VELOCITÀ DELLA BALL AD OGNI LIVELLO
    public void increasesSpeed(int level) {
        double newSpeed = 3 * Math.pow(1.08, level);;
        dx = dx > 0 ? newSpeed : -newSpeed;
        dy = dy > 0 ? newSpeed : -newSpeed;
    }
}