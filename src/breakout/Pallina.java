package breakout;

/**
 * CLASSE PALLINA
 */
public class Pallina {

    // ===== POSIZIONE =====
    private double x, y;

    // ===== VELOCITÀ =====
    private double dx = 3;
    private double dy = -3;

    // ===== DIMENSIONE =====
    private final int diametro = 20;

    // ===== COSTRUTTORE =====
    public Pallina(int inizioX, int inizioY) {
        this.x = inizioX;
        this.y = inizioY;
    }

    // ===== METODI  =====
    public void muoviti() {
        x += dx;
        y += dy;
    }

    // ===== RIMBALZO VERTICALE ======
    public void invertiDirezioney() {
        dy = -dy;
    }

    // ==== RIMBALZO SULL'ASTICELLA =====
    public void rimbalzaAsticella(Asticella asticella) {
        double posizioneImpattata = (x + diametro / 2 - asticella.dammiX()) / (double) asticella.dammiLarghezza();
        if (posizioneImpattata < 0) 
            posizioneImpattata = 0;
        if (posizioneImpattata > 1) 
            posizioneImpattata = 1;

        dx = (posizioneImpattata - 0.5) * 6;
        dy = -Math.abs(dy); // sempre verso l'alto
    }

    // ===== RIMBALZO SUI MURI ======
    public void rimbalzaMuri(int larghezzaPannello, int altezzaPannello) {
        if(x < 0) 
        { 
            x = 0;
            dx = -dx; 
        }
        if(x + diametro > larghezzaPannello) 
        { 
            x = larghezzaPannello - diametro; 
            dx = -dx; 
        }
        if(y < 0) 
        {
            y = 0;
            dy = -dy; 
        }
    }

    // ===== METODI GET/SET =====
    public double dammiX() 
    { 
        return x; 
    }
    public double dammiY() 
    { 
        return y; 
    }
    public double dammiDX() 
    {
        return dx;
    }
    public double dammiDY() 
    {
        return dy; 
    }
    public int dammiDiametro()
    { 
        return diametro; 
    }

    public void impostaDX(double dx) 
    { 
        this.dx = dx; 
    }
    public void impostaDY(double dy) 
    { 
        this.dy = dy; 
    }
    public void impostaPosizione(double x, double y) 
    { 
        this.x = x; 
        this.y = y; 
    }

   
    public java.awt.Rectangle rettangolo() {
        return new java.awt.Rectangle((int)x, (int)y, diametro, diametro);
    }

    // ===== DISEGNA LA PALLINA A SCHERMO =====
    public void disegnati(java.awt.Graphics g) {
        g.setColor(java.awt.Color.WHITE);
        g.fillOval((int)x, (int)y, diametro, diametro);
    }

    // ====== AUMENTA LA VELOCITÀ AD OGNI LIVELLO ======
    public void aumentaVelocita(int livello) {
        double nuovaVel = 3 + livello;
        dx = dx > 0 ? nuovaVel : -nuovaVel;
        dy = dy > 0 ? nuovaVel : -nuovaVel;
    }
}