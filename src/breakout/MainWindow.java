package breakout;

import javax.swing.JFrame;
import java.awt.BorderLayout;
/*
* CLASSE CHE RAPPRESENTA LA FINESTRA DI GIOCO
*/
public class MainWindow extends JFrame {
    // COSTRUTTORE
    public MainWindow(GameView view, TopPanel top, BottomPanel bottom) {
        super("Breakout");
        System.out.println("MainWindow creata");
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