


// this class is in charge of the food piece
class Food{
	PVector pos;
	int id=0;
	Food(){
		pos = new PVector ( int(random(width)), int(random(height)));

	}
	void display(){
		fill(sin(radians(millis()*0.5))*255);
		rectMode(CENTER);
		ellipse(pos.x,pos.y,25,25);
		fill(255);
		textAlign(CENTER,CENTER);
		text(id,pos.x,pos.y-3);
	}
	void newPos(){
		id++;
		pos = new PVector ( int(random(width)), int(random(height)));
	}
}

// this class is in charge of the whole snake 
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
	void scale(int _size){
		size=_size;
	}
	void move(Food _food){
		if(speedCount< 8){
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
			/* this could use a modulo function */
			float offX = abs(width-((int(width/size)+1)*size));
			float offY = abs(height-((int(height/size)+1)*size));
			// println(offX);
			if(pos.x>width+offX){
				pos.x=width/2 - (int(width/2/size)*size);
			}
			if(pos.x<0-offX){
				pos.x=width/2 + (int(width/2/size)*size);
			}
			if(pos.y>height+offY){
				pos.y=height/2 - (int(height/2/size)*size);
			}
			if(pos.y<0-offY){
				pos.y=height/2 + (int(height/2/size)*size);
			}
			eat(_food);
			moveTail();
		}
		
	}
	void updateDirection(String _direction){
		direction = _direction;
	}
	void update(Food _food){
		shiftTwo();
		if(!collide){
			move(_food);
			collision();
			tapControl();
		}
	
	}
	void moveTail(){
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
	void displayTail(){
		for(int i = 0; i < tail.size()-1; i ++){
			// if(i>0){
				stroke(255);
				strokeWeight(2);
				fill(0);
				ellipse(tail.get(i).x, tail.get(i).y,size*0.85,size*0.85);
			// }
		}
	}
	void shiftTwo(){
		if(tail.size()<2){
			tail.add(new PVector(pos.x,pos.y));
		}else{
			if(!startTail){
				startTail=true;
			}
		}
	}
	void eat(Food _food){
		if(_food.pos.dist(pos)<size){
			tail.add(new PVector(pos.x,pos.y));
			_food.newPos();
			s.rate(random(0.6,0.85));
			s.pan(map(pos.x,0,width,-1,1));
			s.play();
			// println("eating yes");
		}
	}
	void collision(){

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
	/* This could use some vector physics like an attractor or so for the head of the snake */
	void tapControl(){
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
	/* This could use some vector physics like an attractor or so for the head of the snake */
	void display(){
		displayTail();
		fill(40);
		stroke(255);
		strokeWeight(2);
		rectMode(CENTER);
		rect((pos.x),(pos.y),size,size);
		
	}
}