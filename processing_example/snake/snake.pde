// by ndr3svt zurich 15 december 2020
// first attempt to create the snake game for mobile web
// for visitors to my page
// use your phone for what your phone was made for: playing snake



import processing.sound.*;
SoundFile s;
SoundFile gOver;
SoundFile bell;


// declare snake and food
Snake snake ;
Food food;
String snakeDir="";
boolean debug=false;

int pubSize=60;
void setup(){
	size(displayWidth,displayHeight,FX2D);
	// initialize snake
	snake = new Snake(width/2,height/2,pubSize);
	// initialize food
	food = new Food();
	s =  new SoundFile(this, "data/snack_mono.wav");
	gOver = new SoundFile(this, "data/gameover.wav");
	bell = new SoundFile(this,"data/bell.wav");
	bell.jump(0.5);
}

void draw(){
	background(0);
	grid();
	snake.update(food);
	snake.display();
	pubSize=int(map(mouseX,0,width,10,200));
	/* this could use a scroll mouse zoom function */
	/* maybe this function could be also automated to make it a bit harder */
	snake.scale(pubSize);
	/* this could use a scroll mouse zoom function or a finger zoom on the mobile... */
	food.display();
	gameIsOver();
}
// controls
void keyPressed(){
	if(key == CODED){
		switch(keyCode){
			case UP:
			// println("up");
			snake.updateDirection("up");
			if(bell.isPlaying()){
				bell.stop();
			}
			bell.rate(0.5*0.25);
			// bell.rate(0.5 *( (food.id+1) *0.05));

			bell.play();
			break;
			case DOWN:
			
			snake.updateDirection("down");
			if(bell.isPlaying()){
				bell.stop();
			}
			bell.rate(0.75*0.25);
			// bell.rate(0.75*( (food.id+1) *0.05));
			bell.play();
			break;
			case LEFT:
			
			snake.updateDirection("left");
			if(bell.isPlaying()){
				bell.stop();
			}
			bell.rate(0.95*0.25);
			// bell.rate(0.95*( (food.id+1) *0.05));
			bell.play();
			break;
			case RIGHT:

			snake.updateDirection("right");
			if(bell.isPlaying()){
				bell.stop();
			}
			bell.rate(1.5*0.25);
			// bell.rate(1.5*( (food.id+1) *0.05));
			bell.play();
			break;
		}
	}
	if(key == 'd'){
		debug = !debug;
	}
}

void grid(){
			stroke(100);
		strokeWeight(0.25);

	// float offsetX = width - int(width/pubSize)*pubSize;
	// println(offsetX , width%pubSize);
	// float offsetX = width%pubSize /2;
	// println(offsetX);
	float x2 = width/2 - pubSize/2;
	for(float x = width/2+pubSize/2; x < width; x = x+pubSize){
		line(x,0,x,height);
		line(x2,0,x2,height);
		x2 = x2-pubSize;
	}
	float y2 = height/2 - pubSize/2;
	for(float y = height/2+pubSize/2; y < height; y = y+pubSize){
		line(0,y,width,y);
		line(0,y2,width,y2);
		y2 = y2-pubSize;
	}
}

int countGO=0;
void gameIsOver(){
	if(snake.collide && countGO<100){
		fill(random(255),random(255),random(255),100);
		rectMode(CENTER);
		rect(width/2,height/2,width,height);
		countGO++;

	}else{
		if(snake.collide){
			snake.collide=false;
			countGO=0;
			snake.pos = new PVector(width/2,height/2);
			snake.direction = "";
			food.id=0;
			snake.tail = new ArrayList<PVector>();
		}
	}
}
