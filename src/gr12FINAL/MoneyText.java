package gr12FINAL;

import java.awt.Color;
import java.awt.Graphics;

//Wendy Shen, Jan 27, 2023
//Purpose: draw money that customers give us
public class MoneyText {

	private String money;
	private int x = Driver.exitX, y = Driver.exitY, moveFrame = 0, width;
	private static Color bill = new Color(170, 214, 161), billOutline = new Color(118, 179, 105);
	
	//Constructor
	public MoneyText(Graphics g, double moneyText) {
		money = "+ $" + String.format("%.2f", moneyText);
		g.setFont(UI.smallFont);
		width = g.getFontMetrics().stringWidth(money);
	}

	//Description: draw money
	//Parameters: graphics
	//Return: none
	public void drawText(Graphics g) {
		g.setColor(billOutline);
		g.fillRect(x - 25, y - 10, width + 70, 30 + 20);
		g.setColor(bill);
		g.fillRect(x - 20, y - 5, width + 60, 30 + 10);
		g.setColor(billOutline);
		g.setFont(UI.smallFont);
		g.drawString(money, x, y + 20);
		move();
	}
	
	//Description: move the money
	//Parameters: none
	//Return: none
	public void move() {
		moveFrame++;
		if(moveFrame >= 10) {
			x += 30;
			moveFrame = 0;
		}
	}
	
	//Description: check if money is off screen
	//Parameters: none
	//Return: whether or not money is off screen
	public boolean isOffScreen() {
		if(x > Driver.W)
			return true;
		return false;
	}
}
