import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.LinkedList;

/**
 * The Application's Game Logic class.
 * @author Dmitriy Stepanov
 */
public class GameLogic {
    private static final Color COLOR_TILE = new Color(227, 13, 239);
    private JButton countClick;         
    private JButton pressButton;         
    private JPanel board;
    private JButton[][] buttons;
    private int buttonClick;              
    private int size;
    private int recordClick;
    private JFrame game;

    /**
     * Constructor - creating game with values for the field size and button font
     * @param size size of the game field
     * @param bFont the font for the values of the buttons
     * @param bgPanel main panel app
     * @param game the main window app
     * @see GameLogic#GameLogic(int,Font,BackgroundPanel,JFrame)
     */
    public GameLogic(int size, Font bFont, BackgroundPanel bgPanel, JFrame game) {
        this.size = size;
        this.game = game;
        board = new JPanel();

        JButton buttonNewGame = new JButton("New game");
        buttonNewGame.setBackground(Color.lightGray);
        JButton buttonExitGame = new JButton("Reset");
        buttonExitGame.setBackground(Color.lightGray);
        countClick = new JButton("Fifteen");

        bgPanel.add(board);
        bgPanel.setLayout(null);
        bgPanel.add(buttonNewGame);
        bgPanel.add(buttonExitGame);
        bgPanel.add(countClick);

        board.setBounds(25, 25, 250, 250);
        countClick.setBounds(25, 280, 250, 35);
        buttonNewGame.setBounds(25, 325, 120, 33);
        buttonExitGame.setBounds(155, 325, 120, 33);

        countClick.setEnabled(false);
        game.add(bgPanel);
        game.setVisible(true);

        buttons = new JButton[size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setFont(bFont);
                buttons[i][j].setForeground(Color.black);
                buttons[i][j].setFocusable(false);
                buttons[i][j].setBackground(COLOR_TILE);
                buttons[i][j].addActionListener(e -> {
                    pressButton = (JButton) e.getSource();
                    for (int k = 0; k < buttons.length; k++)
                        for (int l = 0; l < buttons[k].length; l++) {
                            if (pressButton == buttons[k][l]) {
                                setCountSteps();
                                if (isEmptyButton(k - 1, l)) {
                                    setTileNumber(k - 1, l);                
                                } else if (isEmptyButton(k + 1, l)) {
                                    setTileNumber(k + 1, l);                
                                } else if (isEmptyButton(k, l - 1)) {
                                    setTileNumber(k, l - 1);                
                                } else if (isEmptyButton(k, l + 1)) {
                                    setTileNumber(k, l + 1);                
                                }
                                if (checkWin()) {
                                    congratulations();
                                }
                                return;
                            }
                        }
                });
                board.add(buttons[i][j]);
            }

        buttonNewGame.addActionListener(e -> writeNumberTile());
        buttonExitGame.addActionListener(e -> {
            new Fifteen();
            game.dispose();
        });
    }

    /**
     * The application of digits on buttons
     */
    public void writeNumberTile() {
        int[] field = new int[size * size];
        LinkedList<String> numbers = new LinkedList<>();
        board.setLayout(new GridLayout(size, size));
        buttonClick = 0;
        countClick.setText("You didn't make a move");

        for (int i = 1; i < field.length; i++) {
            numbers.add(String.valueOf(i));
        }

        numbers.add("");
        Collections.shuffle(numbers);

        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                buttons[i][j].setText(String.valueOf(numbers.poll()));
            }
    }

    private boolean checkWin() {
        int count = 1;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                String victoryText = String.valueOf(count);
                if (!buttons[i][j].getText().equals(victoryText) && count < size * size)
                    return false;
                count++;
            }
        }
        return true;
    }

    private void setCountSteps() {
        String txt;
        if (buttonClick == 0) {
            txt = "step";
        } else {
            txt = "steps";
        }
        countClick.setText("You did " + (buttonClick + 1) + " " + txt + "!");
    }

    private boolean isEmptyButton(int row, int col) {
        if (row < 0 || row >= size)
            return false;
        if (col < 0 || col >= size)
            return false;
        return buttons[row][col].getText().equals("");
    }

    private void setTileNumber(int row, int col) {
        buttons[row][col].setText(pressButton.getText());
        pressButton.setText("");
        buttonClick++;
    }

    private void congratulations() {
        String recordText;
        if (buttonClick < recordClick) {
            recordText = "You set a record!<br>";
        } else{
            recordText = "Record " + recordClick + " moves!<br>";
        }
        String txt = "<html><center><H2>Congratulate!</H2><br> You won!<br>You did: " + buttonClick + " moves!<br>"
                + recordText + "Do you want to play again?</center></html>";
        recordClick = buttonClick;

        final JDialog victory = new JDialog();
        victory.setTitle("Victory");
        victory.setIconImage(Fifteen.loadImage("/icon.png"));
        victory.setBounds(550, 250, 300, 250);
        victory.setLayout(null);
        victory.setResizable(false);

        JButton buttonYes = new JButton("Yes");
        buttonYes.setBackground(Color.lightGray);
        victory.add(buttonYes);
        buttonYes.setBounds(10, 180, 120, 30);
        buttonYes.addActionListener(e -> {
            writeNumberTile();
            victory.dispose();
        });

        JButton buttonNo = new JButton("No");
        buttonNo.setBackground(Color.lightGray);
        victory.add(buttonNo);
        buttonNo.setBounds(160, 180, 120, 30);
        buttonNo.addActionListener(e -> {
            new Fifteen();
            victory.dispose();
            game.dispose();
        });

        JLabel information = new JLabel(txt);
        information.setHorizontalAlignment(SwingConstants.CENTER);
        victory.add(information);
        information.setBounds(10, 10, 270, 160);
        victory.setVisible(true);
    }
}
