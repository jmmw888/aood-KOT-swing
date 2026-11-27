import java.util.Random;

public class PlayerRand implements PlayerCPU {
    private final Random rand = new Random();

    @Override
    public boolean[] reroll(int[] dice, EngineState engineState) {
        boolean[] decisions = new boolean[dice.length];
        for (int i = 0; i < decisions.length; i++) {
            decisions[i] = rand.nextBoolean();
        }
        return decisions;
    }

    @Override
    public boolean wantToLeave(EngineState engineState) {
        return rand.nextBoolean();
    }
}
