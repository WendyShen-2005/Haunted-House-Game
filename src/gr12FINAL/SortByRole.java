package gr12FINAL;

import java.util.Comparator;

//Wendy Shen, Jan 27, 2023
//Purpose: sort candidates by role
public class SortByRole implements Comparator<Monster> {

	//Description: compare this monster to that monster
	//Paramters: this monster and that monster
	//Return: int for which is greater
	public int compare(Monster o1, Monster o2) {
		return o1.role.compareTo(o2.role);
	}

}
