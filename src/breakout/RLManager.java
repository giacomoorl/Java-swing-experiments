package breakout;

import java.util.List;
import java.io.FileWriter;
/*
* Classe che fa da intermediario da l'agente e il resto del sistema 
*/
public class RLManager{
    // CAMPI DATI , RIFERIMENTO ALL'AGENTE CHE  GESTISCE
    private RLAgent agente;
    // COSTRUTTORE
    public RLManager(RLAgent agent) {
        this.agente = agent;
    }
    // DICE ALL'AGENTE DI IMPOSTARE EPSILON
    /*public void setEpsilon(double e){
        agente.setEpsilon(e);
    }*/
    // SCEGLIE L'AZIONE DA FARE
    public int chooseAction(int stato) {
        return agente.chooseAction(stato);
    }
    // IMPOSTA IL TRAINING
    public void setTraining(boolean training){
        agente.trainer = training;
    }
    // DICE ALL'AGENTE DI AGGIUNGERE LA REWARD E DI AGGIORNARE LA TABELLA
    public void updateLearning(int state, int action, int reward, int newState) {
        if (agente.trainer) {
            agente.addReward(reward);
            agente.updateTable(state, action, reward, newState);
        }
    }
    // DICE ALL'AGENTE DI RITORNARE IL REWARD STORICO
    public List<Double> getRewardStorico(){
        return agente.getHistoricalReward();
    }
    // DICE ALL'AGENTE COSA FARE IN CASO DI GAME OVER
    public void endEpisode() {
        if (!agente.trainer) 
            return;
         
        double total = agente.closeEpisode();
        
        System.out.println("EPISODIO: " + agente.getEpisodes() +
                           " | epsilon: " + agente.getEpsilon() +
                           " | reward: " + total);
        
        try (FileWriter fw = new FileWriter("rewards.txt", true)) {
            fw.write(total + "\n");
        } 
        catch(Exception e) {}

        if (agente.getEpisodes() % 10 == 0) {
            agente.saveTable("Table.txt");
        }
       
        if (agente.getEpisodes() >= agente.getMaxEpisodes()) {
            System.out.println("TRAINING FINITO!");

            agente.saveTable("Table.txt");
            agente.trainer = false;
        }
    }
}
