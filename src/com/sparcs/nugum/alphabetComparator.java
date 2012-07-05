package com.sparcs.nugum;

import java.util.Comparator;

public class alphabetComparator implements Comparator<Object> {
	@Override
	public int compare(Object object1, Object object2) {
		if (object1 instanceof Person)
			return comparePerson((Person)object1, (Person)object2);
		if (object1 instanceof String) 
			return compareString((String)object1, (String)object2);
		return 0;
	}
	private int comparePerson(Person person1, Person person2) {
		return person1.name.compareTo(person2.name);
	}
	private int compareString(String string1, String string2) {
		return string1.compareTo(string2);
	}
}