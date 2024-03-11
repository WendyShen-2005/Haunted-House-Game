package gr12FINAL;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

//Wendy Shen, Jan 12, 2023
//Description: Template for blocks that are getting added to map
abstract class BuildingBlocks {
	protected String type;
	protected int x = 100, y = 100;
	protected Image img;
	protected static int blockSize = 60, mapX = 200, mapY = 50;
	protected static double costMultiplier = 1;

	//Constructor
	public BuildingBlocks(int x, int y, String type, Image img) {
		this.x = x;
		this.y = y;
		this.type = type;
		this.img = img;
	}

	//Description: draw block
	//Parameters: graphcis
	//Return: none
	public void drawBlock(Graphics g) {
		g.setColor(Color.red);
		g.fillRect(x, y, 60, 60);
	}

	//Description: draw outline of block if hovering over block
	//Parameters: graphics
	//Return: none
	public static void drawOutline(Graphics g, int x, int y) {
		g.setColor(Color.white);//highlighting the fact that we're hovering over thing
		g.fillRect(x, y, blockSize, 5);//top
		g.fillRect(x, y + blockSize - 5, blockSize, 5);//bottom
		g.fillRect(x, y, 5, blockSize);//left
		g.fillRect(x + blockSize - 5, y, 5, blockSize);//right
		g.setColor(Color.black);
		g.drawRect(x + 5, y + 5, blockSize - 10, blockSize - 10);
	}

	//Description: see if this block is bigger than that block
	//Parameters: other block
	//Return: int of which is bigger
	public int compareTo(BuildingBlocks b) {
		return this.type.compareTo(b.type);
	}

	//Description: see if this block is equal to that block
	//Parameters: other block
	//Return: if they're equal or not
	public boolean equals(BuildingBlocks b) {
		return this.type.equals(b.type);
	}

	//Description: put block info in a string
	//Parameters: none
	//Return: string of block info
	public String toString() {
		return type + ": x = " + x + ", y = " + y;
	}

	//getters and setters
	public static int getBlockSize() {
		return blockSize;
	}

	public static int getMapX() {
		return mapX;
	}

	public static int getMapY() {
		return mapY;
	}

	public static void setMapXY(int x, int y) {
		mapX = x;
		mapY = y;
		for(int i = 0; i < Driver.map.length; i++)
			for(int j = 0; j < Driver.map[0].length; j++)
				if(Driver.map[i][j] != null)
					Driver.map[i][j].setXY(i * blockSize + mapX, j * blockSize + mapY);
	}
	
	public String getType() {
		return type;
	}

	public double getCostMultiplier() {
		return costMultiplier;
	}
	
	public static void setCostMultiplier(double multiplier) {
		costMultiplier = multiplier;
	}
	
	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
