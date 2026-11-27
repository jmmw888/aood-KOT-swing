public class State {
    public int myIndex;
    public int[] healths;
    public int[] fames;
    public int currentPlayer;
    public int inTokyo;
    public boolean extraTurnable;

    public State(int myIndex, int[] healths, int[] fames, int currentPlayer, 
    int inTokyo, boolean extraTurnable) {
        this.myIndex = myIndex;
        this.healths = healths.clone();
        this.fames = fames.clone();
        this.currentPlayer = currentPlayer;
        this.inTokyo = inTokyo;
        this.extraTurnable = extraTurnable;
    }
}
