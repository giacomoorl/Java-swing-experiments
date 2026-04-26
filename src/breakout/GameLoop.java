package breakout;

import javax.swing.Timer;
/*
* CLASSE LOOP , CHE DETTA I TEMPI DI AGGIORNAMENTO DEL GIOCO
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
        // DICE AL CONTROLLER DI AGGIORNARE IL GIOCO 
        controller.update();  
        // DICE AL TOPPANEL DI AGGIORNARE PUNTI E LIVELLO 
        top.updatePoints(controller.getState().getPoints());
        top.updateLevel(controller.getState().getLevel());
        // DICE ALLA VIEW DI RIDISEGNARSI , LA REPAINT AUTOMATICAMENTE INCOVA 
        // PAINTCOMPONENT
        view.repaint();
    }
    // DICE AL LOOP DI INIZIARE DARE I TEMPI DI AGGIORNAMENTO
    public void run() {
        timer.start();
    }
    // DICE AL LOOP DI FERMARE IL TEMPO DI GIOCO
    public void stop() {
        timer.stop();
    }
}