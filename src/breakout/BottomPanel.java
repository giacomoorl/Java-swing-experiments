package breakout;

import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JPanel;
/*
* CLASSE CHE GESTISCE IL PANNELLO INFERIORE , IN CUI CI SONO I PULSANTI 
* CON CUI L'UTENTE INTERAGISCE COL PROGRAMMA
*/
class BottomPanel extends JPanel {
    // CAMPI DATI
    private JButton run, stop, ai, playAI;
    private RLManager rlManager;
    // COSTRUTTORE
    public BottomPanel(GameLoop loop, GameView view, GameController controller, RLManager rlManager){
        setBackground(Color.DARK_GRAY);
        // ISTANZIA L'RLMANAGER
        this.rlManager = rlManager;
        // ISTANZIA I PULSANTI
        run = new JButton("Play");
        stop = new JButton("Stop");
        ai = new JButton("Training");
        playAI = new JButton("PlayAI");
        // CASO IN CUI L'UTENTE PREME PLAY
        run.addActionListener(e -> {
            controller.setMode(GameController.Mode.HUMAN);
            rlManager.setTraining(false);
            loop.run();
            view.requestFocusInWindow();
        });
        //CASO IN CUI L'UTENTE PREME TRAINING 
        ai.addActionListener(e -> {
            controller.setMode(GameController.Mode.AI_TRAINING);
            rlManager.setTraining(true);
            loop.run();
            view.requestFocusInWindow();
        });
        // CASO IN CUI L'UTENTE PREME STOP
        stop.addActionListener(e -> {
            loop.stop();
        });
        // CASO IN CUI L'UTENTE PREME PLAYAI
        playAI.addActionListener(e -> {
            controller.setMode(GameController.Mode.AI_PLAY);
            rlManager.setTraining(false);
            loop.run();
            view.requestFocusInWindow();
        });
        // AGGIUNGO AL BOTTOMPANEL , CON I METODI EREDITATI DA JPANEL 
        // I PULSANTI
        add(run);
        add(ai);
        add(stop);
        add(playAI);
    }
}