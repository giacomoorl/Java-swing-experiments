package breakout;

import java.awt.Color;
import java.awt.Rectangle;

/**
 * Classe Mattoncino per Breakout.
 * Logica separata dal disegno, testabile senza GUI.
 */
public class Mattoncino {

    private int x, y;
    private int larghezza, altezza;
    private Color colore;
    private boolean distrutto;

    // ===== COSTRUTTORE =====
    public Mattoncino(int x, int y, int larghezza, int altezza, Color colore) {
        this.x = x;
        this.y = y;
        this.larghezza = larghezza;
        this.altezza = altezza;
        this.colore = colore;
        this.distrutto = false;
    }

    // ===== LOGICA =====
    public boolean èDistrutto() { return distrutto; }
    public void distruggi() { distrutto = true; }

    /** Restituisce il rettangolo per collisioni */
    public Rectangle rettangolo() {
        return new Rectangle(x, y, larghezza, altezza);
    }

    // ===== GETTERS =====
    public int dammiX() { return x; }
    public int dammiY() { return y; }
    public int dammiLarghezza() { return larghezza; }
    public int dammiAltezza() { return altezza; }
    public Color dammiColore() { return colore; }

    // ===== GUI =====
    public void disegnati(java.awt.Graphics g) {
        if (!distrutto) {
            g.setColor(colore);
            g.fillRect(x, y, larghezza, altezza);
        }
    }
}