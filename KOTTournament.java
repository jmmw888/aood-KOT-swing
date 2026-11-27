import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class KOTTournament implements ActionListener {
    JFrame frame;
    JPanel contentPane, playerPanel;
    JTextField gamesField;
    JComboBox<Integer> playerCount;
    JComboBox<String>[] playerTypes;
    JButton play;
    JTextArea results;

    public void makeUI() {
        frame = new JFrame("King of Tokyo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        contentPane = new JPanel();
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));

        contentPane.add(new JLabel("How many games?"));
        gamesField = new JTextField("10", 10);
        contentPane.add(gamesField);

        contentPane.add(new JLabel("How many players?"));
        playerCount = new JComboBox<Integer>(new Integer[]{2, 3, 4, 5, 6});
        playerCount.addActionListener(this);
        contentPane.add(playerCount);

        playerTypes = new JComboBox[6];
        for (int i = 0; i < playerTypes.length; i++) {
            playerTypes[i] = new JComboBox<String>(new String[]{"PlayerStrat", "PlayerRand"});
            if (i > 0) playerTypes[i].setSelectedItem("PlayerRand");
        }
        playerPanel = new JPanel();
        playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.PAGE_AXIS));
        contentPane.add(playerPanel);
        showPlayers();

        play = new JButton("Click to play!");
        play.addActionListener(this);
        contentPane.add(play);

        results = new JTextArea(10, 30);
        results.setEditable(false);
        contentPane.add(new JScrollPane(results));

        frame.setContentPane(contentPane);
        frame.pack();
        frame.setVisible(true);
    }

    public void showPlayers() {
        playerPanel.removeAll();
        int count = (Integer) playerCount.getSelectedItem();
        for (int i = 0; i < count; i++) {
            JPanel row = new JPanel();
            row.add(new JLabel("Player " + (i + 1)));
            row.add(playerTypes[i]);
            playerPanel.add(row);
        }
        playerPanel.revalidate();
        playerPanel.repaint();
    }

    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == playerCount) {
            showPlayers();
            frame.pack();
        }

        if (event.getSource() == play) {
            int games;
            try {
                games = Integer.parseInt(gamesField.getText());
                if (games < 1 || games > 100000) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Enter a number of games from 1 to 100000.");
                return;
            }

            int count = (Integer) playerCount.getSelectedItem();
            int[] wins = new int[count];
            for (int game = 0; game < games; game++) {
                PlayerCPU[] players = new PlayerCPU[count];
                for (int i = 0; i < count; i++) {
                    String type = (String) playerTypes[i].getSelectedItem();
                    if (type.equals("PlayerStrat")) players[i] = new PlayerStrat();
                    if (type.equals("PlayerRand")) players[i] = new PlayerRand();
                }
                Engine engine = new Engine(players);
                int winner = engine.playGame();
                wins[winner]++;
            }

            results.setText("Results:\n");
            int mostWins = 0;
            for (int i = 0; i < count; i++) {
                double percent = 100.0 * wins[i] / games;
                results.append("Player " + (i + 1) + ": " + wins[i] + " wins (" + String.format("%.1f", percent) + "%)\n");
                if (wins[i] > mostWins) mostWins = wins[i];
            }
            results.append("\nWinner(s): ");
            for (int i = 0; i < count; i++) {
                if (wins[i] == mostWins) results.append("Player " + (i + 1) + "  ");
            }
        }
    }

    public static void main(String[] args) {
        KOTTournament app = new KOTTournament();
        app.makeUI();
    }
}
