package breakout;
/*
* Controller principale del gioco
*/
public class GameController {
    // CAMPI DATI
    private boolean aiAttiva = false;
    private GameState stato;
    private static final int LARGHEZZA = 1600;
    private static final int ALTEZZA= 800;
    private RLManager rlManager;
    private StateEncoder encoder;
    // COSTRUTTORE
    public GameController(GameState stato){
        System.out.println("Controller creato");
        this.stato = stato;
        this.encoder = new StateEncoder();
    }
    // IMPOSTA RLMANAGER
    public void setRLManager(RLManager rlManager){
        this.rlManager = rlManager;
    }
    // ATTIVA L'AI
    public void attivaAI(boolean attiva){
        aiAttiva = attiva;
    }
    // RITORNA SE I MATTONI SONO TUTTI DISTRUTTI
    private boolean tuttiDistrutti(Mattoncino[][] mattoni) {
        for (int i = 0; i < mattoni.length; i++)
            for (int j = 0; j < mattoni[i].length; j++)
                if(!mattoni[i][j].èDistrutto())
                    return false;
            return true;
       }
    // MUOVE A SINISTRA L'ASTICELLA
    public void muoviSinistra() {
        stato.getAsticella().muovitiSinistra();
    }
    // MUOVE A DESTRA L'ASTICELLA 
    public void muoviDestra(int larghezza) {
        stato.getAsticella().muovitiDestra(larghezza);
    }
    // RITORNA LO STATO CORRENTE 
    public GameState getStato() {
        return stato;
    }
    // METODO PER DIRE AL CONTROLLER DI AGGIORNARE LO STATO 
    // ALL'ENCODER CHIEDE LO STATO ATTUALE 
    // INIZIALIAZZA L'AZIONE A 0 ( FERMO ) E LA REWARD A 0
    // CONTROLLA SE L'AI È ATTIVA E SE RLMANAGER È DIVERSO DA NULL
    // E SE SI DICE A RLMANAGER DI SCEGLIERE L'AZIONE ( CHE A SUA VOLTA LO CHIEDE ALL'AGENTE )
    // IN OGNI CASO DICE ALLA PALLINA DI MUOVERSI E DI RIMBALZARE SUI MURI
    // CONTROLLA LO STATO DEI MATTONI E SE LA PALLINA TOCCA IL MATTONCINO 
    // LO DISTRUGGE E INVERTE LA DIREZIONE DELLA PALLINA E DICE ALLO STATO 
    // DI AUMENTARE IL PUNTEGGIO
    // CONTROLLA LA DISTANZA TRA PADDLE E PALLINA ( IN CASO L'AI SIA ATTIVA )
    // CONTROLLA SE LA PARTITA È FINITA 
    // SI FA DARE LO STATO DALL'ENCODER ( CHE PER IL CONTROLLER È IL NUOVO STATO 
    // , DOPO QUELLO SUCCESSO PRIMA E CONTROLLA SE L'AI È ATTIVA E RLMANAGER != NULL 
    // E SE SI , DICE ALL' RLMANAGER DI AGGIORNARE L'APPRENDIMENTO E INFINE CONTROLLA SE TUTTI I MATTONI SONO STATI DITRUTTI 
    // E SE SI DICE ALLO STATO DI CAMBIARE LIVELLO
    public void aggiorna(){

        Pallina pallina = stato.getPallina();
        Asticella asticella = stato.getAsticella();
        Mattoncino[][] mattoni = stato.getMattoncini();

        int statoRL = encoder.encode(stato);
        int azione = 0;
        int reward = 0;
       
        // ===== AI =====
        if(aiAttiva && rlManager != null){
            azione = rlManager.scegliAzione(statoRL);
            
            reward -= 1;  
            if(azione == 1) 
                muoviSinistra();
            if(azione == 2) 
                muoviDestra(1600);
        }

        // ===== UPDATE =====
        pallina.muoviti();
        pallina.rimbalzaMuri(LARGHEZZA, ALTEZZA);

        // ===== PADDLE =====
        if (pallina.rettangolo().intersects(asticella.rettangolo())) {
            pallina.rimbalzaAsticella(asticella);
            reward += 20; // Reward se il paddle prende la pallina
        }

        // ===== MATTONI =====
        for (int i = 0; i < mattoni.length; i++) {
            for (int j = 0; j < mattoni[i].length; j++) {
                Mattoncino m = mattoni[i][j];

                if (!m.èDistrutto() && pallina.rettangolo().intersects(m.rettangolo())) {
                    m.distruggi();
                    pallina.invertiDirezioney();
                    stato.aggiungiPunti(10);

                    reward += 50; // Se la pallina distrugge un mattoncino
                }
            }
        }

        // ===== DISTANZA PALLINA-PADDLE (GUIDA L’AI) =====
        double centroPalla = pallina.dammiX() + 10;
        double centroPaddle = asticella.dammiX() + asticella.dammiLarghezza() / 2;
        double distanza = Math.abs(centroPalla - centroPaddle);

        if (distanza < 50) 
            reward += 2;      // Reward ( Premio ) se il paddle è vicino alla pallina
        else 
            reward -= 2;                    // Punizione se il paddle è lontano dalla pallina

        // ===== GAME OVER =====
        boolean perso = false;

        if (pallina.dammiY() > 800) {
            reward -= 200;   // Punizione se non prende la pallina e finisce la partita
            perso = true;
        }
        
        int nuovoStato = encoder.encode(stato);

        // ===== Q UPDATE =====
        if (aiAttiva && rlManager != null) 
            rlManager.aggiornaApprendimento(statoRL, azione, reward, nuovoStato);
        

        // ===== FINE EPISODIO =====
        if (perso){
            if (aiAttiva && rlManager != null)
                 rlManager.fineEpisodio();

            stato.reset();
          
        }

        // ===== LIVELLO =====
        if (tuttiDistrutti(mattoni)) {
            stato.prossimoLivello();
            pallina.aumentaVelocita(stato.getLivello());
            reward += 300 + stato.getLivello() * 50;
        }
    }
}