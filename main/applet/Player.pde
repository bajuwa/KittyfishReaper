class Player extends Kitty
{
  int lives;
  int immuneCounter;

  Player()
  {    
    lives = 5;
    fishSize = 0.2;
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
    fishSize = 0.2;
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

  void moveFish()
  {
    if (keyPressed) {
      if (key == CODED) {
        if (keyCode == UP)
          vel.y -= 0.4;

        if (keyCode == DOWN)
          vel.y += 0.4;

        if (keyCode == LEFT)
        {
          vel.x -= 0.4;
          backwards = false;
        }

        if (keyCode == RIGHT)
        {
          vel.x += 0.4;
          backwards = true;
        }
      }
    }
    pos.x += vel.x;
    pos.y += vel.y;
  }

  void drawFish()
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

  void eaten()
  {
    if (immuneCounter <= 0)
    {
      super.eaten();
      lives -= 1;
    }
  }

  void update()
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

