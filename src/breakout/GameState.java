package breakout;
/*
* Classe che dice all'asticella e la pallina dove mettersi
*/
public class GameState {
    // CAMPI DATI 
    private Asticella asticella;
    private Pallina pallina;
    private Mattoncino[][] mattoncini;

    private int punti;
    private int livello;
    private boolean aiAttiva;
   
    private final int livelloMassimo = 5;
    private final int numRighe = 5;
    private final int numColonne = 10;
    // COSTRUTTORE
    public GameState() {
        System.out.println("Stato creato");
        livello = 1;
        punti = 0;
        aiAttiva = false;

        asticella = new Asticella(750, 700);
        pallina = new Pallina(800, 670);

        creaMattoncini();
    }

    private void creaMattoncini() {
        mattoncini = new Mattoncino[numRighe][numColonne];

        java.awt.Color[] colori = {
            java.awt.Color.RED,
            java.awt.Color.ORANGE,
            java.awt.Color.YELLOW,
            java.awt.Color.GREEN,
            java.awt.Color.CYAN
        };

        int gap = 10;
        int totaleGap = gap * (numColonne + 1);
        int brickWidth = (1600 - totaleGap) / numColonne;
        int brickHeight = 30;
        int yStart = 50;

        for (int i = 0; i < numRighe; i++) {
            for (int j = 0; j < numColonne; j++) {
                int x = gap + j * (brickWidth + gap);
                int y = yStart + i * (brickHeight + gap);

                mattoncini[i][j] = new Mattoncino(x, y, brickWidth, brickHeight, colori[i]);
            }
        }
    }

    public void reset() {
        asticella.impostaX(750);
        asticella.impostaY(700);
        pallina.impostaPosizione(800, 670);
        pallina.impostaDX(3);
        pallina.impostaDY(-3);
        creaMattoncini();
        punti = 0;
        livello = 1;
    }

    public int numeroTotaleStati() {
        return 5 * 2 * 4 * 5; 
    }

    // ===== GETTER =====
    public Asticella getAsticella(){
        return asticella;
    }
    public Pallina getPallina(){
        return pallina; 
    }
    public Mattoncino[][] getMattoncini(){ 
        return mattoncini;
    }
    public int getPunti(){
        return punti; 
    }
    public int getLivello(){
        return livello; 
    }

    public void aggiungiPunti(int p){
        punti += p; 
    }

    public void prossimoLivello(){
        if (livello < livelloMassimo){
            livello++;
            // reset posizioni
            asticella.impostaX(750);
            asticella.impostaY(700);

            pallina.impostaPosizione(800, 670);
            creaMattoncini();
            pallina.aumentaVelocita(livello);
        }
    }
}