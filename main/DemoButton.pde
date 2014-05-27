class DemoButton extends Button
{
  int demoKeyCode;

  DemoButton()
  {
    super();
  }

  DemoButton(String sm, String am, int w, int h, color c, color hc, color sc, color tc, int dkc)
  {
    super(sm, am, w, h, c, hc, sc, tc);
    demoKeyCode = dkc;
  }

  void updateButton(int x, int y)
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

  void run(int x, int y)
  {
    this.updateButton(x, y);
    this.drawButton(x, y);
  }
}
