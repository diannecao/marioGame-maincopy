import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.util.ArrayList;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/***
 * Step 0 for keyboard control - Import
 */


//*******************************************************************************
// Class Definition Section

public class gameApp implements Runnable, KeyListener {

    //Variable Definition Section
    //Declare the variables used in the program
    //You can set their initial values too

    //Sets the width and height of the program window

    public boolean gameOver = false;
    final int WIDTH = 1500;
    final int HEIGHT = 700;

    public static int speed = 3;
    //public ArrayList<int>
    //Declare the variables needed for the graphics
    public JFrame frame;
    public Canvas canvas;
    public JPanel panel;
    public SoundFile themeMusic;

    public BufferStrategy bufferStrategy;
    public Image backgroundPic;
    public Image gameoverPic;
    public Image blobPic;
    public Image platformPic;
    public Image platform2;
    //Declare the objects used in the program
    //These are things that are made up of more than one variable type
    public boolean speedingUp;
    public ArrayList<Integer> scores;
    private Timer timer;
    public long gameStartTime = 0;
    public long elapsedTime = (System.currentTimeMillis() - gameStartTime) / 1000;
    private int timeElapsed;
    public Player blob;
    public Platform platform;
    public ArrayList<Platform> platforms;

    // Background positions
    private int starryForegroundX = 0;
    private int starryStarX = 0;

    // Background scroll speed
    private int scrollSpeedStarryForeground = -1; // Foreground moves faster for parallax effect
    private int scrollSpeedStarryStar = -2; // Stars move the fastest for deeper parallax effect

    private boolean useLilyBackground = false;
    public Image starrybackground;
    public Image starryforeground;
    public Image starrystar;
    public Image lilybackground;
    public Image lilybridge;
    public Image lilyflower;

    // Lily background positions
    private int lilyFlowerX = 0;

    // Lily background scroll speed
    private int scrollSpeedLilyFlower = -1;


    //-------------------------------------


    // Main method definition
    // This is the code that runs first and automatically
    public static void main(String[] args) {
        gameApp ex = new gameApp();   //creates a new instance of the game
        new Thread(ex).start();                 //creates a threads & starts up the code in the run( ) method
    }

    public gameApp() {
        setUpGraphics();
        canvas.addKeyListener(this);
        platforms = new ArrayList<>();
        scores = new ArrayList<>();
        platformPic = Toolkit.getDefaultToolkit().getImage("platform.PNG");
        platform2 = Toolkit.getDefaultToolkit().getImage("platform2.png");
        blob = new Player("blob", 400, HEIGHT-100);
        platforms.add(new Platform(380, HEIGHT - 50));
        blobPic = Toolkit.getDefaultToolkit().getImage("blob.gif");
        //hackathon project
        gameoverPic = Toolkit.getDefaultToolkit().getImage("Gameover.JPG");
        starrybackground = Toolkit.getDefaultToolkit().getImage("starrybackground.png");
        starryforeground = Toolkit.getDefaultToolkit().getImage("starryforeground.PNG");
        starrystar = Toolkit.getDefaultToolkit().getImage("starrystar.PNG");
        lilybackground = Toolkit.getDefaultToolkit().getImage("lilybackground.png");
        lilybridge = Toolkit.getDefaultToolkit().getImage("lilybridge.PNG");
        lilyflower = Toolkit.getDefaultToolkit().getImage("lilyflower.PNG");

        themeMusic = new SoundFile ("thememusic.wav");
        themeMusic.play();


//        platform = new Platform(WIDTH, HEIGHT -200);
        for (int i = 0; i < 2; i++){
            platforms.add(new Platform(500+getRandomX(), HEIGHT -50 - getRandomY()));
            platforms.add(new Platform(500+WIDTH/3 + getRandomX(), HEIGHT -50 - getRandomY()));
            platforms.add(new Platform(500+WIDTH/3*2 + getRandomX(), HEIGHT -50 - getRandomY()));
        }
        int delay = 1000; // Delay in milliseconds (1 second)
        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                // Increment the time elapsed
                timeElapsed++;
            }
        };
        timer = new Timer(delay, taskPerformer);
        timer.start(); // Start the timer
    }
    public int getRandomX(){
        return (int)(Math.random()*WIDTH/3);
    }
    public int getRandomY(){
        return (int)(Math.random()*4*150);
    }
    public void run() {
        //for the moment we will loop things forever.
        while (true){
            if (blob.isAlive){
                if (gameStartTime == 0){
                    gameStartTime = System.currentTimeMillis();
                    System.out.println("START@@@@@@@"+gameStartTime);
                }
                // Calculate the elapsed time in seconds
                 elapsedTime = (System.currentTimeMillis() - gameStartTime) / 1000;
                System.out.println(elapsedTime);
                if (elapsedTime >= 20 && !useLilyBackground) { // 10 seconds have passed
                    useLilyBackground = true; // Switch to lily backgrounds
                }

            }


            moveThings();  //move all the game objects
            crash();
//            speedingUp = false;
//            speedUp();
//            mimimizeMario();
            death();
            render();  // paint the graphics
            pause(20); // sleep for 10 ms
        }
    }
    public void death(){
        if (blob.xpos < -blob.width || blob.ypos > HEIGHT){
            blob.isAlive = false;
            gameStartTime = System.currentTimeMillis();
            gameOver = true;
            useLilyBackground = false;
        }
    }
    public void speedUp(){
        if (timeElapsed % 10 == 0 && !speedingUp){
            speed ++;
            speedingUp = true;
            if (timeElapsed % 10 == 5 && !speedingUp){
                speedingUp = false;
            }
        }
    }
    public void moveThings() {
        //calls the move( ) code in the objects
        blob.moveOnOwn();
        for (int i = 0; i < platforms.size(); i++) {
            Platform platform = platforms.get(i);
            platform.move(); // Assuming you have a move method in your Platform class
            if (platform.xpos <= -platform.width) { // Assuming platform's x coordinate is its left edge
                platforms.remove(i); // Remove platform that reached the edge
                platforms.add(new Platform(WIDTH, HEIGHT -50 - getRandomY())); // Add a new platform from the right edge
            }
        }
//        for (Platform p : platforms){
//            if (p.xpos < 10){
//                p.isAlive = false;
//                platforms.remove(p);
//                platforms.add(new Platform(WIDTH, HEIGHT -50 - getRandomY()));
//            }
//            p.move();
//        }
        if (!useLilyBackground) {
            starryForegroundX += scrollSpeedStarryForeground;
            starryStarX += scrollSpeedStarryStar;
        } else {
            // Update positions for lily backgrounds
            lilyFlowerX += scrollSpeedLilyFlower;

            // Reset positions if the backgrounds move off-screen for continuous scrolling
            if (lilyFlowerX <= -WIDTH) lilyFlowerX = 0;
        }
        //------------

        // Reset positions if the backgrounds move off-screen
        if (starryForegroundX <= -WIDTH) starryForegroundX = 0;
        if (starryStarX <= -WIDTH) starryStarX = 0;

    }

    public void crash(){
        for (Platform p: platforms){
            if (blob.rec.intersects(p.rec) && !blob.isCrashing){
                blob.standOnPlatform(p.ypos, p.height);
            }
        }
    }

    //Pauses or sleeps the computer for the amount specified in milliseconds
    public void pause(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
        }
    }
    public void keyPressed(KeyEvent event) {
        //This method will do something whenever any key is pressed down.
        //Put if(  statements here
        char key = event.getKeyChar();     //gets the character of the key pressed
        int keyCode = event.getKeyCode();  //gets the keyCode (an integer) of the key pressed
        System.out.println("Key Pressed: " + key + "  Code: " + keyCode);
        if (keyCode == 10) {// enter
            gameStartTime = 0;
            gameOver = false;
            blob = new Player("blob", 400, HEIGHT-100);
            platforms.clear();
            platforms.add(new Platform(380, HEIGHT - 50));
            for (int i = 0; i < 2; i++){
                platforms.add(new Platform(500+getRandomX(), HEIGHT -50 - getRandomY()));
                platforms.add(new Platform(500+WIDTH/3 + getRandomX(), HEIGHT -50 - getRandomY()));
                platforms.add(new Platform(500+WIDTH/3*2 + getRandomX(), HEIGHT -50 - getRandomY()));
            }
            int delay = 1000; // Delay in milliseconds (1 second)
            ActionListener taskPerformer = new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    // Increment the time elapsed
                    timeElapsed++;
                }
            };
            timer = new Timer(delay, taskPerformer);
            timer.start(); // Start the timer
        }
        if (keyCode == 39) {// right arrow
            blob.right = true;
        }
        if (keyCode == 83) {// s (move down)
            blob.down = true;
        }
        if (keyCode == 32 && blob.jumps < 3) {// space bar
            blob.dy = -15;
            blob.jumps++;
            System.out.println("jump #: " + blob.jumps);
        }
        if (keyCode == 37) {// left arrow
            blob.left = true;
        }
    }//keyPressed()

    public void keyReleased(KeyEvent event) {
        char key = event.getKeyChar();
        int keyCode = event.getKeyCode();
        //This method will do something when a key is released
        if (keyCode == 39) {
            blob.right = false;
        }
        if (keyCode == 83) {
            blob.down = false;
        }
        if (keyCode == 32 && blob.jumps < 2) {
            blob.dy = -20;
            blob.jumps++;
        }
        if (keyCode == 37) {
            blob.left = false;
        }

    }//keyReleased()
    public void keyTyped(KeyEvent event) {
        // handles a press of a character key (any key that can be printed but not keys like SHIFT)
        // we won't be using this method, but it still needs to be in your program
    }//keyTyped()

    //Graphics setup method
    private void setUpGraphics() {
        frame = new JFrame("Application Template");   //Create the program window or frame.  Names it.

        panel = (JPanel) frame.getContentPane();  //sets up a JPanel which is what goes in the frame
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));  //sizes the JPanel
        panel.setLayout(null);   //set the layout

        // creates a canvas which is a blank rectangular area of the screen onto which the application can draw
        // and trap input events (Mouse and Keyboard events)
        canvas = new Canvas();
        canvas.setBounds(0, 0, WIDTH, HEIGHT);
        canvas.setIgnoreRepaint(true);

        panel.add(canvas);  // adds the canvas to the panel.

        // frame operations
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //makes the frame close and exit nicely
        frame.pack();  //adjusts the frame and its contents so the sizes are at their default or larger
        frame.setResizable(false);   //makes it so the frame cannot be resized
        frame.setVisible(true);      //IMPORTANT!!!  if the frame is not set to visible it will not appear on the screen!

        // sets up things so the screen displays images nicely.
        canvas.createBufferStrategy(2);
        bufferStrategy = canvas.getBufferStrategy();
        canvas.requestFocus();
        System.out.println("DONE graphic setup");
    }

    //Paints things on the screen using bufferStrategy

    private void render() {
        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
        g.clearRect(0, 0, WIDTH, HEIGHT);


        if (!gameOver) {
            if (!useLilyBackground) {
                int imageWidth = WIDTH; // Assuming the background image covers the full width of the window

                g.drawImage(starrybackground, -10, 0, null);

                g.drawImage(starrystar, starryStarX, 0, null);
                g.drawImage(starrystar, starryStarX + imageWidth, 0, null);
                // System.out.println(starryStarX+imageWidth);

                g.drawImage(starryforeground, starryForegroundX, 0, null);
                g.drawImage(starryforeground, starryForegroundX + imageWidth, 0, null);
            } else {

                // Draw lily bridge and lily flower with scrolling effect
                int imageWidth = WIDTH; // Assuming the images cover the full width of the window
                g.drawImage(lilybackground, -10, 0, null);

                g.drawImage(lilyflower, lilyFlowerX, 0, null);
                g.drawImage(lilyflower, lilyFlowerX + imageWidth, 0, null);
                g.drawImage(lilybridge, -10, 0, null);
            }
            g.drawImage(backgroundPic, 0, 0, WIDTH, HEIGHT, null);
            g.drawImage(blobPic, blob.xpos, blob.ypos, blob.width, blob.height, null);
//            g.drawRect(blob.xpos, blob.ypos, blob.width, blob.height);
//            g.drawImage(platformPic, platform.xpos, platform.ypos, platform.width, platform.height, null);
//            g.drawRect(platform.xpos, platform.ypos, platform.width, platform.height);
            for (Platform p : platforms) {
                if(!useLilyBackground){
                    g.drawImage(platformPic, p.xpos, p.ypos, p.width, p.height, null);
                }else{
                    g.drawImage(platform2, p.xpos, p.ypos, p.width, p.height, null);
                }

            }
        } else {
            g.drawImage(gameoverPic,0,0, WIDTH, HEIGHT, null);
            scores.add((int) elapsedTime);
            System.out.println("------------------------------");
            System.out.println("YOUR SCORE: "+elapsedTime);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20)); // You can adjust the font size and style

        String timeString = "Time Survived: " + elapsedTime + "s";

        int stringWidth = g.getFontMetrics().stringWidth(timeString);
        g.drawString(timeString, WIDTH - stringWidth - 10, 30);

        g.dispose();
        bufferStrategy.show();
    }

}


