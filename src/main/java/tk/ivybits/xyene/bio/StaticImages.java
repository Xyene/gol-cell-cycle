package tk.ivybits.xyene.bio;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class StaticImages {
    public static Image FRAME_ICON_IMAGE, MENU_PLAY_ICON, MENU_ABOUT_ICON, MENU_HELP_ICON, MENU_EXIT_ICON;
    public static BufferedImage MENU_BANNER_IMAGE;
    public static BufferedImage ABOUT_PAGE_BANNER;

    public static Image resize(Image img, int w, int h) {
        return img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
    }

    static {
        FRAME_ICON_IMAGE = read("ButtonHover.png");
        MENU_PLAY_ICON = resize(read("ButtonHover.png"), 64, 64);

        BufferedImage blank = new BufferedImage(64, 64, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D blk = blank.createGraphics();
        blk.setBackground(new Color(0, 0, 0, 0));
        blk.clearRect(0, 0, 64, 64);
        blk.dispose();

        MENU_ABOUT_ICON = resize(read("MenuAboutIcon.png"), 64, 64);
        MENU_HELP_ICON = resize(read("MenuHelpIcon.png"), 64, 64);
        MENU_EXIT_ICON = resize(read("MenuExitIcon.png"), 64, 64);
        MENU_BANNER_IMAGE = read("MenuBanner.png");
        ABOUT_PAGE_BANNER = read("AboutBanner.png");
    }

    private static BufferedImage read(String path) {
        try {
            return ImageIO.read(ClassLoader.getSystemResourceAsStream(path));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
