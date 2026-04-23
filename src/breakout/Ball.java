package breakout;

/**
 * CLASSE PALLINA
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
        dy = -Math.abs(dy); 
    }
    // METODO PER DIRE ALLA PALLINA COSA FARE IN CASO DI RIMBALZO SUI MURI
    public void bounceWall(int panelWidth) {
        if(x < 0) 
        { 
            x = 0;
            dx = -dx; 
        }
        if(x + diameter > panelWidth) 
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
    // METODO PER FARSI DARE DALLA PALLINA LA SUA COORDINATA ORIZZONTALE
    public double getX() 
    { 
        return x; 
    }
    // METODO PER FARSI DARE DALLA PALLINA LA SUA COORDINATA VERTICALE
    public double getY() 
    { 
        return y; 
    }
     // METODO PER FARSI DARE DALLA PALLINA LA SUA VELOCITÀ ORIZZONTALE
    public double getDX() 
    {
        return dx;
    }
     // METODO PER FARSI DARE DALLA PALLINA LA SUA VELOCITÀ VERTICALE
    public double getDY() 
    {
        return dy; 
    }
    // METODO PER FARSI DARE DALLA PALLINA IL SUO DIAMETRO
    public int getDiameter()
    { 
        return diameter; 
    }
    // METODO PER IMPOSTARE LA VELOCITÀ ORIZZONTALE DELLA PALLINA
    public void setDX(double dx) 
    { 
        this.dx = dx; 
    }
    // METODO PER IMPOSTARE LA VELOCITÀ VERTICALE DELLA PALLINA
    public void setDY(double dy) 
    { 
        this.dy = dy; 
    }
    // METODO PER IMPOSTARE LA POSIZIONE DELLA PALLINA
    public void setPosition(double x, double y) 
    { 
        this.x = x; 
        this.y = y; 
    }
    // CREA E RESTITUISCE IL RETTANGOLO DELLA PALLINA
    // SERVE PER CONTROLLARE LE COLLISIONI (con mattoni e paddle)
    public java.awt.Rectangle rettangle() {
        return new java.awt.Rectangle((int)x, (int)y, diameter, diameter);
    }
    // METODO PER DIRE ALLA PALLINA DI DISEGNARSI A SCHERMO
    public void draw(java.awt.Graphics g) {
        g.setColor(java.awt.Color.WHITE);
        g.fillOval((int)x, (int)y, diameter, diameter);
    }
    // METODO PER DIRE ALLA PALLINA DI AUMENTARE LA SUA VELOCITÀ AD OGNI LIVELLO
    public void increasesSpeed(int level) {
        double newSpeed = 3 + level;
        dx = dx > 0 ? newSpeed : -newSpeed;
        dy = dy > 0 ? newSpeed : -newSpeed;
    }
}