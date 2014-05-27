class Button
{
  boolean hover;
  boolean pressed;
  boolean active;
  int bWidth;
  int bHeight;
  color bgColour;
  color hoverColour;
  color strokeColour;
  color textColour;
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

  Button(String sm, int w, color c, color h)
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

  Button(String sm, String am, int w, int h, color c, color hc, color sc, color tc)
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
  
  void updateButton(int x, int y)
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

  void drawButton(int x, int y)
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
      scale(0.9);
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
  
  void run(int x, int y)
  {
    updateButton(x,y);
    drawButton(x,y);
  }
}

