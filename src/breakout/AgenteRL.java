package breakout;
import java.io.*;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
/* 
* CLASSE AGENTE AI CHE IMPARA CON APPRENDIMENTO PER RINFORZO
*/
public class AgenteRL {
    // CAMPI DATI
    private double[][] tabellaQ;
    private final double alfa = 0.1;
    private final double gamma = 0.9;
    private double epsilon = 0.8;
    private List<Double> rewardStorico = new ArrayList<>();

    public boolean trainer = true;
    private final Random casuale;
    private int episodi = 0;
    private int maxEpisodi = 5000;
    private double rewardEpisodio = 0;
    
    public AgenteRL(int numStati, int numAzioni){
        tabellaQ = new double[numStati][numAzioni];
        casuale = new Random();

        // INIZIALIZZA QTABLE A ZERO
        for (int i = 0; i < numStati; i++)
            for (int j = 0; j < numAzioni; j++)
                tabellaQ[i][j] = 0.0;
    }
    public void salvaRewardEpisodio(double r){
        rewardStorico.add(r);
    }

    public List<Double> getRewardStorico(){
        return rewardStorico;
    }
    // ===== GETTERS =====
    public double[][] dammiTabellaQ(){
        return tabellaQ; 
    }
    public int dammiEpisodi(){ 
        return episodi;
    }
    public double dammiEpsilon(){
        return epsilon; 
    }
    public int dammiMaxEpisodi(){
        return maxEpisodi; 
    }

    public void incrementaEpisodi(){
        episodi++; 
    }
    
    public void aggiungiReward(double r){
        rewardEpisodio += r;
    }
    
   public double chiudiEpisodio(){
        double totale = rewardEpisodio;
        rewardEpisodio = 0;
        return totale;
    }
    // ===== AZIONE =====
    public int scegliAzione(int stato){
        if (stato < 0 || stato >= tabellaQ.length) return 0;
        if (casuale.nextDouble() < epsilon)
            return casuale.nextInt(tabellaQ[stato].length);
        else {
            int migliore = 0;
            double maxQ = tabellaQ[stato][0];
            for (int i = 1; i < tabellaQ[stato].length; i++)
                if (tabellaQ[stato][i] > maxQ) {
                    maxQ = tabellaQ[stato][i];
                    migliore = i;
                }
            return migliore;
        }
    }
    // ===== RIDUCI EPSILON =====
    public void riduciEpsilon(){
        epsilon *= 0.995;
        if (epsilon < 0.01) 
            epsilon = 0.01;
    }
    // ===== AGGIORNA Q-TABLE =====
    public void aggiornaTabellaQ(int stato, int azione, int ricompensa, int nuovoStato){
        if (stato < 0 || nuovoStato < 0 || stato >= tabellaQ.length || nuovoStato >= tabellaQ.length)
            return;

        double maxFuturo = tabellaQ[nuovoStato][0];
        for (int i = 1; i < tabellaQ[nuovoStato].length; i++)
            if (tabellaQ[nuovoStato][i] > maxFuturo)
                maxFuturo = tabellaQ[nuovoStato][i];

        tabellaQ[stato][azione] += alfa * (ricompensa + gamma * maxFuturo - tabellaQ[stato][azione]);
    }
    // SALVA Q-TABLE
    public void salvaTabella(String nomeFile){
        try (PrintWriter out = new PrintWriter(nomeFile)){

            for (int i = 0; i < tabellaQ.length; i++){
                for (int j = 0; j < tabellaQ[i].length; j++){
                    out.print(tabellaQ[i][j] + " ");
                }
                out.println();
            }

            // salva epsilon UNA VOLTA
            out.println("#EPSILON " + epsilon);

            // salva episodi
            out.println("#EPISODI " + episodi);

        } 
        catch(Exception e){
            System.out.println("Errore nel salvataggio Q-table");
        }
    }
    // CARICA Q-TABLE
    public void caricaTabella(String nomeFile){
        File f = new File(nomeFile);
        if (!f.exists()) 
            return;

        try (BufferedReader br = new BufferedReader(new FileReader(f))){
            String line;
            int i = 0;

            while((line = br.readLine()) != null){

                if (line.startsWith("#EPISODI")){
                    this.episodi = Integer.parseInt(line.split(" ")[1]);
                    continue;
                }

                if (line.startsWith("#EPSILON")){
                    this.epsilon = Double.parseDouble(line.split(" ")[1]);
                    continue;
                }

                if (i >= tabellaQ.length)
                    break;

                String[] valori = line.trim().split("\\s+");

                for(int j = 0; j < valori.length && j < tabellaQ[i].length; j++){
                    tabellaQ[i][j] = Double.parseDouble(valori[j]);
                }
                i++;
            }
        } 
        catch(Exception e){
            System.out.println("Errore nel caricamento Q-table, si parte da zero");
            this.episodi = 0;
        }
    }
    public void setEpsilon(double epsilon){
        this.epsilon = epsilon;
    }
}