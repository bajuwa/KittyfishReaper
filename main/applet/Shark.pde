class Shark extends Enemy
{

  int lives;
  PVector target;
  boolean hungry;

  Shark()
  {
    super();
    amplitude = 1;
    bodyColour = color(56);
    fishSize = random(1.5*player.fishSize, 2*player.fishSize);
    fishRadius = 75*fishSize;
    lives = 4;
    hungry = false;
    if (!intro && !buttonMuteSFX.active)
      jawsSFX.play();
  }

  void drawFish()
  {

    if (chompCounter%3 == 1 && !death)
      bodyColour = color(55, 155, 55);
    else
      bodyColour = color(55);


      scale(fishSize);
      if (vel.x < 0)
        backwards = false;
      else
        backwards = true;
      if (backwards) {
        scale(-1, 1);
      }

    if (!death)
    {
      //line traits
      stroke(strokeColour);
      strokeWeight(1);

      fill(bodyColour);


      //shark image....
      //tail
      triangle(25, 0, 75, -35, 75, 35);
      //topfin
      triangle(-25, -30, 25, -75, 25, 0);
      //backfin
      triangle(-30, 0, -30, 70, 0, 0);
      //frontfin
      triangle(-30, 0, 0, 75, 0, 0);
      //body
      ellipse(0, 0, 130, 80);
      //eye
      if (hungry)
      {
        fill(255, 0, 0);
        ellipse(-30, -5, 15*info.pulse, 15*info.pulse);
      }
      else
      {
        fill(0);
        ellipse(-30, -5, 15, 15);
      }
      //teeth
      fill(255);
      rectMode(CORNER);
      rect(-60, 10, 40, 20);
      triangle(-60, 30, -50, 10, -40, 30);
      triangle(-40, 30, -30, 10, -20, 30);
    }
    
    if (death && chompCounter%2 != 0)
    {
      scale(1, -1);
      bodyColour = color(55, 155, 55);
      
      //draw skeleton for death
      fill(255);
      strokeWeight(1);
      //tail
      line(50,0, 75, 40);
      line(50,0, 75, 0);
      line(50,0, 75, -40);
      //backbone
      line(-50, 0, 50, 0);
      //skull
      arc(-20, 0, 80, 70, PI/2, 3*PI/2);
      //eye
      fill(0);
      ellipse(-40, -10, 15, 15);
      //ribs
      noFill();
      arc(10, 0, 20, 60, PI/2, 3*PI/2);
      arc(20, 0, 20, 60, PI/2, 3*PI/2);
      arc(30, 0, 20, 60, PI/2, 3*PI/2);
    }
    
    //reincarnate shark
    if (death && chompCounter == 0)
      shark = new Shark();
  }


  void dies()
  {
    jawsSFX.rewind();
    player.fishSize += 0.05;
    this.death = true;
    if (player.lives < 9)
    {
      player.lives++;
      info.points += 1000*this.fishSize;
    }
    this.vel.x = 0;
    this.vel.y = 2;
    this.wave = 0;
    this.waveSpeed = 0;
  }

  void eaten()
  {
    this.lives--;
    this.chompCounter = 30;
    if (lives <= 0)
      this.dies();
  }

  void interFishHit(Player p)
  {
    //make sure they're both active
    if (!death && !p.death) {
      //if the distance between them is less than the radi of both fish
      if (pos.dist(p.pos) <= (p.fishRadius + fishRadius)) {
        //if they arent 'facing eachother', determine who eats who
        if ((backwards && (p.pos.x < pos.x)) 
          || (!backwards && (p.pos.x > pos.x))) 
        {
          if (chompCounter == 0)
          {
            this.eaten();
          }
        }
        else
        {
          p.eaten();
          hungry = false;
          vel = new PVector(random(2)*random(-1, 1), random(2)*random(-1, 1));
        }
      }
    }
  }

  void interFishHit(Enemy e)
  {
    //make sure they're both active
    if (!death && !e.death && hungry) 
    {
      //if the distance between them is less than the radi of both fish
      if (this.pos.dist(e.pos) <= (e.fishRadius + this.fishRadius)) 
      {
        e.eaten(false);
        hungry = false;
        vel = new PVector(random(2)*random(-1, 1), random(2)*random(-1, 1));
      }
    }
  }

  void seekFood()
  {
    if (hungry)
    {
      target = PVector.sub(player.pos, this.pos);
      if (target.mag()>15)
        target.mult(15/target.dot(target) );
      this.vel.add(target);
    }
  }

  void runAI()
  {


    if (chompCounter > 0)
    {
      chompCounter--;
      if (chompCounter == 0)
        hungry = true;
    }

    //randomly determine if shark becomes hungry
    if (!hungry && random(player.fishRadius*100) < 1)
      hungry = true;
    
    pushMatrix();
    translate(pos.x, pos.y); 
    this.seekFood();
    this.moveFish();
    this.interFishHit(player);
    this.boundDetect();
    this.drawFish();
    popMatrix();
  }
}

