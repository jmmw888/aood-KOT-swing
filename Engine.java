import java.util.Arrays;
import java.util.Random;

public class Engine {
    private static final int DICE_COUNT = 6;
    private static final int MAX_REROLLS = 2;
    private static final int WINNING_FAME = 20;

    private final PlayerCPU[] cpus;
    private final boolean debug;
    private final Random random = new Random();
    private Player[] players;
    private int turn;
    private int inTokyo;
    private boolean extraTurnPossible;

    public Engine(PlayerCPU[] cpus, boolean debug) {
        this.cpus = cpus;
        this.debug = debug;
    }

    public Engine(PlayerCPU[] cpus) {
        this(cpus, false);
    }

    private void enterTokyo(int playerIndex, int fame) {
        if (debug && inTokyo != playerIndex) {
            System.out.println("Player " + inTokyo + " leaves Tokyo");
        }
        inTokyo = playerIndex;
        players[playerIndex].fame += fame;
        players[playerIndex].justEnteredTokyo = true;
        if (debug) System.out.println("Player " + playerIndex + " enters Tokyo (" + fame + " fame)");
    }

    public State getState(int myIndex) {
        int[] healths = new int[players.length];
        int[] fames = new int[players.length];
        for (int i = 0; i < players.length; i++) {
            healths[i] = players[i].health;
            fames[i] = players[i].fame;
        }
        return new State(myIndex, healths, fames, turn, inTokyo, extraTurnPossible);
    }

    private EngineState engineState() {
        return new EngineState(turn, inTokyo);
    }

    public int playGame() {
        players = new Player[cpus.length];
        for (int i = 0; i < cpus.length; i++) {
            players[i] = new Player();
        }
        turn = 0;
        inTokyo = 0;
        extraTurnPossible = true;
        enterTokyo(0, 0);

        while (checkWin(true) == -1) {
            if (players[turn].health <= 0) {
                advanceTurn();
                continue;
            }
            if (players[turn].justEnteredTokyo) {
                players[turn].justEnteredTokyo = false;
            } else if (turn == inTokyo && extraTurnPossible) {
                players[turn].fame += 2;
            }
            if (checkWin(true) >= 0) break;
            resolveDice(roll());
        }
        return checkWin(false);
    }

    private void advanceTurn() {
        turn = (turn + 1) % players.length;
        extraTurnPossible = true;
    }

    public int[] roll() {
        int[] dice = new int[DICE_COUNT];
        for (int i = 0; i < dice.length; i++) dice[i] = random.nextInt(6) + 1;

        for (int attempt = 0; attempt < MAX_REROLLS; attempt++) {
            if (debug) System.out.println("Player " + turn + " rolled " + Arrays.toString(dice));
            boolean[] rerolls = cpus[turn].reroll(dice.clone(), engineState());
            boolean rerolled = false;
            for (int i = 0; i < DICE_COUNT; i++) {
                if (rerolls[i]) {
                    dice[i] = random.nextInt(6) + 1;
                    rerolled = true;
                }
            }
            if (!rerolled) break;
        }
        if (debug) System.out.println("Player " + turn + " final dice " + Arrays.toString(dice));
        return dice;
    }

    public void resolveDice(int[] dice) {
        int[] counts = new int[7];
        for (int die : dice) {
            counts[die]++;
        }

        int fame = 0;
        for (int number = 1; number <= 3; number++) {
            if (counts[number] >= 3) fame += number + counts[number] - 3;
        }
        players[turn].fame += fame;

        int attack = counts[6];
        if (turn == inTokyo) {
            for (int i = 0; i < players.length; i++) {
                if (i != inTokyo && players[i].health > 0) {
                    players[i].health -= attack;
                    if (attack > 0 && players[i].health <= 0) players[turn].fame++;
                }
            }
        } else {
            players[turn].health = Math.min(10, players[turn].health + counts[5]);
            if (attack > 0 && players[inTokyo].health > 0) {
                players[inTokyo].health -= attack;
                if (players[inTokyo].health <= 0) {
                    enterTokyo(turn, 2);
                } else if (cpus[inTokyo].wantToLeave(engineState())) {
                    enterTokyo(turn, 1);
                }
            }
        }

        if (debug) System.out.println("Player " + turn + " has " + players[turn].health
                + " health and " + players[turn].fame + " fame");
        if (counts[4] >= 3 && extraTurnPossible && checkWin(true) == -1) {
            extraTurnPossible = false;
        } else {
            advanceTurn();
        }
    }

    public int checkWin(boolean quiet) {
        for (int i = 0; i < players.length; i++) {
            if (players[i].fame >= WINNING_FAME) {
                if (!quiet) System.out.println("Player " + i + " wins with " + players[i].fame + " fame");
                return i;
            }
        }
        int survivor = -1;
        for (int i = 0; i < players.length; i++) {
            if (players[i].health > 0) {
                if (survivor != -1) return -1;
                survivor = i;
            }
        }
        if (survivor >= 0 && !quiet) System.out.println("Player " + survivor + " wins as the last survivor");
        return survivor;
    }
}
