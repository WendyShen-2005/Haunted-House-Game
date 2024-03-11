package gr12FINAL;

import java.awt.Color;
import java.awt.Graphics;

//Wendy Shen, Jan 12, 2023
//Description: Draw/add carpet block to show where npcs will be walking (also contains which direction the character will go in)
public class Carpet extends BuildingBlocks{

	private static int cost, excitement;
	private static Carpet prev;
	private char direction;
	int[] xTri = new int[3];
	int[] yTri = new int[3];
	private static Color carpet = new Color(74, 5, 5), arrow = new Color(125, 55, 1);

	//Constructor
	public Carpet(int x, int y, char direction) {
		super(x, y, "Carpet", null);
		this.direction = 'n';
		Driver.budget--;
		Driver.excitement++;
		cost++;
		excitement++;
		if(prev != null) {//if this is not the first carpet piece
			prev.setDirection(direction);
			prev.assignPrevBlock();
		} 
		prev = this;
	}

	//Description: assign which direction the arrow on carpet will point
	//Parameters: none
	//Return: none
	public void assignPrevBlock() {
		if(direction == 'u') {
			xTri[0] = x + 30;
			xTri[1] = x + 20;
			xTri[2] = x + 40;
			yTri[0] = y + 20;
			yTri[1] = y + 40;
			yTri[2] = y + 40;
		} else if(direction == 'd') {
			xTri[0] = x + 20;
			xTri[1] = x + 30;
			xTri[2] = x + 40;
			yTri[0] = y + 20;
			yTri[1] = y + 40;
			yTri[2] = y + 20;
		} else if(direction == 'l') {
			xTri[0] = x + 20;
			xTri[1] = x + 40;
			xTri[2] = x + 40;
			yTri[0] = y + 30;
			yTri[1] = y + 20;
			yTri[2] = y + 40;
		} else {
			xTri[0] = x + 20;
			xTri[1] = x + 20;
			xTri[2] = x + 40;
			yTri[0] = y + 20;
			yTri[1] = y + 40;
			yTri[2] = y + 30;
		}
	}

	//Description: draw block
	//Parameters: graphics
	//Return: none
	public void drawBlock(Graphics g) {
		g.setColor(carpet);
		if(direction == 'u' || direction == 'd')
			g.fillRect(x + 5, y, blockSize - 10, blockSize);
		else
			g.fillRect(x, y + 5, blockSize, blockSize - 10);

		g.setColor(arrow);

		if(prev != null)//if this block has an arrow
			g.fillPolygon(xTri, yTri, 3);
	}

	//getters and setters
	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
		assignPrevBlock();
	}

	public void setDirection(char d) {
		direction = d;
	}

	public char getDirection() {
		return direction;
	}

	public static void resetPrevCarpet() {
		prev = null;
		Driver.budget += cost;
		Driver.excitement -= excitement;
		cost = 0;
		excitement = 0;
	}

}
