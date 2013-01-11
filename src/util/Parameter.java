package util;

import java.util.Map;

public class Parameter {

	public static final String ENC = "UTF-8";

	public static Parameter getInstance() {
		return ParameterHolder.INSTANCE;
	}

	private static class ParameterHolder {
		public static final Parameter INSTANCE = new Parameter();
	}

	private String dbIp = "";
	private int dbPort = -1;
	private String dbUsername = "";
	private String dbPassword = "";
	private String dbDatabase = "";

	private Parameter() {
		loadConfig();
	}

	public String getDbIp() {
		return dbIp;
	}

	public int getDbPort() {
		return dbPort;
	}

	public String getDbUsername() {
		return dbUsername;
	}

	public String getDbPassword() {
		return dbPassword;
	}

	public String getDbDatabase() {
		return dbDatabase;
	}

	private void loadConfig() {
		String filePath = "config.properties";
		Map<String, String> cmap = FileHelper.loadProperties(filePath);
		if (cmap.size() > 0) {
			dbIp = loadStringParam(cmap, "db_ip", filePath);
			dbPort = loadNumberParam(cmap, "db_port", filePath);
			dbUsername = loadStringParam(cmap, "db_username", filePath);
			dbPassword = loadStringParam(cmap, "db_password", filePath);
			dbDatabase = loadStringParam(cmap, "db_database", filePath);
		} else {
			failExit("load " + filePath + " error");
		}
		checkAll();
	}

	private int loadNumberParam(Map<String, String> map, String parameter,
			String filePath) {
		int res = -1;
		if (map.containsKey(parameter) && Tool.checkNumber(map.get(parameter))) {
			res = Integer.parseInt(map.get(parameter));
		} else {
			failExit("read " + parameter + " from " + filePath + " failed");
		}
		return res;
	}

	private String loadStringParam(Map<String, String> map, String parameter,
			String filePath) {
		String res = "";
		if (map.containsKey(parameter)) {
			res = map.get(parameter);
		} else {
			failExit("read " + parameter + " from " + filePath + " failed");
		}
		return res;
	}

	private void checkAll() {
		if (!Tool.checkIP(dbIp)) {
			failExit("invalid ip format for db_ip: " + dbIp);
		}
	}

	private void failExit(String log) {
		Log.error(log);
		System.exit(-1);
	}

}
