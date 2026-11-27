
public class PlayerStrat implements PlayerCPU {
    public boolean[] reroll(int[] dice, EngineState state) {
        boolean inTokyo =
            state.getTurn() == state.getIndexOfPlayerInTokyo();

        int sixes = 0;
        for (int die : dice) {
            if (die == 6) sixes++;
        }

        boolean[] reroll = new boolean[dice.length];

        for (int i = 0; i < dice.length; i++) {
            if (dice[i] == 4) {
                reroll[i] = true;
            } else if (dice[i] == 5) {
                reroll[i] = inTokyo;
            } else if (dice[i] == 6) {
                reroll[i] = sixes < 2;
            } else {
                reroll[i] = true;
            }
        }

        return reroll;
    }

    public boolean wantToLeave(EngineState state) {
        return true;
    }
}
