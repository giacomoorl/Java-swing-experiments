package breakout;

public class GameController {
    // CAMPI DATI
    private boolean aiAttiva = false;
    private GameState stato;
    private static final int LARGHEZZA = 1600;
    private static final int ALTEZZA= 800;
    private RLManager rlManager;
    private StateEncoder encoder;
    
    public GameController(GameState stato){
        System.out.println("Controller creato");
        this.stato = stato;
        this.encoder = new StateEncoder();
    }
    public void setRLManager(RLManager rlManager){
        this.rlManager = rlManager;
    }
  

    public void attivaAI(boolean attiva){
        aiAttiva = attiva;
    }

    public void aggiorna(){

        Pallina pallina = stato.getPallina();
        Asticella asticella = stato.getAsticella();
        Mattoncino[][] mattoni = stato.getMattoncini();

        int statoRL = encoder.encode(stato);
        int azione = 0;
        int reward = 0;
       
        // ===== AI =====
        if (aiAttiva && rlManager != null){
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
            rlManager.step(statoRL, azione, reward, nuovoStato);
        

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

    private boolean tuttiDistrutti(Mattoncino[][] mattoni) {
        for (int i = 0; i < mattoni.length; i++)
            for (int j = 0; j < mattoni[i].length; j++)
                if (!mattoni[i][j].èDistrutto())
                    return false;
        return true;
    }

    public void muoviSinistra() {
        stato.getAsticella().muovitiSinistra();
    }

    public void muoviDestra(int larghezza) {
        stato.getAsticella().muovitiDestra(larghezza);
    }

    public GameState getStato() {
        return stato;
    }
}