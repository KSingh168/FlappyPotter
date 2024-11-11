import java.awt.*;
import javax.swing.*;

/**
 * InstructionsFrame is a JFrame subclass that displays an instruction window.
 * This frame is intended to provide users with instructions for the game.
 */
public class InstructionsFrame extends JFrame {
    private Image backgroundImage;

    /**
     * Constructs an InstructionsFrame Instance.
     */
    public InstructionsFrame() {
        setTitle("Instructions");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        backgroundImage = new ImageIcon("./instructions.png").getImage();

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