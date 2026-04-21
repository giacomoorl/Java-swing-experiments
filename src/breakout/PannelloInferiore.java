package breakout;

import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JPanel;

class PannelloInferiore extends JPanel {
    // CAMPI DATI
    private JButton avvia, ferma, ai, test;
    private RLManager rlManager;
    // COSTRUTTORE
    public PannelloInferiore(GameLoop loop, GameView view, GameController controller, RLManager rlManager){
        setBackground(Color.DARK_GRAY);

        avvia = new JButton("AVVIA");
        ferma = new JButton("STOP");
        ai    = new JButton("TRAINING");
        test = new JButton("TEST");
        this.rlManager = rlManager;
        
        // Manuale
        avvia.addActionListener(e -> {
            loop.avviaManuale();             // parte Timer
            view.requestFocusInWindow();     // focus su GameView
        });

        // AI
       ai.addActionListener(e -> {
            controller.setRLManager(rlManager);
            controller.attivaAI(true);

            rlManager.setEpsilon(0.8);

            loop.avviaAI();
            view.requestFocusInWindow();
        });


        // Ferma
        ferma.addActionListener(e -> loop.ferma());
        // Test
        test.addActionListener(e -> {
            controller.setRLManager(rlManager);
            controller.attivaAI(true);

            rlManager.setTraining(false); // ❌ no training
            rlManager.setEpsilon(0);      // ❌ no random

            loop.avviaAI();
            view.requestFocusInWindow();
        });
        
        add(avvia);
        add(ai);
        add(ferma);
        add(test);
    }
}