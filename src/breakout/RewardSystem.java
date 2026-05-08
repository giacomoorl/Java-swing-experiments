package breakout;

public class RewardSystem {
    public RewardSystem(){
          System.out.println("RewardSystem creato");
    }
    public int paddleReward() {
        return 5;
    }

    public int brickReward() {
        return 40;
    }

    public int levelReward() {
        return 50;
    }

    public int deathPenalty() {
        return -50;
    }
}