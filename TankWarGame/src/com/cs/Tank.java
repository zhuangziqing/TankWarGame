package com.cs;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/*
 * This class make new tank instance. 
 */

public class Tank {
	private int x,y,oldX,oldY;
	TankClient tc;
	public static final int XSPEED = 5;
	public static final int YSPEED = 5;
	public static final int WIDTH = 30;
	public static final int HEIGHT = 30;
	private boolean bL=false,bU=false,bR=false,bD=false;
	
	private Direction dir = Direction.STOP;
	private Direction ptDir = Direction.D;
	private boolean good;
	private int life = 100;
	private boolean live = true;
	private static Random r = new Random();
	private int step = r.nextInt(12) + 3;
	private BloodBar bb = new BloodBar();
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image[] tankImgs = null;
	private static Map<String, Image> imgs = new HashMap<String,Image>();
	static {
		tankImgs = new Image[] {
			tk.getImage(Tank.class.getClassLoader().getResource("images/tankL.gif")),
			tk.getImage(Tank.class.getClassLoader().getResource("images/tankLU.gif")),
			tk.getImage(Tank.class.getClassLoader().getResource("images/tankU.gif")),
			tk.getImage(Tank.class.getClassLoader().getResource("images/tankRU.gif")),
			tk.getImage(Tank.class.getClassLoader().getResource("images/tankR.gif")),
			tk.getImage(Tank.class.getClassLoader().getResource("images/tankRD.gif")),
			tk.getImage(Tank.class.getClassLoader().getResource("images/tankD.gif")),
			tk.getImage(Tank.class.getClassLoader().getResource("images/tankLD.gif")),
		};
		imgs.put("L", tankImgs[0]);
		imgs.put("LU", tankImgs[1]);
		imgs.put("U", tankImgs[2]);
		imgs.put("RU", tankImgs[3]);
		imgs.put("R", tankImgs[4]);
		imgs.put("RD", tankImgs[5]);
		imgs.put("D", tankImgs[6]);
		imgs.put("LD", tankImgs[7]);
	}
	
	
	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public boolean isGood() {
		return good;
	}
	
	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	public Tank(int x, int y, boolean good) {
		this.x = x;
		this.y = y;
		this.good = good;
	}
	
	public Tank(int x,int y,boolean good,Direction dir,TankClient tc){
		this(x,y,good);
		this.dir = dir;
		this.tc = tc;
	}
	
	public void draw(Graphics g){
		if(!live){
			if(!good){tc.tanks.remove(this);}
			return ;
		}
		
		 if(good){bb.draw(g);}
		 move();
		 
		 switch(ptDir){
			case L:
				g.drawImage(imgs.get("L"), x, y, null);
				break;
			case LU:
				g.drawImage(imgs.get("LU"), x, y, null);
				break;
			case U:
				g.drawImage(imgs.get("U"), x, y, null);
				break;
			case RU:
				g.drawImage(imgs.get("RU"), x, y, null);
				break;
			case R:
				g.drawImage(imgs.get("R"), x, y, null);
				break;
			case RD:
				g.drawImage(imgs.get("RD"), x, y, null);
				break;
			case D:
				g.drawImage(imgs.get("D"), x, y, null);
				break;
			case LD:
				g.drawImage(imgs.get("LD"), x, y, null);
				break;
			}
	}
	
	// This method control tank move.
	void move(){
		
		this.oldX = x;
		this.oldY = y;
		switch(dir){
		case L:
			x -= XSPEED;
			break;
		case LU:
			x -= XSPEED;
			y -= YSPEED;
			break;
		case U:
			y -= YSPEED;
			break;
		case RU:
			x += XSPEED;
			y -= YSPEED;
			break;
		case R:
			x += XSPEED;
			break;
		case RD:
			x += XSPEED;
			y += YSPEED;
			break;
		case D:
			y += YSPEED;
			break;
		case LD:
			x -= XSPEED;
			y += YSPEED;
			break;
		case STOP:
			break;
		}
		if(this.dir != Direction.STOP) {
			this.ptDir = this.dir;
		}
		if (x<0){x = 0;}
		if (y<30){y = 30;}
		if (x + Tank.WIDTH>TankClient.GAME_WIDTH){
			x = TankClient.GAME_WIDTH - Tank.WIDTH;
		}
		if (y + Tank.HEIGHT >TankClient.GAME_HEIGHT){
			y = TankClient.GAME_HEIGHT - Tank.HEIGHT;
		}
		
		if(!good){
			Direction[] dirs = Direction.values();
			if(step == 0){
			step = r.nextInt(12) + 3;	
			int rn = r.nextInt(dirs.length);
			dir = dirs[rn];
			}
			if(r.nextInt(40)>35){this.fire();}
			step--;
		}
	}
	
	public void keyPressed(KeyEvent e){
		int key = e.getKeyCode();
		switch(key){
		
		case KeyEvent.VK_LEFT:
			bL = true;
			break;
		case KeyEvent.VK_RIGHT:
			bR = true;
			break;
		case KeyEvent.VK_UP:
			bU = true;
			break;
		case KeyEvent.VK_DOWN:
			bD = true;
			break;
		}
		locateDirection();
	}

	
	void locateDirection(){
		if(bL && !bU && !bR && !bD) dir=Direction.L;
		else if(bL && bU && !bR && !bD) dir=Direction.LU;
		else if(!bL && bU && !bR && !bD) dir=Direction.U;
		else if(!bL && bU && bR && !bD) dir=Direction.RU;
		else if(!bL && !bU && bR && !bD) dir=Direction.R;
		else if(!bL && !bU && bR && bD) dir=Direction.RD;
		else if(!bL && !bU && !bR && bD) dir=Direction.D;
		else if(bL && !bU && !bR && bD) dir=Direction.LD;
		else if(!bL && !bU && !bR && !bD) dir=Direction.STOP;
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key){
		case KeyEvent.VK_CONTROL:
			fire();  
			break;
		case KeyEvent.VK_LEFT:
			bL = false;
			break;
		case KeyEvent.VK_RIGHT:
			bR = false;
			break;
		case KeyEvent.VK_UP:
			bU = false;
			break;
		case KeyEvent.VK_DOWN:
			bD = false;
			break;
		case KeyEvent.VK_A:
			superFire();
			break;
		case KeyEvent.VK_F2:
			tc.resurvive();
			break;
		}
		locateDirection();
	}
/*
 * This method let the tank fire a missile. 
 * After it fires, the missle will go forward as same as the tank's direction.
 */
	public Missile fire(){
		if(!live){ return null;}
		int x = this.x + Tank.WIDTH/2 - Missile.WIDTH/2;
		int y = this.y + Tank.HEIGHT/2 - Missile.HEIGHT/2;
		Missile m = new Missile(x,y,ptDir,good,tc);
		tc.missiles.add(m);
		return m;
	}
	/**
	public Missile fire(Direction dir){
		if(!live){ return null;}
		int x = this.x + Tank.WIDTH/2 - Missile.WIDTH/2;
		int y = this.y + Tank.HEIGHT/2 - Missile.HEIGHT/2;
		Missile m = new Missile(x,y,dir,good,tc);
		tc.missiles.add(m);
		return m;
	}
	*/
	public void superFire(){
		Direction[] dirs = Direction.values();
		for(int i = 0; i< 8;i++){
			int x = this.x + Tank.WIDTH/2 - Missile.WIDTH/2;
			int y = this.y + Tank.HEIGHT/2 - Missile.HEIGHT/2;
			Missile m = new Missile(x,y,dirs[i],good,tc);
			tc.missiles.add(m);
		}
	}
	
	public void stay(){
		this.x = this.oldX;
		this.y = this.oldY;
	}
	/**
	 * When one instance of tank hit the wall.
	 * @param w Wall
	 * @return true when tank hit wall, it will stay��
	 */
	public boolean collidesWithWall(Wall w){
		if(this.live && this.getRect().intersects(w.getRect())){
			stay();
			return true;
		}
		return false;
	}
	
	// When two tanks collides, both of them stay.
	public boolean collidesWithTanks(java.util.List<Tank> tanks){
		for(int i = 0; i<tanks.size();i++){
			Tank t = tanks.get(i);
			if(this != t){
				if(this.live && t.isLive() && this.getRect().intersects(t.getRect()) ){
					this.stay();
					t.stay();
					return true;
				}
			}
		}
		return false;
	}
	
	public Rectangle getRect() {
		return new Rectangle(x,y,WIDTH,HEIGHT);
	}
	
	// This class show how many lives current tank has.
	private class BloodBar{
		public void draw(Graphics g){
			Color c = g.getColor();
			g.drawRect(x, y -10, WIDTH, 10);
			g.setColor(Color.RED);
			int w = WIDTH * life/100;
			g.fillRect(x,y-10,w,10);
			g.setColor(c);
		}
	}
	
	// my tank can gain more life by eating blood.
	public boolean eatBlood(){
		if(good && this.live  && tc.b.isLive() && this.getRect().intersects(tc.b.getRect())){
			tc.b.setLive(false);
			this.setLife(100);
			return true;
		}
		return false;
	}
}

