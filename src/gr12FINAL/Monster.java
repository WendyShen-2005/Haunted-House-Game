package gr12FINAL;

import java.awt.Color;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

//Wendy Shen, Jan 12, 2023
//Description: Template for employees
public abstract class Monster extends UI implements Comparable<Monster> {

	protected int x, y, hp, maxHP;
	protected String name, role;
	protected Image profile;
	protected int monsterID;
	protected static int numMonsters = 0;
	protected double wage;
	protected static double wageMultiplier = 1;

	//Constructor
	public Monster(String name, String role, int hp) throws IOException, FontFormatException{
		this.hp = hp;
		this.hp *= 10;
		maxHP = this.hp;
		this.name = name;
		this.role = role;
		this.profile = Toolkit.getDefaultToolkit().getImage(role + ".gif");
		monsterID = numMonsters;
		numMonsters++;
	}

	//Description: draw option button to click into a monster's profile
	//Parameters: graphics, position (on scroll list -- is it at the top? middle? buttom?)
	//Return: int for if this is clicked
	public int monsterOption(Graphics g, int position, int len, int x, int y) {
		return button(g, name + ", " + role, x + 30, y + 90 * position + 30, len, monsterID + Driver.scene + 1);
	}

	//Description: draw profile of when we click into a monster to see their info
	//Parameters: graphics, x and y pos of box
	//Return: none
	public void drawMonsterProfile(Graphics g, int x, int y) {
		g.setColor(UI.lightPink);
		g.fillRoundRect(500, 100, 500, 500, 20, 20);
		g.drawImage(profile, 525, 125, 50, 50, null);
		g.setColor(UI.boldPink);
		g.setFont(UI.buttonFont);
		g.drawString(name, 550, 100);
		g.drawString(role, 550, 150);
	}

	//Description: draws hp bar above monster
	//Parameters: graphics
	//Return: none
	public void drawHPBar(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(x, y - 20, Block.blockSize, 15);
		g.setColor(Color.red);
		g.fillRect(x + 2, y - 18, Block.blockSize - 4, 11);
		g.setColor(Color.green);
		g.fillRect(x + 2, y - 18, (int)((Block.blockSize - 4) * (1.0 * hp / maxHP)), 11);
	}

	//Description: draw monster button (for recall button on guards)
	//Parameters: graphics, text on button, x and y pos
	//Return: whether or not button has been clicked
	public boolean monsterButton(Graphics g, String a, int x, int y) {
		g.setColor(Color.gray);
		g.setFont(smallFont);
		int width = g.getFontMetrics().stringWidth(a) + 20;
		g.fillRect(x, y, width, 30);

		//if hovering over button
		if(Driver.mouseXM >= x && Driver.mouseXM <= x + width && Driver.mouseYM >= y && Driver.mouseYM <= y + 30) {
			g.fillRect(x - 5, y - 5, width + 10, 40);
			if(Driver.mouseXC != -1 && Driver.mouseYC != -1)//if click on button
				return true;
		} else
			g.fillRect(x, y, width, 30);


		g.setColor(Color.white);
		g.drawString(a, x + 10, y + 20);

		return false;
	}

	//Description: draw the monster
	//Parameters: graphics
	//Return: none
	public void drawMonster(Graphics g) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		if(!role.equals("SpecialCustomer"))
			drawHPBar(g);
		g.drawImage(profile, x, y, 60, 60, null);
	}

	//Description: see if this monster is equal to that monster
	//Parameters: other monster
	//Return: whether they're the same or not
	public boolean equals(Monster mosnter) {
		return role.equals(mosnter.role);
	}

	//Description: compare this monster to that monster
	//Parameters: return which is bigger
	//Return: integer of which is bigger
	public int compareTo(Monster monster) {
		return this.name.compareTo(monster.name);
	}

	//Getters and setters
	public int getMonsterID() {
		return monsterID;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public String getRole() {
		return role;
	}

	public int getHP() {
		return hp;
	}

	public int getMaxHP() {
		return maxHP;
	}

	public double getWage() {
		return wage;
	}

	public static double getWageMultiplier() {
		return wageMultiplier;
	}

	public static void setWageMultiplier(double multiplier) {
		wageMultiplier = multiplier;
	}

	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void damage(int dmg) {
		hp -= dmg;
	}
	
	public boolean isDead() {
		if(hp < 0)
			return true;
		return false;
	}
}
