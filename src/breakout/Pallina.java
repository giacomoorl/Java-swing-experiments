package breakout;

/**
 * CLASSE PALLINA
 */

public class Pallina {
    // POSIZIONE 
    private double x, y;
    // VELOCITÀ 
    private double dx = 3;
    private double dy = -3;
    // DIMENSIONE
    private final int diametro = 20;
    // COSTRUTTORE 
    public Pallina(int inizioX, int inizioY) {
        this.x = inizioX;
        this.y = inizioY;
    }
    // METODO PER DIRE ALLA PALLINA DI MUOVERSI
    public void muoviti() {
        x += dx;
        y += dy;
    }
    // METODO PER DIRE ALLA PALLINA DI INVERTIRE LA DIREZIONE IN CASO DI COLLISIONI 
    // CON GLI ALTRI OGGETTI DEL GIOCO 
    public void invertiDirezioney() {
        dy = -dy;
    }
    // METODO CHE GESTISCE IL RIMBALZO DELLA PALLINA SULL'ASTICELLA
    // LA DIREZIONE CAMBIA IN BASE A DOVE COLPISCE (SINISTRA / CENTRO / DESTRA)
    // PIÙ COLPISCE AI LATI → PIÙ LA PALLINA VA IN ORIZZONTALE
    // IN POSIZIONEIMPATTA C'È IL RISULTATO DEL CALCOLO DI DOVE HA TOCCATO LA PALLINA
    // IL PADDLE , ( 0 A SINISTRA , 1 A DESTRA , 0.5 IN CENTRO ) 
    public void rimbalzaAsticella(Asticella asticella) {
        double posizioneImpattata = (x + diametro / 2 - asticella.dammiX()) / (double) asticella.dammiLarghezza();
        if (posizioneImpattata < 0) 
            posizioneImpattata = 0;
        if (posizioneImpattata > 1) 
            posizioneImpattata = 1;

        dx = (posizioneImpattata - 0.5) * 6;
        dy = -Math.abs(dy); 
    }
    // METODO PER DIRE ALLA PALLINA COSA FARE IN CASO DI RIMBALZO SUI MURI
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
    // METODO PER FARSI DARE DALLA PALLINA LA SUA COORDINATA ORIZZONTALE
    public double dammiX() 
    { 
        return x; 
    }
    // METODO PER FARSI DARE DALLA PALLINA LA SUA COORDINATA VERTICALE
    public double dammiY() 
    { 
        return y; 
    }
     // METODO PER FARSI DARE DALLA PALLINA LA SUA VELOCITÀ ORIZZONTALE
    public double dammiDX() 
    {
        return dx;
    }
     // METODO PER FARSI DARE DALLA PALLINA LA SUA VELOCITÀ VERTICALE
    public double dammiDY() 
    {
        return dy; 
    }
    // METODO PER FARSI DARE DALLA PALLINA IL SUO DIAMETRO
    public int dammiDiametro()
    { 
        return diametro; 
    }
    // METODO PER IMPOSTARE LA VELOCITÀ ORIZZONTALE DELLA PALLINA
    public void impostaDX(double dx) 
    { 
        this.dx = dx; 
    }
    // METODO PER IMPOSTARE LA VELOCITÀ VERTICALE DELLA PALLINA
    public void impostaDY(double dy) 
    { 
        this.dy = dy; 
    }
    // METODO PER IMPOSTARE LA POSIZIONE DELLA PALLINA
    public void impostaPosizione(double x, double y) 
    { 
        this.x = x; 
        this.y = y; 
    }
    // CREA E RESTITUISCE IL RETTANGOLO DELLA PALLINA
    // SERVE PER CONTROLLARE LE COLLISIONI (con mattoni e paddle)
    public java.awt.Rectangle rettangolo() {
        return new java.awt.Rectangle((int)x, (int)y, diametro, diametro);
    }
    // METODO PER DIRE ALLA PALLINA DI DISEGNARSI A SCHERMO
    public void disegnati(java.awt.Graphics g) {
        g.setColor(java.awt.Color.WHITE);
        g.fillOval((int)x, (int)y, diametro, diametro);
    }
    // METODO PER DIRE ALLA PALLINA DI AUMENTARE LA SUA VELOCITÀ AD OGNI LIVELLO
    public void aumentaVelocita(int livello) {
        double nuovaVel = 3 + livello;
        dx = dx > 0 ? nuovaVel : -nuovaVel;
        dy = dy > 0 ? nuovaVel : -nuovaVel;
    }
}