package breakout;
/*
* CLASSE CHE AVVIA L'ESECUZIONE DEL GIOCO 
*/
public class Main {
        public static void main(String[] args) {
            GameState stato = new GameState();
            GameController controller = new GameController(stato);
            GameView view = new GameView(stato);
            // PANNELLOSUPERIORE
            PannelloSuperiore top = new PannelloSuperiore();
            // GAMELOOP
            GameLoop loop = new GameLoop(controller, view, top);
            // ✅ CREA L'AI
            int numStati = stato.numeroTotaleStati();  
            AgenteRL agente = new AgenteRL(numStati, 3);   
            agente.caricaTabella("Tabella.txt"); 
            // CREA IL MANAGER AI
            RLManager rlManager = new RLManager(agente);
            controller.setRLManager(rlManager);
            // PANNELLO INFERIORE CON PULSANTI
            PannelloInferiore bottom = new PannelloInferiore(loop, view, controller, rlManager);
            // FINESTRA PRINCIPALE
            new FinestraPrincipale(view, top, bottom);
            // INPUT DA TASTIERA
            InputHandler input = new InputHandler(controller, view);
            view.addKeyListener(input);
            view.setFocusable(true);
            view.requestFocusInWindow();
            // CREA IL VISUALIZZATORE DEL GRAFICO DELLE REWARD
            RewardPlotter plot = new RewardPlotter();
            new RewardWindow(plot);
        }
}