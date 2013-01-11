package util;

import org.joda.time.DateTime;

public class Tool {

	public static boolean checkIP(String ip) {
		return ip == null ? false : ip.equals("localhost")
				|| ip.matches("(\\d+\\.){3}\\d+");
	}

	public static boolean checkNumber(String number) {
		return number == null ? false : number
				.matches("^[+-]?(([0-9]+)|([0-9]+\\.?[0-9]+))$");
	}

	public static String getCurrentTime() {
		return new DateTime().toString("yyyy-MM-dd HH:mm:ss");
	}

	public static String getDateTime(long time) {
		return new DateTime(time).toString("yyyy-MM-dd HH:mm:ss");
	}

}
