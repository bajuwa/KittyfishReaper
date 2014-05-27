/*
TO DO:
 finish demo buttons and info display
 animate other tutorial pages?
 turn gamescreen into a class (Tank) - will be easier to restart things
 step by step score breakdown
 */


//import processing.opengl.*;
import ddf.minim.*;

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


void setup() {
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

void draw() {

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

void refresh()
{

  //refresh and show blank background
  background(0); 
  strokeWeight(2);
  stroke(180);
  fill(100);
  rectMode(CORNER);
  rect(border-2, border-2, width-(border*2)+4, height-(border*2)-50+4);
}

void mouseReleased()
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

void keyReleased()
{
  if (keyPress)
  {
    keyPress = false;
    miniMenuButton.active = !miniMenuButton.active;
    if (!buttonMuteSFX.active)
      buttonSFX.play(0);
  }
}

