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
  color bodyColour;
  color finColour;
  color strokeColour;

  //constructor
  Kitty () {

    swim = false;
    death = false;
    blink = false;
    chompCounter = 0;

    //generate colours
    finColour = color(int(random(50, 256)));
    bodyColour = color(int(random(50, 256)), 
    int(random(50, 256)), 
    int(random(50, 256)));
    strokeColour = color(0);
  }

  void boundDetect()
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
  void drawFish() {
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

  void eaten()
  {
    death = true;
    chompCounter = 30;
    if (!buttonMuteSFX.active)
      chompSFX.play(0);
  }
}

