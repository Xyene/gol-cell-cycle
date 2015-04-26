package tk.ivybits.xyene.bio;

import javax.swing.*;
import java.awt.*;

public class Application {
    public static Dimension VIEWPORT = new Dimension(640+220, 508);
    public static GamePanel gamePanel;

    public static void main(String[] argv) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("The Game of Life - Cell Edition");
            frame.setIconImage(StaticImages.FRAME_ICON_IMAGE);
            frame.setLayout(new BorderLayout());
            frame.add(new MenuPanel(), BorderLayout.CENTER);
            //  frame.add(gamePanel=new GamePanel(), BorderLayout.CENTER);
            frame.setSize(VIEWPORT);
            frame.setMinimumSize(VIEWPORT);
            frame.setPreferredSize(VIEWPORT);
            frame.setMaximumSize(VIEWPORT);
            frame.setResizable(false);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}
