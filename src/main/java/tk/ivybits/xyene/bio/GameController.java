package tk.ivybits.xyene.bio;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class GameController {
    public PlayerPiece red;
    public PlayerPiece blue;
    public PlayerPiece[] players;
    public PlayerPiece currentPlayer;

    public GameController(int redType, int blueType) {
        red = new PlayerPiece(this, PlayerPiece.Team.RED, redType);
        blue = new PlayerPiece(this, PlayerPiece.Team.BLUE, blueType);
        currentPlayer = red;
        players = new PlayerPiece[]{red, blue};
    }

    public void nextTurn() {
        currentPlayer = players[(currentPlayer.team.ordinal() + 1) % 2];
        if (currentPlayer.turnsMissed > 0) {
            currentPlayer.turnsMissed--;
            currentPlayer = players[(currentPlayer.team.ordinal() + 1) % 2];
        }
    }

    public void draw(Graphics2D g) {
        if (currentPlayer == red) blue.draw(g, false);
        else red.draw(g, false);
        currentPlayer.draw(g, true);

        String txt = Application.gamePanel.pane.getText();
        String bootstrap = CardDef.BOOTSTRAP_CARDS[currentPlayer.getPhase()][1 + Arrays.binarySearch(CardDef.CellType.TYPES, currentPlayer.cellType)];
        if(!txt.equals(bootstrap)) {
            Application.gamePanel.pane.setText("\n\n\n" + bootstrap);
            Application.gamePanel.pane.revalidate();
        }

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(new Color(0, 0, 0, 0.5f));
        g.fillRect(0, 430, 340, 100);
        g.setColor(new Color(0xB8E64C));
        g.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 15));
        String disp = String.format("%s (%s) player's turn!", currentPlayer.team.humanName,
                CardDef.CellType.LABELS[Arrays.binarySearch(CardDef.CellType.TYPES, currentPlayer.cellType)]);
        String phase = CardDef.Phase.LABELS[currentPlayer.getPhase()];
        g.drawString(disp, 5, 450);
        g.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD | Font.ITALIC, 15));
        g.drawString(phase, 10, 470);
    }

    private boolean inGame = true;

    int[] phases = {-1, -1};

    public void dieRolled(int roll) {
        System.out.println("Rolled a " + roll);
        if (inGame) {
            currentPlayer.inc(roll, () -> {
                inGame = false;

                int phase = currentPlayer.getPhase();

                java.util.List<CardDef> cardSet = CardDef.CARDS[phase][Arrays.binarySearch(CardDef.CellType.TYPES, currentPlayer.cellType)];
                int rand = (int) Math.ceil(Math.random() * cardSet.size());
                CardDef def = cardSet.get(rand - 1);
                Application.gamePanel.overlay.setCard(def);
                Application.gamePanel.overlay.setVisible(true);
                if (def.requiresRoll) {
                    Application.gamePanel.dieButton.reset();
                } else {
                    Application.gamePanel.dieButton.removeAll();
                    Application.gamePanel.dieButton.revalidate();
                    dieRolled(-1);
                }
            });
        } else {
            new Thread(() -> {
                try {
                    Thread.sleep(Application.gamePanel.overlay.card.phases == -1 ? 7000 : 3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                SwingUtilities.invokeLater(() -> {
                    Application.gamePanel.overlay.card.callback.callback(currentPlayer, roll);
                    Application.gamePanel.overlay.setVisible(false);
                    Application.gamePanel.dieButton.reset();
                    nextTurn();
                });
                inGame = true;
            }).start();
        }
    }
}
