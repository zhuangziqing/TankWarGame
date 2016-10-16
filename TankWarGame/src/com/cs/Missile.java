package com.cs;
import java.awt.*;
import java.util.List;
import java.util.*;

/*
 *  This class make instance of missile. 
 *  Missile can hit wall or hit tank.
 */
public class Missile {
	int x,y;
	Direction dir;
	
	public static final int XSPEED = 10;
	public static final int YSPEED = 10;
	
	public static final int WIDTH = 10;
	public static final int HEIGHT = 10;
	
	private boolean live = true;
	private boolean good;
	private TankClient tc;
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image[] MissileImgs = null;
	private static Map<String, Image> imgs = new HashMap<String,Image>();
	static {
		MissileImgs = new Image[] {
			tk.getImage(Tank.class.getClassLoader().getResource("images/missileL.gif")),
			tk.getImage(Tank.class.getClassLoader().getResource("images/missileLU.gif")),
			tk.getImage(Tank.class.getClassLoader().getResource("images/missileU.gif")),
			tk.getImage(Tank.class.getClassLoader().getResource("images/missileRU.gif")),
			tk.getImage(Tank.class.getClassLoader().getResource("images/missileR.gif")),
			tk.getImage(Tank.class.getClassLoader().getResource("images/missileRD.gif")),
			tk.getImage(Tank.class.getClassLoader().getResource("images/missileD.gif")),
			tk.getImage(Tank.class.getClassLoader().getResource("images/missileLD.gif")),
		};
		imgs.put("L", MissileImgs[0]);
		imgs.put("LU", MissileImgs[1]);
		imgs.put("U", MissileImgs[2]);
		imgs.put("RU", MissileImgs[3]);
		imgs.put("R", MissileImgs[4]);
		imgs.put("RD", MissileImgs[5]);
		imgs.put("D", MissileImgs[6]);
		imgs.put("LD", MissileImgs[7]);
	}
	
	public Missile(int x, int y, Direction dir) {
		this.x = x;
		this.y = y;
		this.dir = dir;
	}
	
	public Missile(int x, int y, Direction dir, boolean good,TankClient tc){
		this(x, y, dir);
		this.good = good;
		this.tc = tc;
	}
	
	public void draw(Graphics g){
		
		if(this.live == false){
			tc.missiles.remove(this);
			return ;
		}
		/*Color c = g.getColor(); 
		if(good) {g.setColor(Color.RED);}
		else {g.setColor(Color.BLACK);}
		g.fillOval(x,y,WIDTH,HEIGHT);	
		g.setColor(c);*/
		switch(dir){
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
		
		move(); 
	}
	
	// This method control movement of missile.
	private void move(){
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
		}
		
		if(x < 0 || y < 0 || x > TankClient.GAME_WIDTH || y > TankClient.GAME_HEIGHT ){
			live =false;
			tc.missiles.remove(this);
		}
	}
	
	public Rectangle getRect() {
		return new Rectangle(x,y,WIDTH,HEIGHT);
	}
	
	public void setLive(boolean live) {
		this.live = live;
	}
	
	// This method runs when a missile hit a tank.
	public boolean hitTank(Tank t){
		if(this.live && this.getRect().intersects(t.getRect())  && t.isLive()==true && this.good != t.isGood() ){
			if(t.isGood()){
				t.setLife(t.getLife() -20);
				if (t.getLife() == 0){t.setLive(false);}
			} else{
				t.setLive(false);
			}
			this.setLive(false);
			Explode e = new Explode(x,y,tc);
			tc.explodes.add(e);
			return true;
		}
		return false;
	}
	
	public boolean hitTanks(List<Tank> tanks){
		for (int i = 0; i <tanks.size();i++){
			if(hitTank(tanks.get(i))){
				return true;
			}
		}
		return false;
	}
	// When a missile hit wall, its live will be set false.
	public boolean hitWall(Wall w){
		if(this.live && this.getRect().intersects(w.getRect())){
			this.live = false;
			return true;
		}
		return false;
	}
}
