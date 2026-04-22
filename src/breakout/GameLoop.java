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
    private PannelloSuperiore top;
    // COSTRUTTORE
    public GameLoop(GameController controller, GameView view, PannelloSuperiore top) {
        System.out.println("Loop creato");
      
        this.controller = controller;
        this.view = view;
        this.top = top;

        // 16 ms ≈ 60 FPS
        timer = new Timer(16, e -> aggiorna());
    }
    // METODO CHE AGGIORNA LO STATO DEL GIOCO OGNI 16 MS
    // DICE AL CONTROLLER DI AGGIORNARE IL GIOCO OGNI 16 MS
    // DICE AL PANNELLO SUPERIORE DI AGGIORNARE PUNTI E LIVEELO OGNI 16 MS
    // DICE ALLA VIEW DI RISDISEGNARE LO SCHERMO DEL GIOCO
    private void aggiorna() {
       
        controller.aggiorna();  
        
        // aggiorna punti e livello nel pannello superiore
        top.aggiornaPunti(controller.getStato().getPunti());
        top.aggiornaLivello(controller.getStato().getLivello());
     
        view.repaint();
    }

    // ===== METODI PER AVVIARE/FERMARE =====
    public void avviaManuale() {
        controller.attivaAI(false);
        timer.start();
    }

    public void avviaAI() {
        controller.attivaAI(true);
        timer.start();
    }

    public void ferma() {
        timer.stop();
    }
}