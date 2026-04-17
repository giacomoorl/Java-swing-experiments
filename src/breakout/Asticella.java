package breakout;

import java.awt.Rectangle;

/**
 * CLASSE ASTICELLA
 */
public class Asticella {
    // CAMPI DATI 
    private int x, y;          
    private final int larghezza; 
    private final int altezza; 
    private int velocita;      

    // ===== COSTRUTTORE =====
    public Asticella(int inizioX, int inizioY){
        this.x = inizioX;
        this.y = inizioY;
        this.larghezza = 120;
        this.altezza = 15;
        this.velocita = 25;
    }

    // ===== MUOVE PADDLE A SINISTRA =====
    public void muovitiSinistra(){
        x -= velocita;
        if (x < 0)
            x = 0;
    }
    // ===== MUOVE PADDLE A DESTRA =====
    public void muovitiDestra(int limiteDx){
        x += velocita;
        if (x + larghezza > limiteDx) 
            x = limiteDx - larghezza;
    }

    // ===== RETTANGOLO PER COLLISIONI =====
    public Rectangle rettangolo() {
        return new Rectangle(x, y, larghezza, altezza);
    }

    // ===== GETTER =====
    public int dammiX() { return x; }
    public int dammiY() { return y; }
    public int dammiLarghezza() { return larghezza; }
    public int dammiAltezza() { return altezza; }
    public int dammiVelocita() { return velocita; }

    // ===== SETTER =====
    public void impostaX(int x){
        this.x = x;
    }
    public void impostaY(int y){
        this.y = y; 
    }
    public void impostaVelocita(int v){ 
        this.velocita = v;
    }
    public void aumentaVelocita(int livello){
        velocita = 25 + livello; 
    }
    
    // ===== DISEGNA L'ASTICELLA =====
    public void disegnati(java.awt.Graphics g){
        g.setColor(java.awt.Color.BLUE);
        g.fillRect(x, y, larghezza, altezza);
    }
}