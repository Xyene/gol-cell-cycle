package tk.ivybits.xyene.bio;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;

public class PlayerPiece {
    private int roll;
    private Runnable callback;
    public int cellType;

    public int getPhase() {
        int pos = mapIndex;
        int phase = 0;
        for (; phase < pos; phase++) {
            int s = MapPoints.PHASE_MAP[phase][0];
            int e = MapPoints.PHASE_MAP[phase][1];
            if (pos >= s && pos <= e) {
                break;
            }
        }
        return phase;
    }

    public static enum Team {
        RED(Color.RED, "Red"), BLUE(Color.BLUE, "Blue");

        public Color color;
        public String humanName;

        Team(Color color, String humanName) {
            this.color = color;
            this.humanName = humanName;
        }
    }

    private final GameController controller;
    public final Team team;
    public int turnsMissed;
    public int mapIndex;
    public int targetMapIndex;
    public BufferedImage render;
    public BufferedImage inactiveRender;
    public Point2D.Float selfPoint = new Point2D.Float(MapPoints.WAYPOINTS[0].x, MapPoints.WAYPOINTS[0].y);

    public PlayerPiece(GameController controller, Team team, int celltype) {
        this.cellType = celltype;
        this.controller = controller;
        this.team = team;
        this.mapIndex = 0;
        this.targetMapIndex = 0;

        try {
            String pathPrefix = "cells/" + (team == Team.RED ? "Red" : "Blue");
            pathPrefix += new String[] {"Mitosis", "Meiosis I", "Meiosis II"}[Arrays.binarySearch(CardDef.CellType.TYPES, celltype)];
            pathPrefix += ".png";
            this.render = ImageIO.read(ClassLoader.getSystemResourceAsStream(pathPrefix));
            this.inactiveRender = new BufferedImage(this.render.getWidth(), this.render.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
            for (int x = 0; x < this.inactiveRender.getWidth(); x++) {
                for (int y = 0; y < this.inactiveRender.getWidth(); y++) {
                    int clr = render.getRGB(x, y);
                    int alpha = (clr >>> 24) & 0xFF;
                    int red = (clr >>> 16) & 0xFF;
                    int green = (clr >>> 8) & 0xFF;
                    int blue = (clr) & 0xFF;

                    float luminance = (red * 0.2126f + green * 0.7152f + blue * 0.0722f) / 255;

                    inactiveRender.setRGB(x, y, new Color(luminance, luminance, luminance, alpha / 255f).getRGB());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void inc(int roll, Runnable callback) {
        if(roll == 0) {callback.run(); return;}
        roll = Math.min(MapPoints.PATH_LENGTH - 1, mapIndex + roll) - mapIndex;
        this.roll = roll;
        this.callback = callback;
        selfPoint = (Point2D.Float) MapPoints.WAYPOINTS[targetMapIndex].clone();
        mapIndex = targetMapIndex;
        targetMapIndex++;
    }

    public void missTurns(int turns) {
        turnsMissed += turns;
    }

    public void setCell(int cell) {
        targetMapIndex = mapIndex = cell;
        selfPoint = MapPoints.WAYPOINTS[cell];
    }

    int renderCycle;

    public void draw(Graphics2D g, boolean active) {
        renderCycle++;
        g.setColor(team.color);

//        for (int i = 1; i < MapPoints.WAYPOINTS.length; i++) {
//            g.drawLine((int) MapPoints.WAYPOINTS[i - 1].x, (int) MapPoints.WAYPOINTS[i - 1].y, (int) MapPoints.WAYPOINTS[i].x, (int) MapPoints.WAYPOINTS[i].y);
//
//            //  g.fillRect(MapPoints.WAYPOINTS[i - 1].x - 4, MapPoints.WAYPOINTS[i - 1].y - 4, 8, 8);
//        }

        Point2D.Float targetPoint = MapPoints.WAYPOINTS[targetMapIndex];
        Point2D.Float currentPoint = MapPoints.WAYPOINTS[mapIndex];
        if (selfPoint.distance(targetPoint) < 4) {
            mapIndex = targetMapIndex;
            if (roll != 0) {
                roll--;
                if (roll > 0) {
                    targetMapIndex++;
                } else {
                    callback.run();
                }
            }
        } else {
            double magnitude = currentPoint.distance(targetPoint);
            if (magnitude > 0) {
                selfPoint.x += (float) ((targetPoint.x - currentPoint.x) / magnitude);
                selfPoint.y += (float) ((targetPoint.y - currentPoint.y) / magnitude);
            }
        }

        g.setColor(Color.GREEN);

        int rx = (int) selfPoint.x;
        int ry = (int) (selfPoint.y +
                ((Math.sin(
                        selfPoint.distance(targetPoint) > 4 ? (selfPoint.distance(targetPoint) / 3) : renderCycle / 4
                ) * (active ? 3 : 0))));

        g.drawImage(active ? render : inactiveRender, rx - 30, ry - 30, 60, 60, null, null);
    }
}
