package gr12FINAL;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringTokenizer;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;

//Wendy Shen, Jan 12, 2023
//Description: Main program

public class Driver extends UI implements MouseListener, MouseMotionListener, KeyListener, MouseWheelListener{

	Thread thread;
	static int mouseXC = -1, mouseYC = -1;
	static int mouseXM = -1, mouseYM = -1;	
	final static int W = 1200, H = 700;
	static BuildingBlocks[][] map = new BuildingBlocks[12][7];
	static boolean playing = false;

	static int scene = 0;

	static Clip ding, bgMusic, bulletSound, death, goodNoise, no, placeBlock, click;
	static boolean soundEffects = true, bg = true, dialogue = false;

	static int employeeNum = -1, scroll = 0, dragging = 0, dialogueNum = 0;
	//	static boolean dialogue = false;
	static int banditGeneratorFrame = 0, customerGeneratorFrame = 0, banditGenerationNum = 250, customerGenerationNum = 200, wagePayout = 2400, difficulty = 0;

	static int entranceX = -1, entranceY = -1, exitX = -1, exitY = -1, entranceX2, entranceY2;

	static double budget = 300;
	static int spookiness, familyFriendliness, excitement;
	static double pr = 1, baseFee = 15.0, customersMultiplier = 1, paymentMultiplier = 1;

	static HashMap<String, Block> optionBlocks = new HashMap<String, Block>();

	static ArrayList<Monster> candidates = new ArrayList<Monster>();
	static ArrayList<Monster> employees = new ArrayList<Monster>();
	static LinkedList<Guard> guards = new LinkedList<Guard>();
	static ArrayList<Monster> inGameMonsters = new ArrayList<Monster>(); 
	static HashSet<Bandit> bandits = new HashSet<Bandit>();
	static HashMap<String, Customer> customers = new HashMap<String, Customer>();	
	static LinkedList<MoneyText> money = new LinkedList<MoneyText>();

	static String[] names = new String[18239];


	static char[] direction = new char[12 * 7];
	static int carpetLen = 0;

	BasicEmployee kitty;
	ScrollList candidateList = new ScrollList(candidates, 70, 120, 500, 400, 4);
	ScrollList employeeList = new ScrollList(employees, 600, 120, 520, 400, 4);

	static Image grass, titleScreen, dialogueImg, statsExplanation, PRBanditsExplanation, 
	addDeleteExplanation, aboutPage, instructionsPage, statsCheatsheet;

	//Constructor
	public Driver() throws FontFormatException, IOException, UnsupportedAudioFileException, LineUnavailableException{
		BuildingBlocks.setMapXY(240, 100);
		thread = new Thread(this);
		thread.start();

		addMouseListener (this);
		addMouseMotionListener(this);
		addKeyListener(this);
		setFocusable(true);
		addMouseWheelListener(this);

		kitty = new BasicEmployee("SpecialCustomer", "Kitty", 1, 1, 1);

		AudioInputStream in = AudioSystem.getAudioInputStream(new File("ding.wav"));
		ding = AudioSystem.getClip();
		ding.open(in);
		in = AudioSystem.getAudioInputStream(new File("22 Lost, Then Found!.wav"));
		bgMusic = AudioSystem.getClip();
		bgMusic.open(in);
		in = AudioSystem.getAudioInputStream(new File("Bullet.wav"));
		bulletSound = AudioSystem.getClip();
		bulletSound.open(in);
		in = AudioSystem.getAudioInputStream(new File("death.wav"));
		death = AudioSystem.getClip();
		death.open(in);
		in = AudioSystem.getAudioInputStream(new File("coin.wav"));
		goodNoise = AudioSystem.getClip();
		goodNoise.open(in);
		in = AudioSystem.getAudioInputStream(new File("no.wav"));
		no = AudioSystem.getClip();
		no.open(in);
		in = AudioSystem.getAudioInputStream(new File("placeBlock.wav"));
		placeBlock = AudioSystem.getClip();
		placeBlock.open(in);
		in = AudioSystem.getAudioInputStream(new File("click.wav"));
		click = AudioSystem.getClip();
		click.open(in);

		grass = Toolkit.getDefaultToolkit().getImage("grass.png");
		titleScreen = Toolkit.getDefaultToolkit().getImage("TitleScreen.png");
		dialogueImg = Toolkit.getDefaultToolkit().getImage("Dialogue.gif");
		statsExplanation = Toolkit.getDefaultToolkit().getImage("stats.png");
		PRBanditsExplanation = Toolkit.getDefaultToolkit().getImage("PRBanditsExplanation.png");
		addDeleteExplanation = Toolkit.getDefaultToolkit().getImage("addDeleteExplanation.png");
		aboutPage = Toolkit.getDefaultToolkit().getImage("AboutPage.png");
		instructionsPage = Toolkit.getDefaultToolkit().getImage("InstructionsPage.png");
		statsCheatsheet = Toolkit.getDefaultToolkit().getImage("statsCheatsheet.png");

		BufferedReader br = new BufferedReader(new FileReader("startingCandidates.txt"));
		String line, info;
		StringTokenizer stringInfo;

		while((line = br.readLine()) != null) {//importing starting candidates
			stringInfo = new StringTokenizer(line, " ");
			if(!(info = stringInfo.nextToken()).equals("Guard")) 
				candidates.add(new BasicEmployee(info, stringInfo.nextToken(), Integer.parseInt(stringInfo.nextToken()), Integer.parseInt(stringInfo.nextToken()), Integer.parseInt(stringInfo.nextToken())));
			else 
				candidates.add(new Guard(stringInfo.nextToken(), Integer.parseInt(stringInfo.nextToken()), Integer.parseInt(stringInfo.nextToken())));
		}

		br.close();

		br = new BufferedReader(new FileReader("blocks.txt"));
		int j = 0;
		while((line = br.readLine()) != null) {//importing building blocks
			stringInfo = new StringTokenizer(line, " ");
			String blockName = stringInfo.nextToken();
			Block block = new Block(255 + j * (Block.blockSize + 10), 550, false, blockName, Integer.parseInt(stringInfo.nextToken()), Integer.parseInt(stringInfo.nextToken()), Integer.parseInt(stringInfo.nextToken()), Double.parseDouble(stringInfo.nextToken()));
			optionBlocks.put(blockName, block);
			j++;
		}

		br.close();

		br = new BufferedReader(new FileReader("names.txt"));

		for(int i = 0; i < names.length; i++) {//import names (used for customers & new candidates)
			line = br.readLine();
			names[i] = line;
		}

		br.close();

	}

	//Description: draw all monsters + update stats associate with them
	//Parameters: graphics
	//Return: none
	public void drawMonsters(Graphics g) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		Iterator<Bandit> it = bandits.iterator();
		while(it.hasNext()) {//draw bandits
			Bandit thisBandit = it.next();
			if(!thisBandit.isMoving()) {//if bandit reaches exit
				it.remove();
				noise(death);
				pr -= 0.1;
				budget -= 15;
			} else if(thisBandit.getHP() < 0) {//if bandit dead
				it.remove();
				noise(goodNoise);
				pr += 0.05;
			} else//if bandit still alive and walking
				thisBandit.drawMonster(g);
		}

		Iterator<String> it2 = customers.keySet().iterator();
		while(it2.hasNext()) {//draw customers
			Customer thisCustomer = customers.get(it2.next());
			if(thisCustomer.getHP() < 0) {//if dead
				it2.remove();
				noise(death);
				pr -= 0.05;
			} else if(!thisCustomer.isMoving()) {//if reaches exit
				it2.remove();
				noise(goodNoise);
				Double payment = baseFee;
				if(thisCustomer.getHP() >= thisCustomer.getMaxHP() * 0.75) {//determine how much extra they pay
					payment += thisCustomer.calculateTip(spookiness, familyFriendliness, excitement, pr) * paymentMultiplier;
					pr += 0.05;
				}

				money.add(new MoneyText(g, payment));
				budget += payment;
			} else //otherwise, just draw the guy
				thisCustomer.drawMonster(g);
		}
		
		Iterator<Monster> it3 = inGameMonsters.iterator();
		while(it3.hasNext()) {//draw employees
			Monster thisMonster = it3.next();
			if(thisMonster.getHP() < 0) {//if dead
				it3.remove();
				noise(death);
				pr -= 0.05;
			} else {//if not dead
				thisMonster.drawMonster(g);
				if(wagePayout <= 0) {//if it's time to pay
					budget -= thisMonster.getWage() * Monster.getWageMultiplier();
				}
			}
		}

		Iterator<MoneyText> itMoney = money.iterator();
		while(itMoney.hasNext()) {//draw money (when customers pay us)
			MoneyText mon = itMoney.next();
			if(mon.isOffScreen())
				itMoney.remove();
			else
				mon.drawText(g);
		}
	}

	//Description: count num of monsters with specific role in collection
	//Parameters: role we're searching for, collection we want to search through
	//Return: how many monsters with that role there are
	public int numOfRole(String role, Collection <Monster> collect) {
		int num = 0;

		for(Monster mon: collect)
			if(mon.getRole().equals(role))
				num++;

		return num;
	}

	//Description: save map layout to a .txt file
	//Parameters: none
	//Return: none
	public void saveMap() {
		noise(ding);
		try {
			PrintWriter out = new PrintWriter(new FileWriter("mapLayout.txt"));//file to write to
			String printLine = "";
			for(int i = 0; i < map.length; i++) {//for each row
				for(int j = 0; j < map[0].length; j++) //for each column
					if(map[i][j] == null)
						printLine += "null ";
					else 
						printLine += map[i][j].getType() + " ";
				out.println(printLine);//save line to file
				printLine = "";
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//Description: load map from mapLayout.txt
	//Parameters: none
	//Return: none
	public void loadMap() throws IOException {
		resetMap();
		noise(ding);
		BufferedReader br = new BufferedReader(new FileReader("mapLayout.txt"));
		StringTokenizer tokenizer;
		String block;

		for(int i = 0; i < map.length; i++) {//for each row
			tokenizer = new StringTokenizer(br.readLine(), " ");//divide each word
			for(int j = 0; j < map[0].length; j++) {//for each column
				block = tokenizer.nextToken();//block we are currently looking at
				int x = i * Block.blockSize + Block.mapX;
				int y = j * Block.blockSize + Block.mapY;
				if(block.equals("DoorEntrance")) {
					map[i][j] = new Door(x, y, "Entrance");
					entranceX = x;
					entranceY = y;
					entranceX2 = i;
					entranceY2 = j;
					kitty.setCoordinates(entranceX, entranceY);
				} else if(block.equals("DoorExit")) {
					map[i][j] = new Door(x, y, "Exit");
					exitX = x;
					exitY = y;
				} else if(!block.equals("null")) {
					Block bl = optionBlocks.get(block);
					map[i][j] = new Block(x, y, true, block, bl.getSpook(), bl.getFun(), bl.getExcite(), bl.getCost());
					bl.setStats(1);
				}
			}
		}

		br.close();
	}

	//Description: find all the candidates with the same role (ex: manager) and remove them all
	//Parameters: role to find
	//Return: none
	public void removeRoleCandidates(String role) {
		Iterator<Monster> it = candidates.iterator();
		while(it.hasNext())
			if(it.next().getRole().equals(role))
				it.remove();
	}

	//Description: draw grass background
	//Parameters: graphics
	//Return: none
	public void grassBG(Graphics g) {
		for(int i = 0; i < 3; i++) {//for 3 layers
			int y = i * W/4;
			g.drawImage(grass, 0, y, W/4, W/4, null);
			g.drawImage(grass, W/4, y, W/4, W/4, null);
			g.drawImage(grass, W/2, y, W/4, W/4, null);
			g.drawImage(grass, W/4*3, y, W/4, W/4, null);
		}
	}

	//Description: draw map
	//Parameters: graphics
	//Return: none
	public void drawMap(Graphics g) {
		grassBG(g);

		if(budget <= -50)
			g.setColor(Color.red);
		else
			g.setColor(Color.black);
		g.setFont(buttonFont);
		String budgetString = String.format("$%.2f", budget);
		g.drawString(budgetString, 50, 50);

		g.setColor(Color.black);
		g.fillRect(Block.getMapX(), Block.getMapY(), Block.getBlockSize() * 12, Block.getBlockSize() * 7);
		g.drawString("PR: %" + (int)(pr * 100), 500, 50);
		g.setFont(smallFont);
		g.drawString("Spook: " + spookiness, 50, 105);
		g.drawString("Family-fun: " + familyFriendliness, 50, 160);
		g.drawString("Excitement: " + excitement, 50, 215);

		g.setColor(Color.white);
		for(int i = 0; i < 12; i++)//vertical lines
			g.drawLine(Block.getMapX() + i * Block.getBlockSize(), //x
					Block.getMapY(), //y
					Block.getMapX() + i * Block.getBlockSize(), //x
					Block.getMapY() + Block.getBlockSize() * 7);//y
		for(int i = 0; i < 7; i++) //horizontal lines
			g.drawLine(Block.getMapX(), //x
					Block.getMapY() + i * Block.getBlockSize(), //y
					Block.getMapX() + Block.getBlockSize() * 12, //x
					Block.getMapY() + i * Block.getBlockSize());//y

		for(int i = 0; i < map.length; i++) 
			for(int j = 0; j < map[0].length; j++) 
				if(map[i][j] != null) 
					map[i][j].drawBlock(g);
	}

	//Description: draw map but without all the stats
	//Parameters: graphics, random parameter to differentiate from above
	//Return: none
	public void drawMap(Graphics g, int x) {
		grassBG(g);

		g.setColor(Color.black);
		g.fillRect(Block.getMapX(), Block.getMapY(), Block.getBlockSize() * 12, Block.getBlockSize() * 7);
		g.setColor(Color.white);
		for(int i = 0; i < 12; i++)//vertical lines
			g.drawLine(Block.getMapX() + i * Block.getBlockSize(), //x
					Block.getMapY(), //y
					Block.getMapX() + i * Block.getBlockSize(), //x
					Block.getMapY() + Block.getBlockSize() * 7);//y
		for(int i = 0; i < 7; i++) //horizontal lines
			g.drawLine(Block.getMapX(), //x
					Block.getMapY() + i * Block.getBlockSize(), //y
					Block.getMapX() + Block.getBlockSize() * 12, //x
					Block.getMapY() + i * Block.getBlockSize());//y

		for(int i = 0; i < map.length; i++) 
			for(int j = 0; j < map[0].length; j++) 
				if(map[i][j] != null) 
					map[i][j].drawBlock(g);
	}

	//Description: pick entrance and exit location
	//Parameters: e -- 1 = picking entrance, -1 = exit
	//Return: none
	public void pickEntranceExit(int e) {
		if(e == 1) {
			entranceX = (mouseXM - Block.getMapX())/Block.getBlockSize() * Block.getBlockSize() + Block.getMapX();
			entranceY = (mouseYM - Block.getMapY())/Block.getBlockSize() * Block.getBlockSize() + Block.getMapY();
			scene = 22;
			map[(entranceX - Block.getMapX())/Block.getBlockSize()][(entranceY - Block.getMapY())/Block.getBlockSize()] = new Door(entranceX, entranceY, "Entrance");
		} else {
			exitX = (mouseXM - Block.getMapX())/Block.getBlockSize() * Block.getBlockSize() + Block.getMapX();
			exitY = (mouseYM - Block.getMapY())/Block.getBlockSize() * Block.getBlockSize() + Block.getMapY();
			scene = 22;
			map[(exitX - Block.getMapX())/Block.getBlockSize()][(exitY - Block.getMapY())/Block.getBlockSize()] = new Door(exitX, exitY, "Exit");
		}
	}

	//Description: find index of monsters
	//Parameters: arrayList, monster ID
	//Return: index
	public int indexOfMonster(ArrayList<Monster> list, int monsterID) {
		for(int i = 0 ;i < list.size(); i++) 
			if(list.get(i).getMonsterID() == monsterID)
				return i;
		return -1;
	}

	//Description: reset map
	//Parameters: none
	//Return: none
	public void resetMap() {
		budget = 300;
		spookiness = 0;
		familyFriendliness = 0;
		excitement = 0;
		pr = 1;
		
		for(int i = 0; i < map.length; i++) 
			for(int j = 0; j < map[0].length; j++)
				map[i][j] = null;
		entranceX = -1;
		entranceY = -1;
		entranceX2 = -1;
		entranceY2 = -1;
		exitX = -1;
		exitY = -1;
		difficulty = 0;
		for(int i = 0; i < 12; i++) 
			for(int j = 0; j < 7; j++) 
				if(map[i][j] != null && map[i][j].type.equals("Carpet"))
					map[i][j] = null;
		for(int i = 0; i < direction.length; i++)
			direction[i] = 0;

		Carpet.resetPrevCarpet();

		carpetLen = 0;
		kitty.setCoordinates(entranceX, entranceY);
	}

	//Description: paint component (way better than jcomponents)
	//Parameters: graphics
	//Return: none
	public void paintComponent(Graphics g){
		super.paintComponent(g);

		if(scene != 11 && scene != 30 && scene != 26)//see if we're in a dialogue scene
			dialogueNum = 0;

		if(scene == 21)//see if we can change block layout or no
			Block.setMoveable(true);
		else
			Block.setMoveable(false);

		if(bg)//if music is enabled
			bgMusic.loop(Clip.LOOP_CONTINUOUSLY);
		else {
			bgMusic.setFramePosition(0);
			bgMusic.stop();
		}

		if(scene == 0) {//main menu
			g.drawImage(titleScreen, 0, 0, null);
			g.setColor(Color.yellow);
			g.setFont(buttonFont);
			g.drawString("Haunted Dungeons!", 500, 600);
			playing = false;
			scene = button(g, "New Game", 100, 100, 350, 11);
			if(scene == 11) 
				resetMap();
			try {//if there is already a map layout, we can load it
				FileReader br = new FileReader("MapLayout.txt");
				br.close();
				if(button(g, "Load", 100, 200, 350, 1) == 1) {
					loadMap();
					scene = 23;
				}
			} catch (IOException e) {//otherwise we cannot load it
				nonClickButton(g, "Load", 100, 200, 350);
			}
			if(entranceX != -1 && budget > -100 && inGameMonsters.size() > 0)
				scene = button(g, "Continue", 100, 300, 350, 32);
			else
				nonClickButton(g, "Continue", 100, 300, 350);
			scene = button(g, "About", 100, 400, 350, 13);
			scene = button(g, "Settings", 100, 500, 350, 14);
			scene = button(g, "Exit", 1000, 550, 1);
		} else if(scene == 11) {//start
			if(!dialogue) {
				g.drawImage(dialogueImg, 0, 0, null);
				scene = back(g, 0);
				if(dialogueNum == 0) {
					dialogue(g, "Hello aspiring haunted-house/"
							+ "entrepreneur! My name is Kitty/"
							+ "and I will guide you through", "?");
					if(button(g, "Next", 975, 365, 1) == 1)
						dialogueNum++;
				} else if(dialogueNum == 1) {
					dialogue(g, "how we will make this the best/"
							+ "haunted house ever! We will start/"
							+ "by picking the entrance and exit.", "Kitty");
					scene = button(g, "Start Building!", 750, 365, 20);
					if(scene == 20 && entranceX != -1)
						scene = 21;
				}
			} else
				if(entranceX != -1)
					scene = 21;
				else
					scene = 20;
		} else if(scene == 20) {//picking entrance and exit
			drawMap(g, 1);
			if(entranceX == -1) 
				instructions(g, "First, pick your entrance.", 375, 600, Color.black);
			else 
				instructions(g, "Now, pick your exit.", 410, 600, Color.black);

			g.setColor(boldPink);
			if(mouseXM > Block.getMapX() && 
					mouseXM < Block.getMapX() + Block.getBlockSize() * 12 && 
					mouseYM > Block.getMapY() && 
					mouseYM < Block.getMapY() + Block.getBlockSize() * 7) //if hovering over map
				if(!(mouseXM >= Block.getMapX() + Block.getBlockSize() && 
				mouseXM <= Block.getMapX() + Block.getBlockSize() * 11 && 
				mouseYM >= Block.getMapY() + Block.getBlockSize() && 
				mouseYM <= Block.getMapY() + Block.getBlockSize() * 6)) {//if hovering over edges of map
					g.fillRect((mouseXM - Block.getMapX())/Block.getBlockSize() * Block.getBlockSize() + Block.getMapX(), (mouseYM - Block.getMapY())/Block.getBlockSize() * Block.getBlockSize() + Block.getMapY(), Block.getBlockSize(), Block.getBlockSize());
					if(mouseXC != -1) //if picked entrance/exit
						if(entranceX == -1) //picking entrance
							pickEntranceExit(1);
						else //exit
							pickEntranceExit(-1);
				}
			mouseXC = -1;
			mouseYC = -1;

		} else if(scene == 22) {//confirmation screen for door placement
			drawMap(g, 1);
			if(button(g, "Confirm >", 850, 550, 20) != 22) {//confirm placements
				kitty.setCoordinates(entranceX, entranceY);
				entranceX2 = (entranceX - Block.getMapX())/Block.getBlockSize();
				entranceY2 = (entranceY - Block.getMapY())/Block.getBlockSize();
				if(exitX == -1) {//confirm, go back to previous scene to pick exit
					scene = 20;
					map[entranceX2][entranceY2] = new Door(entranceX, entranceY, "Entrance");
				} else {
					scene = 26;
				}
			} else if(button(g, "< Redo", 625, 550, 20) != 22) {//redo placements
				scene = 20;
				if(exitX == -1) {//if want to redo entrances
					map[(entranceX - Block.getMapX())/Block.getBlockSize()][(entranceY - Block.getMapY())/Block.getBlockSize()] = null;
					entranceX = -1;
					entranceY = -1;
				} else {//if want to redo exit
					map[(exitX - Block.getMapX())/Block.getBlockSize()][(exitY - Block.getMapY())/Block.getBlockSize()] = null;
					exitX = -1;
					exitY = -1;
				}
			}
		} 

		else if(scene == 26) {
			if(!dialogue) {
				if(dialogueNum == 0) {
					g.drawImage(dialogueImg, 0, 0, null);
					dialogue(g, "Great job! Now we will be/"
							+ "placing decorations. Each item/"
							+ "impacts your stats which", "Kitty");
					if(button(g, "Next", 975, 365, 1) == 1)
						dialogueNum++;
				} else if(dialogueNum == 1) {
					g.drawImage(statsExplanation, 0, 0, null);
					dialogue(g, "has different effects. Each/"
							+ "block costs money so be careful! /"
							+ "You have $300 to start.", "Kitty");
					if(button(g, "Next", 975, 365, 1) == 1)
						dialogueNum++;
				} else if(dialogueNum == 2) {
					g.drawImage(PRBanditsExplanation, 0, 0, null);
					dialogue(g, "Also keep track of your PR stat./"
							+ "PR attracts customers & bandits. /"
							+ "Bandits will kill & steal from you.", "Kitty");
					if(button(g, "Next", 975, 365, 1) == 1)
						dialogueNum++;
				} else if(dialogueNum == 3) {
					g.drawImage(addDeleteExplanation, 0, 0, null);
					dialogue(g, "So let's get started! Drag blocks./"
							+ "from the options to place them./"
							+ "Drag them off-screen to delete.", "Kitty");
					scene = button(g, "Start Building!", 750, 365, 21);
				}
			} else {
				scene = 21;
			}
		}
		else if(scene == 21) {//building stuff **actual placing of blocks on map happens in block class
			drawMap(g);
			if(!playing) 
				scene = button(g, "Done!", 985, 550, 175, 23);
			else
				scene = button(g, "Done!", 985, 550, 175, 32);

			scene = back(g, 11);
			g.setColor(Color.black);
			g.fillRect(240, 535, 720, 90);

			Iterator<String> it = optionBlocks.keySet().iterator();
			while(it.hasNext())//draw blocks we can pick from
				optionBlocks.get(it.next()).drawBlock(g);

			if(button(g, "Save", 985, 450, 175, 1) == 1) 
				saveMap();

		} else if(scene == 23) {//drawing out path **ADDING CARPET STUFF IS DOWN IN KEY LISTENERS
			drawMap(g);

			int x = 50, y = 300;
			g.setColor(Color.black);
			g.fillRect(x - 20, y - 40, 200, 200);
			g.setFont(smallFont);
			g.setColor(Color.red);
			g.drawString("A longer path = ", x, y);
			g.drawString("high cost (more", x, y + 30);
			g.drawString("carpet- $1/sqr)", x, y + 60);
			g.setColor(Color.green);
			g.drawString("But raises", x, y + 90);
			g.drawString("excitement", x, y + 120);


			if(button(g, "Rebuild", 20, 550, 20) != 23) {//change layout
				for(int i = 0; i < 12; i++) 
					for(int j = 0; j < 7; j++) 
						if(map[i][j] != null && map[i][j].type.equals("Carpet"))
							map[i][j] = null;
				for(int i = 0; i < direction.length; i++)
					direction[i] = 0;

				Carpet.resetPrevCarpet();

				scene = 21;
				kitty.setCoordinates(entranceX, entranceY);
				carpetLen = 0;
			} else if(button(g, "Reset", 950, 550, 24) != 23)//resets path
				scene = 25;

			instructions(g, "Now, show kitty how to get from", 310, 575, Color.black);
			instructions(g, "here to the exit (use arrow keys)!", 300, 625, Color.black);
			try {
				kitty.drawMonster(g);
			} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
				e.printStackTrace();
			}

		} else if(scene == 24) {//confirmation page for path
			drawMap(g);
			if(button(g, "< Redo Path", 500, 550, 20) != 24) 
				scene = 25;
			else if(button(g, "Confirm >", 850, 550, 20) != 24) 
				scene = 30;
		} else if(scene == 25) {//reset path
			scene = 23;
			for(int i = 0; i < 12; i++) 
				for(int j = 0; j < 7; j++) 
					if(map[i][j] != null && map[i][j].type.equals("Carpet"))
						map[i][j] = null;
			for(int i = 0; i < direction.length; i++)
				direction[i] = 0;

			Carpet.resetPrevCarpet();

			carpetLen = 0;
			kitty.setCoordinates(entranceX, entranceY);
		} else if(scene == 30) {//dialogue
			if(!dialogue) {
				if(dialogueNum == 0) {
					g.drawImage(dialogueImg, 0, 0, null);
					dialogue(g, "Amazing design! You're almost/"
							+ "done! We just need to hire/"
							+ "monsters to run it.", "Kitty");
					if(button(g, "Next", 975, 365, 1) == 1)
						dialogueNum++;
				} else if(dialogueNum == 1) {
					g.drawImage(dialogueImg, 0, 0, null);
					dialogue(g, "Keep in mind you can only have 1/"
							+ "manager, accountant, sales, and/"
							+ "tours person at a time. ", "Kitty");
					if(button(g, "Next", 975, 365, 1) == 1)
						dialogueNum++;
				} else if(dialogueNum == 2) {
					g.drawImage(dialogueImg, 0, 0, null);
					dialogue(g, "But you can have as many/"
							+ "guards as you want. So come/"
							+ "on! Let's start hiring!", "Kitty");
					scene = button(g, "START HIRING!!!1!11!", 600, 365, 31);
				}
			} else
				scene = 31;
		} else if(scene == 31) {
			g.setColor(Color.black);
			g.fillRect(0, 0, W, H);

			//if selected a monster to look at
			if(employeeNum != -1) {

				//type out information
				int index = indexOfMonster(candidates, employeeNum);

				Monster mon = candidates.get(index);
				mon.drawMonsterProfile(g, 70, 120);

				g.setFont(buttonFont);
				g.setColor(Color.green);

				if(!mon.getRole().equals("Guard")) {
					g.drawString("*Charisma raises PR", 625, 120);
					g.drawString("Skill effect: ", 625, 420);
					if(mon.getRole().equals("Manager")) {
						g.drawString("Raises guards'", 625, 490);
						g.drawString("strength", 625, 540);
					} else if(mon.getRole().equals("Acct.")) {
						g.drawString("Lowers block &", 625, 490);
						g.drawString("wage cost", 625, 540);
					} else if(mon.getRole().equals("Sales")) {
						g.drawString("Sells more tickets", 625, 490);
						g.drawString("(more customers)", 625, 540);
					} else
						g.drawString("Raises customer's pay", 625, 490);
				} else {
					g.drawString("Defends customers & ", 625, 420);
					g.drawString("employees from", 625, 470);
					g.drawString("bandits", 625, 520);
				}
				g.setColor(Color.red);

				double totalWage = 0;
				for(Monster mons: employees)
					totalWage += mons.getWage();
				g.drawString("Total wage pay: $" + totalWage, 625, 190);
				g.setColor(Color.yellow);
				g.drawString("Your budget: $" + budget, 625, 260);

				nonClickButton(g, "Profile", 70, 30, 520);

				if(button(g, "Hire", 620, 300, 200, 1) != scene) {
					noise(ding);
					Monster temp = candidates.remove(index);
					employees.add(temp);
					employeeNum = -1;
					if(temp.role.equals("Guard"))
						guards.add((Guard)temp);
					else {
						BasicEmployee basic = (BasicEmployee)temp;
						pr += basic.getCharisma()/100.0;
						if(mon.getRole().equals("Manager")) {
							Guard.setStrengthMultiplier(1 + (basic.getSkill()/30.0));
							inGameMonsters.add(basic);
							basic.setXY(1030, 100);
							removeRoleCandidates("Manager");
						} else if(mon.getRole().equals("Acct.")) {
							BuildingBlocks.setCostMultiplier((50 - basic.getSkill())/50.0);//smaller % to decrease cost
							Monster.setWageMultiplier((50 - basic.getSkill())/50.0);//smaller % to decrease wage
							inGameMonsters.add(basic);
							basic.setXY(10, 500);
							removeRoleCandidates("Acct.");
						} else if(mon.getRole().equals("Sales")) {
							customersMultiplier = ((50 - basic.getSkill())/50.0);//smaller % to decrease intervals
							inGameMonsters.add(basic);
							basic.setXY(entranceX, entranceY);
							removeRoleCandidates("Sales");
						} else {
							paymentMultiplier = 1 + (basic.getSkill()/50.0);//raise payments
							inGameMonsters.add(basic);
							basic.setXY(exitX, exitY);
							removeRoleCandidates("Tours");
						}
					}
				} else if(button(g, "Reject", 850, 300, 200, -1) != scene) {
					candidates.remove(index);
					employeeNum = -1;
				}

				if(back(g, 30) != scene)
					employeeNum = -1;
			} else {//if selecting employee to hire
				scene = back(g, 30);

				if(inGameMonsters.size() > 0)
					scene = button(g, "Next", 1000, 550, 32);
				else {
					g.setColor(Color.yellow);
					g.setFont(smallFont);
					g.drawString("*Hire at least 1 non-guard employee to run your dungeon", 500, 600);
				}

				if(scene == 32) {
					playing = true;
				}

				nonClickButton(g, "Candidates", 70, 30, 500);

				int tempInt = candidateList.drawSrollList(g);


				g.setColor(Color.white);
				g.drawString("Sort by:", 600, 175);

				if(button(g, "Name (A-Z)", 600, 200, 500, 1) == 1)
					Collections.sort(candidates);
				else if(button(g, "Role/Job (A-Z)", 600, 300, 500, 1) == 1)
					Collections.sort(candidates, new SortByRole());
				else if(button(g, "Wage (low -> high)", 600, 400, 500, 1) == 1)
					Collections.sort(candidates, new SortByWage());

				if(tempInt != -1) 
					for(int i = 0; i < candidates.size(); i++) 
						if(candidates.get(i).getMonsterID() + scene + 1 == tempInt) {
							employeeNum = tempInt - 1 - scene;
							break;
						}
			}

		} else if(scene == 32) {//ACTUAL GAME PLAY
			difficulty++;
			wagePayout--;
			banditGeneratorFrame++;
			customerGeneratorFrame++;

			drawMap(g);

			scene = button(g, "Main Menu", 850, 10, 0);

			if(banditGeneratorFrame >= banditGenerationNum) {//make new bandits on random intervals
				try {
					bandits.add(new Bandit());
				} catch (FontFormatException | IOException e) {
					e.printStackTrace();
				}
				banditGeneratorFrame = 0;
				banditGenerationNum = (int)(Math.random() * (1000 - pr * 500)) + 1000 - difficulty/10;
			}

			if(customerGeneratorFrame >= customerGenerationNum) {//make new customers on random intervals
				int customerName = (int)(Math.random() * names.length);
				try {
					customers.put(names[customerName], new Customer(names[customerName]));
				} catch (IOException | FontFormatException e) {
					e.printStackTrace();
				}
				customerGeneratorFrame = 0;
				customerGenerationNum = (int)(Math.random() * (500 - pr * 200)) + 500;
				customerGenerationNum *= customersMultiplier;
			}

			guardList(g);
			try {
				drawMonsters(g);
			} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
				e.printStackTrace();
			}

			if(wagePayout <= 0) 
				wagePayout = 2400;
			payoutCountdown(g);

			if(button(g, "Hire", 275, 565, 300, 1) == 1) {//make new candidates if not enough candidates
				scene = 31;
				//make new guards
				if(guards.size() <= 1 && numOfRole("Guard", candidates) <= 1)
					for(int i = 0; i < 5; i++) {
						String name = names[(int)(Math.random() * names.length)];
						Monster mon;
						try {
							mon = new Guard(name, (int)(((int)(Math.random() * 5) + 1) * pr), (int)(((int)(Math.random() * 10) + 1) * pr));
							candidates.add(mon);
						} catch (IOException | FontFormatException e) {
							e.printStackTrace();
						}
					}
				else {//make new basic employees
					String[] roles = {"Manager", "Acct.", "Sales", "Tours"};
					for(String role: roles) 
						if(numOfRole(role, inGameMonsters) <= 1 && numOfRole(role, candidates) <= 1) 
							for(int i = 0; i < 5; i++) {
								String name = names[(int)(Math.random() * names.length)];
								try {
									int hp = (int)(Math.random() * 10) + 1;
									hp = (int)(hp * pr);
									int charisma = (int)(Math.random() * 10) + 1;
									charisma = (int)(charisma * pr);
									int skill = (int)(Math.random() * 10) + 1;
									skill = (int)(skill * pr);
									Monster mon = new BasicEmployee(role, name, hp, charisma, skill);
									candidates.add(mon);
								} catch (IOException | FontFormatException e) {
									e.printStackTrace();
								}
							}
				}
			} else if(button(g, "Renovate", 625, 565, 300, 1) == 1)
				scene = 21;

			if(budget <= -100 || inGameMonsters.size() == 0)
				scene = 33;

		} else if(scene == 33) {//death screen
			drawMap(g);
			g.setColor(Color.red);
			g.fillRect(0, 180, W, 210);
			g.setColor(Color.black);
			g.fillRect(0, 185, W, 200);
			g.setFont(buttonFont);
			g.setColor(Color.red);
			if(budget <= -100) {
				g.drawString("Your dungeon has gone into bankruptcy,", 120, 260);
				g.drawString("you can no longer continue.", 250, 330);
			} else {
				g.drawString("Everyone is dead. No one can continue to", 110, 260);
				g.drawString("run the business.", 390, 330);
			}
			scene = button(g, "Main Menu", 230, 420, 300, 0);
			scene = button(g, "Restart", 630, 420, 300, 0);
			if(scene != 33) {
				budget = 300;
				inGameMonsters.removeAll(inGameMonsters);
			}

		} else if(scene == 12) {//instructions
			g.drawImage(instructionsPage, 0, -100, W, H, null);
			scene = back(g, 13);
			scene = button(g, "Stats cheatsheet", 700, 550, 15);
		} else if(scene == 15) {//stats cheatsheet
			g.drawImage(statsCheatsheet, 0, -50, W, H, null);
			scene = back(g, 12);
		}
		else if (scene == 13) {//about
			g.drawImage(aboutPage, 0, -100, W, H, null);
			scene = back(g, 0);
			scene = button(g, "Instructions", 800, 550, 12);
		} else if (scene == 14) {//settings
			g.drawImage(titleScreen, 0, 0, W, H, null);

			scene = back(g, 0);

			int temp;
			if((temp = switchButton(g, "Music", bg, 100, 100)) == 1)
				bg = true;
			else if(temp == -1)
				bg = false;

			if((temp = switchButton(g, "Sound Effects", soundEffects, 100, 200)) == 1)
				soundEffects = true;
			else if(temp == -1)
				soundEffects = false;

			if((temp = switchButton(g, "Skip Dialogue", dialogue, 100, 300)) == 1)//TODO
				dialogue = true;
			else if(temp == -1)
				dialogue = false;

		} else if(scene == 1)
			System.exit(0);


	}

	public static void main(String[] args) throws FontFormatException, IOException, UnsupportedAudioFileException, LineUnavailableException {
		JFrame frame = new JFrame("Very good game");
		Driver panel = new Driver();
		panel.setPreferredSize (new Dimension(WIDTH, HEIGHT));
		frame.add(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setResizable(false);
		frame.pack();
		frame.setVisible(true);
		frame.setSize(W, H);

		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
		frame.setLocation(x, y);

	}
	@Override
	public void keyTyped(KeyEvent e) {		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if(scene == 23) {//making carpet + telling customers & bandits which directions to go in
			Carpet newCarpet;
			if(key == KeyEvent.VK_UP && kitty.y > Block.getMapY()) {
				if(kitty.x == exitX && kitty.y - Block.getBlockSize() == exitY) {//if we hit exit door
					kitty.setCoordinates(kitty.x, kitty.y - Block.getBlockSize());
					new Carpet(kitty.x, kitty.y, 'u');
					scene = 24;
				} else if(map[(kitty.x - Block.mapX)/Block.getBlockSize()][(kitty.y - Block.getBlockSize() - Block.mapY)/Block.getBlockSize()] == null) {
					kitty.setCoordinates(kitty.x, kitty.y - Block.getBlockSize());
					newCarpet = new Carpet(kitty.x, kitty.y, 'u');
					map[(kitty.x - Block.mapX)/Block.getBlockSize()][(kitty.y - Block.mapY)/Block.getBlockSize()] = newCarpet;
					direction[carpetLen] = 'u';
					carpetLen++;
				}
			} else if(key == KeyEvent.VK_DOWN) {
				if(kitty.x == exitX && kitty.y + Block.getBlockSize() == exitY) {
					kitty.setCoordinates(kitty.x, kitty.y + Block.getBlockSize());
					new Carpet(kitty.x, kitty.y, 'd');
					scene = 24;
				} else if(kitty.y < Block.mapY + Block.blockSize * 6 && map[(kitty.x - Block.mapX)/Block.getBlockSize()][(kitty.y + Block.getBlockSize() - Block.mapY)/Block.getBlockSize()] == null) {
					kitty.setCoordinates(kitty.x, kitty.y + Block.getBlockSize());
					newCarpet = new Carpet(kitty.x, kitty.y, 'd');
					map[(kitty.x - Block.mapX)/Block.getBlockSize()][(kitty.y - Block.mapY)/Block.getBlockSize()] = newCarpet;
					direction[carpetLen] = 'd';
					carpetLen++;
				}
			} else if(key == KeyEvent.VK_LEFT && kitty.x > Block.mapX) {
				if(kitty.x - Block.getBlockSize() == exitX && kitty.y == exitY) {
					kitty.setCoordinates(kitty.x - Block.getBlockSize(), kitty.y);
					new Carpet(kitty.x, kitty.y, 'l');
					scene = 24;
				} else if(map[(kitty.x - Block.getBlockSize() - Block.mapX)/Block.getBlockSize()][(kitty.y - Block.mapY)/Block.getBlockSize()] == null) {
					kitty.setCoordinates(kitty.x - Block.getBlockSize(), kitty.y);
					newCarpet = new Carpet(kitty.x, kitty.y, 'l');
					map[(kitty.x - Block.mapX)/Block.getBlockSize()][(kitty.y - Block.mapY)/Block.getBlockSize()] = newCarpet;
					direction[carpetLen] = 'l';
					carpetLen++;
				}
			} else if(key == KeyEvent.VK_RIGHT && kitty.x < Block.mapX + Block.blockSize * 11) {
				if(kitty.x + Block.getBlockSize() == exitX && kitty.y == exitY) {
					kitty.setCoordinates(kitty.x + Block.getBlockSize(), kitty.y);
					new Carpet(kitty.x, kitty.y, 'r');
					scene = 24;
				} else if(map[(kitty.x + Block.getBlockSize() - Block.mapX)/Block.blockSize][(kitty.y - Block.mapY)/Block.getBlockSize()] == null) {
					kitty.setCoordinates(kitty.x + Block.getBlockSize(), kitty.y);
					newCarpet = new Carpet(kitty.x, kitty.y, 'r');
					map[(kitty.x - Block.mapX)/Block.getBlockSize()][(kitty.y - Block.mapY)/Block.getBlockSize()] = newCarpet;
					direction[carpetLen] = 'r';
					carpetLen++;
				}
			}
		} else {
			if(key == KeyEvent.VK_UP)
				scroll = 1;
			else if(key == KeyEvent.VK_DOWN)
				scroll = -1;
		}

	}
	@Override
	public void keyReleased(KeyEvent e) {		
	}
	@Override
	public void mouseDragged(MouseEvent e) {	
		dragging = 1;
		mouseXC = e.getX();
		mouseYC = e.getY();
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		dragging = -1;
		mouseXM = e.getX();
		mouseYM = e.getY();
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		noise(click);
		dragging = -1;
		mouseXC = e.getX();
		mouseYC = e.getY();
	}
	@Override
	public void mousePressed(MouseEvent e) {
	}
	@Override
	public void mouseReleased(MouseEvent e) {	
		dragging = -1;
	}
	@Override
	public void mouseEntered(MouseEvent e) {		
	}
	@Override
	public void mouseExited(MouseEvent e) {		
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		int event = e.getWheelRotation();
		if(event < 0)
			scroll = 1;
		else if(event > 0)
			scroll = -1;

	}


}
