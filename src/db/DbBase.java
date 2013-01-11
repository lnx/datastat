package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import util.FileHelper;
import util.Log;
import util.Parameter;

public class DbBase {

	public static void clearDatabase(String database) {
		List<String> tables = DbQuery.getTableNames(database);
		if (tables.size() > 0) {
			Statement statement = getStatement(database);
			if (statement != null) {
				for (String table : tables) {
					try {
						statement.executeUpdate("drop table " + table);
						Log.info("drop table " + table + " success");
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public static void createTable(String table) {
		if (table != null) {
			Statement statement = getStatement(Parameter.getInstance()
					.getDbDatabase());
			if (statement != null) {
				String[] sqls = FileHelper
						.loadFileGetString("./sql/gwat_template.sql")
						.replaceAll("gwat_template", table).split(";");
				for (String sql : sqls) {
					if (!StringUtils.isBlank(sql)) {
						try {
							statement.executeUpdate(sql);
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
			}
			Log.info("create table " + table + " success");
		}
	}

	public static Statement getStatement(String database) {
		Statement statement = null;
		Connection connection = ConnectionPool.getConnection(database);
		if (connection != null) {
			try {
				statement = connection.createStatement();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			Log.error("losing database " + database + "'s connecting");
		}
		return statement;
	}

	public static PreparedStatement getPreparedStatement(String database,
			String sql) {
		PreparedStatement ps = null;
		Connection connection = ConnectionPool.getConnection(database);
		if (connection != null) {
			try {
				ps = connection.prepareStatement(sql);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			Log.error("losing database " + database + "'s connecting");
		}
		return ps;
	}

}
