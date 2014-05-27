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

  void drawButton(int x, int y)
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
      scale(0.9);
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
  
  void run(int x, int y)
  {
    this.updateButton(x,y);
    this.drawButton(x,y);
  }
}

