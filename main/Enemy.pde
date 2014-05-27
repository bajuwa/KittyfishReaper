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
    waveSpeed = random(0.1, 0.3);
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

  void moveFish() {
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


  void drawFish()
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

  void interFishHit(Enemy k) {
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
          vel.x *= random(-1.5, 0);
        } 
        else {
          vel.x *= random(0, 1.5);
        }
        if (pos.y < k.pos.y) {
          vel.y *= random(-1.5, 0);
        } 
        else {
          vel.y *= random(0, 1.5);
        }
        k.vel.x = -1*vel.x;
        k.vel.y = -1*vel.y;
      }
    }
  }

  void eaten(boolean p)
  {
    super.eaten();
    //if eaten by the player
    if (p)
    {
    player.fishSize += 0.01;
    info.points += int(100*fishSize);
    }
  }

  void interFishHit(Player p) 
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
  
  void runAI()
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

