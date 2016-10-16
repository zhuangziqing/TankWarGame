package com.cs;
import java.awt.*;

/**
 * This class control the blood object to let my tank gain more life.
 * @author ziqing
 *
 */
public class Blood {
	private int x,y,w=20,h=10;
	private int step = 0;
	private int pos[][] = {
			{300,300},{300,330},{330,330},{350,350},{370,370}
	};
	TankClient tc;
	private boolean live = true;
	
	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}
 
	Blood(){
		this.x = pos[0][0];
		this.y = pos[0][1];
	}
	
	public void draw(Graphics g){
		if(!live){return;}
		Color c = g.getColor();
		g.setColor(Color.MAGENTA);
		g.fillRect(x, y, w, h);
		g.setColor(c);
		move();
	}
	
	public Rectangle getRect() {
		return new Rectangle(x,y,w,h);
	}
	
	public void move(){
		step++;
		if(step == 5){
		step = 0;
		}
		x = pos[step][0];
		y = pos[step][1];
	}
}

