package gr12FINAL;

import java.awt.Color;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

//Wendy Shen, Jan 12, 2023
//Description: basic employee information (not shooters)
public class BasicEmployee extends Monster {

	private int charisma, competency;
	private boolean selected = false;
	//Constructor
	public BasicEmployee(String role, String name, int hp, int charisma, int competency) throws IOException, FontFormatException {
		super(name, role, hp);
		this.charisma = charisma;
		this.competency = competency;
		wage = (charisma + competency)/10.0 * 15;
		wage = ((int)(wage * 100))/100.0;
		this.role = role;
		this.name = name;
	}

	//Description: draw profile with all the monster's info
	//Parameters: graphics, x and y pos of where box is
	//Return: none
	public void drawMonsterProfile(Graphics g, int x, int y) {
		g.setColor(UI.boldPink);
		g.fillRoundRect(x, y, 520, 400, 40, 40);
		g.setColor(UI.lightPink);
		g.fillRoundRect(x + 10, y + 10, 500, 380, 20, 20);
		g.drawImage(profile, x + 35, y + 35, 180, 180, null);
		g.setColor(UI.boldPink);
		g.setFont(UI.buttonFont);
		g.drawString(name, x + 220, y + 75);
		g.drawString(role, x + 220, y + 125);
		g.drawString("HP: " + hp, x + 220, y + 175);
		g.setFont(UI.instructions);
		g.drawString("Wage: $" + wage + "/hr", x + 35, y + 255);
		g.drawString("Skill: " + competency + "/10", x + 35, y + 305);
		g.drawString("Charisma: " + charisma + "/10", x + 35, y + 355);
	}

	//Description: draw monster
	//Parameters: graphics
	//Return: none
	public void drawMonster(Graphics g) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		if(Driver.mouseXM >= x && Driver.mouseXM <= x + Block.blockSize && Driver.mouseYM >= y && Driver.mouseYM <= y + Block.blockSize)
			inGameInfo(g);
		if(!role.equals("SpecialCustomer"))
			drawHPBar(g);
		g.drawImage(profile, x, y, 60, 60, null);
		
		if(Driver.mouseXM >= x && Driver.mouseXM <= x + Block.blockSize && Driver.mouseYM >= y && Driver.mouseYM <= y + Block.blockSize) {
			Block.drawOutline(g, x, y);
			if(Driver.mouseXC != -1) {
				selected = true;
			}
			Driver.mouseXC = -1;
			Driver.mouseYC = -1;
		}
		
		if(selected) 
			if(monsterButton(g, "Fire", x, y + Block.blockSize + 10)) 
				Driver.inGameMonsters.remove(this);
	}

	//Description: draw employee info in game
	//Parameters: graphics
	//Return: none
	public void inGameInfo(Graphics g) {
		if(x >= 350) {
			g.setColor(Color.gray);
			g.fillRect(x - 160, y - 20, 300, 100);
			g.setColor(Color.white);
			g.setFont(smallFont);
			g.drawString(name + ", HP:" + hp, x - 155, y + 10);
			g.drawString("Char:" + charisma + " Skill:" + competency,  x - 155, y + 40);
			g.drawString("$" + wage + "/hr", x - 155, y + + 70);
		} else {
			g.setColor(Color.gray);
			g.fillRect(x - 10, y - 20, 300, 100);
			g.setColor(Color.white);
			g.setFont(smallFont);
			g.drawString(name + ", HP:" + hp, x  + Block.blockSize + 10, y + 10);
			g.drawString("Char:" + charisma + " Skill:" + competency,  x + Block.blockSize + 10, y + 40);
			g.drawString("$" + wage + "/hr", x  + Block.blockSize + 10, y + + 70);
		}

	}

	//getters and setters
	public void setCoordinates(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getCharisma() {
		return charisma;
	}

	public int getSkill() {
		return competency;
	}

}
