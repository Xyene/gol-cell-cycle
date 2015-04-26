package tk.ivybits.xyene.bio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MenuPanel extends JPanel {
    public MenuPanel() {
        setOpaque(false);
        setSize(Application.VIEWPORT);
        JButton play = new JButton("Play! ", new ImageIcon(StaticImages.MENU_PLAY_ICON));
        play.setHorizontalAlignment(SwingConstants.LEFT);
        JButton about = new JButton("About the game", new ImageIcon(StaticImages.MENU_ABOUT_ICON));
        about.setHorizontalAlignment(SwingConstants.LEFT);
        JButton help = new JButton("Help me!", new ImageIcon(StaticImages.MENU_HELP_ICON));
        help.setHorizontalAlignment(SwingConstants.LEFT);
        JButton exit = new JButton("Exit game", new ImageIcon(StaticImages.MENU_EXIT_ICON));
        exit.setHorizontalAlignment(SwingConstants.LEFT);

        about.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Window parent = SwingUtilities.getWindowAncestor(getParent());
                final JDialog d = new JDialog(parent);
                d.setSize(new Dimension(320, 240));
                d.setLocationRelativeTo(parent);
                d.setLayout(new BorderLayout());
                d.setTitle("About");

                d.add(new JLabel("<html><b>The Game of Life - Cell Edition<br/><br/>By Ryan Zhen, Tudor Brindus & Tony Li<br/>2015</html>", JLabel.CENTER), BorderLayout.CENTER);
                d.setModal(true);
                d.setVisible(true);
            }
        });

        help.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Window parent = SwingUtilities.getWindowAncestor(getParent());
                final JDialog d = new JDialog(parent);
                d.setSize(new Dimension(320, 240));
                d.setLocationRelativeTo(parent);
                d.setLayout(new BorderLayout());
                d.setTitle("Help");

                d.add(new JLabel("<html>We had a help text, but we accidentally it.</html>", JLabel.CENTER), BorderLayout.CENTER);
                d.setModal(true);
                d.setVisible(true);
            }
        });

        play.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Container parent = getParent();
                parent.removeAll();
                parent.add(new CellCellectionPanel(), BorderLayout.CENTER);
                parent.revalidate();
            }
        });
//        play.addActionListener(new AbstractAction() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                Container parent = getParent();
//                parent.removeAll();
//                parent.add(Application.gamePanel = new GamePanel(), BorderLayout.CENTER);
//                parent.revalidate();
//            }
//        });

        JPanel title = new JPanel() {
            {
                setLayout(new BorderLayout());
                setPreferredSize(new Dimension(Application.VIEWPORT.width, 150));
                JLabel label = new JLabel(new ImageIcon(StaticImages.MENU_BANNER_IMAGE));
                label.setOpaque(false);
                setBackground(new Color(0xffbff1f6));
                add(label, BorderLayout.CENTER);
            }
        };

        exit.addActionListener((e) -> {
            System.exit(0);
        });

        JPanel options = new JPanel() {
            {
                setOpaque(false);
                GridLayout box = new GridLayout(0, 1);
                setLayout(box);
                add(play);
                add(help);
                add(about);
                add(exit);
            }
        };

        setLayout(new BorderLayout());
        add(title, BorderLayout.NORTH);
        add(options, BorderLayout.CENTER);
    }
}
