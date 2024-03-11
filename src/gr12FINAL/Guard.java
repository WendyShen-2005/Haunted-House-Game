package gr12FINAL;

import java.awt.Color;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

//Wendy Shen, Jan 19, 2023
//Purpose: store information about guards we hire

public class Guard extends Monster {

	private static double strengthMultiplier = 1;
	private int strength, bulletSize, bulletSpeed, shootFrame = 0, poseChange = 0, placeable = 0;
	private LinkedList<Projectile> projectiles = new LinkedList<Projectile>();
	private boolean selected = false, recall = false;
	private static Image idle = Toolkit.getDefaultToolkit().getImage("GuardIdle.png"), 
			shoot = Toolkit.getDefaultToolkit().getImage("Guard.gif"), 
			hurt = Toolkit.getDefaultToolkit().getImage("GuardSad.png");
	private Image currentPose = idle;
	private static Bandit target = null;

	//constructor
	public Guard(String name, int hp, int strength) throws IOException, FontFormatException {
		super(name, "Guard", hp);
		this.strength = strength;
		bulletSize = strength * 2 + 10;
		bulletSpeed = 3 * strength + 11; //woowowow y = mx + b omg grade 9 so easy hahhahahahahahha
		wage = hp + strength;
	}

	//Description: draw monster profile with all the monster's info
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
		g.drawString(name, x + 220, y + 115);
		g.drawString(role, x + 220, y + 165);
		g.setFont(UI.instructions);
		g.drawString("Wage: $" + wage + "/hr", x + 35, y + 255);
		g.drawString("HP: " + hp, x + 35, y + 305);
		g.drawString("Strength: " + strength + "/10", x + 35, y + 355);
	}

	//Description: guard shoots new bullet in specified trajectory
	//Parameters: x and y pos of direction bullet heads in
	//Return: none
	public void shoot(Graphics g, int endX, int endY) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		Iterator<Projectile> it = projectiles.iterator();
		poseChange++;

		//while there are still projectiles, check if we should remove them or not
		while(it.hasNext()) {
			Projectile proj = it.next();
			if(proj.isOffscreen())
				it.remove();
			else
				proj.drawProjectile(g);
		}

		if(shootFrame >= 50 * strength && (Driver.bandits.size() > 0)) {//shootframe adds "laggy" effect, see if there's any bandits to target
			currentPose = shoot;
			poseChange = 0;
			Projectile proj = new Projectile(Color.black, bulletSize, bulletSpeed, x, y);
			Bandit banned = null;
			int shootX = -1000, shootY = -1000;
			double currentHyp = Math.sqrt(Math.pow(Math.abs(shootX - x), 2) + Math.pow(Math.abs(shootY - y), 2));

			if(target == null || target.getHP() <= 0 || !target.isMoving()) {
				for(Bandit band: Driver.bandits) {//for every bandit, figure out which bandit to target (closest one to us)
					int leg1 = Math.abs(band.getX() - x);
					int leg2 = Math.abs(band.getY() - y);
					double newHyp = Math.sqrt(Math.pow(leg1, 2) + Math.pow(leg2, 2));
					if(newHyp < currentHyp) {
						shootX = band.getX();
						shootY = band.getY();
						currentHyp = Math.sqrt(Math.pow(Math.abs(shootX - x), 2) + Math.pow(Math.abs(shootY - y), 2));
						banned = band;
					}
				}
				target = null;
			} else {
				shootX = target.getX();
				shootY = target.getY();
			}
			
			if(banned != null)
				banned.damage((int)(strength * strengthMultiplier));
			proj.setTrajectory(shootX, shootY);
			projectiles.add(proj);
			shootFrame = 0;
		} 

		if(poseChange > 200)
			currentPose = idle;

	}

	//Description: draw guard and bullets
	//Parameters: graphics
	//Return: none
	public void drawMonster(Graphics g) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		shootFrame++;
		drawHPBar(g);
		shoot(g, 0, 0);

		if(selected)//be able to relocate by recalling guards
			if(monsterButton(g, "Recall", x, y + Block.blockSize + 10)) {
				Driver.guards.add(this);
				Driver.inGameMonsters.remove(Driver.inGameMonsters.indexOf(this));
				Driver.pr += 0.01;
				placeable = 1200;
				hp = maxHP;
			}

		//display guard info
		if(Driver.mouseXM >= x && Driver.mouseXM <= x + Block.blockSize && Driver.mouseYM >= y && Driver.mouseYM <= y + Block.blockSize) {
			inGameGuardInfo(g);
		} else
			if(Driver.mouseXC != -1 && Driver.mouseYC != -1) {
				selected = false;
			}

		if(selected)
			BuildingBlocks.drawOutline(g, x, y);

		Iterator<Projectile> it = projectiles.iterator();
		while(it.hasNext()) {
			Projectile proj = it.next();
			proj.drawProjectile(g);
			if(proj.isOffscreen())
				it.remove();
			else
				for(Bandit band: Driver.bandits)
					if(proj.isColliding(band.getX(), band.getY(), Block.blockSize, Block.blockSize)) {
						band.damage((int)(strength * strengthMultiplier));
						it.remove();
						break;
					}
		}

		g.drawImage(currentPose, x, y, Block.blockSize, Block.blockSize, null);

	}

	//Description: draw guard info in game
	//Parameters: graphics
	//Return: none
	public void inGameGuardInfo(Graphics g) {
		g.setColor(Color.gray);
		g.fillRect(x - 160, y - 20, 235, 100);
		g.setColor(Color.white);
		g.setFont(smallFont);
		g.drawString(name, x - 155, y + 10);

		if(placeable <= 0) {
			g.drawString("HP:" + hp + " Str:" + strength, x - 155, y + 40);
			g.drawString("$" + wage + "/hr", x - 155, y + + 70);

			if(Driver.mouseXC >= x && Driver.mouseXC <= x + Block.blockSize && Driver.mouseYC >= y && Driver.mouseYC <= y + Block.blockSize) {
				selected = true;
				Driver.mouseXC = -1;
				Driver.mouseYC = -1;
			}
		} else {
			g.setColor(Color.yellow);
			g.drawString("Deployable in: " + (placeable/40), x - 155, y + 40);		
		}
	}

	//getters and setters
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getHP() {
		return hp;
	}

	public int getStrength() {
		return strength;
	}

	public void setImage() {
		currentPose = hurt;
	}

	public boolean isSelected() {
		return selected;
	}

	public static void setStrengthMultiplier(double multiplier) {
		strengthMultiplier = multiplier;
	}

	public int getPlaceable() {
		return placeable;
	}

	public void setPlaceable() {
		placeable--;
	}

	public static void setTarget(Bandit bandit) {
		target = bandit;
	}
}
