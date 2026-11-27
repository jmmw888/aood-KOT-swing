public class EngineState {
    private final int turn;
    private final int indexOfPlayerInTokyo;

    public EngineState(int turn, int indexOfPlayerInTokyo) {
        this.turn = turn;
        this.indexOfPlayerInTokyo = indexOfPlayerInTokyo;
    }

    public int getTurn() {
        return turn;
    }

    public int getIndexOfPlayerInTokyo() {
        return indexOfPlayerInTokyo;
    }
}
