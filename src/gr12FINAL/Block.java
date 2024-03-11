package gr12FINAL;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

//Wendy Shen, Jan 12, 2023
//Description: Props, decorations, etc. that player adds to spice up their room
public class Block extends BuildingBlocks {
	private static boolean moveable = true;
	private static Color spookColor = new Color(255, 148, 152), 
			familyColor = new Color(148, 255, 153), 
			excitementColor = new Color(241, 255, 148), 
			costColor = new Color(148, 187, 255);

	private boolean inGame = false;
	private int prevX, prevY;
	private int spook = 0, familyFriendliness = 0, excitement = 0; 
	private double cost;

	private boolean dragging;

	//Constructor
	public Block(int x, int y, boolean inGame, String type, int spook, int familyFriendliness, int excitement, double cost) {
		super(x, y, type, Toolkit.getDefaultToolkit().getImage(type + ".gif"));		
		this.inGame = inGame;
		this.spook = spook;
		this.familyFriendliness = familyFriendliness;
		this.excitement = excitement;
		this.cost = cost;

		if(inGame) {
			prevX = (x - mapX)/blockSize;
			prevY = (y - mapY)/blockSize;
		}

		Driver.pr += 0.01;
	}

	//Description: draw block
	//Parameters: graphics, size
	//Return: none
	public void drawBlock(Graphics g) {
		g.drawImage(img, x, y, blockSize, blockSize, null);

		if(moveable) {
			if(Driver.mouseXM >= x && Driver.mouseXM <= x + blockSize && Driver.mouseYM >= y && Driver.mouseYM <= y + blockSize) {//if hovering
				if(!inGame) 
					blockStats(g);

				drawOutline(g, x, y);
				if(Driver.dragging == 1)//if clicking on thingy, drag
					dragging = true;
			} else
				dragging = false;

			if(dragging) //if dragging on thing
				if(!inGame) //if it's one of those option blocks
					drawOptionBlock(g);
				else
					drawInGameBlock(g);	
		}
	}

	//Description: draw option block (the blocks that are there to add more blocks...... yeah)
	//Parameters: graphics
	//Return: none
	public void drawOptionBlock(Graphics g) {
		if(Driver.dragging == -1 && Driver.mouseXC >= mapX && Driver.mouseXC <= mapX + blockSize * 12 && Driver.mouseYC >= mapY && Driver.mouseYC <= mapY + blockSize * 7) {//if released
			int newX = (Driver.mouseXC - mapX)/blockSize, newY = (Driver.mouseYC - mapY)/blockSize;
			if(Driver.budget - cost * costMultiplier < 0)
				UI.noise(Driver.no);
			else if(Driver.map[newX][newY] == null) {//check if player is placing block on valid block
				Block newBlock = new Block(newX * blockSize + mapX, newY * blockSize + mapY, true, type, spook, familyFriendliness, excitement, cost);
				Driver.map[newX][newY] = newBlock;
				setStats(1);
				UI.noise(Driver.placeBlock);
			}
			Driver.mouseXM = -1;
			Driver.mouseYM = -1;
		} else //being dragged
			g.drawImage(img, Driver.mouseXC, Driver.mouseYC, blockSize, blockSize, null);
	}

	//Description: draw the blocks that are on the map
	//Parameters: graphics
	//Return: none
	public void drawInGameBlock(Graphics g) {
		if(Driver.dragging == -1) {//if mouse released

			Driver.map[prevX][prevY] = null;
			if(Driver.mouseXC >= mapX && Driver.mouseXC <= mapX + blockSize * 12 && Driver.mouseYC >= mapY && Driver.mouseYC <= mapY + blockSize * 7) {//if player releases block in building area
				int newX = (Driver.mouseXC - mapX)/blockSize, newY = (Driver.mouseYC - mapY)/blockSize;
				if(Driver.map[newX][newY] == null) {//check if block is valid
					prevX = newX;
					prevY = newY;
					x = newX * blockSize + mapX;
					y = newY * blockSize + mapY;
					Driver.map[newX][newY] = this;
				} 
			} else {
				setStats(-1);
				x = -100;
				y = -100;
			}
			dragging = false;
			Driver.dragging = -1;
		} else //being dragged
			g.drawImage(img, Driver.mouseXC, Driver.mouseYC, blockSize, blockSize, null);
	}

	//Description: draw block stats in game
	//Parameters: graphics
	//Return: none
	public void blockStats(Graphics g) {
		int xBox = 975, yBox = 100;
		g.setColor(Color.gray);
		g.fillRect(xBox, yBox, 175, 205);
		g.setFont(UI.smallFont);
		g.setColor(Color.white);
		g.drawString(type, xBox + 5, yBox + 25);
		g.setColor(spookColor);
		g.drawString("Spook: " + spook + "/10", xBox + 5, yBox + 55);
		g.setColor(familyColor);
		g.drawString("Family-fun:", xBox + 5, yBox + 85);
		g.drawString(familyFriendliness + "/10", xBox + 5, yBox + 110);
		g.setColor(excitementColor);
		g.drawString("Excitement:", xBox + 5, yBox + 140);
		g.drawString(excitement + "/10", xBox + 5, yBox + 165);
		g.setColor(costColor);
		g.drawString("Cost: $" + cost, xBox + 5, yBox + 195);
	}

	//getters and setters
	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void setStats(int num) {
		if(num == 1) {
			Driver.budget -= cost * costMultiplier;
			Driver.spookiness += spook;
			Driver.familyFriendliness += familyFriendliness;
			Driver.excitement += excitement;
			Driver.pr += 0.01;
		} else {
			Driver.budget += cost * costMultiplier;
			Driver.spookiness -= spook;
			Driver.familyFriendliness -= familyFriendliness;
			Driver.excitement -= excitement;
			Driver.pr -= 0.01;
		}
	}

	public int getSpook() {
		return spook;
	}

	public int getFun() {
		return familyFriendliness;
	}

	public int getExcite() {
		return excitement;
	}

	public double getCost() {
		return cost;
	}

	public static void setMoveable(boolean move) {
		moveable = move;
	}
}
