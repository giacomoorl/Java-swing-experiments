package breakout;

import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JPanel;
/*
* CLASSE CHE RAPPRESENTA IL PANNELLO INFERIORE
*/
class BottomPanel extends JPanel {
    // CAMPI DATI
    private JButton run, stop, ai, playAI;
    private RLManager rlManager;
    // COSTRUTTORE
    public BottomPanel(GameLoop loop, GameView view, GameController controller, RLManager rlManager){
        setBackground(Color.DARK_GRAY);

        run = new JButton("Play");
        stop = new JButton("Stop");
        ai    = new JButton("Training");
        playAI = new JButton("PlayAI");
        this.rlManager = rlManager;
        
        // GIOCA L'UTENTE
        run.addActionListener(e -> {
            loop.runManual();             
            view.requestFocusInWindow();     
        });

        // AI SI ADDESTRA
       ai.addActionListener(e -> {
            controller.setRLManager(rlManager);
            controller.attivaAI(true);

            rlManager.setEpsilon(0.8);

            loop.runAI();
            view.requestFocusInWindow();
        });
        // FERMA GIOCO
        stop.addActionListener(e -> loop.stop());
        // GIOCA L'AI
        playAI.addActionListener(e -> {
            controller.setRLManager(rlManager);
            controller.attivaAI(true);

            rlManager.setTraining(false); // ❌ no training
            rlManager.setEpsilon(0);      // ❌ no random

            loop.runAI();
            view.requestFocusInWindow();
        });
        
        add(run);
        add(ai);
        add(stop);
        add(playAI);
    }
}