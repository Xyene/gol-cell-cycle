package tk.ivybits.xyene.bio;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class GamePanel extends JPanel {
    private static Image BOARD_BACKGROUND;
    public DieButton dieButton;
    public DescriptionOverlayPanel overlay;
    public GameController controller;
    private Timer repainter;

    static {
        try {
            BOARD_BACKGROUND = ImageIO.read(ClassLoader.getSystemResourceAsStream("Board3.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeNotify() {
        super.removeNotify();
        repainter.stop();
    }

    JTextPane pane;

    public GamePanel(GameController controller) {
        this.controller = controller;
        dieButton = new DieButton(controller);
        overlay = new DescriptionOverlayPanel();

        (repainter = new Timer(
                1000 / 60, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paintImmediately(getBounds());
                //Point2D.Float pt = piece.selfPoint;
                //repaint(0, (int) pt.x - 20, (int) pt.y - 20, 40, 40);
            }
        }
        )).start();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.printf("new Point2D.Float(%d, %d),\n", e.getX(), e.getY());
            }
        });

        setLayout(null);
        setSize(Application.VIEWPORT);
        setPreferredSize(Application.VIEWPORT);
        add(dieButton);
        add(overlay);

        int W = 220;

        dieButton.reshape(525, 380, dieButton.getWidth(), dieButton.getHeight());
        overlay.reshape(0, 0, overlay.getWidth(), overlay.getHeight());
        setComponentZOrder(overlay, 0);

        pane = new JTextPane();
        pane.setSize(new Dimension(300, 0));
        pane.setBackground(Color.DARK_GRAY);
        pane.setForeground(new Color(0xB8E64C));
        pane.setFont(pane.getFont().deriveFont(Font.BOLD));

        pane.setFocusable(false);

        add(pane);
        pane.reshape(640, 0, W, 480);

        overlay.setVisible(false);
    }

    @Override
    public void paintComponent(Graphics _g) {
        Graphics2D g = (Graphics2D) _g;
        super.paintComponent(_g);
        g.drawImage(BOARD_BACKGROUND, 0, 0, 640, 480, this);
        controller.draw(g);
    }
}
