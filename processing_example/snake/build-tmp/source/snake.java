import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.sound.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class snake extends PApplet {


SoundFile s;
SoundFile gOver;
SoundFile bell;


// declare snake and food
Snake snake ;
Food food;
String snakeDir="";
boolean debug=false;

int pubSize=60;
public void setup(){
	
	// initialize snake
	snake = new Snake(width/2,height/2,pubSize);
	// initialize food
	food = new Food();
	s =  new SoundFile(this, "data/snack_mono.wav");
	gOver = new SoundFile(this, "data/gameover.wav");
	bell = new SoundFile(this,"data/bell.wav");
	bell.jump(0.5f);
}

public void draw(){
	background(0);
	grid();
	snake.update(food);
	snake.display();
	pubSize=PApplet.parseInt(map(mouseX,0,width,10,200));
	snake.scale(pubSize);
	food.display();
	gameIsOver();
}
// controls
public void keyPressed(){
	if(key == CODED){
		switch(keyCode){
			case UP:
			// println("up");
			snake.updateDirection("up");
			if(bell.isPlaying()){
				bell.stop();
			}
			bell.rate(0.5f*0.25f);
			// bell.rate(0.5 *( (food.id+1) *0.05));

			bell.play();
			break;
			case DOWN:
			
			snake.updateDirection("down");
			if(bell.isPlaying()){
				bell.stop();
			}
			bell.rate(0.75f*0.25f);
			// bell.rate(0.75*( (food.id+1) *0.05));
			bell.play();
			break;
			case LEFT:
			
			snake.updateDirection("left");
			if(bell.isPlaying()){
				bell.stop();
			}
			bell.rate(0.95f*0.25f);
			// bell.rate(0.95*( (food.id+1) *0.05));
			bell.play();
			break;
			case RIGHT:

			snake.updateDirection("right");
			if(bell.isPlaying()){
				bell.stop();
			}
			bell.rate(1.5f*0.25f);
			// bell.rate(1.5*( (food.id+1) *0.05));
			bell.play();
			break;
		}
	}
	if(key == 'd'){
		debug = !debug;
	}
}

public void grid(){
			stroke(40);
		strokeWeight(0.5f);

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
public void gameIsOver(){
	if(snake.collide && countGO<100){
		fill(random(255),100);
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




class Food{
	PVector pos;
	int id=0;
	Food(){
		pos = new PVector ( PApplet.parseInt(random(width)), PApplet.parseInt(random(height)));

	}
	public void display(){
		fill(sin(radians(millis()*0.5f))*255);
		rectMode(CENTER);
		ellipse(pos.x,pos.y,25,25);
		fill(255);
		textAlign(CENTER,CENTER);
		text(id,pos.x,pos.y-3);
	}
	public void newPos(){
		id++;
		pos = new PVector ( PApplet.parseInt(random(width)), PApplet.parseInt(random(height)));
	}
}
class Snake{
	ArrayList<PVector> tail= new ArrayList<PVector>();
	PVector pos;
	PVector lastPos;
	int size;
	String direction="";
	int speedCount=0;
	boolean startTail=false;
	boolean collide=false;
	Snake(float x, float y, int _size){
		pos = new PVector(x,y);
		lastPos= pos;
		size = _size;
	}
	public void scale(int _size){
		size=_size;
	}
	public void move(Food _food){
		if(speedCount< 4){
			speedCount++;
		}else{
			speedCount=0;
			switch(direction){
				case "up":
				pos.y = pos.y- (1 * size);
				break;
				case "down":
				pos.y = pos.y+ (1 * size);
				break;
				case "left":
				pos.x = pos.x - (1*size);
				break;
				case "right":
				pos.x = pos.x + (1*size);
				break;
			}
			float offX = abs(width-((PApplet.parseInt(width/size)+1)*size));
			float offY = abs(height-((PApplet.parseInt(height/size)+1)*size));
			// println(offX);
			if(pos.x>width+offX){
				pos.x=width/2 - (PApplet.parseInt(width/2/size)*size);
			}
			if(pos.x<0-offX){
				pos.x=width/2 + (PApplet.parseInt(width/2/size)*size);
			}
			if(pos.y>height+offY){
				pos.y=height/2 - (PApplet.parseInt(height/2/size)*size);
			}
			if(pos.y<0-offY){
				pos.y=height/2 + (PApplet.parseInt(height/2/size)*size);
			}
			eat(_food);
			moveTail();
		}
		
	}
	public void updateDirection(String _direction){
		direction = _direction;
	}
	public void update(Food _food){
		shiftTwo();
		if(!collide){
			move(_food);
			collision();
			tapControl();
		}
	
	}
	public void moveTail(){
		if(startTail){
		// if(tail.size()){
			// shifting tail
			for (int i = tail.size()-1; i > 0; i--) {
				tail.get(i).x = tail.get(i-1).x;
				tail.get(i).y = tail.get(i-1).y;

	
			}
			tail.get(0).x = pos.x;
			tail.get(0).y = pos.y;
		}
		// }
	}
	public void displayTail(){
		for(int i = 0; i < tail.size()-1; i ++){
			// if(i>0){
				stroke(255);
				strokeWeight(2);
				fill(0);
				ellipse(tail.get(i).x, tail.get(i).y,size*0.85f,size*0.85f);
			// }
		}
	}
	public void shiftTwo(){
		if(tail.size()<2){
			tail.add(new PVector(pos.x,pos.y));
		}else{
			if(!startTail){
				startTail=true;
			}
		}
	}
	public void eat(Food _food){
		if(_food.pos.dist(pos)<size){
			tail.add(new PVector(pos.x,pos.y));
			_food.newPos();
			s.rate(random(0.6f,0.85f));
			s.pan(map(pos.x,0,width,-1,1));
			s.play();
			// println("eating yes");
		}
	}
	public void collision(){

		for(int i =0;i<tail.size();i++){
			if(i>2){
				PVector piece=tail.get(i);
				if(piece.dist(pos)<5 && !collide){
					collide = true;
					println("collision!!!!!");
					// direction="";
					gOver.play();
				}
			}
		}
		
	}
	public void tapControl(){
		float threshold =60;
		PVector tap = new PVector(mouseX,mouseY);
		if(mousePressed){
			if(tap.x>(pos.x+threshold) && (abs(tap.y-pos.y)<threshold*2) ){
				direction = "right";
			}
			if(tap.x<(pos.x-threshold) && abs(tap.y-pos.y)<threshold*2 ){
				direction="left";
			}

			if(tap.y>(pos.y+threshold) && abs(tap.x-pos.x)<threshold*2 ){
				direction = "down";
			}
			if(tap.y<(pos.y-threshold) && abs(tap.x-pos.x)<threshold*2 ){
				direction="up";
			}
		}
	}
	public void display(){
		displayTail();
		fill(40);
		stroke(255);
		strokeWeight(2);
		rectMode(CENTER);
		rect((pos.x),(pos.y),size,size);
		
	}
}
  public void settings() { 	size(displayWidth,displayHeight,FX2D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "snake" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
