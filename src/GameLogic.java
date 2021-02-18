import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.LinkedList;

/**
 * The Application's Game Logic class.
 * @author Dmitriy Stepanov
 */
public class GameLogic implements ActionListener {

    /** Static variable to retrieve the color of a button */
    private static final Color COLOR_TILE = new Color(227, 13, 239);

    private JButton countClick;          /** number of moves */
    private JButton pressButton;         /** pressed button on the board */
    private JPanel board;
    private JButton[][] buttons;
    private int buttonClick;              /** the number of movements of buttons */
    private int size;
    private int recordClick;

    /**
     * Constructor - creating game with values for the field size and button font
     * @param size size of the game field
     * @param bFont the font for the values of the buttons
     * @see GameLogic#GameLogic(int,Font)
     */
    public GameLogic(int size, Font bFont) {
        this.size = size;
        board = new JPanel();

        JButton buttonNewGame = new JButton("New game");
        buttonNewGame.setBackground(Color.lightGray);
        JButton buttonExitGame = new JButton("Reset");
        buttonExitGame.setBackground(Color.lightGray);
        countClick = new JButton("Fifteen");

        GraphicInterface.bgPanel.add(board);
        GraphicInterface.bgPanel.setLayout(null);
        GraphicInterface.bgPanel.add(buttonNewGame);
        GraphicInterface.bgPanel.add(buttonExitGame);
        GraphicInterface.bgPanel.add(countClick);

        board.setBounds(25, 25, 250, 250);
        countClick.setBounds(25, 280, 250, 35);
        buttonNewGame.setBounds(25, 325, 120, 33);
        buttonExitGame.setBounds(155, 325, 120, 33);

        countClick.setEnabled(false);
        GraphicInterface.game.add(GraphicInterface.bgPanel);
        GraphicInterface.game.setVisible(true);

        buttons = new JButton[size][size];
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setFont(bFont);
                buttons[i][j].setForeground(Color.black);
                buttons[i][j].setFocusable(false);
                buttons[i][j].setBackground(COLOR_TILE);
                buttons[i][j].addActionListener(this);
                board.add(buttons[i][j]);
            }

        buttonNewGame.addActionListener(e -> writeNumberTile());
        buttonExitGame.addActionListener(e -> {
            new GraphicInterface();
            GraphicInterface.game.dispose();
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

    /**
     * Check the victory in the game
     * @return true if the game is over
     */
    public boolean checkWin() {
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

    /**
     * Count the number of steps taken
     */
    public void setCountSteps() {
        String txt;
        if (buttonClick == 0) {
            txt = "step";
        } else {
            txt = "steps";
        }
        countClick.setText("You did " + (buttonClick + 1) + " " + txt + "!");
    }

    /**
     * Check on an empty button
     * @param row строка
     * @param col столбец
     * @return the button without a number
     */
    private boolean isEmptyButton(int row, int col) {
        if (row < 0 || row >= size)
            return false;
        if (col < 0 || col >= size)
            return false;
        return buttons[row][col].getText().equals("");
    }

    /**
     * Get the number of the pressed button
     * @param row строка
     * @param col столбец
     */
    private void setTileNumber(int row, int col) {
        buttons[row][col].setText(pressButton.getText());
        pressButton.setText("");
        buttonClick++;
    }

    /**
     * Button Click EventHandler
     */
    public void actionPerformed(ActionEvent e) {
        pressButton = (JButton) e.getSource();
        for (int i = 0; i < buttons.length; i++)
            for (int j = 0; j < buttons[i].length; j++) {
                if (pressButton == buttons[i][j]) {
                    setCountSteps();
                    if (isEmptyButton(i - 1, j)) {
                        setTileNumber(i - 1, j);                // moving down a column
                    } else if (isEmptyButton(i + 1, j)) {
                        setTileNumber(i + 1, j);                // moving up a column
                    } else if (isEmptyButton(i, j - 1)) {
                        setTileNumber(i, j - 1);                // move to the left in a row
                    } else if (isEmptyButton(i, j + 1)) {
                        setTileNumber(i, j + 1);                // move to the right in a row
                    }
                    if (checkWin()) {
                        congratulations();
                    }
                    return;
                }
            }
    }

    /**
     * Receive a victory message
     */
    public void congratulations() {
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
        victory.setIconImage(GraphicInterface.loadImage("/icon.png"));
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
            new GraphicInterface();
            victory.dispose();
            GraphicInterface.game.dispose();
        });

        JLabel information = new JLabel(txt);
        information.setHorizontalAlignment(SwingConstants.CENTER);
        victory.add(information);
        information.setBounds(10, 10, 270, 160);
        victory.setVisible(true);
    }
}