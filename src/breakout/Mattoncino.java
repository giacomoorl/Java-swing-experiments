package breakout;

import java.awt.Color;
import java.awt.Rectangle;

/**
 * Classe Mattoncino per Breakout.
 * Logica separata dal disegno, testabile senza GUI.
 */

public class Mattoncino {
    // CAMPI DATI
    private int x, y;
    private int larghezza, altezza;
    private Color colore;
    private boolean distrutto;

    // COSTRUTTORE 
    public Mattoncino(int x, int y, int larghezza, int altezza, Color colore) {
        this.x = x;
        this.y = y;
        this.larghezza = larghezza;
        this.altezza = altezza;
        this.colore = colore;
        this.distrutto = false;
    }

    // METODO CHE RIRTORNA SE IL MATTONCINO È DISTRUTTO O NO 
    public boolean èDistrutto() { return distrutto; }
    // METODO CHE DISTRUGGE IL MATTONCINO
    public void distruggi() { distrutto = true; }

    // METODO CHE RESTITUISCE IL RETTANGOLO DEL MATTONCINO
    // SERVE PER CONTROLLARE LE COLLISIONI (es. con la pallina)
    public Rectangle rettangolo() {
        return new Rectangle(x, y, larghezza, altezza);
    }

    // METODI GETTERS 
    public int dammiX() { return x; }
    public int dammiY() { return y; }
    public int dammiLarghezza() { return larghezza; }
    public int dammiAltezza() { return altezza; }
    public Color dammiColore() { return colore; }

    // METODO CHE DICE AL MATTONCINO DI DISEGNARSI A SCHERMO
    public void disegnati(java.awt.Graphics g) {
        if (!distrutto) {
            g.setColor(colore);
            g.fillRect(x, y, larghezza, altezza);
        }
    }
}