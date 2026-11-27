public class Main {
    public static void main(String[] args) {
        final int gameCount = 1000;
        final int playerCount = 4;
        int[] wins = new int[playerCount];

        for (int game = 0; game < gameCount; game++) {
            PlayerCPU[] players = {
                new PlayerStrat(), // Player 0: strategic
                new PlayerRand(),  // Player 1: random
                new PlayerRand(),  // Player 2: random
                new PlayerRand()   // Player 3: random
            };
            int winner = new Engine(players).playGame();
            wins[winner]++;
        }

        for (int i = 0; i < playerCount; i++) {
            System.out.printf("Player %d (%s): %d wins (%.1f%%)%n",
                    i, i == 0 ? "smart" : "random", wins[i],
                    100.0 * wins[i] / gameCount);
        }
    }
}
