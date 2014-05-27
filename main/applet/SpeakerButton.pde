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
  
 
  void updateButton(int x, int y)
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
  
  void drawButton(int x, int y)
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
  
  void run(int x, int y)
  {
    this.updateButton(x,y);
    this.drawButton(x,y);
  }
}
