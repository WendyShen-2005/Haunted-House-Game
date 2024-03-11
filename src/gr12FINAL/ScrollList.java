package gr12FINAL;

import java.awt.Graphics;
import java.util.ArrayList;

//Wendy Shen, Jan 19, 2023
//Purpose: store info about each scroll list
public class ScrollList {

	private int x, y, width, height, numOfStuff;
	private ArrayList<Monster> set;
	private int scrollOffset = 0;

	//Constructor
	public ScrollList(ArrayList<Monster> set, int x, int y, int width, int height, int numOfStuff) {
		this.set = set;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.numOfStuff = numOfStuff;
	}

	//Description: draw scroll list
	//Parameters: graphics
	//Return: int of which option we pick
	public int drawSrollList(Graphics g) {
		//show that we're hovering over thing
		if(scroll()) 
			g.setColor(UI.boldPink);
		else
			g.setColor(UI.lightPink);

		//draw box
		g.fillRoundRect(x, y, width, height, 50, 50);
		g.setColor(UI.lightPink);
		g.fillRoundRect(x + 10, y + 10, width - 20, height - 20, 30, 30);

		if(set.size() <= numOfStuff)//if stuff can fit within the box without needing a scroll bar
			for(int i = 0; i < set.size(); i++) {//draw each option
				int returnNum = set.get(i + scrollOffset).monsterOption(g, i, width - 70, x, y);
				if(returnNum != Driver.scene)
					return returnNum;
			}
		else {//if we need scroll bar
			//calculate bar size
			int barSize = (height - 40)/(set.size() - numOfStuff + 1);
			if(barSize < 14)
				barSize = 14;

			//draw bar
			g.setColor(UI.boldPink);
			g.fillRoundRect(x + width - 45, y + 20, 20, height - 40, 20, 20);
			g.setColor(UI.lightPink);
			g.fillRoundRect(x + width - 42, y + barSize * scrollOffset + 24, 14, barSize, 15, 15);

			//draw options
			for(int i = 0; i < numOfStuff; i++) {
				int returnNum = set.get(i + scrollOffset).monsterOption(g, i, width - 100, x, y);
				if(returnNum != Driver.scene)
					return returnNum;
			}
		}
		return -1;
	}

	//Description: if program detects scrolling, change scroll offset
	//Parameters: none
	//Return: if we're hovering on this scroll list
	public boolean scroll() {
		while(scrollOffset + numOfStuff > set.size())
			scrollOffset--;
		if(scrollOffset < 0)
			scrollOffset = 0;
		
		if(Driver.mouseXM >= x && Driver.mouseXM <= x + width && Driver.mouseYM >= y && Driver.mouseYM <= y + height) {
			if(Driver.scroll == -1 && scrollOffset < set.size() - numOfStuff)//down
				scrollOffset++;
			else if(Driver.scroll == 1 && scrollOffset > 0)//up
				scrollOffset--;
			
			Driver.scroll = 0;//reset scroll
			return true;
		}
		return false;
	}
	
}
