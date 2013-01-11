package util;

public class Log {

	public static void info(String log) {
		System.out.println("[" + Tool.getCurrentTime() + " # info ] " + log);
	}

	public static void warn(String log) {
		System.out.println("[" + Tool.getCurrentTime() + " # warn ] " + log);
	}

	public static void error(String log) {
		System.out.println("[" + Tool.getCurrentTime() + " # error] " + log);
	}

}
