package com.cs;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * This class is the manager of whole project. 
 * Our program start from here.
 * @author Administrator
 *
 */
public class TankClient extends Frame{
	public static final int GAME_WIDTH = 800;
	public static final int GAME_HEIGHT = 600;
	int x = 50,y =50;
	
	Tank myTank = new Tank(x,y,true,Direction.STOP,this);
	Wall w1 = new Wall(300,100,150,40,this), w2 = new Wall(100,300,40,350,this);
	Blood b = new Blood();
	
	List<Missile> missiles = new ArrayList<Missile>();
	List<Explode> explodes = new ArrayList<Explode>();
	List<Tank> tanks = new ArrayList<Tank>();
	
	Image offScreenImage = null;
	
	public void paint(Graphics g) {
		g.drawString("Missiles count:"+missiles.size(), 50, 50);
		g.drawString("Explodes count:"+explodes.size(), 50, 60);
		g.drawString("Tanks count:"+tanks.size(), 50, 70);
		g.drawString("Tanks life:"+myTank.getLife(), 50, 80);
		 for (int i =0;i<missiles.size();i++){
			 Missile m = missiles.get(i);
			 m.hitTanks(tanks);
			 m.hitTank(myTank);
			 m.hitWall(w1);
			 m.hitWall(w2);
			 
			 m.draw(g);
		 }
		 for (int i =0;i<explodes.size();i++){
			 Explode e = explodes.get(i);
			 e.draw(g);
		 }
		 for (int i =0;i<tanks.size();i++){
			 Tank t = tanks.get(i);
			 t.collidesWithWall(w1);
			 t.collidesWithWall(w2);
			 t.collidesWithTanks(tanks);
			
			 t.draw(g);
		 }
		 myTank.draw(g);
		 myTank.eatBlood();
		 w1.draw(g);
		 w2.draw(g);
		 b.draw(g);
		 
		 while(tanks.size() == 0){
			for (int i =0; i<Integer.parseInt(PropertyMgr.getProperty("reproduceTankCount"));i++){
				tanks.add(new Tank(50+40*i,200,false,Direction.D,this));
				
			}
		 } 
	}
	
	public void update(Graphics g) {
		if(offScreenImage==null){
			offScreenImage = this.createImage(GAME_WIDTH,GAME_HEIGHT);
		}
		Graphics gOffScreen = offScreenImage.getGraphics();
		Color c = gOffScreen.getColor();
		gOffScreen.setColor(Color.BLACK);
		gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
		gOffScreen.setColor(c);
		paint(gOffScreen);
		g.drawImage(offScreenImage,0,0,null);
	}
/**
 * 
 */
	public void launchFrame(){
		this.setLocation(200,100);
		this.setSize(GAME_WIDTH,GAME_HEIGHT);
		this.setBackground(Color.BLACK);
		this.setResizable(false);
		this.setTitle("TankWar");
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				setVisible(false);
				System.exit(0);
			}
		});
		addKeyListener(new KeyMonitor());
		
		
		
		int initTankCount = Integer.parseInt(PropertyMgr.getProperty("initTankCount"));
		for (int i =0; i<initTankCount;i++){
			tanks.add(new Tank(50+40*i,200,false,Direction.D,this));
			
		}
	
		this.setVisible(true);
		PaintThread pt = new PaintThread();
		Thread t = new Thread(pt);
		t.start();
	}
	
	public static void main(String[] args) {
		TankClient tc = new TankClient();
		tc.launchFrame();

	}
	
	private class PaintThread implements Runnable{

		public void run() {
			while(true){
				repaint();
				try {
					Thread.sleep(70);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

	private class KeyMonitor extends KeyAdapter{

		public void keyReleased(KeyEvent e) {
			 myTank.keyReleased(e);
		}

		public void keyPressed(KeyEvent e){
			myTank.keyPressed(e);
		} 
	}

	public void resurvive() {
		while(myTank.isLive() == false){
			myTank.setLive(true);
			myTank.setLife(100);
		}
	}

}
