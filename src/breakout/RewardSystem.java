package breakout;

public class RewardSystem {

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