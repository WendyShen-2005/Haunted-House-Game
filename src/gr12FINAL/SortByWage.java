package gr12FINAL;

import java.util.Comparator;

//Wendy Shen, Jan 27, 2023
//Description: sort candidates by wage
public class SortByWage implements Comparator<Monster>{

	//Description: compare this monster to that monster
	//Paramaters: this monster and that monster
	//Return: which monster is greater
	public int compare(Monster o1, Monster o2) {
		if(o1.wage == o2.wage)
			return 0;
		else if(o1.wage > o2.wage)
			return 1;
		else
			return -1;
	}

}
