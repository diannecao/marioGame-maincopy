import java.awt.*;


public class Player {

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
    public boolean isCrashing;
    public boolean right;
    public boolean down;
    public boolean left;
    public Rectangle rec;

    public Player (String pName, int pXpos, int pYpos){
        name = pName;
        xpos = pXpos;
        ypos = pYpos;
        dx = 4;
        dy = -4;
        width = 100;
        height = 80;
        isAlive = true;
        isCrashing = false;
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
    public void standOnPlatform(int platformY, int platformHeight){
        if (ypos < platformY){
            ypos = platformY - height;
            dy = 0;
            jumps = 0;
        }

        if (ypos < platformY){
            dy = -5;
        }
    }


    public void moveOnOwn() {
        xpos = xpos + dx - gameApp.speed;
        ypos = ypos + dy;
        if (!isCrashing){
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

//            if (ypos >= 550) {
//                ypos = 550;
//                jumps = 1;
//            }

            //always put this after you've done all the changing of the xpos and ypos values
            rec = new Rectangle(xpos, ypos, width, height);
        }

    }
}

