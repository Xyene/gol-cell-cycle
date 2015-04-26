package tk.ivybits.xyene.bio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static tk.ivybits.xyene.bio.CardDef.Phase.*;
import static tk.ivybits.xyene.bio.CardDef.CellType.*;

public class CardDef {
    public BufferedImage img;

    public interface Phase {
        int INTERPHASE = 0x1, PROPHASE = 0x2, ANAPHASE = 0x4, METAPHASE = 0x8, TELOPHASE = 0x10, CYTOKINESIS = 0x20;
        int[] PHASES = {INTERPHASE, PROPHASE, ANAPHASE, METAPHASE, TELOPHASE, CYTOKINESIS};
        String[] LABELS = {"Interphase", "Prophase", "Anaphase", "Metaphase", "Telophase", "Cytokinesis"};
    }

    public interface CellType {
        int MITOSIS = 0x1, MEIOSIS_1 = 0x2, MEIOSIS_2 = 0x4;
        int[] TYPES = {MITOSIS, MEIOSIS_1, MEIOSIS_2};
        String[] LABELS = {"Mitosis", "Meiosis I", "Meiosis II"};

        public static int fromString(String x) {
            switch (x) {
                case "Mitosis":
                    return MITOSIS;
                case "Meiosis I":
                    return MEIOSIS_1;
                case "Meiosis II":
                    return MEIOSIS_2;
                default:
                    return -1;
            }
        }
    }

    public interface CardCallback {
        void callback(PlayerPiece piece, int roll);
    }

    public String desc, title;
    public CardCallback callback;
    public boolean requiresRoll;

    public int phases;
    private final int celltype;
    public static final List<CardDef>[][] CARDS;

    public static final String[][] BOOTSTRAP_CARDS = {
            // Mitosis description, Meiosis I description, Meiosis II description
            {"Interphase", "Cell is entering interphase. The cell grows and replicates its DNA during this phase.", "Cell is entering interphase. The cell grows and replicates its DNA during this phase.", "Cell is entering interphase. The cell grows during this phase, but does not replicate its DNA."},
            {"Prophase", "Cell is entering prophase. The chromatin condenses into chromosomes and the nuclear membrane disappears. The centrosomes move to the poles and the spindle fibres form.", "Cell is entering prophase. The chromatin condenses into chromosomes and the nuclear membrane disappears. The centrosomes move to the poles and the spindle fibres form. Homologous chromosomes cross-over, exchanging DNA to increase genetic variation.", "Cell is entering prophase. The chromatin condenses into chromosomes and the nuclear membrane disappears. The centrosomes move to the poles and the spindle fibres form."},
            {"Metaphase", "Cell is entering metaphase. The chromosomes are guided to the metaphase plate by spindle fibres.", "Cell is entering metaphase. The chromosomes are guided to the metaphase plate by spindle fibres.", "Cell is entering metaphase. The chromosomes are guided to the metaphase plate by spindle fibres."},
            {"Anaphase", "Cell is entering anaphase. The centromeres connecting sister chromatids break apart and sister chromatids are pulled apart to opposite sides of the cell.", "Cell is entering anaphase. The centromeres connecting homologous chromosomes break apart and the homologous chromosomes are pulled apart to opposite sides of the cell.", "Cell is entering anaphase. The centromeres connecting sister chromatids break apart and sister chromatids are pulled apart to opposite sides of the cell."},
            {"Telophase", "Cell is entering telophase. The sister chromatids (now chromosomes), unwind into chromatin and two nuclear membranes form around each group of chromosomes.", "Cell is entering telophase. Two nuclear membranes form around each group of chromosomes. Chromosomes sometime recondense, depending on the cell.", "Cell is entering telophase. The sister chromosomes unwind into chromatin and two nuclear membranes form around each group of chromosomes."},
            {"Cytokinesis", "Cell divides!", "Cell divides!", "Cell divides!"}
    };

    static {
        CARDS = new ArrayList[PHASES.length][TYPES.length];
        for (int i = 0; i < PHASES.length; i++) for (int j = 0; j < TYPES.length; j++) CARDS[i][j] = new ArrayList<>();

        new CardDef("Virus!",
                "Roll a die. If the die rolls a 5 or 6, your cell is infected by a virus - miss 2 turns.", (piece, roll) -> {
            if (roll == 5 || roll == 6) {
                piece.missTurns(2);
            }
        }, true, INTERPHASE | PROPHASE | METAPHASE | TELOPHASE);
        new CardDef("DNA Damage (1)", "Your DNA is damaged! Roll a die: if the roll is > 1," +
                " DNA is repairable - miss next turn. Otherwise, " +
                "apoptosis occurs - move to beginning of interphase.", (piece, roll) -> {
            if (roll > 1) {
                piece.missTurns(1);
            } else {
                piece.setCell(0);
            }
        }, true, INTERPHASE | PROPHASE | METAPHASE | TELOPHASE);
        new CardDef("Favourable Conditions", "Roll a die. If the roll is > 3," +
                " conditions are favourable for mitosis/meiosis - move forward 3 spaces.", (piece, roll) -> {
            if (roll > 3)
                piece.setCell(piece.mapIndex + 3);
        }, true, INTERPHASE);
        new CardDef("Malfunction", "Spindle fibres malfunction, and daughter cells gets the wrong number of chromosomes." +
                " Move to first square of telophase, but miss 3 turns.", (piece, roll) -> {
            piece.setCell(MapPoints.PHASE_MAP[Arrays.binarySearch(PHASES, ANAPHASE)][0]);
            piece.missTurns(3);
        }, false, ANAPHASE, MITOSIS); // TODO: normal cell only
        new CardDef("Unfavourable Conditions", "Roll a die. If a 1 is rolled," +
                " cell does not have enough nutrients for mitosis/meiosis - miss a turn.", (piece, roll) -> {
            if (roll == 1)
                piece.missTurns(1);
        }, true, INTERPHASE);
        new CardDef("Undetected Non-disjunction", "Non-disjunction not detected during anaphase -" +
                " daughter cells do not have proper number of chromosomes. Move to last square of telophase and miss 2 turns.", (piece, roll) -> {
            piece.setCell(MapPoints.PHASE_MAP[Arrays.binarySearch(PHASES, TELOPHASE)][1]);
            piece.missTurns(2);
        }, true, CYTOKINESIS, MEIOSIS_2); // TODO: Meiosis II only
        new CardDef("Condensing", "DNA failed to condense - go back to the last square of interphase.", (piece, roll) -> piece.setCell(MapPoints.PHASE_MAP[Arrays.binarySearch(PHASES, INTERPHASE)][1]), false, PROPHASE);
        new CardDef("Unattached Spindle Fibres", "Spindle fibres not attached to chromosomes. Roll a die, and move back that number of squares.", new CardCallback() {
            @Override
            public void callback(PlayerPiece piece, int roll) {
                piece.setCell(piece.mapIndex - roll);
            }
        }, true, METAPHASE);
        new CardDef("Unattached Spindle Fibres", "Spindle fibres do not form properly. Roll a die. If a number > 1 is rolled, " +
                "move back that number of squares. Otherwise, go to beginning of interphase.", (piece, roll) -> {
            if (roll > 1)
                piece.setCell(piece.mapIndex - roll);
            else
                piece.setCell(0);
        }, true, METAPHASE);
        new CardDef("DNA Damage (2)", "DNA is damaged and needs repair! Roll a die, and miss next turn if a number less than 4 is rolled.", new CardCallback() {
            @Override
            public void callback(PlayerPiece piece, int roll) {
                if (roll < 4)
                    piece.missTurns(1);
            }
        }, true, INTERPHASE | METAPHASE);
        new CardDef("Beneficial Mutation", "A mutation occurs, allowing your cell to better survive. Roll a die, and move forward the number that many cells.", new CardCallback() {
            @Override
            public void callback(PlayerPiece piece, int roll) {
                piece.setCell(piece.mapIndex + roll);
            }
        }, true, INTERPHASE);
        new CardDef("Chromosome Crossing", "Chromosomes take longer to cross over than usual - miss a turn.", (piece, roll) -> piece.missTurns(1), false, PROPHASE, MEIOSIS_1); // TODO: Meiosis I only
        new CardDef("Missed Checkpoint", "Meiotic checkpoint missed." +
                " If a number > 3 is rolled - move forward that number of spaces." +
                " Otherwise, a damaged gene is not detected - move back double the number of spaces shown on the die.", (piece, roll) -> piece.setCell(piece.mapIndex + (roll > 3 ? roll : -roll * 2)), true, PROPHASE, MEIOSIS_1); // TODO: Meiosis I only
        new CardDef("DNA Duplication", "DNA duplication is not required - move forward 4 spaces.", (piece, roll) -> piece.setCell(piece.mapIndex + 4), false, INTERPHASE, MEIOSIS_2); // TODO: Meiosis II only
        new CardDef("Nondisjunction", "Nondisjunction occurs. Roll a die. If the roll is greater than 2," +
                " miss a turn to allow chromatids to disjoin, otherwise, move to beginning of interphase.", (piece, roll) -> {
            if (roll > 2)
                piece.missTurns(1);
            else
                piece.setCell(0);
        }, true, ANAPHASE, MEIOSIS_1 | MEIOSIS_2); // TODO: Meiosis only

        new CardDef("Cytokinesis", "Roll a die. If > 2, you divided correctly. " +
                "Otherwise, organelles are divided unevenly and a daughter cell dies - go back to interphase.", (piece, roll) -> {
            if (roll <= 2) {
                piece.setCell(0);
            } else {
                SwingUtilities.invokeLater(() -> {
                    Window parent = SwingUtilities.getWindowAncestor(Application.gamePanel);
                    final JDialog d = new JDialog(parent);
                    d.setSize(new Dimension(320, 240));
                    d.setLocationRelativeTo(parent);
                    d.setLayout(new BorderLayout());

                    JLabel won = new JLabel(piece.team.humanName + " player won!", new ImageIcon(piece.render), JLabel.CENTER);
                    won.setForeground(piece.team.color);
                    won.setFont(won.getFont().deriveFont(Font.BOLD, 18f));
                    d.add(won, BorderLayout.CENTER);

                    JButton again = new JButton("<< Back to menu");
                    AbstractAction end;
                    again.addActionListener(end=new AbstractAction() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            d.dispose();
                            Container parent = Application.gamePanel;
                            parent.removeAll();
                            parent.add(new MenuPanel(), BorderLayout.CENTER);
                            parent.revalidate();
                        }
                    });
                   // d.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                    d.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent e) {
                           end.actionPerformed(null); // Clean up stuff
                        }
                    });
                    d.add(again, BorderLayout.SOUTH);
                    d.setUndecorated(true);
                    d.setVisible(true);
                    d.setModal(true);
                });
            }
        }, true, CYTOKINESIS);
        for (int x = 0; x < 1; x++)
            new CardDef("OK!", "The cycle is proceeding as normal.", (piece, roll) -> {
            }, false, INTERPHASE | PROPHASE | METAPHASE | ANAPHASE | TELOPHASE);
    }

    public CardDef(String title, String desc, CardCallback callback, boolean requiresRoll, int phases) {
        this(title, desc, callback, requiresRoll, phases, 0xFFFFFFFF);
    }

    public CardDef(String title, String desc, CardCallback callback, boolean requiresRoll, int phases, int celltype) {
        this.title = title;
        this.desc = desc;
        this.callback = callback;
        this.requiresRoll = requiresRoll;
        this.phases = phases;
        this.celltype = celltype;
        for (int i = 0, phasesLength = PHASES.length; i < phasesLength; i++) {
            int p = PHASES[i];
            if ((phases & p) > 0)
                for (int i1 = 0, typesLength = TYPES.length; i1 < typesLength; i1++) {
                    int t = TYPES[i1];
                    if ((celltype & t) > 0)
                        CARDS[i][i1].add(this);
                }
        }
        img = StaticImages.ABOUT_PAGE_BANNER;
    }
}
