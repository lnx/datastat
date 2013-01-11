package util;

import java.util.HashSet;

public class SetFactory {

	public static <E> HashSet<E> newHashSet() {
		return new HashSet<E>();
	}

}
