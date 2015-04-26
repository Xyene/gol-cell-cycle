package tk.ivybits.xyene.bio;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class DieButton extends JPanel {
    private static Image DIE_BUTTON_HOVER, DIE_BUTTON_NORMAL, DIE_BUTTON_PRESSED;
    private static Image[] DIE_FACES = new Image[6];
    private GameController controller;

    static {
        try {
            DIE_BUTTON_HOVER = StaticImages.resize(ImageIO.read(ClassLoader.getSystemResourceAsStream("ButtonHover.png")), 96, 96);
            DIE_BUTTON_NORMAL =StaticImages. resize(ImageIO.read(ClassLoader.getSystemResourceAsStream("Button.png")), 96, 96);
            DIE_BUTTON_PRESSED = StaticImages.resize(ImageIO.read(ClassLoader.getSystemResourceAsStream("ButtonPressed.png")), 96, 96);
            for(int i = 0; i < DIE_FACES.length; i++) {
//                BufferedImage buf = new BufferedImage(96, 96, BufferedImage.TYPE_4BYTE_ABGR);
//                Graphics2D g = buf.createGraphics();
//                g.setBackground(Color.WHITE);
//                g.fillRect(0, 0, 96, 96);
//                g.setColor(Color.BLACK);
//                g.setFont(new Font("Arial", 0, 20));
//                g.drawString((i + 1) + "", 20, 20);
                DIE_FACES[i] = ImageIO.read(ClassLoader.getSystemResourceAsStream("DieFace" + (i + 1) + ".png"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JButton dieButton = new JButton();

    public void reset() {
        SwingUtilities.invokeLater(() -> {
            removeAll();
            add(dieButton, BorderLayout.CENTER);
            revalidate();
        });
    }

    public DieButton(GameController controller) {
        this.controller = controller;
        dieButton.setIcon(new ImageIcon(DIE_BUTTON_NORMAL));
        dieButton.setPressedIcon(new ImageIcon(DIE_BUTTON_PRESSED));
        dieButton.setRolloverIcon(new ImageIcon(DIE_BUTTON_HOVER));
        setSize(new Dimension(DIE_BUTTON_NORMAL.getWidth(this), DIE_BUTTON_NORMAL.getHeight(this)));
        dieButton.setOpaque(false);
        dieButton.setBorderPainted(false);
        dieButton.setFocusPainted(false);
        dieButton.setUI(new BasicButtonUI());
        dieButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeAll();
                int roll = (int) Math.ceil(Math.random() * 6);
                add(new JLabel(new ImageIcon(DIE_FACES[roll - 1])), BorderLayout.CENTER);
                revalidate();
                controller.dieRolled(roll);
            }
        });
        setOpaque(true);
        setLayout(new BorderLayout());
        setBackground(new Color(0, 0, 0, 0));
        reset();
    }
}
