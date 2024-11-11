import java.awt.*;
import javax.swing.*;

/**
 * StoryFrame is a JFrame subclass that displays a Story window.
 * This frame is intended to provide users with the backstory for the game.
 */
public class StoryFrame extends JFrame {
    private Image backgroundImage;

    /**
    * Constructs a StoryFrame instance.
    */
    public StoryFrame() {
        setTitle("Story");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        backgroundImage = new ImageIcon("./backstory.png").getImage();

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(new BorderLayout());
        add(panel);
    }
}