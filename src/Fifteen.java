import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Application Interface Class.
 * @author Dmitriy Stepanov
 */
public class Fifteen {
    private static final Font bFont = new Font("Verdana", Font.BOLD, 11);
    private static final Font bFont2 = new Font("Verdana", Font.BOLD, 18);

    private static final int WIDTH = 300;
    private static final int HEIGHT = 450;

    private static final String TITLE_HELP = "Help";
    private static final String TXTHELP = "<html><center><H2>Help</H2></center><br>" +
            "<center>The goal of the game is to arrange or move the knuckles from left to right in ascending order in " +
            "the box and thereby achieve their ordering by numbers. At the same time, an additional task is set - to " +
            "make as few moves as possible.<br> To move a knuckle, click on the one you want to move, and it will " +
            "automatically move.</center></html>";
    private static final String TITLE_ABOUT = "About";
    private static final String TXTABOUT = "<html><center><H2>About</H2></center><br>" +
            "<center>Game Fifteen is a very famous game that can be attributed to puzzles. At one time, this game " +
            "was very popular among schoolchildren. The game of tag was created by Noah Chapman and it happened" +
            " back in 1874. In 1874, Noah Palmer Chapman demonstrated the game to his friends.<br></center></html>";

    private JFrame moreInform;
    private JFrame game;
    private BackgroundPanel bgPanel;
    private static Image windowIcon;

    /**
     * Constructor - creating the interface game
     * @see Fifteen#Fifteen()
     */
    public Fifteen() {
        game = new JFrame("15");
        windowIcon = loadImage("/fifteen.png");
        game.setIconImage(windowIcon);

        game.setSize(WIDTH, HEIGHT);
        game.setLocationRelativeTo(null);
        game.setResizable(false);
        game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // download function of the background
        bgPanel = new BackgroundPanel();
        Image backImage = loadImage("/bg.jpg");
        bgPanel.setImage(backImage);

        createMenu();   // calling the menu creation function
        game.add(bgPanel);
        game.setVisible(true);
    }

    /**
     * Loading images with the game
     * @param pathImage passes the path along which the images are located
     * @return an image of the BufferedImage type
     */
    public static BufferedImage loadImage(String pathImage) {
        try {
            return ImageIO.read(Fifteen.class.getResource(pathImage));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    private void createMenu() {
        JMenuBar gameMenu = new JMenuBar();
        JMenu start = new JMenu("Game");
        JMenu level = new JMenu("Select the level");
        gameMenu.add(start);
        start.add(level);

        JMenuItem easyLevel = new JMenuItem("Easy");
        KeyStroke ctrl3KeyStroke = KeyStroke.getKeyStroke("control 3");
        easyLevel.setAccelerator(ctrl3KeyStroke);
        easyLevel.addActionListener(e -> informMessage(3));
        level.add(easyLevel);

        JMenuItem middleLevel = new JMenuItem("Middle");
        KeyStroke ctrl4KeyStroke = KeyStroke.getKeyStroke("control 4");
        middleLevel.setAccelerator(ctrl4KeyStroke);
        middleLevel.addActionListener(e -> informMessage(4));
        level.add(middleLevel);

        JMenuItem hardLevel = new JMenuItem("Hard");
        KeyStroke ctrl5KeyStroke = KeyStroke.getKeyStroke("control 5");
        hardLevel.setAccelerator(ctrl5KeyStroke);
        hardLevel.addActionListener(e -> informMessage(5));
        level.add(hardLevel);

        JMenuItem helpGame = new JMenuItem("Help");
        KeyStroke ctrlHKeyStroke = KeyStroke.getKeyStroke("control H");
        helpGame.setAccelerator(ctrlHKeyStroke);
        helpGame.addActionListener(e -> {
            informGame(TITLE_HELP, TXTHELP, 300, 450);
            addPicture(moreInform);
        });
        start.add(helpGame);

        JMenuItem aboutGame = new JMenuItem("About");
        KeyStroke ctrlAKeyStroke = KeyStroke.getKeyStroke("control A");
        aboutGame.setAccelerator(ctrlAKeyStroke);
        aboutGame.addActionListener(e -> informGame(TITLE_ABOUT, TXTABOUT, 300, 300));
        start.add(aboutGame);
        start.addSeparator();

        JMenuItem exitGame = new JMenuItem("Exit");
        KeyStroke ctrlQKeyStroke = KeyStroke.getKeyStroke("control Q");
        exitGame.setAccelerator(ctrlQKeyStroke);
        exitGame.addActionListener(e -> System.exit(0));
        start.add(exitGame);
        game.setJMenuBar(gameMenu);
    }

    private void informMessage(int size) {
        ImageIcon iconMessage = new ImageIcon(loadImage("/warn.png"));
        int res = JOptionPane.showConfirmDialog(null,
                "Do you really want to choose" +
                        " this level?", "Level Selection", JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE, iconMessage);
        if(size == 3){
            if(res == 0){
                new GameLogic(size, bFont2, bgPanel, game).writeNumberTile();
            }
        } else if(size == 4){
            if(res == 0){
                new GameLogic(size, bFont2, bgPanel, game).writeNumberTile();
            }
        } else {
            if(res == 0){
                new GameLogic(size, bFont, bgPanel, game).writeNumberTile();
            }
        }
    }

    private void informGame(String name, String text, int width, int height) {
        moreInform = new JFrame(name);
        JLabel txtMessage = new JLabel(text);
        txtMessage.setHorizontalAlignment(SwingConstants.CENTER);
        txtMessage.setVerticalAlignment(SwingConstants.TOP);
        txtMessage.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        moreInform.setIconImage(windowIcon);
        moreInform.setSize(width, height);
        moreInform.setLocationRelativeTo(null);
        moreInform.setVisible(true);
        moreInform.add(txtMessage);
    }

    private void addPicture(JFrame MoreInform) {
        JLabel jlImage = new JLabel(new ImageIcon(loadImage("/15.jpg")));
        MoreInform.add(jlImage);
        MoreInform.setLayout(new GridLayout(2, 1));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Fifteen::new);
    }
}
