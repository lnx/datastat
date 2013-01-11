package db;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import util.ListFactory;
import util.Tool;

public class DbQuery {

	public static long getTableSize(String database, String table) {
		long ret = 0;
		List<List<String>> tables = query(database, "select count(*) from "
				+ table);
		if (tables.size() > 1 && tables.get(1).size() > 0
				&& Tool.checkNumber(tables.get(1).get(0))) {
			ret = Long.parseLong(tables.get(1).get(0));
		}
		return ret;
	}

	public static double getMax(String database, String table, String item) {
		double ret = 0;
		List<List<String>> tables = query(database, "select max(" + item
				+ ") from " + table);
		if (tables.size() > 1 && tables.get(1).size() > 0
				&& Tool.checkNumber(tables.get(1).get(0))) {
			ret = Double.parseDouble(tables.get(1).get(0));
		}
		return ret;
	}
	
	public static List<List<String>> getTableDetails(String database)
			throws ClassNotFoundException, SQLException {
		List<List<String>> ret = ListFactory.newArrayList();
		List<String> tables = ListFactory.newArrayList();
		List<List<String>> queryList = query(database,
				"show full tables where Table_Type = 'BASE TABLE'");
		for (int i = 1, size = queryList.size(); i < size; i++) {
			tables.add(queryList.get(i).get(0).toString());
		}
		ret.add(Arrays.asList("Table Name", "Engine", "Table Rows",
				"Index Length", "Data Length", "Total Size"));
		for (String table : tables) {
			String sql = "select table_name, engine, table_rows, index_length, data_length, index_length+data_length as total_size from tables where table_schema='"
					+ database + "' and table_name='" + table + "'";
			List<List<String>> infoList = query("information_schema", sql);
			for (int i = 1, size = infoList.size(); i < size; i++) {
				ret.add(infoList.get(i));
			}
		}
		return ret;
	}

	public static List<String> getTableNames(String database) {
		List<String> tableNames = ListFactory.newArrayList();
		List<List<String>> queryList = query(database,
				"show full tables where Table_Type = 'BASE TABLE'");
		for (int i = 1, size = queryList.size(); i < size; i++) {
			tableNames.add(queryList.get(i).get(0).toString());
		}
		return tableNames;
	}

	public static List<List<String>> query(String database, String sql) {
		List<List<String>> ret = ListFactory.newArrayList();
		Statement statement = DbBase.getStatement(database);
		if (statement != null && !StringUtils.isBlank(sql)) {
			try {
				ResultSet rs = statement.executeQuery(sql);
				ResultSetMetaData rsmd = rs.getMetaData();
				List<String> columnList = ListFactory.newArrayList();
				int columnCount = rsmd.getColumnCount();
				for (int i = 1; i <= columnCount; i++) {
					columnList.add(rsmd.getColumnLabel(i));
				}
				ret.add(columnList);
				while (rs.next()) {
					List<String> resultList = ListFactory.newArrayList();
					for (int i = 1; i <= columnCount; i++) {
						resultList.add(rs.getObject(i) == null ? "" : rs
								.getObject(i).toString());
					}
					ret.add(resultList);
				}
				rs.close();
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return ret;
	}

}
