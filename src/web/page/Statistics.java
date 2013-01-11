package web.page;

import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import util.FileHelper;
import util.ListFactory;
import util.MapFactory;
import util.Parameter;
import web.TemplateReader;

import com.google.gson.Gson;

import db.DbQuery;

@Path("/statistics")
public class Statistics {

	@GET
	@Produces("text/html")
	public String statistics() {
		Map<String, Object> root = MapFactory.newHashMap();
		List<String> tableNames = DbQuery.getTableNames(Parameter.getInstance()
				.getDbDatabase());
		root.put("tableNames", tableNames);
		return TemplateReader.getInstance().readTemplate("statistics.html",
				root);
	}

	@GET
	@Path("/{tableName}/{statName}")
	@Produces("text/plain")
	public String getPoints(@PathParam("tableName") String tableName,
			@PathParam("statName") String statName) {
		List<Point> pointList = ListFactory.newArrayList();
		List<String> lines = FileHelper.loadFileGetList("stat/" + tableName
				+ "/" + statName);
		if (lines.size() > 0) {
			for (int i = 1, size = lines.size(); i < size; i++) {
				String[] parts = lines.get(i).split("\t");
				pointList.add(new Point(Double.parseDouble(parts[0]), Double
						.parseDouble(parts[1])));
			}
		}
		return new Gson().toJson(pointList);
	}

	public class Point {

		private final double x;

		private final double y;

		public Point(double x, double y) {
			this.x = x;
			this.y = y;
		}

		public double getX() {
			return x;
		}

		public double getY() {
			return y;
		}

		public String toString() {
			return "(" + x + ", " + y + ")";
		}

	}

}
