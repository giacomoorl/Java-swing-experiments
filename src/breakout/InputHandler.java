package breakout;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * intercetta l'input da tastiera
 */
public class InputHandler implements KeyListener {
    // CAMPI DATI , RIFERIMENTI AL CONTROLLER E LA VIEW
    private GameController controller;
    private GameView view;
    // COSTRUTTORE
    public InputHandler(GameController controller, GameView view) {
        this.controller = controller;
        this.view = view;
    }

   @Override
    public void keyPressed(KeyEvent e) {

        if(!controller.isHuman())
            return; // 🔥 blocco totale AI mode

        int codice = e.getKeyCode();

        if (codice == KeyEvent.VK_LEFT)
            controller.moveLeft();
        else if (codice == KeyEvent.VK_RIGHT)
            controller.moveRight(view.getWidth());

        view.repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) { }

    @Override
    public void keyTyped(KeyEvent e) { }
}
