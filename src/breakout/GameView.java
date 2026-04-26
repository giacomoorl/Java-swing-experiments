package breakout;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
/*
* CLASSE VIEW
*/
public class GameView extends JPanel {
    // CAMPI DATI
    private GameState state;
    // COSTRUTTORE
    public GameView(GameState state) {
        System.out.println("View creata");
        this.state = state;
        setBackground(Color.BLACK);
        setFocusable(true);
    }
    // AD OGNI REPAINT DISEGNA GLI OGGETTI DEL GIOCO
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // DISEGNA PADDLE
        state.getPaddle().draw(g);
        // DISEGNA PALLINA
        state.getBall().draw(g);
        // DISEGNA MATTONCINI
        Brick[][] bricks = state.getBricks();
        for (int i = 0; i < bricks.length; i++) {
            for (int j = 0; j < bricks[i].length; j++) {
                bricks[i][j].draw(g);
            }
        }
    }
}