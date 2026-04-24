package breakout;

import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JPanel;

class BottomPanel extends JPanel {

    private JButton run, stop, ai, playAI;
    private RLManager rlManager;

    public BottomPanel(GameLoop loop, GameView view, GameController controller, RLManager rlManager){
        setBackground(Color.DARK_GRAY);

        this.rlManager = rlManager;

        run = new JButton("Play");
        stop = new JButton("Stop");
        ai = new JButton("Training");
        playAI = new JButton("PlayAI");

        // ================= HUMAN MODE =================
        run.addActionListener(e -> {
            controller.setMode(GameController.Mode.HUMAN);
            rlManager.setTraining(false);
            loop.run();
            view.requestFocusInWindow();
        });

        // ================= TRAINING MODE =================
        ai.addActionListener(e -> {
            controller.setMode(GameController.Mode.AI_TRAINING);
            rlManager.setTraining(true);
            loop.run();
            view.requestFocusInWindow();
        });

        // ================= STOP =================
        stop.addActionListener(e -> {
            loop.stop();
        });

        // ================= AI PLAY MODE =================
        playAI.addActionListener(e -> {
            controller.setMode(GameController.Mode.AI_PLAY);
            rlManager.setTraining(false);
            loop.run();
            view.requestFocusInWindow();
        });

        add(run);
        add(ai);
        add(stop);
        add(playAI);
    }
}