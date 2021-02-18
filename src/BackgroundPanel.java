import javax.swing.*;
import java.awt.*;

/**
 * The class background of the application with the property <b>image</b>.
 * @author Dmitriy Stepanov
 */
public class BackgroundPanel extends JPanel {
    private Image image;

    /**
     * Receive picture {@link BackgroundPanel#image}
     * @param image the background image
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * Draw background
     * @param g graphic component for drawing an image
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        }
    }
}