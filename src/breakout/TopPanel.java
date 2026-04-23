package breakout;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;
/*
* CLASSE PANNELLO SUPERIORE 
*/
public class TopPanel extends JPanel {
    private JLabel labelPoints;
    private JLabel labelLevel;
    // COSTRUTTORE
    public TopPanel() {
        setBackground(Color.BLUE);

        labelPoints = new JLabel("Punti: 0");
        labelPoints.setForeground(Color.YELLOW);
        labelPoints.setFont(new Font("Arial", Font.BOLD, 18));

        labelLevel = new JLabel("Livello: 1");
        labelLevel.setForeground(Color.WHITE);
        labelLevel.setFont(new Font("Arial", Font.BOLD, 18));

        add(labelPoints);
        add(labelLevel);
    }
    // AGGIORNA I PUNTI
    public void updatePoints(int punti) {
        labelPoints.setText("Punti: " + punti);
    }
    // AGGIORNA IL LIVELLO
    public void updateLevel(int livello) {
        labelLevel.setText("Livello: " + livello);
    }
}