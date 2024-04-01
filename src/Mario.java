import java.awt.*;


public class Mario {

    public String name;               //name of the hero
    public int xpos;                  //the x position
    public int ypos;                  //the y position
    public int dx;                    //the speed of the hero in the x direction
    public int dy;                    //the speed of the hero in the y direction
    public int width;                 //the width of the hero image
    public int height;                //the height of the hero image
    public int lives;
    public int jumps = 1;

    public boolean isAlive;           //a boolean to denote if the hero is alive or dead
    public boolean isCrashing = false;
    public boolean isMinimizing = false;
    public boolean minimizingBill = false;
    public boolean right;
    public boolean down;
    public boolean left;
    public Rectangle rec;

    public Mario (String pName, int pXpos, int pYpos){
        name = pName;
        xpos = pXpos;
        xpos = (int)(Math.random()*500+100);
        ypos = pYpos;
        // ypos = (int)(Math.random()*500+50);
        dx = 3;
        dy = -3;
        width = 100;
        height = 80;
        isAlive = true;
        rec = new Rectangle(xpos, ypos, width, height);
    } // end mario constructor

    public void stop(){
        dx = 0;
        dy = 0;
    }

    public void bounce() {
        xpos = xpos + dx;
        ypos = ypos + dy;
        // if mario hits the right side, reverse dx direction
        if (xpos >= 1050 - width || xpos <= 0) {
            dx = -dx;
        }
        // if alien hits the top
        if (ypos <= 0 || ypos >= 530 - height) {
            dy = -dy;
        }
        rec = new Rectangle(xpos,ypos,width,height);
    }

    public void mushroomBounce() {
        xpos = xpos + dx;
        ypos = ypos + dy;
        // if mario hits the right side, reverse dx direction
        if (xpos >= 1100 - width || xpos <= 0) {
            dx = -dx;
        }
        if (ypos <= 350 || ypos >= 530 - height) {
            dy = -dy;
        }
        rec = new Rectangle(xpos,ypos,width,height);
    }

    public void moveOnOwn() {

        xpos = xpos + dx;
        ypos = ypos + dy;

        if(right){
            dx = 5;
        }
        else if (left){
            dx = -5;
        }
        else {
            dx = 0;
        }

        dy = dy + 1;

        if (ypos >= 480) {
            ypos = 480;
            jumps = 1;
        }

        //always put this after you've done all the changing of the xpos and ypos values
        rec = new Rectangle(xpos, ypos, width, height);

    }
}

