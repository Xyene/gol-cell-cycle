package tk.ivybits.xyene.bio;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class CellCellectionPanel extends JPanel {
    public CellCellectionPanel() {
        super(new BorderLayout());
        setSize(Application.VIEWPORT);
        setLayout(new GridLayout(0, 1));

        DefaultComboBoxModel<String> cell1, cell2;

        JComboBox<String> player1 = new JComboBox<>(cell1 = new DefaultComboBoxModel<>(CardDef.CellType.LABELS));
        JComboBox<String> player2 = new JComboBox<>(cell2 = new DefaultComboBoxModel<>(CardDef.CellType.LABELS));

        class IconListRenderer extends DefaultListCellRenderer {

            private final boolean isRed;

            public IconListRenderer(boolean isRed) {
                this.isRed = isRed;
            }

            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                Icon icon = null;
                try {
                    icon = new ImageIcon(ImageIO.read(ClassLoader.getSystemResourceAsStream("cells/" + (isRed ? "Red" : "Blue") + value + ".png")));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                label.setIcon(icon);
                return label;
            }
        }

        player1.setRenderer(new IconListRenderer(true));
        player2.setRenderer(new IconListRenderer(false));

        player1.setBorder(BorderFactory.createTitledBorder("Player 1 cell type"));
        player2.setBorder(BorderFactory.createTitledBorder("Player 2 cell type"));

        add(player1);
        add(player2);

        JButton play = new JButton("Start game!");
        add(new JSeparator());
        add(play);

        play.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Container parent = getParent();
                parent.removeAll();
                parent.add(Application.gamePanel = new GamePanel(new GameController(
                        CardDef.CellType.fromString((String) cell1.getSelectedItem()),
                        CardDef.CellType.fromString((String) cell2.getSelectedItem())
                )), BorderLayout.CENTER);
                parent.revalidate();
            }
        });
    }
}
