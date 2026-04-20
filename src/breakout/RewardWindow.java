package breakout;

import javax.swing.*;

public class RewardWindow extends JFrame {

    public RewardWindow(RewardPlotter plot) {

        super("Reward Graph");

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        add(plot);

        setSize(800, 300);
        setLocation(100, 100);

        setVisible(true);
    }
}