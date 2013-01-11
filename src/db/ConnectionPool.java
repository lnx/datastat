package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import util.Log;
import util.MapFactory;
import util.Pair;
import util.Parameter;

public class ConnectionPool {

	private static final Parameter P = Parameter.getInstance();

	private static final String IP = P.getDbIp();
	private static final int PORT = P.getDbPort();
	private static final String USERNAME = P.getDbUsername();
	private static final String PASSWORD = P.getDbPassword();

	private static final int POOL_SIZE = 25;
	private static final Map<String, Pair<Connection, Long>> POOL = MapFactory
			.newConcurrentHashMap();

	public static Connection getConnection(String database)
			 {
		Connection connection = null;
		Pair<Connection, Long> pair = POOL.get(database);
		if (pair != null) {
			connection = pair.getA();
			try {
				if (connection != null && !connection.isClosed()) {
					pair.setB(DateTime.now().getMillis());
				} else {
					removeConnection(database);
					connection = connect(database);
					putConnection(database, connection);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			connection = connect(database);
			putConnection(database, connection);
		}
		return connection;
	}

	private static void removeConnection(String database) {
		Pair<Connection, Long> pair = POOL.remove(database);
		if (pair != null) {
			Connection removeConnection = pair.getA();
			if (removeConnection != null) {
				try {
					removeConnection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				removeConnection = null;
			}
			pair = null;
		}
	}

	private static Connection connect(String database)
			 {
		Connection connection = null;
		if (!StringUtils.isBlank(database)) {
			String url = "jdbc:mysql://" + IP + ":" + PORT + "/" + database;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				connection = DriverManager.getConnection(url, USERNAME, PASSWORD);
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
			Log.info("connect to " + url + " success");
		}
		return connection;
	}

	private static void putConnection(String database, Connection connection)
			 {
		try {
			if (!StringUtils.isBlank(database) && connection != null
					&& !connection.isClosed()) {
				while (POOL.size() >= POOL_SIZE) {
					removeConnection(getNotActiveOne());
				}
				POOL.put(database, new Pair<Connection, Long>(connection, DateTime
						.now().getMillis()));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static String getNotActiveOne() {
		String ret = null;
		long oldest = DateTime.now().getMillis();
		for (String name : POOL.keySet()) {
			long lastUsedTime = POOL.get(name).getB();
			if (lastUsedTime <= oldest) {
				ret = name;
				oldest = lastUsedTime;
			}
		}
		return ret;
	}

}
