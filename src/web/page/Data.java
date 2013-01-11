package web.page;

import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import util.MapFactory;
import util.Parameter;
import web.TemplateReader;
import db.DbQuery;

@Path("/data")
public class Data {

	private static final int displayNo = 25;

	@GET
	@Produces("text/html")
	public String data() {
		Map<String, Object> root = MapFactory.newHashMap();
		String database = Parameter.getInstance().getDbDatabase();
		List<String> tableNames = DbQuery.getTableNames(database);
		root.put("tableNames", tableNames);
		if (tableNames.size() > 0) {
			root.put("tableName", tableNames.get(0));
			long tableSize = DbQuery.getTableSize(database, tableNames.get(0));
			root.put("tableSize", tableSize);
			List<List<String>> tables = query(tableNames.get(0), 1);
			if (tables.size() > 0) {
				List<String> tableHeads = tables.remove(0);
				root.put("tableHeads", tableHeads);
				root.put("tableValues", tables);
			}
			root.put("page", 1);
			int pageTotal = (int) Math.ceil(tableSize / displayNo);
			root.put("pageTotal", pageTotal < 1 ? 1 : pageTotal);
		}
		return TemplateReader.getInstance().readTemplate("data.html", root);
	}

	@GET
	@Path("/{tableName}")
	@Produces("text/html")
	public String dataQuery(@PathParam("tableName") String tableName,
			@QueryParam(value = "page") int page) {
		Map<String, Object> root = MapFactory.newHashMap();
		String database = Parameter.getInstance().getDbDatabase();
		List<String> tableNames = DbQuery.getTableNames(database);
		root.put("tableNames", tableNames);
		root.put("tableName", tableName);
		long tableSize = DbQuery.getTableSize(database, tableName);
		root.put("tableSize", tableSize);
		int pageTotal = (int) Math.ceil(tableSize / displayNo);
		if (page < 1) {
			page = 1;
		} else if (page > pageTotal) {
			page = pageTotal;
		}
		List<List<String>> tables = query(tableName, page);
		if (tables.size() > 0) {
			List<String> tableHeads = tables.remove(0);
			root.put("tableHeads", tableHeads);
			root.put("tableValues", tables);
		}
		root.put("page", page);
		root.put("pageTotal", pageTotal < 1 ? 1 : pageTotal);
		return TemplateReader.getInstance().readTemplate("data.html", root);
	}

	private List<List<String>> query(String tableName, int page) {
		String sql = "select gwat_id as '#', job_id JID, wait_time WT, n_procs NP, "
				+ "avg_cpu_time_used ACTU, "
				+ "used_mem UM, "
				+ "req_n_procs RNP, "
				+ "req_time RT, "
				+ "req_memory RM, "
				+ "status S, "
				+ "user_id UID, "
				+ "group_id GID, "
				+ "executable_id EID, "
				+ "queue_id QID, "
				+ "partition_no PN, "
				+ "orig_site_id OSID, "
				+ "last_run_site_id LRSID, "
				+ "job_str JS, "
				+ "job_str_param JSP, "
				+ "used_network UN, "
				+ "used_disk UD, "
				+ "used_res UR from "
				+ tableName
				+ " limit " + displayNo * (page - 1) + ", " + displayNo;
		return DbQuery.query(Parameter.getInstance().getDbDatabase(), sql);
	}

}
