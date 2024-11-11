import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import javax.sound.sampled.*;
import javax.swing.*;

/** 
 * FlappyPotter.
 * 
 * @author Kanupriya Singh
 * @id 2133067
 * @author Antea Lani
 * @id 2088118
 * @date 27.10.2024
 * 
 */

//FlappyPotter is a JPanel that implements the game where the player controls Harry Potter
//as he navigates through plants while collecting power-ups and avoiding obstacles.
public class FlappyPotter extends JPanel implements ActionListener, KeyListener {
    public static FlappyPotter flappyPotter;
    private JFrame frame; // Main application frame
    private JLabel countdownLabel = new JLabel("", SwingConstants.CENTER);
    int height = 800;
    int width = 1300; 


    // images
    Image backgroundImg;
    Image harryImg;
    Image topPlantImg;
    Image bottomPlantImg;
    Image blueTopPlantImg;
    Image blueBottomPlantImg;
    Image redTopPlantImg;
    Image redBottomPlantImg;
    Image hallowImg;


    private Clip clip;

    /**
     * Plays background music.
     * @param musicFilePath The path to the music file.
     */
    public void playMusic(String musicFilePath) {
        try {
            URL url = this.getClass().getResource(musicFilePath);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.loop(Clip.LOOP_CONTINUOUSLY); // Loop the music
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    // Harry Potter class
    int harryX = 20;
    int harryY = height / 2;
    int harryWidth = 120;
    int harryHeight = 120;
    int lives = 3;
    boolean invisible = false;
    long invisibleEnd = 0;

    class Harry {
        int x = harryX;
        int y = harryY;
        int width = harryWidth;
        int height = harryHeight;
        Image img;

        Harry(Image img) {
            this.img = img;
        }
    }

    //Plants class
    int plantX = width;
    int plantY = 0;
    int plantWidth = 150;
    int plantHeight = 350; 

    class Plant {
        int x = plantX;
        int y = plantY;
        int width = plantWidth;
        int height = plantHeight;
        Image img;
        boolean passed = false;

        Plant(Image img) {
            this.img = img;
        }
    }

    //Power-up class
    boolean speedup = false;
    long speedupend = 0;
    int actualvelocityx = -6;
    int hallowX;
    int hallowY;
    int hallowWidth = 50;
    int hallowHeight = 50;

    class Powerup {
        int x = hallowX;
        int y = hallowY;
        int width = hallowWidth;
        int height = hallowHeight;
        Image img;
        boolean collect = false;

        /**
         * Initialises the image and position where the power up needs to be placed.
         * @param img The image representing the power-up
         * @param x The X coordinate of the power-up
         * @param y The Y coordinate of the power-up
         */
        Powerup(Image img, int x, int y) {
            this.img = img;
            this.x = hallowX;
            this.y = hallowY;
        }
    }



    //Game logic
    Harry harry;
    int velocityX = -6; //moves plants to the left to simulate Harry moving to the right
    int velocityY = 0;  //Harry moves up/down
    double gravity = 1;

    ArrayList<Plant> plants;
    Random random = new Random();

    Timer gameloop;
    Timer placePlantTimer;
    boolean gameOver = false;
    static double score = 0;
    static double highscore = 0;
    Powerup powerup;

    /**
     * Constructor for the FlappyPotter class.
     * @param frame The main application frame
     */
    FlappyPotter(JFrame frame) {
        this.frame = frame; // Initialize the frame
        // Set up the countdown label
        countdownLabel.setBounds(600, 300, 120, 50);
        countdownLabel.setFont(new Font("Futura", Font.BOLD, 50));
        countdownLabel.setForeground(Color.WHITE);
        this.setLayout(null);
        this.add(countdownLabel);

        // Start the countdown
        startCountdown();

        setPreferredSize(new Dimension(width, height));
        setFocusable(true);
        addKeyListener(this);

        // Load images
        backgroundImg = new ImageIcon(getClass().getResource("background.jpg")).getImage();
        harryImg = new ImageIcon(getClass().getResource("harry.png")).getImage();
        topPlantImg = new ImageIcon(getClass().getResource("plant.png")).getImage();
        bottomPlantImg = new ImageIcon(getClass().getResource("plant.png")).getImage();
        blueTopPlantImg = new ImageIcon(getClass().getResource("blueplantimg.png")).getImage();
        blueBottomPlantImg = new ImageIcon(getClass().getResource("blueplantimg.png")).getImage();
        redTopPlantImg = new ImageIcon(getClass().getResource("redplantimg.png")).getImage();
        redBottomPlantImg = new ImageIcon(getClass().getResource("redplantimg.png")).getImage();
        hallowImg = new ImageIcon(getClass().getResource("hallows.png")).getImage();
 
        harry = new Harry(harryImg); //Creates an instance for the the class Harry
        plants = new ArrayList<Plant>();

        //Place plants timer    
        placePlantTimer = new Timer(1500, new ActionListener() {   
            @Override
            public void actionPerformed(ActionEvent e) {
                placePlants();
            }
        });

        //Game loop timer
        gameloop = new Timer(1000 / 60, this);
        placePowerup();
    

        // Start playing music
        playMusic("Frost Waltz.wav");

    }

    private void startCountdown() {
        countdownLabel.setText("3");
        Timer countdownTimer = new Timer(1000, new ActionListener() {
            int countdown = 3;

            @Override
            public void actionPerformed(ActionEvent e) {
                countdown--;
                if (countdown > 0) {
                    countdownLabel.setText(String.valueOf(countdown));
                } else {
                    ((Timer) e.getSource()).stop();
                    countdownLabel.setText("");
                    // Start the game logic here
                    startGame();
                }
            }
        });
        countdownTimer.start();
    }

    private void startGame() {
        // Start the game loop and place plants timer
        placePlantTimer.start();
        gameloop.start();
    }
    
    /**
     * Places plant obstacles at random heights in the gameplay.
     */
    public void placePlants() {
        int randomPlantY;
        int openingSpace;

        if (score >= 0 && score <= 10) {
            randomPlantY = (int) (plantY - plantHeight / 2 - Math.random() * (plantHeight / 2));
            openingSpace = height / 2;
        } else if (score > 10 && score <= 30) {
            randomPlantY = (int) (plantY - plantHeight / 3 - Math.random() * (plantHeight / 3));
            openingSpace = height / 3;
        } else {
            randomPlantY = (int) (plantY - plantHeight / 4 - Math.random() * (plantHeight / 4));
            openingSpace = height / 4;
        }

        Image topPlantImage;
        Image bottomPlantImage;
        
        if (score >= 0 && score <= 10) {
            topPlantImage = topPlantImg;
            bottomPlantImage = bottomPlantImg;
        } else if (score > 10 && score <= 30) {
            topPlantImage = blueTopPlantImg;
            bottomPlantImage = blueBottomPlantImg;
        } else {
            topPlantImage = redTopPlantImg;
            bottomPlantImage = redBottomPlantImg;
        }
        Plant topPlant = new Plant(topPlantImage);
        topPlant.y = randomPlantY;
        plants.add(topPlant);
    
        Plant bottomPlant = new Plant(bottomPlantImage);
        bottomPlant.y = topPlant.y  + plantHeight + openingSpace;
        plants.add(bottomPlant);

    }

    /**
     * Places power-up at random positions in gameplay.
     */
    public void placePowerup() {
        int randomhallowX = width + random.nextInt(500);
        int randomhallowY = random.nextInt(height - 100);
        powerup = new Powerup(hallowImg, randomhallowX, randomhallowY);
        powerup.x = randomhallowX;
        powerup.y = randomhallowY;
    }

    /**
     * Paints the game components on the panel.
     * 
     * @param g The Graphics object used for drawing.
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    /**
    *  Draws the game components on the screen.
    * @param g The graphics used for drawing
    */
    public void draw(Graphics g) {
        //background
        g.drawImage(backgroundImg, 0, 0, this.width, this.height, null);

        //harry image
        g.drawImage(harryImg, harry.x, harry.y, harry.width, harry.height, null);

        //plants
        for (int i = 0; i < plants.size(); i++) {
            Plant plant = plants.get(i);
            g.drawImage(plant.img, plant.x, plant.y, plant.width, plant.height, null);
        }

        //powerup
        if (powerup != null && !powerup.collect) {
            g.drawImage(hallowImg, powerup.x, powerup.y, powerup.width, powerup.height, null);
        }

        //score
        g.setColor(Color.white);

        g.setFont(new Font("Futura", Font.PLAIN, 32));
        //score
        g.drawString("Score:" + String.valueOf((int) score), 10, 35);
        //lives
        g.drawString("Lives: " + lives, 10, 85);
    }

    /**
     * Updates the game by moving the player and handling collisions.
     */
    public void move() {
        //harry 
        velocityY += gravity;
        harry.y += velocityY;
        harry.y = Math.max(harry.y, 0);

        //plants
        
        if (!invisible) {
            for (int i = 0; i < plants.size(); i++) {
                Plant plant = plants.get(i);
                plant.x += velocityX;
                
                if (!plant.passed && harry.x > plant.x + plant.width) {
                    score += 0.5; 
                    //0.5 because there are 2 plants! so 0.5*2 = 1, 1 for each set of plants
                    plant.passed = true;
                }
                if (collision(harry, plant)) {
                    //decreases and updates invisible on collision with a plant
                    lives--;
                    invisible = true;
                    invisibleEnd = System.currentTimeMillis() + 2000;
                    if (lives <= 0) {
                        gameOver = true; //Ends game if no lives are left
                    } else {
                        //Resets game positions to respawn with a new life
                        harry.y = harryY;
                        velocityY = 0;
                        plants.clear();
                        placePlantTimer.restart();

                    }
                }     
            } 
        } else {
            if (invisible && System.currentTimeMillis() > invisibleEnd) {
                invisible = false;
            }
        }
        
        if (harry.y + harry.height > height) {
            lives--; // Decrease life if Harry hits the ground
            if (lives <= 0) {
                gameOver = true; // End game if no lives are left
            } else {
                //Resets game positions to respawn with a new life
                harry.y = harryY; 
                velocityY = 0;
                plants.clear();
                placePlantTimer.restart();

            } 
        } 
        //Power-up collection
        if (powerup != null) {
            powerup.x += velocityX;
            //Checks for collision between Harry and the power-up
            if (collision(harry, powerup)) {
                speedup = true;
                speedupend = System.currentTimeMillis() + 5000; //duration of power up
                velocityX = actualvelocityx * 3; 
                placePlantTimer.setDelay(1500 / 2);  
                powerup.collect = true;
            }
            if (powerup.collect) {
                powerup = null;
            }
            
        }
        //Removes effects of power-up when the power-up duration has ended
        if (speedup && System.currentTimeMillis() > speedupend) {
            speedup = false;
            velocityX = actualvelocityx;  
            placePlantTimer.setDelay(1500);  
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
        move();
        if (gameOver) {
            placePlantTimer.stop();
            gameloop.stop();
            clip.stop();
            //Updates High Score
            if (score > highscore) {
                highscore = score;
            }
            // Switch to GameOver panel
            showGameOverScreen();
        }
    }

    /**
     * Displays the Game Over screen, replacing the game panel.
     */
    private void showGameOverScreen() {
        GameOver gameOverPanel = new GameOver(frame);
        frame.getContentPane().removeAll();
        frame.add(gameOverPanel);
        frame.revalidate();
        frame.repaint();
    }

    /**
     * Checks for collision between Harry and a power-up.
     * @param a The object created from the class Harry.
     * @param b The object created from the class Powerup.
     * @return true if  collision is detected, false otherwise.
     */
    boolean collision(Harry a, Powerup b) {
        return a.x < b.x + b.width       //a's top left corner doesn't reach b's top right corner
               && a.x + a.width > b.x    //a's top right corner passes b's top left corner
               && a.y < b.y + b.height   //a's top left corner doesn't reach b's bottom left corner
               && a.y + a.height > b.y;  //a's bottom left corner passes b's top left corner
    }

    /**
     * Checks for collision between harry and a plant.
     * @param a The object created from the class Harry.
     * @param b The object created from the class Plant
     * @return True if a collision is detetected, false otherwise
     */
    boolean collision(Harry a, Plant b) {
        return a.x < b.x + b.width       //a's top left corner doesn't reach b's top right corner
               && a.x + a.width > b.x    //a's top right corner passes b's top left corner
               && a.y < b.y + b.height   //a's top left corner doesn't reach b's bottom left corner
               && a.y + a.height > b.y;  //a's bottom left corner passes b's top left corner
    }
                
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_UP) {
            if (gameOver) {
                // Restart game by resetting conditions
                harry.y = harryY;
                velocityY = 0;
                plants.clear();
                gameOver = false;
                score = 0;
                speedup = false;
                velocityX = actualvelocityx;
                placePlantTimer.setDelay(1500);
                placePowerup();
                gameloop.start();
                placePlantTimer.start();
                lives = 3;
            } else {
                velocityY = -9;
            }
        }
    }



    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

}