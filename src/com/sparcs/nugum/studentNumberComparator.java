package com.sparcs.nugum;

import java.util.Comparator;

public class studentNumberComparator implements Comparator<Object> {
	@Override
	public int compare(Object object1, Object object2) {
		if (object1 instanceof Person)
			return comparePerson((Person)object1, (Person)object2);
		if (object1 instanceof String) 
			return compareString((String)object1, (String)object2);
		return 0;
	}
	
	private int comparePerson(Person person1, Person person2) {
		if (Integer.parseInt(person1.num) == 0) return 1;
		if (Integer.parseInt(person2.num) == 0) return -1;
		
		if (Integer.parseInt(person1.num) < Integer.parseInt(person2.num)) return -1;
		if (Integer.parseInt(person1.num) > Integer.parseInt(person2.num)) return 1;
		if (person1.name.compareTo(person2.name) > 0) return 1;
		return -1;
	}
	
	private int compareString(String string1, String string2) {
		if (Integer.parseInt(string1) == 0) return 1;
		if (Integer.parseInt(string2) == 0) return -1;
		if (Integer.parseInt(string1) < Integer.parseInt(string2)) return -1;
		else return 1;
	}
}