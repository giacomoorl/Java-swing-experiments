package breakout;

import javax.swing.JFrame;
import java.awt.BorderLayout;

public class FinestraPrincipale extends JFrame {

    public FinestraPrincipale(GameView view, PannelloSuperiore top,
                              PannelloInferiore bottom) {

        super("Breakout");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
      

       setLayout(new BorderLayout());

        add(top, BorderLayout.NORTH);
        add(view, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
     
        setSize(1600, 800);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}