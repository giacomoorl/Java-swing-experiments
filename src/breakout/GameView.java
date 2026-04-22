package breakout;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;

public class GameView extends JPanel {
    // CAMPI DATI
    private GameState stato;
    // COSTRUTTORE
    public GameView(GameState stato) {
        System.out.println("View creata");
        
        this.stato = stato;
        setBackground(Color.BLACK);
        setFocusable(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Disegna asticella
        stato.getAsticella().disegnati(g);

        // Disegna pallina
        stato.getPallina().disegnati(g);

        // Disegna mattoni
        Mattoncino[][] mattoni = stato.getMattoncini();
        for (int i = 0; i < mattoni.length; i++) {
            for (int j = 0; j < mattoni[i].length; j++) {
                mattoni[i][j].disegnati(g);
            }
        }
    }
}