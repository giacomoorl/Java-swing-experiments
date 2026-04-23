package breakout;

import java.io.*;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
/* 
* CLASSE AGENTE AI CHE IMPARA CON APPRENDIMENTO PER RINFORZO
* USA UNA TABELLA (Q-TABLE) PER IMPARARE QUALI AZIONI SONO MIGLIORI
*/
public class RLAgent {
    // CAMPI DATI
    private double[][] table;
    private final double alfa = 0.1;
    private final double gamma = 0.9;
    private double epsilon = 0.8;
    private List<Double> historicalReward = new ArrayList<>();
    public boolean trainer = true;
    private final Random casual;
    private int episodes = 0;
    private int maxEpisodes = 5000;
    private double rewardEpisode = 0;
    // COSTRUTTORE , INIZIALIZZA TABELLA DI APPRENDIMENTO A ZERO
    public RLAgent(int numState, int numActions){
        table = new double[numState][numActions];
        casual = new Random();
        for (int i = 0; i < numState; i++)
            for (int j = 0; j < numActions; j++)
                table[i][j] = 0.0;
    }
    // SALVA LA RICOMPENSA DI OGNI EPISODIO
    public void saveRewardEpisode(double r){
        historicalReward.add(r);
    }
    // RITORNA LA RICOMPENSA STORICA
    public List<Double> getHistoricalReward(){
        return historicalReward;
    }
    // RITORNA A TABELLA 
    public double[][] getTable(){
        return table; 
    }
    // RITORNA IL NUMERO DI EPISODIO
    public int getEpisodes(){ 
        return episodes;
    }
    // RITORNA EPSILON
    public double getEpsilon(){
        return epsilon; 
    }
    // RITORNA IL NUMERO MASSIMO DI EPISODI
    public int getMaxEpisodes(){
        return maxEpisodes; 
    }
    // INCREMENTA IL NUMERO DI EPISODI
    public void increasesEpisodes(){
        episodes++; 
    }
    // AGGIUNGE REWARD ALL'EPISODIO CORRENTE
    public void addReward(double r){
        rewardEpisode += r;
    }
    // CHIUDE L'EPISODIO E RESTITUISCE IL REWARD TOTALE
    // RESETTA IL CONTATORE PER IL PROSSIMO EPISODIO
   public double closeEpisode(){
        double total = rewardEpisode;
        rewardEpisode = 0;
        return total;
    }
    // SCEGLIE L'AZIONE DA FARE IN BASE ALLO STATO
    // A VOLTE FA MOSSE RANDOM (ESPLORA)
    // A VOLTE SCEGLIE LA MIGLIORE (USA QUELLO CHE HA IMPARATO
    public int chooseAction(int state){
        if (state < 0 || state >= table.length) return 0;
        if (casual.nextDouble() < epsilon)
            return casual.nextInt(table[state].length);
        else {
            int best = 0;
            double max = table[state][0];
            for (int i = 1; i < table[state].length; i++)
                if (table[state][i] > max) {
                    max = table[state][i];
                    best = i;
                }
            return best;
        }
    }
    // IMPOSTA EPSILON
    public void setEpsilon(double epsilon){
        this.epsilon = epsilon;
    }
    // RIDUCE L'ESPLORAZIONE NEL TEMPO
    // L'AI DIVENTA SEMPRE PIÙ "SICURA"
    public void reduceEpsilon(){
        epsilon *= 0.995;
        if (epsilon < 0.01) 
            epsilon = 0.01;
    }
    // AGGIORNA LA Q-TABLE IN BASE ALLA RICOMPENSA OTTENUTA
    // PIÙ UNA SCELTA È BUONA → PIÙ AUMENTA IL SUO VALORE
    public void updateTable(int state, int action, int reward, int newState){
        if (state < 0 || newState < 0 || state >= table.length || newState >= table.length)
            return;

        double maxFuturo = table[newState][0];
        for (int i = 1; i < table[newState].length; i++)
            if (table[newState][i] > maxFuturo)
                maxFuturo = table[newState][i];

        table[state][action] += alfa * (reward + gamma * maxFuturo - table[state][action]);
    }
    // SALVA LA TABELLA DI APPRENDIMENTO
    public void saveTable(String nameFile){
        try(PrintWriter out = new PrintWriter(nameFile)){
            for(int i = 0; i < table.length; i++){
                for(int j = 0; j < table[i].length; j++){
                    out.print(table[i][j] + " ");
                }
                out.println();
            }
            out.println("#EPSILON " + epsilon);
            out.println("#EPISODI " + episodes);
        } 
        catch(Exception e){
            System.out.println("Errore nel salvataggio Q-table");
        }
    }
    // CARICA LA TABELLA DI APPRENDIMENTO
    public void loadTable(String nameFile){
        File f = new File(nameFile);
        if (!f.exists()) 
            return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))){
            String line;
            int i = 0;
            while((line = br.readLine()) != null){
                if (line.startsWith("#EPISODI")){
                    this.episodes = Integer.parseInt(line.split(" ")[1]);
                    continue;
                }
                if (line.startsWith("#EPSILON")){
                    this.epsilon = Double.parseDouble(line.split(" ")[1]);
                    continue;
                }
                if(i >= table.length)
                    break;
                String[] values = line.trim().split("\\s+");
                for(int j = 0; j < values.length && j < table[i].length; j++){
                    table[i][j] = Double.parseDouble(values[j]);
                }
                i++;
            }
        } 
        catch(Exception e){
            System.out.println("Errore nel caricamento Q-table, si parte da zero");
            this.episodes = 0;
        }
    }
}   