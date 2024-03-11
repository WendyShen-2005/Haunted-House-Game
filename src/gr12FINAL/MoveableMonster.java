package gr12FINAL;

import java.awt.FontFormatException;
import java.awt.Graphics;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

//Wendy Shen, Jan 27, 2023
//Purpose: store information about monsters that can more (customers & bandits)
public abstract class MoveableMonster extends Monster{

	protected int speed = 5, endX, endY, carpetPos = 0, frame = 0;
	protected boolean moving = true;

	//constructor
	public MoveableMonster(String name, String role, int speed, int hp) throws FontFormatException, IOException {
		super(name, role, hp);
		x = Driver.entranceX2 * Block.blockSize + Block.mapX;
		y = Driver.entranceY2 * Block.blockSize + Block.mapY;
		endX = x;
		endY = y;
		setTrajectory(Driver.direction[carpetPos]);
	}

	//Description: draw monster
	//Parameterers: graphics
	//Return: none
	public void drawMonster(Graphics g) throws LineUnavailableException {
		drawHPBar(g);
		frame++;
		g.drawImage(profile, x, y, 60, 60, null);
		if(moving)
			move();
	}

	//Description: set trajectory for where this monster goes
	//Parameters: direction (u, r, l, d)
	//Return: none
	public void setTrajectory(char c) {
		if(c == 'l') //left
			endX = ((x - Block.mapX)/Block.blockSize - 1) * Block.blockSize + Block.mapX;
		else if(c == 'r')//right
			endX = ((x - Block.mapX)/Block.blockSize + 1) * Block.blockSize + Block.mapX;
		else if(c == 'u')//up
			endY = ((y - Block.mapY)/Block.blockSize - 1) * Block.blockSize + Block.mapY;
		else if(c == 'd')
			endY = ((y - Block.mapY)/Block.blockSize + 1) * Block.blockSize + Block.mapY;
	}

	//Description: move character
	//Paramters: none
	//Return: none
	public void move() {
		if(frame == 10) {
			if(Math.abs(endX - x) < speed && Math.abs(endY - y) < speed) {//if we walk 1 more step, but overshoot trajector, snap to end position
				x = endX;
				y = endY;
				carpetPos++;
				if(carpetPos >= Driver.carpetLen)
					moving = false;
				setTrajectory(Driver.direction[carpetPos]);
			} else//otherwise walk as normal
				if(endX < x)//if go left
					x-= speed;
				else if(endX > x)//if go right
					x += speed;
				else if(endY < y)//if go up
					y -= speed;
				else//if go down
					y += speed;
			frame = 0;
		}
	}

	//getters and setters
	public boolean isMoving() {
		return moving;
	}

}
