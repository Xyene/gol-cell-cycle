package tk.ivybits.xyene.bio;

import java.awt.geom.Point2D;

public class MapPoints {
    public static final Point2D.Float[] WAYPOINTS = {
            new Point2D.Float(58, 335),
            new Point2D.Float(111, 335),
            new Point2D.Float(161, 335),
            new Point2D.Float(213, 335),
            new Point2D.Float(264, 334),
            new Point2D.Float(315, 336),
            new Point2D.Float(365, 334),
            new Point2D.Float(417, 336),
            new Point2D.Float(467, 334),
            new Point2D.Float(521, 336),
            new Point2D.Float(579, 332),
            new Point2D.Float(576, 267),
            new Point2D.Float(520, 264),
            new Point2D.Float(468, 263),
            new Point2D.Float(419, 265),
            new Point2D.Float(366, 263),
            new Point2D.Float(317, 265),
            new Point2D.Float(267, 264),
            new Point2D.Float(214, 263),
            new Point2D.Float(162, 260),
            new Point2D.Float(109, 265),
            new Point2D.Float(51, 263),
            new Point2D.Float(47, 188),
            new Point2D.Float(111, 194),
            new Point2D.Float(158, 192),
            new Point2D.Float(214, 195),
            new Point2D.Float(266, 195),
            new Point2D.Float(317, 195),
            new Point2D.Float(365, 194),
            new Point2D.Float(418, 195),
            new Point2D.Float(470, 195),
            new Point2D.Float(518, 191),
            new Point2D.Float(574, 186),
            new Point2D.Float(579, 122),
            new Point2D.Float(520, 122),
            new Point2D.Float(469, 122),
            new Point2D.Float(417, 121),
            new Point2D.Float(365, 120),
            new Point2D.Float(315, 120),
            new Point2D.Float(263, 122),
            new Point2D.Float(209, 122),
            new Point2D.Float(159, 119),
            new Point2D.Float(109, 120),
            new Point2D.Float(57, 114),
            new Point2D.Float(56, 50),
            new Point2D.Float(110, 52),
            new Point2D.Float(165, 50),
            new Point2D.Float(214, 49),
            new Point2D.Float(263, 49),
            new Point2D.Float(312, 52),
            new Point2D.Float(368, 49),
            new Point2D.Float(419, 50),
            new Point2D.Float(467, 50),
            new Point2D.Float(521, 53),
            new Point2D.Float(581, 51),
    };

    public static final int[][] PHASE_MAP = {
            {0, 10},
            {11, 21},
            {22, 32},
            {33, 43},
            {44, 53},
            {54, 54}
    };

    public static final int PATH_LENGTH = WAYPOINTS.length;
}
