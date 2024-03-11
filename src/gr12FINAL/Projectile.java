package gr12FINAL;

import java.awt.Color;
import java.awt.Graphics;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

//Wendy Shen, Jan 19, 2023
//Purpose: store information about each projectile
public class Projectile {

	private double x, y;
	private double addX, addY;
	private int size, speed, drawFrame = 0;
	private Color color;
	
	//Constructor
	public Projectile(Color color, int size, int speed, int x, int y) {
		
		this.color = color;
		this.size = size;
		this.speed = speed;
		this.x = x;
		this.y = y;
	}

	//Description: set trajectory of where bullet goes
	//Parameters: x and y to calculate trajectory
	//Return: none
	public void setTrajectory(int newX, int newY) {
		double legH = newX - x;//calculate run (cause rise over run y = mx + b hahahahah omg grade 9 math)
		double legV = newY - y;//calculate rise
		double hyp = Math.sqrt((legH * legH + legV * legV)); 

		double scaleFactor = speed/hyp;//see how much we need to multiply hypotenuse by in order to get the speed

		//use scale factor to figure out what we should add to x and y each time
		addX = scaleFactor * legH;
		addY = scaleFactor * legV;
	}

	//Description: draw projectile and move projectile
	//Parameters: graphics
	//Return: none
	public void drawProjectile(Graphics g) {
		drawFrame++;

		g.setColor(Color.white);
		g.fillOval((int)x, (int)y, size, size);
		g.setColor(color);
		g.fillOval((int)x + 2, (int)y + 2, size - 4,size - 4);

		if(drawFrame == 10) {
			x += addX;
			y += addY;
			drawFrame = 0;
		}
	}

	//Description: see if this bullet is colliding with the thing specified in parameters
	//Parameters: x and y pos, width and height of hitbox
	//Return: colliding yes? colliding no?
	public boolean isColliding(int x, int y, int w, int h) {
		if(this.x >= x && this.x + size <= x + w && this.y >= y && this.y + size <= y + h) {
			Driver.bulletSound.setFramePosition(0);
			Driver.bulletSound.start();
			return true;
		}
		return false;
	}

	//Description: check is this projectil is off screen (off map)
	//Parameters: none
	//Return: is it off screen
	public boolean isOffscreen() {
		if((x < Block.mapX)||
				(y < Block.mapY) ||
				x + size > Block.mapX + Block.blockSize * 12 ||
				y + size > Block.mapY + Block.blockSize * 7) {
			return true;
		}
		return false;
	}

	//getters and setters
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
}
