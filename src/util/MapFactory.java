package util;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class MapFactory {

	public static <K, V> HashMap<K, V> newHashMap() {
		return new HashMap<K, V>();
	}

	public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap() {
		return new ConcurrentHashMap<K, V>();
	}

}
