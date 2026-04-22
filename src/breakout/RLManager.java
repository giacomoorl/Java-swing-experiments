package breakout;

import java.util.List;
import java.io.FileWriter;
/*
* Classe che fa da intermediario da l'agente e il resto del sistema 
*/
public class RLManager{
    // CAMPI DATI , RIFERIMENTO ALL'AGENTE CHE  GESTISCE
    private AgenteRL agente;
    // COSTRUTTORE
    public RLManager(AgenteRL agente) {
        this.agente = agente;
    }
    
    public void setEpsilon(double e){
        agente.setEpsilon(e);
    }
    
    public int scegliAzione(int stato) {
        return agente.scegliAzione(stato);
    }
    
    public void setTraining(boolean training){
        agente.trainer = training;
    }
    
    public void aggiornaApprendimento(int stato, int azione, int reward, int nuovoStato) {
        if (agente.trainer) {
            agente.aggiungiReward(reward);
            agente.aggiornaTabella(stato, azione, reward, nuovoStato);
        }
    }
    public List<Double> getRewardStorico(){
        return agente.getRewardStorico();
    }
    public void fineEpisodio() {
        if (!agente.trainer) 
            return;

        agente.incrementaEpisodi();
        agente.riduciEpsilon();

        double totale = agente.chiudiEpisodio();
        
        try (FileWriter fw = new FileWriter("rewards.txt", true)) {
            fw.write(totale + "\n");
        } 
        catch(Exception e) {}
        
        System.out.println("EPISODIO: " + agente.dammiEpisodi() +
                           " | epsilon: " + agente.dammiEpsilon() +
                           " | reward: " + totale);

        if (agente.dammiEpisodi() % 10 == 0) {
            agente.salvaTabella("Tabella.txt");
        }
       
        if (agente.dammiEpisodi() >= agente.dammiMaxEpisodi()) {
            System.out.println("TRAINING FINITO!");

            agente.salvaTabella("Tabella.txt");
            agente.trainer = false;
            agente.setEpsilon(0);
        }
    }
}
