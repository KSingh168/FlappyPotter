import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * MenuPanel is a JPanel that represents the main menu of the game "Flappy Potter".
 * It includes buttons for starting the game, viewing instructions, reading the backstory, 
 * and quitting the application. 
 */
public class MenuPanel extends JPanel implements ActionListener {
    public JFrame frame;
    private JButton play = new JButton(new ImageIcon("play.jpg"));
    private JButton instructions = new JButton(new ImageIcon("instructions.jpg"));
    private JButton story = new JButton(new ImageIcon("backstory.jpg"));
    private JButton quit = new JButton(new ImageIcon("quit.jpg"));
    private Image backgroundImage;

    /**
     * Creates a default constructor for the class MenuPanel with the specified JFrame.
     * @param frame The frame of the application
     */
    public MenuPanel(JFrame frame) {
        this.frame = frame;
        this.setLayout(null); // Use absolute layout

        // Load the background image
        backgroundImage = new ImageIcon("gamestart.jpg").getImage();

        // Set up the buttons
        play.setBounds(600, 400, 120, 50); // Adjust the position and size as needed
        instructions.setBounds(600, 500, 120, 50);
        story.setBounds(600, 600, 120, 50);
        quit.setBounds(1100, 650, 120, 50);

        // Add buttons to the panel
        this.add(play);
        this.add(instructions);
        this.add(story);
        this.add(quit);


        // Add action listeners to the buttons
        play.addActionListener(this);
        instructions.addActionListener(this);
        story.addActionListener(this);
        quit.addActionListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the background image
        g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
    }

    /**
     * This method will be invoked when a button is clicked.
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == play) {
            startGame();

        } else if (e.getSource() == quit) {
            frame.dispose();
        } else if (e.getSource() == instructions) {
            new InstructionsFrame().setVisible(true);
        } else if (e.getSource() == story) {
            new StoryFrame().setVisible(true);
        }
    }

    private void startGame() {
        FlappyPotter gamePanel = new FlappyPotter(frame);
        frame.getContentPane().removeAll();
        frame.add(gamePanel);
        frame.revalidate();
        frame.repaint();
        gamePanel.requestFocusInWindow();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Flappy Potter");
        MenuPanel menu = new MenuPanel(frame);

        frame.add(menu);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1300, 800);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
    }
}