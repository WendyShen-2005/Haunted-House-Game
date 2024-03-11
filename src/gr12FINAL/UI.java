package gr12FINAL;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeSet;

import javax.sound.sampled.Clip;
import javax.swing.JPanel;
import javax.swing.text.html.HTMLDocument.Iterator;

//Wendy Shen, Jan 12, 2023
//Description: Contain all user interface stuff (buttons, dialogue, instructions, etc.)
abstract class UI extends JPanel implements Runnable {
	private long startTime, timeElapsed;
	private int frameCount = 0;
	private int FPS = 60;
	private Image butt1Hover = Toolkit.getDefaultToolkit().getImage("buttOnleft.png");
	private Image butt2Hover = Toolkit.getDefaultToolkit().getImage("buttOnmid.png");
	private Image butt3Hover = Toolkit.getDefaultToolkit().getImage("buttOnright.png");
	private Image butt1Default = Toolkit.getDefaultToolkit().getImage("buttOffleft.png");
	private Image butt2Default = Toolkit.getDefaultToolkit().getImage("buttOffmid.png");
	private Image butt3Default = Toolkit.getDefaultToolkit().getImage("buttOffright.png");
	public static Font buttonFont, instructions, smallFont;
	public static Color boldPink = new Color(241, 95, 192), lightPink = new Color(255, 224, 245), good = new Color(41, 133, 61);

	public static int scrollOffset = 0;

	//Constructor
	public UI() throws FontFormatException, IOException {
		buttonFont = Font.createFont(Font.TRUETYPE_FONT, new File("Gamer.ttf")).deriveFont(70f);
		instructions = Font.createFont(Font.TRUETYPE_FONT, new File("Gamer.ttf")).deriveFont(50f);
		smallFont = Font.createFont(Font.TRUETYPE_FONT, new File("lunchds.ttf")).deriveFont(20f);
	}

	//Description: draw payout countdown
	//Paramters: graphics
	//Return: none
	public void payoutCountdown(Graphics g) {
		int x = 10, y = 300, w = 200, h = 150;
		g.setColor(Color.yellow);
		g.fillRect(x, y, w + 20, h + 20);
		g.setColor(Color.black);
		g.fillRect(x + 10, y + 10, w, h);
		g.setColor(Color.yellow);

		g.setFont(instructions);
		g.drawString("Wage", x + 70, y + 45);
		g.drawString("Payout", x + 60, y + 85);

		g.setFont(buttonFont);
		int fontOffset = g.getFontMetrics().stringWidth("" + (Driver.wagePayout/40));
		g.drawString(Driver.wagePayout/40 + "", x + (w - fontOffset)/2 + 10, y + 135);

	}

	//Description: make a switch for the settings menu
	//Parameters: graphics, option text, boolean of thing we want to switch, x and y pos
	//Return: int of whether or not thing has been pressed (0 = not pressed, 1 = switch to true, -1 = switch to false)
	public int switchButton(Graphics g, String option, boolean thing, int xPos, int yPos) {
		g.setFont(instructions);
		int width = g.getFontMetrics().stringWidth(option);
		g.setColor(Color.black);
		g.drawString(option, xPos, yPos + 30);

		//if hovering
		if(Driver.mouseXM >= xPos + width && Driver.mouseXM <= xPos + width + 150 && Driver.mouseYM >= yPos && Driver.mouseYM <= yPos + 60) {
			g.setColor(Color.black);
			g.fillRect(xPos + 20 + 20 + width, yPos - 10, 85, 70);
			g.fillOval(xPos + 10 + width, yPos - 10, 70, 70); // left
			g.fillOval(xPos + 65 + 20 + width, yPos - 10, 70, 70); // right
		}
		if (thing) {//if on
			g.setColor(new Color(95, 186, 76));
			g.fillRect(xPos + 40 + width, yPos - 5, 80, 60);
			g.fillOval(xPos + 15 + width, yPos - 5, 60, 60); // left
			g.fillOval(xPos + 90 + width, yPos - 5, 60, 60); // right

			g.setColor(new Color(194, 255, 179));
			g.fillRect(xPos + 25 + 20 + width, yPos, 75, 50);
			g.fillOval(xPos + 20 + width, yPos, 50, 50); // left
			g.setColor(new Color(95, 186, 76));
			g.fillOval(xPos + 75 + 20 + width, yPos, 50, 50); // right
			g.setFont(smallFont);
			g.drawString("ON", xPos + 35 + width, yPos + 35);

			//if clicked
			if(Driver.mouseXC >= xPos + width && Driver.mouseXC <= xPos + width + 150 && Driver.mouseYC >= yPos && Driver.mouseYC <= yPos + 60) {
				Driver.mouseXC = -1;
				Driver.mouseYC = -1;
				return -1;
			}
		}else {//if off
			g.setColor(new Color(186, 76, 76));
			g.fillRect(xPos + 20 + 20 + width, yPos - 5, 80, 60);
			g.fillOval(xPos - 5 + 20 + width, yPos - 5, 60, 60); // left
			g.fillOval(xPos + 70 + 20 + width, yPos - 5, 60, 60); // right

			g.setColor(new Color(255, 145, 145));
			g.fillRect(xPos + 25 + 20 + width, yPos, 75, 50);
			g.fillOval(xPos + 75 + 20 + width, yPos, 50, 50); // right

			g.setColor(new Color(186, 76, 76));
			g.fillOval(xPos + 20 + width, yPos, 50, 50); // left

			g.setFont(smallFont);
			g.drawString("OFF", xPos + 55 + 20 + width, yPos + 35);

			//if clicked
			if(Driver.mouseXC >= xPos + width && Driver.mouseXC <= xPos + width + 150 && Driver.mouseYC >= yPos && Driver.mouseYC <= yPos + 60) {
				Driver.mouseXC = -1;
				Driver.mouseYC = -1;
				return 1;
			}
		}

		return 0;
	}

	//Description: play noise if sound effects are on
	//Paramters: sound clip
	//Return: none
	public static void noise(Clip clip) {
		if(Driver.soundEffects) {
			clip.setFramePosition(0);
			clip.start();
		}
	}	

	//Description: Draw dialogue
	//Parameters: graphics, dialogue to put in thing, character name
	//Return: none
	public void dialogue(Graphics g, String dialogue, String charaName) {
		int y = 260, x = 230;;
		g.setColor(boldPink);
		g.fillRoundRect(x + 25, y + 120, 900, 250, 50, 50);
		g.setColor(lightPink);
		g.fillRoundRect(x + 35, y + 130, 880, 230, 40, 40);

		nonClickButton(g, charaName, x + 50, y + 105);

		g.setColor(boldPink);
		int firstIndex = dialogue.indexOf('/'), lastIndex = dialogue.lastIndexOf('/');

		if(firstIndex == -1)//of there's only 1 line
			g.drawString(dialogue, x + 50, y + 230);
		else {//multiple lines
			g.drawString(dialogue.substring(0, firstIndex), x + 50, y + 230);//line 1
			if(lastIndex == firstIndex) //if 2 lines
				g.drawString(dialogue.substring(firstIndex + 1), x + 50, y + 280);//line 2
			else {//if 3 lines
				g.drawString(dialogue.substring(firstIndex + 1, lastIndex), x + 50, y + 280);//line 2
				g.drawString(dialogue.substring(lastIndex + 1), x + 50, y + 330);//line 3
			}
		}
	}

	//Description: draw instructions
	//Parameters: graphics, instruction text, x and y pos
	//Return: none
	public void instructions(Graphics g, String instru, int x, int y, Color color) {
		g.setFont(instructions);
		g.setColor(color);
		g.drawString(instru, x, y);
	}

	//Description: draw a button with a specified width
	//Parameters: graphics, button text, x and y pos, width, int to return if clicked
	//Return: int that was specified in thing (ex next scene num)
	public int button(Graphics g, String buttName, int xPos, int yPos, int w, int action) {
		g.setFont(buttonFont);
		int buttW = 53;

		if(Driver.mouseXM >= xPos && Driver.mouseXM <= xPos + w && Driver.mouseYM >= yPos && Driver.mouseYM <= yPos + 70) {//if hovering over button
			g.drawImage(butt1Hover, xPos - 10, yPos - 10, buttW + 10, 90, null);
			g.drawImage(butt2Hover, xPos + buttW, yPos - 10, w - buttW - 10, 90, null);
			g.drawImage(butt3Hover, w + xPos - buttW + 10, yPos - 10, buttW + 10, 90, null);
		} else {//if not hovering
			g.drawImage(butt1Default, xPos, yPos, buttW, 70, null);
			g.drawImage(butt2Default, xPos + buttW, yPos, w - buttW - 10, 70, null);
			g.drawImage(butt3Default, w + xPos - buttW, yPos, buttW, 70, null);
		}

		g.setColor(boldPink);

		int xOffSet = (w - g.getFontMetrics().stringWidth(buttName))/2;

		g.drawString(buttName, xPos + xOffSet, yPos + 48);

		if(Driver.mouseXC >= xPos && Driver.mouseXC <= xPos + w && Driver.mouseYC >= yPos && Driver.mouseYC <= yPos + 70 && Driver.dragging == -1) {//if clicked, change scene
			Driver.mouseXC = -1;
			Driver.mouseYC = -1;
			return action;
		}
		return Driver.scene;
	}

	//Description: draw a button that fits text size exactly
	//Parameters: graphcis, button text, x and y pos, int to return if clicked
	//Return: int that was specified in thing (ex: currently in scene 12, this button returns 13 if clicked)
	public int button(Graphics g, String buttName, int xPos, int yPos, int action) {
		g.setFont(buttonFont);
		int width = g.getFontMetrics().stringWidth(" " + buttName + " ");
		return button(g, " " + buttName + " ", xPos, yPos, width, action);
	}

	//Description: shortcut for back button
	//Parameters: graphics, int for which thing to go back to 
	//Return: int that was specified in thing (ex: currently in scene 12, go back to scene 11)
	public int back(Graphics g, int back) {
		return button(g, "< Back", 25, 550, back);
	}

	//Description: draw button that cannot be clicked with a specified width
	//Parameters: graphics, button text, x and y pos, width
	//Return: none
	public void nonClickButton(Graphics g, String buttName, int xPos, int yPos, int width) {
		g.setFont(buttonFont);
		int w = width;
		int buttW = 53;

		g.drawImage(butt1Hover, xPos, yPos, buttW, 70, null);
		g.drawImage(butt2Hover, xPos + buttW, yPos, w - buttW - 10, 70, null);
		g.drawImage(butt3Hover, w + xPos - buttW, yPos, buttW, 70, null);

		g.setColor(boldPink);

		int xOffSet = (width - g.getFontMetrics().stringWidth(buttName))/2;

		g.drawString(buttName, xPos + xOffSet, yPos + 48);
	}

	//Description: draw button that cannot be clicked that is the size of the text
	//Parameters: graphics, button text, x and y pos
	//Return: none
	public void nonClickButton(Graphics g, String buttName, int xPos, int yPos) {
		g.setFont(buttonFont);
		int width = g.getFontMetrics().stringWidth(" " + buttName + " ");

		nonClickButton(g, " " + buttName + " ", xPos, yPos, width);
	}

	//Description: Draw list of guards that we put on map
	//Parameters: graphics
	//Return: Which guard we click on
	public int guardList(Graphics g) {
		int height = 460, width = 150, y = 180, x = 1000;

		if(Driver.guards.size() <= 4)
			width = 130;

		if(Driver.mouseXM >= x && Driver.mouseXM <= x + width && Driver.mouseYM >= y && Driver.mouseYM <= y + height) {
			g.setColor(Color.gray);
			g.fillRect(x - 5, y - 5, width + 10, height + 10);
			if(Driver.scroll == -1 && scrollOffset < Driver.guards.size() - 4)//down
				scrollOffset++;
			else if(Driver.scroll == 1 && scrollOffset > 0)//up
				scrollOffset--;
		}

		g.setColor(Color.black);
		g.fillRect(x, y, width, height);

		if(Driver.guards.size() > 4) {
			g.setColor(Color.gray);
			int barSize = (height - 40)/(Driver.guards.size() - 4 + 1);
			g.fillRoundRect(x + width - 14, y + 10 + barSize * scrollOffset, 10, barSize, 20, 20);
		}		


		for(int i = 0; i < Math.min(4, Driver.guards.size()); i++) {
			if(Driver.mouseXM >= x + 20 && Driver.mouseXM <= x + 110 && Driver.mouseYM >= y  + i * 110 + 20 && Driver.mouseYM <= y + i * 110 + 110) {
				g.setColor(Color.gray);
				g.fillRect(x - 160, y + i * 110 + 15, 275, 100);
				g.setColor(Color.white);
				g.setFont(smallFont);
				g.drawString(Driver.guards.get(i + scrollOffset).name, x - 155, y + i * 110 + 40);
				
				if(Driver.guards.get(i + scrollOffset).getPlaceable() <= 0) {
					g.drawString("HP:" + Driver.guards.get(i + scrollOffset).getHP() + " Str:" + Driver.guards.get(i + scrollOffset).getStrength(), x - 155, y + i * 110 + 70);
					g.drawString("$" + Driver.guards.get(i + scrollOffset).wage + "/hr", x - 155, y + i * 110 + 100);

					if(Driver.dragging == 1){//if dragging
						g.drawImage(Driver.guards.get(i + scrollOffset).profile, Driver.mouseXC, Driver.mouseYC, Block.blockSize, Block.blockSize, null);
						//else if deploying guard
					} else if(Driver.mouseXC >= Block.mapX && Driver.mouseXC <= Block.mapX + Block.blockSize * 12 && Driver.mouseYC >= Block.mapY && Driver.mouseYC <= Block.mapY + Block.blockSize * 7) {
						//if space is not occupied
						if(Driver.map[(Driver.mouseXC - Block.mapX)/Block.blockSize][(Driver.mouseYC - Block.mapY)/Block.blockSize] == null){
							Guard temp = Driver.guards.remove(i + scrollOffset);
							temp.setPosition((Driver.mouseXC - Block.mapX)/Block.blockSize * Block.blockSize + Block.mapX, (Driver.mouseYC - Block.mapY)/Block.blockSize * Block.blockSize + Block.mapY);
							Driver.inGameMonsters.add(temp);
							Driver.pr -= 0.01;
						}
						if(scrollOffset > 0)
							scrollOffset--;
						if(Driver.guards.size() == 0)
							break;
						Driver.mouseXC = -1;
						Driver.mouseYC = -1;
					}
				} else {
					g.setColor(Color.yellow);
					g.drawString("Deployable in: " + (Driver.guards.get(i + scrollOffset).getPlaceable()/40), x - 155, y + i * 110 + 70);
				}

			}
			if(Driver.guards.get(i + scrollOffset).getPlaceable() > 0)
				Driver.guards.get(i + scrollOffset).setPlaceable();
			g.drawImage(Driver.guards.get(i + scrollOffset).profile, x + 20, y + i * 110 + 20, 90, 90, null);
		}

		if(Driver.guards.size() == 0) {
			g.setFont(smallFont);
			g.setColor(Color.gray);
			g.drawString("No", x + 50, y + height/2 - 60);
			g.drawString("guards", x + 30,  y + height/2 - 30);
			g.drawString("on", x + 50,  y + height/2);
			g.drawString("standby.", x + 20,  y + height/2 + 30);
		}

		return -1;
	}

	//random stuff that I copied from you
	public void run() {
		System.out.println("Thread: Starting thread");
		initialize();
		while(true) {
			update();
			this.repaint();
			try {
				Thread.sleep(1000/FPS);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}		
	}

	public void update() {
		timeElapsed = System.currentTimeMillis() - startTime;
		frameCount++;
	}

	public void initialize() {
		System.out.println("Thread: Initializing game");
		startTime = System.currentTimeMillis();
		timeElapsed = 0;
		FPS = 60;
		for(int i = 0; i < 100000; i++) {
			String s = "set up stuff blah blah blah";
			s.toUpperCase();
		}
		System.out.println("Thread: Done initializing game");
	}

}
