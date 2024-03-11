package gr12FINAL;

import java.awt.Graphics;
import java.awt.Toolkit;

//Wendy Shen, Jan 12, 2023
//Description: Store where the door is
public class Door extends BuildingBlocks{

	//Constructor
	public Door(int x, int y, String entranceExit) {
		super(x, y, "Door", null);
		String direct;
		if(x == Block.getMapX())
			direct = "Right";
		else if(x == Block.getMapX() + Block.getBlockSize() * 11)
			direct = "Left";
		else if(y == Block.getMapY())
			direct = "Up";
		else
			direct = "Down";
		type = "Door" + entranceExit;
		img = Toolkit.getDefaultToolkit().getImage("door" + direct + ".png");
	}

	//Description: draw door
	//Parameters: graphics
	//Return: none
	public void drawBlock(Graphics g) {
		g.drawImage(img, x, y, blockSize, blockSize, null);
	}

}
