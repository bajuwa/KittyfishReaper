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

  void displayPageCount(int x, int y)
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
  
  void displayDemo(int x, int y)
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

