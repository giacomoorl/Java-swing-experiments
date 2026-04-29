package breakout;

import javax.swing.*;
/*
* CLASSE WINDOW FOR THE PLOT
*/
public class RewardWindow extends JFrame {
    // COSTRUTTORE 
    public RewardWindow(RewardPlotter plot) {
        super("Reward Graph");
        System.out.println("RewardWindow creato");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        add(plot);
        setSize(800, 300);
        setLocation(100, 100);
        setVisible(true);
    }
}