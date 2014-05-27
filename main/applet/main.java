import processing.core.*; 
import processing.xml.*; 

import ddf.minim.*; 

import java.applet.*; 
import java.awt.Dimension; 
import java.awt.Frame; 
import java.awt.event.MouseEvent; 
import java.awt.event.KeyEvent; 
import java.awt.event.FocusEvent; 
import java.awt.Image; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class main extends PApplet {

/*
TO DO:
 finish demo buttons and info display
 animate other tutorial pages?
 turn gamescreen into a class (Tank) - will be easier to restart things
 step by step score breakdown
 */


//import processing.opengl.*;


//game vars
int kittyMax;
int border;
ArrayList kitties;
Enemy tempKit;
Enemy tempKit2;
Player player;
Shark shark;
PVector tempPos;
Info info;

//gui vars
Menu tutorial;
boolean intro;
boolean keyPress;
Button tutorialButton;
Button miniMenuButton;
Button endIntro;
Button start;
Button restart;
Button quit;
Button bgUp;
Button bgDown;
Button upArrButton;
Button downArrButton;
Button leftArrButton;
Button rightArrButton;
Button spacebarButton;
SpeakerButton buttonMuteSFX;
SpeakerButton buttonMuteMusic;
int opacity;
int introFontColour;
String endingMess;

//images/visuals
PImage logo;
PImage[] tutPages;
int currPage;
int maxPages;
PImage[] bg;
int bgSelect;
int bgMax;
PFont font;

//sound
Minim minim;
AudioPlayer introSong;
AudioPlayer gameSong;
AudioPlayer buttonSFX;
AudioPlayer chompSFX;
AudioPlayer jawsSFX;


public void setup() {
  //size(800, 600, OPENGL);
  size(800, 600, P3D);
  //size(800, 600);
  //sizes 600-400 
  hint(ENABLE_OPENGL_4X_SMOOTH);
  hint(DISABLE_OPENGL_2X_SMOOTH);
  //smooth();
  frameRate(30);
  border = 10;
  font = loadFont("ACMESecretAgent-24.vlw");
  tutorialButton = new Button("Tutorial");
  miniMenuButton = new Button("Pause", "Resume");
  endIntro = new Button("Enter at your own risk...", 250, color(150, 0, 0), color(240, 0, 0));
  restart = new Button("Restart");
  quit = new Button("Quit");
  bgUp = new Button(">", 20);
  bgDown = new Button("<", 20);
  upArrButton = new DemoButton("^", "^", width/15, height/16, 100, 200, 0, 0, UP);
  downArrButton = new DemoButton(".", ".", width/15, height/16, 100, 200, 0, 0, DOWN);
  leftArrButton = new DemoButton("<", "<", width/15, height/16, 100, 200, 0, 0, LEFT);
  rightArrButton = new DemoButton(">", ">", width/15, height/16, 100, 200, 0, 0, RIGHT);
  spacebarButton = new DemoButton("Space", "Space", width/5, height/16, 100, 200, 0, 0, -1);
  buttonMuteSFX = new SpeakerButton(15);
  buttonMuteMusic = new SpeakerButton(15);
  info = new Info();
  endingMess = "";

  opacity = 0;
  introFontColour = 0;

  //load images
  currPage = 0;
  maxPages = 4;
  tutorial = new Menu(maxPages);
  tutPages = new PImage[maxPages];
  tutPages[0] = loadImage("tutorialIntro.jpg");
  tutPages[1] = loadImage("tutorialReap.jpg");
  tutPages[2] = loadImage("tutorialShark.jpg");
  tutPages[3] = loadImage("tutorialControls.jpg");

  logo = loadImage("logo.jpg");

  bgSelect = 0;
  bgMax = 4;
  bg = new PImage[bgMax];
  bg[0] = loadImage("bg2.jpg");
  bg[1] = loadImage("bg1.jpg");
  bg[2] = loadImage("bg3.jpg");
  bg[3] = loadImage("bg4.jpg");

  //load sounds
  minim = new Minim(this);
  introSong = minim.loadFile("creepy.wav");
  gameSong = minim.loadFile("underwater.wav");
  chompSFX = minim.loadFile("chomp.wav");
  buttonSFX = minim.loadFile("waterdrop.wav");
  jawsSFX = minim.loadFile("jaws.wav");

  //gui
  intro = true;
  if (intro)
    introSong.loop();
  gameSong.loop();


  kitties = new ArrayList();
  kittyMax = width/80;
  player = new Player();
  shark = new Shark();
} //end setup

public void draw() {

  background(0);

  //display the starting screen
  if (intro)
  {
    imageMode(CENTER);
    tint(255, opacity);
    image(logo, width/2+20, height/3+30);
    if (opacity < 254)
      opacity += 2;

    if (opacity >= 254)
    {
      fill(introFontColour);
      textAlign(CENTER);
      textFont(font, 18);
      text("Created by:", width/2, 425);
      text("Kaylyn Garnett", width/2, 455);
      if (introFontColour < 254)
        introFontColour += 2;
    }  

    if (opacity >= 254 && introFontColour >= 250)
    {
      endIntro.run(width/2, 500);
    }
  }

  //show the tutorial
  else if (tutorialButton.active)
  {
    refresh();

    //menu images
    imageMode(CENTER);
    image(tutPages[currPage], width/2, (height-50)/2, width-(border*2), height-(border*2)-50);

    //draw the dynamic demo buttons and navigation
    if (currPage == (maxPages-1))
    {
      info.displayDemoInfo(width/6, 2*(height-50)/3);
      tutorial.displayDemo(5*width/6, (height-50)/3);
    }
    tutorial.displayPageCount(width/2, (height-50-border)-(height/20));
    tutorial.backPage.run(width/2-border-tutorial.play.bWidth, height-30);
    tutorial.forwardPage.run(width/2+border+tutorial.play.bWidth, height-30);
    tutorial.play.run(width/2, height-30);
  }

  //show the popup menu with a blank playing screen beneath it
  else if (miniMenuButton.active)
  {
    refresh();
    imageMode(CORNER);
    image(bg[bgSelect], border, border, width-(border*2), height-(border*2)-50);

    //set up minimenu
    pushMatrix();
    translate(width/2, (height-60)/2);
    strokeWeight(2);
    stroke(180);
    fill(0);
    rectMode(CENTER);
    rect(0, 0, 200, 300);
    textAlign(CENTER);
    fill(255);
    textFont(font, 24);

    //choose between the game over and the inplay menu
    if (player.lives <= 0)
    {
      text("Game Over!", 0, -100);
      textFont(font, 16);
      text("Points:", -50, -60);
      text(""+info.points, 50, -60);
      text("Time:", -55, -40);
      text(""+info.sTime, 50, -40);
      info.score = info.points + info.seconds + (info.minutes*60);
      text("Final Score: "+info.score, 0, -10);

      //choose what message shows up depending on your score
      if (info.score >= info.bestScore)
      {
        info.bestScore = info.score;
        endingMess = "New Record!!";
      }
      else
      {
        if (info.points <= 100)
          endingMess = "That sucked..."; 
        else if (info.points <= 1000)
          endingMess = "Okay, I guess.."; 
        else if (info.points <= 5000)
          endingMess = "Okay, I guess.."; 
        else if (info.points <= 10000)
          endingMess = "Awesome!"; 
        else if (info.points <= 15000)
          endingMess = "Dude no way =o"; 
        else 
          endingMess = "You're shittin' me";
      } 
      text(endingMess, 0, 10); 
      popMatrix();
    }

    //this is the paused menu
    else
    {
      fill(255);
      textFont(font, 24);
      text("Paused", 0, -80);
      popMatrix();  
      //Volume options
      fill(255);
      textFont(font, 16);
      text("SFX:", width/2-20, (height-60)/2-35);
      buttonMuteSFX.run(width/2+35, (height-60)/2-40);
      fill(255);
      textFont(font, 16);
      text("Music:", width/2-20, (height-60)/2-15);
      buttonMuteMusic.run(width/2+35, (height-60)/2-20);

      //BG options
      fill(255);
      textFont(font, 18);
      text("BG" + (bgSelect+1), width/2, (height-60)/2+30);
      bgDown.run(width/2-40, (height-60)/2+20);
      bgUp.run(width/2+40, (height-60)/2+20);

      //regular out of menu buttons
      miniMenuButton.run(width-border-miniMenuButton.bWidth/2, height-30);
      tutorialButton.run(border+tutorialButton.bWidth/2, height-30);
      info.displayInfo(width/2, height-35);

      //if player presses space, pause game/bring up menu
      if (keyPressed)
        if (key == ' ')
        {
          keyPress = true;
        }
    }
    restart.run(width/2, (height-60)/2+60);
    quit.run(width/2, (height-60)/2+100);
  }

  //show and play the game
  else
  {
    refresh();
    imageMode(CORNER);
    image(bg[bgSelect], border, border, width-(border*2), height-(border*2)-50);

    //show buttons
    miniMenuButton.run(width-border-miniMenuButton.bWidth/2, height-30);
    tutorialButton.run(border+tutorialButton.bWidth/2, height-30);
    info.update();
    info.displayInfo(width/2, height-35);

    //spawn fish every couple seconds
    if (info.seconds%2 == 0 || kitties.size() == 0) {
      if (kitties.size() < kittyMax) {
        kitties.add(new Enemy());
      }
    }

    //draw all the fish
    for (int i=0; i<kitties.size(); i++) {   
      tempKit = (Enemy) kitties.get(i);

      //collision between the two fish && shark
      for (int j=i+1; j<kitties.size(); j++) {
        if (!tempKit.death) {
          tempKit2 = (Enemy) kitties.get(j);
          if (!tempKit2.death)
            tempKit.interFishHit(tempKit2);
        }
      }

      tempKit.runAI();
      shark.interFishHit(tempKit);
    } //end draw/move loop

    //deal with the shark
    shark.runAI();

    // deal with the players avatar, depending on its state
    player.update();

    //if player presses space, pause game/bring up menu
    if (keyPressed)
      if (key == ' ')
      {
        keyPress = true;
      }
  }
}  //end draw()

public void refresh()
{

  //refresh and show blank background
  background(0); 
  strokeWeight(2);
  stroke(180);
  fill(100);
  rectMode(CORNER);
  rect(border-2, border-2, width-(border*2)+4, height-(border*2)-50+4);
}

public void mouseReleased()
{

  //deal with what buttons was pressed, if any
  if (restart.hover)
  {
    restart.hover = false;
    miniMenuButton.active = false;
    player = new Player();
    kitties = new ArrayList();
    shark = new Shark();
    info = new Info(info.bestScore);
    if (!buttonMuteSFX.active)
      buttonSFX.play(0);
  }
  if (miniMenuButton.hover)
  {
    miniMenuButton.hover = false;
    miniMenuButton.active = !miniMenuButton.active;
    if (!buttonMuteSFX.active)
      buttonSFX.play(0);
  }

  if (tutorialButton.hover)
  {
    tutorialButton.hover = false;
    tutorialButton.active = true;
    miniMenuButton.active = false;
    if (!buttonMuteSFX.active)
      buttonSFX.play(0);
  }

  if (tutorial.forwardPage.hover)
  {
    tutorial.forwardPage.hover = false;
    if (currPage < maxPages-1)
    {
      currPage++;
      if (!buttonMuteSFX.active)
        buttonSFX.play(0);
    }
  }

  if (tutorial.backPage.hover)
  {
    tutorial.backPage.hover = false;
    if (currPage > 0)
    {
      currPage--;
      if (!buttonMuteSFX.active)
        buttonSFX.play(0);
    }
  }

  if (endIntro.hover)
  {
    endIntro.hover = false;
    intro = false;
    introSong.pause();
    tutorialButton.active = true;
    if (!buttonMuteSFX.active)
      buttonSFX.play(0);
  }

  if (tutorial.play.hover)
  {
    tutorial.play.hover = false;
    tutorialButton.active = false;
    if (!buttonMuteSFX.active)
      buttonSFX.play(0);
  }

  if (bgUp.hover)
  {
    bgUp.hover = false;
    if (bgSelect < bgMax-1)
    {
      bgSelect++;
      if (!buttonMuteSFX.active)
        buttonSFX.play(0);
    }
  }

  if (bgDown.hover)
  {
    bgDown.hover = false;
    if (bgSelect > 0)
    {
      bgSelect--;
      if (!buttonMuteSFX.active)
        buttonSFX.play(0);
    }
  }

  if (buttonMuteSFX.hover)
  {
    buttonMuteSFX.hover = false;
    buttonMuteSFX.active = !buttonMuteSFX.active;
    if (!buttonMuteSFX.active)
      buttonSFX.play(0);
  }

  if (buttonMuteMusic.hover)
  {
    buttonMuteMusic.hover = false;
    if (!buttonMuteMusic.active)
    {
      gameSong.pause();
      buttonMuteMusic.active = true;
    } 
    else
    {   
      gameSong.loop();
      buttonMuteMusic.active = false;
    }
    if (!buttonMuteSFX.active)
      buttonSFX.play(0);
  }

  if (quit.hover)
  {
    quit.hover = false;
    intro = true;
    if (!buttonMuteSFX.active)
      buttonSFX.play(0);
    //exit();
  }
}

public void keyReleased()
{
  if (keyPress)
  {
    keyPress = false;
    miniMenuButton.active = !miniMenuButton.active;
    if (!buttonMuteSFX.active)
      buttonSFX.play(0);
  }
}

class Button
{
  boolean hover;
  boolean pressed;
  boolean active;
  int bWidth;
  int bHeight;
  int bgColour;
  int hoverColour;
  int strokeColour;
  int textColour;
  String message;
  String altMessage;

  Button()
  {
    hover = false;
    pressed = false;
    active = false;
    bWidth = 100;
    bHeight = 30;
    message = "";
    altMessage = "";
    bgColour = 100;
    hoverColour = 200;
    textColour = 0;
    strokeColour = 255;
  }

  Button(String sm)
  {
    hover = false;
    pressed = false;
    active = false;
    bWidth = 100;
    bHeight = 30;
    message = sm;
    altMessage = "";
    bgColour = 100;
    hoverColour = 200;
    textColour = 0;
    strokeColour = 255;
  }

  Button(String sm, String am)
  {
    hover = false;
    pressed = false;
    active = false;
    bWidth = 100;
    bHeight = 30;
    message = sm;
    altMessage = am;
    bgColour = 100;
    hoverColour = 200;
    textColour = 0;
    strokeColour = 255;
  }
  
  Button(String sm, int w)
  {
    hover = false;
    pressed = false;
    active = false;
    bWidth = w;
    bHeight = 30;
    message = sm;
    altMessage = "";
    bgColour = 100;
    hoverColour = 200;
    textColour = 0;
    strokeColour = 255;
  }
  
  Button(String sm, String am, int w)
  {
    hover = false;
    pressed = false;
    active = false;
    bWidth = w;
    bHeight = 30;
    message = sm;
    altMessage = am;
    bgColour = 100;
    hoverColour = 200;
    textColour = 0;
    strokeColour = 255;
  }

  Button(String sm, int w, int c, int h)
  {
    hover = false;
    pressed = false;
    active = false;
    bWidth = w;
    bHeight = 30;
    message = sm;
    altMessage = "";
    bgColour = c;
    hoverColour = h;
    textColour = 0;
    strokeColour = 255;
  }

  Button(String sm, String am, int w, int h, int c, int hc, int sc, int tc)
  {
    hover = false;
    pressed = false;
    active = false;
    bWidth = w;
    bHeight = h;
    message = sm;
    altMessage = am;
    bgColour = c;
    hoverColour = hc;
    strokeColour = sc;
    textColour = tc;
  }
  
  public void updateButton(int x, int y)
  {
    //update button
    if (mouseX <= x+bWidth/2 && mouseX >= x-bWidth/2 &&
      mouseY <= y+bHeight/2 && mouseY >= y-bHeight/2)
    {
      hover = true;
      if (mousePressed)
        pressed = true;
      else
        pressed = false;
    }
    else
    {
      hover = false;
      pressed = false;
    }
  }

  public void drawButton(int x, int y)
  {   
    //draw it
    pushMatrix();
    translate(x, y);
    stroke(strokeColour);
    strokeWeight(2);
    if (hover)
      fill(hoverColour);
    else
      fill(bgColour);
    if (pressed)
      scale(0.9f);
    rectMode(CENTER);
    rect(0, 0, bWidth, bHeight);
    textAlign(CENTER);
    fill(textColour);
    textFont(font, 14);
    if (!active)
      text(message, 0, 5);
    else
      text(altMessage, 0, 5);
    popMatrix();
  }
  
  public void run(int x, int y)
  {
    updateButton(x,y);
    drawButton(x,y);
  }
}

class DemoButton extends Button
{
  int demoKeyCode;

  DemoButton()
  {
    super();
  }

  DemoButton(String sm, String am, int w, int h, int c, int hc, int sc, int tc, int dkc)
  {
    super(sm, am, w, h, c, hc, sc, tc);
    demoKeyCode = dkc;
  }

  public void updateButton(int x, int y)
  {
    if (keyPressed)
    {
      if ((key == CODED && keyCode == demoKeyCode) || 
        (demoKeyCode == -1 && key == ' '))
      {
        hover = true;
        pressed = true;
      }
    }
    else
    {
      hover = false;
      pressed = false;
    }
  }

  public void run(int x, int y)
  {
    this.updateButton(x, y);
    this.drawButton(x, y);
  }
}
class Enemy extends Kitty
{

  float wave;
  float waveSpeed;
  float amplitude;

  Enemy()
  {
    super();

    //figure out size of fish
    fishSize = random(player.fishSize/4, 2*player.fishSize);
    fishRadius = 75*fishSize;

    wave = random(TWO_PI);
    waveSpeed = random(0.1f, 0.3f);
    amplitude = random(1, 3);

    if (wave > 0) {
      swim = true;
    } 
    else {
      swim = false;
    }

    //figure out velocity/direction
    vel = new PVector(random(2)*random(-1, 1), 2-random(0, 4));
    //figure out position
    if (vel.x > 0)
    {
      pos = new PVector(5+border, random(border+fishRadius, height-border-50-fishRadius));
      backwards = true;
    }
    else
    {
      pos = new PVector(width-10-fishRadius-border, random(border+10+fishRadius, height-10-border-50-fishRadius));
      backwards = false;
    }
  }

  public void moveFish() {
    if (!death)
    {
    //movement
    wave += waveSpeed;
    pos.x += vel.x;
    pos.y += vel.y + sin(wave);
    } else {
      pos.y += 2;
    }
  }


  public void drawFish()
  {
    super.drawFish();

    if (wave%TWO_PI < PI) {
      swim = true;
    } 
    else {
      swim = false;
    }
    if (vel.x > 0) {
      backwards = true;
    }
    else if (vel.x < 0) {
      backwards = false;
    }
  }

  public void interFishHit(Enemy k) {
    //make sure they're both active
    if (!this.death && !k.death)
    { 
      //if the distance between them is less than the radi of both fish
      if (pos.dist(k.pos) <= (k.fishRadius + fishRadius)) {
        //figure out the angle created between the two fish (constrained to the first quadrant)
        float angle = degrees(atan(abs(pos.y-k.pos.y)/abs(pos.x-k.pos.x)));
        constrain(angle, 0, 90);
        float number = random(2, 5);
        //create velocityY from angle, then create velociyX from vY
        vel.y = angle/(20*number);
        vel.x = number-vel.y;
        if (pos.x < k.pos.x) {
          vel.x *= random(-1.5f, 0);
        } 
        else {
          vel.x *= random(0, 1.5f);
        }
        if (pos.y < k.pos.y) {
          vel.y *= random(-1.5f, 0);
        } 
        else {
          vel.y *= random(0, 1.5f);
        }
        k.vel.x = -1*vel.x;
        k.vel.y = -1*vel.y;
      }
    }
  }

  public void eaten(boolean p)
  {
    super.eaten();
    //if eaten by the player
    if (p)
    {
    player.fishSize += 0.01f;
    info.points += PApplet.parseInt(100*fishSize);
    }
  }

  public void interFishHit(Player p) 
  {
    //make sure they're both active
    if (!death && !p.death) {
      //if the distance between them is less than the radi of both fish
      if (pos.dist(p.pos) <= (p.fishRadius + fishRadius)) {
        //if they are 'facing eachother', determine who eats who
        if (((p.backwards && !backwards) && (p.pos.x < pos.x)) 
          || ((!p.backwards && backwards) && (pos.x < p.pos.x)))
        {
          if (p.fishSize > fishSize)
          {
            eaten(true);
          }
          else
          {
            p.eaten();
          }
        }
      }
    }
  }
  
  public void runAI()
  {
      //move the origin to where fish needs to be drawn      
      pushMatrix();
      translate(this.pos.x, this.pos.y);

      if (!this.death)
      {    
        this.moveFish();
        this.boundDetect();
        this.interFishHit(player);
        this.drawFish();
      }
      else if (this.chompCounter > 0)
        this.drawFish();
      else
        kitties.remove(this);

      popMatrix();  
  }
}

class ImageButton extends Button
{
  boolean back; //true = left button, false = right button
  int inactiveNum;
  

  ImageButton()
  {
    super();
    back = true;
    inactiveNum = 1;
  }

  ImageButton(boolean b, int m)
  {
    super();
    back = b;
    inactiveNum = m;
  }

  public void drawButton(int x, int y)
  {
    pushMatrix();
    translate(x, y);

    strokeWeight(2);
      
    if (currPage+1 == this.inactiveNum)
    {
      fill(30);
      stroke(30);
    }
    else
    {
    stroke(255);
    if (hover)
      fill(hoverColour);
    else
      fill(bgColour);
    if (pressed)
      scale(0.9f);
    }
      
    if (back)
      scale(-1,1);
    
      
    //draw arrow
    rectMode(CENTER);
    rect(-1*bWidth/4, 0, bWidth/2, bHeight/2);
    triangle(0,-1*bHeight/2, 0,bHeight/2, bWidth/2,0);
    noStroke();
    rect(0, 0, bWidth/4, bHeight/2-2);
      
      
    popMatrix();
  }
  
  public void run(int x, int y)
  {
    this.updateButton(x,y);
    this.drawButton(x,y);
  }
}

class Info
{
  int frameCounter;
  int minutes;
  int seconds;
  int points;
  int score;
  int bestScore;
  String sTime;
  float pulse;
  boolean pulseBigger;

  Info()
  {
    frameCounter = 0;
    minutes = 0;
    seconds = 0;
    points = 0;
    score = 0;
    bestScore = 0;
    sTime = "";
    pulse = 1;
    pulseBigger = true;
  }

  Info(int bs)
  {
    minutes = 0;
    seconds = 0;
    points = 0;
    score = 0;
    bestScore = bs;
    sTime = "";
    pulse = 1;
    pulseBigger = true;
  }

  public void displayInfo(int x, int y)
  {    
    if (pulseBigger)
      pulse += 0.04f;
    else
      pulse -= 0.04f;
    if (pulse > 1.25f || pulse < 0.75f)
      pulseBigger = !pulseBigger;
    
    pushMatrix();
    translate(x, y);
    textAlign(CENTER);
    fill(255);
    textFont(font, 14);
    sTime = "";
    if (minutes < 10)
      sTime += "0";
    sTime += minutes + ":";
    if (seconds < 10)
      sTime += "0";
    sTime += seconds;
    text("Points: " + points, -60, 0);
    text("Time: " + sTime, 60, 0);
    textFont(font, 14);
    text("Lives: ", -90, 20);
    for (int i = 1; i <= player.lives; i++)
    {
      //draw heart
      stroke(255);
      strokeWeight(1);
      fill(255, 0, 0);
      if (i == player.lives)
        ellipse(-75+(i*15), 15, 10*pulse, 10*pulse);
      else
        ellipse(-75+(i*15), 15, 10, 10);
    }
    popMatrix();
  }

  public void update()
  {
    frameCounter++;
    if (frameCounter >= frameRate)
    {
      this.seconds++;
      frameCounter = 0;
    }
    if (seconds >= 60)
    {
      seconds -= 60;
      minutes++;
    }
    if (minutes > 99)
    {
      minutes = 99;
      seconds = 0;
    }
  }
  
    public void displayDemoInfo(int x, int y)
  {    
    if (pulseBigger)
      pulse += 0.04f;
    else
      pulse -= 0.04f;
    if (pulse > 1.25f || pulse < 0.75f)
      pulseBigger = !pulseBigger;
    
    pushMatrix();
    translate(x, y);
    textAlign(CENTER);
    fill(0);
    textFont(font, 14);
    text("Points: 2332", 0, -20);
    text("Time: 03:28", 0, 0);
    for (int i = 1; i <= 5; i++)
    {
      //draw heart
      stroke(0);
      strokeWeight(1);
      fill(255, 0, 0);
      if (i == 5)
        ellipse(-45+(i*15), 15, 10*pulse, 10*pulse);
      else
        ellipse(-45+(i*15), 15, 10, 10);
    }
    popMatrix();
  }

  
}

class Kitty {
  //variables
  PVector vel;
  PVector pos;
  boolean death;
  boolean backwards;
  boolean blink;
  boolean swim;
  float fishSize;
  float fishRadius;
  int chompCounter;

  //fishbits
  int backCheekX = -90;
  int backCheekY = 15;
  int frontCheekX = -70;
  int frontCheekY = 15;
  //visual
  int bodyColour;
  int finColour;
  int strokeColour;

  //constructor
  Kitty () {

    swim = false;
    death = false;
    blink = false;
    chompCounter = 0;

    //generate colours
    finColour = color(PApplet.parseInt(random(50, 256)));
    bodyColour = color(PApplet.parseInt(random(50, 256)), 
    PApplet.parseInt(random(50, 256)), 
    PApplet.parseInt(random(50, 256)));
    strokeColour = color(0);
  }

  public void boundDetect()
  {  
    //boundary collision detection
    if (pos.x < border + fishRadius) {
      pos.x = border + fishRadius;
      vel.x *= -1;
    } 
    else if (pos.x > width-border-fishRadius) {
      pos.x = width-border-fishRadius;
      vel.x *= -1;
    }
    if (pos.y < border+fishRadius) {
      pos.y = border+fishRadius;
      vel.y *= -1;
    } 
    else if (pos.y > height-border-50-fishRadius) {
      pos.y = height-border-50-fishRadius;
      if (!death)
        vel.y *= -1;
    }
  }


  //draw
  public void drawFish() {
    //fish visual effects
    if (chompCounter <= 0)
    {
      scale(fishSize);
      if (backwards) {
        scale(-1, 1);
      }
      if (info.seconds%5 == 0) {
        blink = true;
      }
      else {
        blink = false;
      }
      
      for (int i=15; i>0; i--)
      {
        fill (0, 30);
        noStroke();
        ellipse(0,0, i*10, i*10);
      }

      //line traits
      stroke(strokeColour);
      strokeWeight(1);


      //back eye
      if (blink) {
        fill(bodyColour);
        ellipse(-58, -10, 30, 40);
      } 
      else {
        fill(255);
        ellipse(-58, -10, 30, 40);
        fill(255, 0, 0);
        ellipse(-63, -10, 15, 25);
        fill(0);
        ellipse(-65, -10, 2, 12);
      }

      //back cheek
      strokeWeight(1);
      fill(finColour);
      line(backCheekX+20, backCheekY-10, backCheekX-10, backCheekY-20);
      line(backCheekX+30, backCheekY, backCheekX-20, backCheekY);
      line(backCheekX+20, backCheekY+10, backCheekX-10, backCheekY+20);
      ellipse(backCheekX+30, backCheekY, 40, 40);


      //tail
      fill(finColour);
      if (swim) {
        triangle(70, -40, 20, 0, 70, 40);
        line(70, -25, 50, -10);
        line(70, 0, 45, 0);
        line(70, 25, 50, 10);
      } 
      else {
        triangle(80, -30, 30, 0, 80, 30);
        line(80, -20, 60, -5);
        line(80, 0, 55, 0);
        line(80, 20, 60, 5);
      }

      //body
      fill(bodyColour);
      ellipse(0, 0, 100, 100);

      // face
      //ears
      fill(bodyColour);
      if (blink) {
        triangle(-55, -20, -55, -50, -20, -25);
        triangle(-45, -25, -10, -55, -10, -20);
        fill(225, 125, 125);
        triangle(-30, -25, -10, -55, -10, -20);
      } 
      else {
        triangle(-55, -20, -45, -55, -20, -25);
        triangle(-45, -25, -20, -60, -10, -20);
        fill(225, 125, 125);
        triangle(-30, -25, -20, -60, -10, -20);
      }

      //front eye
      if (blink) {
        fill(bodyColour);
        ellipse(-45, -10, 30, 40);
      } 
      else {
        fill(255);
        ellipse(-45, -10, 30, 40);
        fill(255, 0, 0);
        ellipse(-50, -10, 15, 25);
        fill(0);
        ellipse(-52, -10, 2, 12);
      }

      //front cheek 
      strokeWeight(1); 
      fill(finColour);
      ellipse(frontCheekX+30, frontCheekY, 40, 40);
      line(frontCheekX+40, frontCheekY-10, frontCheekX+70, frontCheekY-20);
      line(frontCheekX+45, frontCheekY, frontCheekX+80, frontCheekY);
      line(frontCheekX+40, frontCheekY+10, frontCheekX+70, frontCheekY+20);

      //nose
      fill(255, 125, 125);
      if (blink) {
        triangle(-75, 0, -55, 0, -62, 15);
      } 
      else {
        triangle(-75, -1, -55, -1, -62, 14);
      }
    }
    else
    {
      stroke(0);
      strokeWeight(1);
      fill(bodyColour);
      ellipse(0,0, 15,15);
      line(-15, -15, 15, 15);
      line(15, -15, -15, 15);
      if (bodyColour <= 50)
        fill(255);
      else
        fill(0);
      textFont(font, 12);
      textAlign(CENTER);
      text("CHOMP!", 0, 5);
      chompCounter--;
    } 
  } //end of drawFish()

  public void eaten()
  {
    death = true;
    chompCounter = 30;
    if (!buttonMuteSFX.active)
      chompSFX.play(0);
  }
}

class Menu
{

  ImageButton forwardPage;
  ImageButton backPage;
  Button play;
  String sPages;

  Menu(int m)
  {
    sPages = "";
    backPage = new ImageButton(true, 1);
    forwardPage = new ImageButton(false, m);
    play = new Button("Shut It 'n' Play!", 150, color(150, 0, 0), color(240, 0, 0));
  }

  public void displayPageCount(int x, int y)
  {
    pushMatrix();
    translate(x-(15*(maxPages-1)), y);
    for (int i=0; i<maxPages; i++)
    {
      if (i == currPage)
        fill(0);
      else
        fill(100);
      stroke(255);
      strokeWeight(2);
      rectMode(CENTER);
      rect(0, 0, 30, 30);
      textAlign(CENTER);
      fill(255);
      textFont(font, 14);
      sPages = "" + (i+1);
      text(sPages, 0, 5);
      translate(30, 0);
    }
    popMatrix();
  }
  
  public void displayDemo(int x, int y)
  {
      pushMatrix();
      translate(x,y);
      upArrButton.run(0, -1*upArrButton.bHeight);
      downArrButton.run(0,0);
      leftArrButton.run(-1*leftArrButton.bWidth, 0);
      rightArrButton.run(rightArrButton.bWidth, 0);
      spacebarButton.run(0, spacebarButton.bHeight);
      popMatrix();
  }
}

class Player extends Kitty
{
  int lives;
  int immuneCounter;

  Player()
  {    
    lives = 5;
    fishSize = 0.2f;
    fishRadius = 75*fishSize;
    //figure out position
    pos = new PVector(width/2+border, (height-border*2-50)/2);
    //figure out velocity/direction
    vel = new PVector(0, 0);
    bodyColour = color(0);
    finColour = color(0);
    chompCounter = 0;
    death = false;
    immuneCounter = 60;
  }

  Player(int l)
  {
    lives = l;
    fishSize = 0.2f;
    fishRadius = 75*fishSize;
    //figure out position
    pos = new PVector(width/2+border, (height-border*2-50)/2);
    //figure out velocity/direction
    vel = new PVector(0, 0);
    bodyColour = color(0);
    finColour = color(0);
    chompCounter = 0;
    death = false;
    immuneCounter = 60;
  }

  public void moveFish()
  {
    if (keyPressed) {
      if (key == CODED) {
        if (keyCode == UP)
          vel.y -= 0.4f;

        if (keyCode == DOWN)
          vel.y += 0.4f;

        if (keyCode == LEFT)
        {
          vel.x -= 0.4f;
          backwards = false;
        }

        if (keyCode == RIGHT)
        {
          vel.x += 0.4f;
          backwards = true;
        }
      }
    }
    pos.x += vel.x;
    pos.y += vel.y;
  }

  public void drawFish()
  {
    if (immuneCounter == 0 || immuneCounter%5 != 0)
    {
      super.drawFish();
      if (death && chompCounter == 0 && player.lives != 0)
      {
        player = new Player(player.lives);
      }
      if (!death)
      {
        fill(bodyColour);
        triangle(-20, 0, 0, -90, 20, 0);
        rotate(radians(15));
        triangle(-20, 0, 0, -80, 20, 0);
        rotate(radians(15));
        triangle(-20, 0, 0, -70, 20, 0);
        rotate(radians(15));
        triangle(-20, 0, 0, -60, 20, 0);
        rotate(radians(15));
        triangle(-20, 0, 0, -50, 20, 0);
      }
    }
  }

  public void eaten()
  {
    if (immuneCounter <= 0)
    {
      super.eaten();
      lives -= 1;
    }
  }

  public void update()
  {    
    pushMatrix();
    translate(this.pos.x, this.pos.y);  

    //dec the immune counter
    if (immuneCounter > 0)
      immuneCounter--;

    //alive
    if (!player.death)
    {   
      this.moveFish();
      this.boundDetect();
      this.drawFish();
    }
    //freshly eaten
    else
    {
      //display the CHOMP
      if (this.chompCounter > 0)
      {
        this.drawFish();
      }
      //display a game over screen if dead and done with 'chomp'
      if (player.lives <= 0)
      {
        miniMenuButton.active = true;
      }
    }
    popMatrix();
  }
}

class Shark extends Enemy
{

  int lives;
  PVector target;
  boolean hungry;

  Shark()
  {
    super();
    amplitude = 1;
    bodyColour = color(56);
    fishSize = random(1.5f*player.fishSize, 2*player.fishSize);
    fishRadius = 75*fishSize;
    lives = 4;
    hungry = false;
    if (!intro && !buttonMuteSFX.active)
      jawsSFX.play();
  }

  public void drawFish()
  {

    if (chompCounter%3 == 1 && !death)
      bodyColour = color(55, 155, 55);
    else
      bodyColour = color(55);


      scale(fishSize);
      if (vel.x < 0)
        backwards = false;
      else
        backwards = true;
      if (backwards) {
        scale(-1, 1);
      }

    if (!death)
    {
      //line traits
      stroke(strokeColour);
      strokeWeight(1);

      fill(bodyColour);


      //shark image....
      //tail
      triangle(25, 0, 75, -35, 75, 35);
      //topfin
      triangle(-25, -30, 25, -75, 25, 0);
      //backfin
      triangle(-30, 0, -30, 70, 0, 0);
      //frontfin
      triangle(-30, 0, 0, 75, 0, 0);
      //body
      ellipse(0, 0, 130, 80);
      //eye
      if (hungry)
      {
        fill(255, 0, 0);
        ellipse(-30, -5, 15*info.pulse, 15*info.pulse);
      }
      else
      {
        fill(0);
        ellipse(-30, -5, 15, 15);
      }
      //teeth
      fill(255);
      rectMode(CORNER);
      rect(-60, 10, 40, 20);
      triangle(-60, 30, -50, 10, -40, 30);
      triangle(-40, 30, -30, 10, -20, 30);
    }
    
    if (death && chompCounter%2 != 0)
    {
      scale(1, -1);
      bodyColour = color(55, 155, 55);
      
      //draw skeleton for death
      fill(255);
      strokeWeight(1);
      //tail
      line(50,0, 75, 40);
      line(50,0, 75, 0);
      line(50,0, 75, -40);
      //backbone
      line(-50, 0, 50, 0);
      //skull
      arc(-20, 0, 80, 70, PI/2, 3*PI/2);
      //eye
      fill(0);
      ellipse(-40, -10, 15, 15);
      //ribs
      noFill();
      arc(10, 0, 20, 60, PI/2, 3*PI/2);
      arc(20, 0, 20, 60, PI/2, 3*PI/2);
      arc(30, 0, 20, 60, PI/2, 3*PI/2);
    }
    
    //reincarnate shark
    if (death && chompCounter == 0)
      shark = new Shark();
  }


  public void dies()
  {
    jawsSFX.rewind();
    player.fishSize += 0.05f;
    this.death = true;
    if (player.lives < 9)
    {
      player.lives++;
      info.points += 1000*this.fishSize;
    }
    this.vel.x = 0;
    this.vel.y = 2;
    this.wave = 0;
    this.waveSpeed = 0;
  }

  public void eaten()
  {
    this.lives--;
    this.chompCounter = 30;
    if (lives <= 0)
      this.dies();
  }

  public void interFishHit(Player p)
  {
    //make sure they're both active
    if (!death && !p.death) {
      //if the distance between them is less than the radi of both fish
      if (pos.dist(p.pos) <= (p.fishRadius + fishRadius)) {
        //if they arent 'facing eachother', determine who eats who
        if ((backwards && (p.pos.x < pos.x)) 
          || (!backwards && (p.pos.x > pos.x))) 
        {
          if (chompCounter == 0)
          {
            this.eaten();
          }
        }
        else
        {
          p.eaten();
          hungry = false;
          vel = new PVector(random(2)*random(-1, 1), random(2)*random(-1, 1));
        }
      }
    }
  }

  public void interFishHit(Enemy e)
  {
    //make sure they're both active
    if (!death && !e.death && hungry) 
    {
      //if the distance between them is less than the radi of both fish
      if (this.pos.dist(e.pos) <= (e.fishRadius + this.fishRadius)) 
      {
        e.eaten(false);
        hungry = false;
        vel = new PVector(random(2)*random(-1, 1), random(2)*random(-1, 1));
      }
    }
  }

  public void seekFood()
  {
    if (hungry)
    {
      target = PVector.sub(player.pos, this.pos);
      if (target.mag()>15)
        target.mult(15/target.dot(target) );
      this.vel.add(target);
    }
  }

  public void runAI()
  {


    if (chompCounter > 0)
    {
      chompCounter--;
      if (chompCounter == 0)
        hungry = true;
    }

    //randomly determine if shark becomes hungry
    if (!hungry && random(player.fishRadius*100) < 1)
      hungry = true;
    
    pushMatrix();
    translate(pos.x, pos.y); 
    this.seekFood();
    this.moveFish();
    this.interFishHit(player);
    this.boundDetect();
    this.drawFish();
    popMatrix();
  }
}

class SpeakerButton
{
  boolean active;
  boolean hover;
  boolean pressed;
  boolean activated;
  int iSize;
  
  SpeakerButton()
  {
    hover = false;
    pressed = false;
    activated = false;
    active = false;
    iSize = 50;
  }
  
  SpeakerButton(int s)
  {
    hover = false;
    pressed = false;
    activated = false;
    active = false;
    iSize = s;
  }
  
 
  public void updateButton(int x, int y)
  {
    //update button
    if (mouseX <= x+iSize/2 && mouseX >= x-iSize/2 &&
      mouseY <= y+iSize/2 && mouseY >= y-iSize/2)
    {
      hover = true;
      if (mousePressed)
        pressed = true;
      else
        pressed = false;
    }
    else
    {
      hover = false;
      pressed = false;
    }
  }
  
  public void drawButton(int x, int y)
  {
    pushMatrix();
    translate(x, y);
    if (!active)
    {
    fill(255);
    noStroke();
    rectMode(CENTER);
    rect(-1 * iSize/4, 0, iSize/2, iSize/2);
    triangle(-1 * iSize/2, 0, 0, -1 * iSize/2, 0, iSize/2);
    noFill();
    stroke(255);
    strokeWeight(1);
    arc(0, 0, iSize/5, iSize/4, -1*PI/4, PI/4);
    arc(iSize/5, 0, iSize/5, iSize/2, -1*PI/4, PI/4);
    arc(2*iSize/5, 0, iSize/5, iSize, -1*PI/4, PI/4);
    }
    else
    {
    fill(255,0,0);
    noStroke();
    rectMode(CENTER);
    rect(-1 * iSize/4, 0, iSize/2, iSize/2);
    triangle(-1 * iSize/2, 0, 0, -1 * iSize/2, 0, iSize/2);
    noFill();
    stroke(255,0,0);
    strokeWeight(1);
    line(iSize/4, iSize/4, 2*iSize/4, -1*iSize/4);
    line(2*iSize/4, iSize/4, iSize/4, -1*iSize/4);
    }
    popMatrix();
  }
  
  public void run(int x, int y)
  {
    this.updateButton(x,y);
    this.drawButton(x,y);
  }
}
  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#F0F0F0", "main" });
  }
}
