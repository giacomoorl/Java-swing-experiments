package breakout;

import javax.swing.Timer;
/*
* Classe motore del ciclo di gioco , da i tempi di esecuzione 
* in particolare dice al controller di aggiornare lo stato del gioco 
* ogni 16 ms
*/
public class GameLoop {
    // CAMPI DATI 
    private Timer timer;
    private GameController controller;
    private GameView view;
    private TopPanel top;
    // COSTRUTTORE
    public GameLoop(GameController controller, GameView view, TopPanel top) {
        System.out.println("Loop creato");
      
        this.controller = controller;
        this.view = view;
        this.top = top;

        // 16 ms ≈ 60 FPS
        timer = new Timer(16, e -> update());
    }
    // METODO CHE AGGIORNA LO STATO DEL GIOCO OGNI 16 MS
    // DICE AL CONTROLLER DI AGGIORNARE IL GIOCO OGNI 16 MS
    // DICE AL PANNELLO SUPERIORE DI AGGIORNARE PUNTI E LIVEELO OGNI 16 MS
    // DICE ALLA VIEW DI RISDISEGNARE LO SCHERMO DEL GIOCO
    private void update() {
       
        controller.update();  
        
        // aggiorna punti e livello nel pannello superiore
        top.updatePoints(controller.getState().getPoints());
        top.updateLevel(controller.getState().getLevel());
     
        view.repaint();
    }

    // ===== METODI PER AVVIARE/FERMARE =====
    public void runManual() {
        controller.attivaAI(false);
        timer.start();
    }

    public void runAI() {
        controller.attivaAI(true);
        timer.start();
    }

    public void stop() {
        timer.stop();
    }
}