package breakout;

import java.util.List;
import java.io.FileWriter;
/*
* ClASSE CHE FA DA INTERMEDIARIO TRA L'AGENTE E IL RESTO DEL SISTEMA 
*/
public class RLManager{
    // CAMPI DATI , RIFERIMENTO ALL'AGENTE CHE  GESTISCE
    private RLAgent agente;
    private boolean trainer;
    // COSTRUTTORE
    public RLManager(RLAgent agent){
        System.out.println("RLManager creato");
        this.agente = agent;
    }
    // SCEGLIE L'AZIONE DA FARE ( NEL TRAINING )
    public int chooseAction(int stato){
        return agente.chooseAction(stato);
    }
    // SCEGLIE L?AZIONE MIGLIORE DA FARE ( NEL GIOCO )
    public int chooseBestAction(int stato){
        return agente.chooseBestAction(stato);
    }
    // IMPOSTA IL TRAINING
    public void setTraining(boolean training){
        trainer = training;
    }
    // DICE ALL'AGENTE DI AGGIUNGERE LA REWARD E DI AGGIORNARE LA TABELLA
    public void updateLearning(int state, int action, int reward, int newState){
        if (trainer){
            agente.addReward(reward);
            agente.updateTable(state, action, reward, newState);
        }
    }
    // DICE ALL'AGENTE DI RITORNARE IL REWARD STORICO
    public List<Double> getRewardStorico(){
        return agente.getHistoricalReward();
    }
    // DICE ALL'AGENTE COSA FARE IN CASO DI GAME OVER
    public void endEpisode(){
        if(!trainer) 
            return;
         double total = agente.closeEpisode();
         System.out.println("EPISODIO: " + agente.getEpisodes() +
                           " | epsilon: " + agente.getEpsilon() +
                           " | reward: " + total);
        try(FileWriter fw = new FileWriter("rewards.txt", true)) {
            fw.write(total + "\n");
        } 
        catch(Exception e) {}
        if(agente.getEpisodes() % 10 == 0){
            agente.saveTable("Table.txt");
        }
       if (agente.getEpisodes() >= agente.getMaxEpisodes()){
            System.out.println("TRAINING FINITO!");
            agente.saveTable("Table.txt");
            trainer=false;
        }
    }
}
