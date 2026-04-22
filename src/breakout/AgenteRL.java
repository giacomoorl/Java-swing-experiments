package breakout;

import java.io.*;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
/* 
* CLASSE AGENTE AI CHE IMPARA CON APPRENDIMENTO PER RINFORZO
* USA UNA TABELLA (Q-TABLE) PER IMPARARE QUALI AZIONI SONO MIGLIORI
*/
public class AgenteRL {
    // CAMPI DATI
    private double[][] tabella;
    private final double alfa = 0.1;
    private final double gamma = 0.9;
    private double epsilon = 0.8;
    private List<Double> rewardStorico = new ArrayList<>();
    public boolean trainer = true;
    private final Random casuale;
    private int episodi = 0;
    private int maxEpisodi = 5000;
    private double rewardEpisodio = 0;
    // COSTRUTTORE 
    // INIZIALIZZA TABELLA DI APPRENDIMENTO A ZERO
    public AgenteRL(int numStati, int numAzioni){
        tabella = new double[numStati][numAzioni];
        casuale = new Random();
        for (int i = 0; i < numStati; i++)
            for (int j = 0; j < numAzioni; j++)
                tabella[i][j] = 0.0;
    }
    // SALVA LA RICOMPENSA DI OGNI EPISODIO
    public void salvaRewardEpisodio(double r){
        rewardStorico.add(r);
    }
    // RITORNA IL REWARD STORICO
    public List<Double> getRewardStorico(){
        return rewardStorico;
    }
    // RITORNA A TABELLA 
    public double[][] dammiTabella(){
        return tabella; 
    }
    // RITORNA IL NUMERO DI EPISODIO
    public int dammiEpisodi(){ 
        return episodi;
    }
    // RITORNA EPSILON
    public double dammiEpsilon(){
        return epsilon; 
    }
    // RITORNA IL NUMERO MASSIMO DI EPISODI
    public int dammiMaxEpisodi(){
        return maxEpisodi; 
    }
    // INCREMENTA IL NUMERO DI EPISODI
    public void incrementaEpisodi(){
        episodi++; 
    }
    // AGGIUNGE REWARD ALL'EPISODIO CORRENTE
    public void aggiungiReward(double r){
        rewardEpisodio += r;
    }
    // CHIUDE L'EPISODIO E RESTITUISCE IL REWARD TOTALE
    // RESETTA IL CONTATORE PER IL PROSSIMO EPISODIO
   public double chiudiEpisodio(){
        double totale = rewardEpisodio;
        rewardEpisodio = 0;
        return totale;
    }
    // SCEGLIE L'AZIONE DA FARE IN BASE ALLO STATO
    // A VOLTE FA MOSSE RANDOM (ESPLORA)
    // A VOLTE SCEGLIE LA MIGLIORE (USA QUELLO CHE HA IMPARATO
    public int scegliAzione(int stato){
        if (stato < 0 || stato >= tabella.length) return 0;
        if (casuale.nextDouble() < epsilon)
            return casuale.nextInt(tabella[stato].length);
        else {
            int migliore = 0;
            double max = tabella[stato][0];
            for (int i = 1; i < tabella[stato].length; i++)
                if (tabella[stato][i] > max) {
                    max = tabella[stato][i];
                    migliore = i;
                }
            return migliore;
        }
    }
    // IMPOSTA EPSILON
    public void setEpsilon(double epsilon){
        this.epsilon = epsilon;
    }
    // RIDUCE L'ESPLORAZIONE NEL TEMPO
    // L'AI DIVENTA SEMPRE PIÙ "SICURA"
    public void riduciEpsilon(){
        epsilon *= 0.995;
        if (epsilon < 0.01) 
            epsilon = 0.01;
    }
    // AGGIORNA LA Q-TABLE IN BASE ALLA RICOMPENSA OTTENUTA
    // PIÙ UNA SCELTA È BUONA → PIÙ AUMENTA IL SUO VALORE
    public void aggiornaTabella(int stato, int azione, int ricompensa, int nuovoStato){
        if (stato < 0 || nuovoStato < 0 || stato >= tabella.length || nuovoStato >= tabella.length)
            return;

        double maxFuturo = tabella[nuovoStato][0];
        for (int i = 1; i < tabella[nuovoStato].length; i++)
            if (tabella[nuovoStato][i] > maxFuturo)
                maxFuturo = tabella[nuovoStato][i];

        tabella[stato][azione] += alfa * (ricompensa + gamma * maxFuturo - tabella[stato][azione]);
    }
    // SALVA LA TABELLA DI APPRENDIMENTO
    public void salvaTabella(String nomeFile){
        try(PrintWriter out = new PrintWriter(nomeFile)){
            for(int i = 0; i < tabella.length; i++){
                for(int j = 0; j < tabella[i].length; j++){
                    out.print(tabella[i][j] + " ");
                }
                out.println();
            }
            out.println("#EPSILON " + epsilon);
            out.println("#EPISODI " + episodi);
        } 
        catch(Exception e){
            System.out.println("Errore nel salvataggio Q-table");
        }
    }
    // CARICA LA TABELLA DI APPRENDIMENTO
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
                if(i >= tabella.length)
                    break;
                String[] valori = line.trim().split("\\s+");
                for(int j = 0; j < valori.length && j < tabella[i].length; j++){
                    tabella[i][j] = Double.parseDouble(valori[j]);
                }
                i++;
            }
        } 
        catch(Exception e){
            System.out.println("Errore nel caricamento Q-table, si parte da zero");
            this.episodi = 0;
        }
    }
}   