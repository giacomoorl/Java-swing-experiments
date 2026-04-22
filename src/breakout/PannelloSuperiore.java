package breakout;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;
/*
* CLASSE PANNELLO SUPERIORE 
*/
public class PannelloSuperiore extends JPanel {
    private JLabel etichettaPunti;
    private JLabel etichettaLivello;
    // COSTRUTTORE
    public PannelloSuperiore() {
        setBackground(Color.BLUE);

        etichettaPunti = new JLabel("Punti: 0");
        etichettaPunti.setForeground(Color.YELLOW);
        etichettaPunti.setFont(new Font("Arial", Font.BOLD, 18));

        etichettaLivello = new JLabel("Livello: 1");
        etichettaLivello.setForeground(Color.WHITE);
        etichettaLivello.setFont(new Font("Arial", Font.BOLD, 18));

        add(etichettaPunti);
        add(etichettaLivello);
    }
    // AGGIORNA I PUNTI
    public void aggiornaPunti(int punti) {
        etichettaPunti.setText("Punti: " + punti);
    }
    // AGGIORNA IL LIVELLO
    public void aggiornaLivello(int livello) {
        etichettaLivello.setText("Livello: " + livello);
    }
}