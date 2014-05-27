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

  void displayInfo(int x, int y)
  {    
    if (pulseBigger)
      pulse += 0.04;
    else
      pulse -= 0.04;
    if (pulse > 1.25 || pulse < 0.75)
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

  void update()
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
  
    void displayDemoInfo(int x, int y)
  {    
    if (pulseBigger)
      pulse += 0.04;
    else
      pulse -= 0.04;
    if (pulse > 1.25 || pulse < 0.75)
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

