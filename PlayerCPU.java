public interface PlayerCPU {
    boolean[] reroll(int[] dice, EngineState engineState);
    boolean wantToLeave(EngineState engineState);
}
