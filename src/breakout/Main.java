package breakout;
/*
* CLASSE CHE AVVIA L'ESECUZIONE DEL GIOCO 
*/
public class Main {
        public static void main(String[] args) {
            GameState state = new GameState();
            GameController controller = new GameController(state);
            GameView view = new GameView(state);
            // PANNELLOSUPERIORE
            TopPanel top = new TopPanel();
            // GAMELOOP
            GameLoop loop = new GameLoop(controller, view, top);
            // ✅ CREA L'AI
            int numStati = state.numTotalState();  
            RLAgent agente = new RLAgent(numStati, 3);   
            agente.loadTable("Table.txt"); 
            // CREA IL MANAGER AI
            RLManager rlManager = new RLManager(agente);
            controller.setRLManager(rlManager);
            // PANNELLO INFERIORE CON PULSANTI
            BottomPanel bottom = new BottomPanel(loop, view, controller, rlManager);
            // FINESTRA PRINCIPALE
            new MainWindow(view, top, bottom);
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