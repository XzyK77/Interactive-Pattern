import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Interactive_Pattern extends PApplet {

PImage frameRDude1, frameRDude2, frameRDude3, frameRDude4;
PImage frameLDude1, frameLDude2, frameLDude3, frameLDude4;
PImage frameDrop01, frameDrop02, blastZoneFrame;
int frameNum;
int shapeSize;
PShape s;
DropDude []man;
int [][]blastZone;
int blastZoneIndex;
int dudeSpeed, dropSpeed;

public void setup(){

  frameRDude1 = loadImage("R_dude1.png");                                //LoadImages
  frameRDude2 = loadImage("R_dude2.png");
  frameRDude3 = loadImage("R_dude3.png");
  frameRDude4 = loadImage("R_dude4.png");
  frameLDude1 = loadImage("L_dude1.png");                                
  frameLDude2 = loadImage("L_dude2.png");
  frameLDude3 = loadImage("L_dude3.png");
  frameLDude4 = loadImage("L_dude4.png");
  frameDrop01 = loadImage("dropFrame1.png");
  frameDrop02 = loadImage("dropFrame2.png");
  blastZoneFrame = loadImage("BlastZoneFrame.png");
  
  frameNum = 0;                                                      //AnimationFrame
  
  blastZoneIndex = 0;                                                //BlastZone Array set up
  blastZone = new int[20][2];
  for(int i = 0; i < blastZone.length; i++){                         //Fills BlastZoneArray with off-screen Locations
    blastZone[i][0] = -200;
    blastZone[i][1] = -200;
  }
  
  man = new DropDude[20];                                            //dude Array set up.
  for(int i = 0; i < man.length; i++){ man[i] = new DropDude(); }    //Sets all dudes to default
  
  
  
                                                      //random size
  frameRate(30);                                                     

}

public void draw(){

  background(0);
  
  for(int i = 0; i < blastZone.length; i++){ image(blastZoneFrame, blastZone[i][0], blastZone[i][1]); }   //Draw BlastZone Array
  
  for(int i = 0; i < man.length; i++){ 
    
    man[i].moveDude();                                              //Movement
    
    if(man[i].impactedGround() == true){                            //BlastZone determination
      blastZone[blastZoneIndex][0] = man[i].x;
      blastZone[blastZoneIndex][1] = man[i].y;
      blastZoneIndex++;
      if(blastZoneIndex == 19){blastZoneIndex = 0;}
    }
    
    man[i].drawDude();                                              //Draws dude
  }
}

class DropDude{
  
  int x, y, dropDistance, frameNum, dudeSpeed, dropSpeed;
  boolean direction, landed;
  
  DropDude(){  setDefaultDude(); }
  
  public void setDefaultDude(){
    x = PApplet.parseInt(random(0, width));
    y = PApplet.parseInt(random(0, height));
    dudeSpeed = PApplet.parseInt(random(8,12));
    dropSpeed = PApplet.parseInt(random(50,60));
    dropDistance = 1000;
    frameNum = 0;
    direction = PApplet.parseBoolean(PApplet.parseInt(random(2)));
    landed = false;
    if(direction == true){ dudeSpeed = -dudeSpeed; }                             //Change to negative speed if going LEFT.
  }
  
  public void moveDude(){
    if(dropDistance > 0){  dropDistance -= dropSpeed;  } else { x += dudeSpeed;} //If (There is a DropDistance) {Move based on drop} else {move like normal}
    if((x < -200) || (x > width)){ setDefaultDude(); }                           //If (Goes off screen) {reset}    
  }
  
  public void drawDude(){
  
    frameNum += 1;
      if(frameNum == 5){frameNum = 1;}
      
      if(dropDistance > 0){
        switch(frameNum){
        case 1:image(frameDrop01, x, y - dropDistance);break;
        case 2:image(frameDrop02, x, y - dropDistance);break;
        case 3:image(frameDrop01, x, y - dropDistance);break;
        case 4:image(frameDrop02, x, y - dropDistance);break;
        }
      } else {
        
        if(direction == true){  //Going LEFT
          switch(frameNum){
            case 1: image(frameLDude1, x, y); break;
            case 2: image(frameLDude2, x, y); break;
            case 3: image(frameLDude3, x, y); break;
            case 4: image(frameLDude4, x, y); break;
          }
        }
        else{
          switch(frameNum){
            case 1: image(frameRDude1, x, y); break;
            case 2: image(frameRDude2, x, y); break;
            case 3: image(frameRDude3, x, y); break;
            case 4: image(frameRDude4, x, y); break;
          }
        }
      }
  }
  
  public boolean impactedGround(){                                    //Determines if the man has hit the ground.
    if((dropDistance <= 0) && (landed == false)){              //if dropDistance is at 0, it has landed, so set landed value = true so it wont trigger again.
      landed = true;
      return true;                                             //Returns true
    }
    return false;                                              //Default return
  }
}

public void mousePressed(){                                                         //Vualla!  Mouse functionality!
  if(mouseButton == LEFT){
    for(int i = 0; i < man.length; i++){
    
      float distance = sqrt(sq(mouseX - man[i].x) + sq(mouseY - man[i].y));  //A squared + B squared = C squared
      if(distance <= 100){ man[i].setDefaultDude(); }                        //Resets the dude for re-drop
    
    }
  }
}

public void keyPressed(){

  if(key == 'f'){ println("Wow.. you picked 'f' as your key..."); }

}
  public void settings() {  size(1116,700); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Interactive_Pattern" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
