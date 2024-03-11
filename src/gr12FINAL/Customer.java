package gr12FINAL;

import java.awt.Color;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.io.IOException;

//Wendy Shen, Jan 27, 2023
//Purpose: hold customer info
public class Customer extends MoveableMonster{

	private int spookPreference, funPreference, excitementPreference;
	//constructor
	public Customer(String name) throws IOException, FontFormatException {
		super(name, "Customer", 5, 10 + Driver.spookiness/4);
		x = Driver.entranceX2 * Block.blockSize + Block.mapX;
		y = Driver.entranceY2 * Block.blockSize + Block.mapY;
		spookPreference = (int)(Math.random() * 10);
		funPreference = (int)(Math.random() * 10);
		excitementPreference = (int)(Math.random() * 20);

		endX = x;
		endY = y;
		setTrajectory(Driver.direction[carpetPos]);
	}

	//Description: draw monster
	//Parameters: graphics
	//Return: none
	public void drawMonster(Graphics g) {

		speed = 5 + Driver.bandits.size() * 5 + Driver.familyFriendliness/20;

		drawHPBar(g);
		frame++;
		g.drawImage(profile, x, y, 60, 60, null);
		if(moving && !isDead())
			move();

		showName(g);
	}

	//Description: show name if we hover over name
	//Parameters: graphics
	//Return: none
	public void showName(Graphics g) {
		if(Driver.mouseXM >= x && Driver.mouseXM <= x + Block.blockSize && Driver.mouseYM >= y && Driver.mouseYM <= y + Block.blockSize) {
			g.setFont(smallFont);
			int nameWidth = g.getFontMetrics().stringWidth(name);
			int nameOffset = -(nameWidth - Block.blockSize)/2;

			g.setColor(Color.blue);
			g.fillRect(x + nameOffset, y - 40, nameWidth, 20);

			g.setColor(Color.white);
			g.drawString(name, x + nameOffset, y - 20);
		}
	}

	//Description: calculate customer's tips
	//Parameters: current spook stat, fun stat and excite stat and pr (not their own preference)
	//Return: tip
	public double calculateTip(int spook, int fun, int excite, double pr) {
		double tip = 0;
		if(pr > 0) {
			tip += (int)((spookPreference * spook / 70) * 100) / 100.0;
			tip += (int)((funPreference * fun / 50) * 70) / 100.0;
			tip += (int)((excitementPreference * excite / 20) * 100) / 100.0;
			tip *= pr/2.0;
			if(pr/2.0 > 0)
				tip *= ((int)(pr/2.0 * 100))/100.0;
		}
		return tip;
	}
}
