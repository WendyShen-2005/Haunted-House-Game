package gr12FINAL;

import java.awt.Color;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

//Wendy Shen, Jan 27, 2023
//Purpose: store bandit info
public class Bandit extends MoveableMonster{

	private int type, shootFrame, strength, shootSpeed;
	private Color bulletColor;

	private LinkedList<Projectile> shoots;
	private static int numOfBandits = 0;
	private int banditID;
	private static Bandit selected = null;

	//Constructor
	public Bandit() throws FontFormatException, IOException {
		super("noOneCares", "Bandit", (int)(Math.random() * 3) + 6, (int)(Math.random() * 10) + 10);
		strength = hp/20;
		shootSpeed = strength * 2;
		speed = (int)(Math.random() * 5) + 10;

		int colorValue = (int)((1.0 - (1.0 * strength / 25)) * 255);
		bulletColor = new Color(255, colorValue, colorValue);

		type = 0;
		if(type == 0)
			shoots = new LinkedList<Projectile>();
		banditID = numOfBandits;
		numOfBandits++;
	}

	//Description: draw bandit
	//Parameters: graphics
	//Return: none
	public void drawMonster(Graphics g) {

		drawHPBar(g);
		frame++;
		shootFrame++;
		g.drawImage(profile, x, y, 60, 60, null);
		if(moving)
			move();

		if(Driver.mouseXM >= x && Driver.mouseXM <= x + Block.blockSize && Driver.mouseYM >= y && Driver.mouseYM <= y + Block.blockSize) {
			Block.drawOutline(g, x, y);
			if(Driver.mouseXC != -1) {
				selected = this;
			}
			Driver.mouseXC = -1;
			Driver.mouseYC = -1;
		}
		
		if(selected == this) {
			if(monsterButton(g, "Target", x, y + Block.blockSize + 10)) {
				Guard.setTarget(this);
				selected = null;
			}
		}
		
		shoot(g);
	}

	//Description: draw bullets & see if any bullets have hit anyone
	//Parameters: graphics
	//Return: none
	public void shoot(Graphics g) {
		Iterator<Projectile> it = shoots.iterator();

		while(it.hasNext()) {//go through each bullet
			Projectile proj = it.next();
			if(proj.isOffscreen())//if it's off screen, remove
				it.remove();
			else {
				//see if it hit any employees or customers
				boolean hit = false;
				for(Monster mon: Driver.inGameMonsters)
					if(proj.isColliding(mon.getX(), mon.getY(), Block.blockSize, Block.blockSize)) {
						mon.damage(strength);
						it.remove();
						hit = true;
						break;
					}
				if(!hit)
					for(String name: Driver.customers.keySet()) {
						Customer custurd = Driver.customers.get(name);
						if(proj.isColliding(custurd.getX(), custurd.getY(), Block.blockSize, Block.blockSize)) {
							custurd.damage(strength);
							it.remove();
							hit = true;
						}
					}
				//otherwise, continue drawing & moving projectile
				if(!hit)
					proj.drawProjectile(g);
			}
		}

		//see if it's time to create a new bullet
		if(shootFrame == ((100 * strength) / 20) && (Driver.guards.size() > 0 || Driver.customers.size() > 0)) {
			Projectile proj = new Projectile(bulletColor, shootSpeed, shootSpeed, x, y);
			int shootX = -1000, shootY = -1000;
			double currentHyp = Math.sqrt(Math.pow(Math.abs(shootX - x), 2) + Math.pow(Math.abs(shootY - y), 2));

			//figure out which monster it should target-----------------
			for(Monster mon: Driver.inGameMonsters) {
				int leg1 = Math.abs(mon.getX() - x);
				int leg2 = Math.abs(mon.getY() - y);
				double newHyp = Math.sqrt(Math.pow(leg1, 2) + Math.pow(leg2, 2));
				if(newHyp < currentHyp) {//see which employee is closest
					shootX = mon.getX();
					shootY = mon.getY();
					currentHyp = Math.sqrt(Math.pow(Math.abs(shootX - x), 2) + Math.pow(Math.abs(shootY - y), 2));
				}
			}
			for(String mon: Driver.customers.keySet()) {
				Customer custer = Driver.customers.get(mon);
				int leg1 = Math.abs(custer.getX() - x);
				int leg2 = Math.abs(custer.getY() - y);
				double newHyp = Math.sqrt(Math.pow(leg1, 2) + Math.pow(leg2, 2));
				if(newHyp < currentHyp) {//see if the customers are closer
					shootX = custer.getX();
					shootY = custer.getY();
					currentHyp = Math.sqrt(Math.pow(Math.abs(shootX - x), 2) + Math.pow(Math.abs(shootY - y), 2));
				}
			}
			//----------------------------------------------------------
			proj.setTrajectory(shootX, shootY);
			shoots.add(proj);
			shootFrame = 0;
		}
	}

	//getters and setters
	public int getBanditID() {
		return banditID;
	}
}
