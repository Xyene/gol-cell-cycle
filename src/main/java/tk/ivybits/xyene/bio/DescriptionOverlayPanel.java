package tk.ivybits.xyene.bio;

import javax.swing.*;
import java.awt.*;

public class DescriptionOverlayPanel extends JPanel {
    private static final Dimension VIEWPORT = new Dimension(640, 320);

    private JTextPane pane = new JTextPane();
    private JLabel image = new JLabel("", JLabel.CENTER);
    public CardDef card;

    public DescriptionOverlayPanel() {
        setSize(VIEWPORT);
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0));

        pane.setSize(new Dimension(300, 0));
        pane.setBackground(new Color(0, 0, 0, 0f));
        image.setBackground(new Color(0, 0, 0, 0f));
        pane.setForeground(new Color(0xB8E64C));
        pane.setFont(pane.getFont().deriveFont(Font.BOLD));
        System.out.println(pane.getFont());
        pane.setFocusable(false);

        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        setLayout(new BorderLayout());
        add(image, BorderLayout.CENTER);
        add(pane, BorderLayout.SOUTH);
    }

    @Override
    public void paintComponent(Graphics _g) {
        Graphics2D g = (Graphics2D) _g;
        g.setColor(new Color(0, 0, 0, 0.5f));
        g.fillRect(0, pane.getY()-5, 640, 320);
        super.paintComponent(_g);
    }

    @Override
    public void update(Graphics g) {
        paint(g);
    }

    public void setCard(CardDef card) {
        this.card = card;
        pane.setText(card.desc);
        pane.revalidate();
      //  image.setIcon(new ImageIcon(card.img));
        image.revalidate();
    }
}
