import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * GameOver is a JPanel that displays the game over screen for the game.
 * It includes options to restart the game or to return to the main menu.
 */
public class GameOver extends JPanel implements ActionListener {

    private JFrame frame;
    private JButton restart = new JButton(new ImageIcon("restart.jpg"));
    private JButton mainmenu = new JButton(new ImageIcon("menu.jpg"));
    private Image gameoverimg;

    /**
     * Creates the constructor for GameOver with the specified JFrame.
     * @param frame The frame of the application.
     */
    public GameOver(JFrame frame) {
        this.frame = frame;
        this.setLayout(null);
        gameoverimg = new ImageIcon(getClass().getResource("/gameover.png")).getImage();
        
        
        // Set up the buttons
        restart.setBounds(450, 450, 120, 50); 
        mainmenu.setBounds(700, 450, 120, 50);
        
        // Add buttons to the panel
        this.add(restart);
        this.add(mainmenu);


        // Add action listeners to the buttons
        restart.addActionListener(this);
        mainmenu.addActionListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(gameoverimg, 0, 0, this.getWidth(), this.getHeight(), this);
        draw(g);
    }

    /**
    *  Draws the score on the screen.
    * @param g The graphics used for drawing
    */
    public void draw(Graphics g) {
        g.setColor(Color.white);
        g.setFont(new Font("Futura", Font.PLAIN, 40));
        //high score
        g.drawString("Score: " + String.valueOf((int) FlappyPotter.score), 10, 35);
        g.drawString("High Score: " + String.valueOf((int) FlappyPotter.highscore), 10, 75);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == restart) {
            restartGame();
        } else if (e.getSource() == mainmenu) {
            showmenu();
        }
    }

    /**
     * Restarts the game, by switching to a new game panel.
     */
    private void restartGame() {
        FlappyPotter gamePanel = new FlappyPotter(frame);
        frame.getContentPane().removeAll();
        frame.add(gamePanel);
        frame.revalidate();
        frame.repaint();
        gamePanel.requestFocusInWindow();
        FlappyPotter.score = 0;
    }

    /**
     * Shows the main menu, by switching to the MenuPanel.
     */
    private void showmenu() {
        MenuPanel mainMenuPanel = new MenuPanel(frame);
        frame.getContentPane().removeAll();
        frame.add(mainMenuPanel);
        frame.revalidate();
        frame.repaint();
    }
}