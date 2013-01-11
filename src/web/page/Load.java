package web.page;

import java.io.File;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.joda.time.DateTime;

import util.FileHelper;
import util.Log;
import util.MapFactory;
import util.Parameter;
import util.Tool;
import web.TemplateReader;
import data.DataCenter;
import data.DataStat;
import db.DbBase;
import db.DbQuery;

@Path("/load")
public class Load {

	private static final DecimalFormat DF = new DecimalFormat("0.00");

	private static Status status = Status.IDLE;

	private static long loadStartTime = -1;

	@GET
	@Produces("text/html")
	public String load() {
		Map<String, Object> root = MapFactory.newHashMap();
		return TemplateReader.getInstance().readTemplate("load.html", root);
	}

	@GET
	@Path("/status")
	@Produces("text/plain")
	public String status() {
		return status.toString();
	}

	@GET
	@Path("/progress")
	@Produces("text/plain")
	public String progress() {
		return DF.format(DataCenter.getProgress() * 100);
	}

	@GET
	@Path("/info")
	@Produces("text/html")
	public String info() {
		Map<String, Object> root = MapFactory.newHashMap();
		String systemStatus = "等待加载";
		String loadStartTimeStr = "-";
		String progressStr = "-";
		String leftTimeStr = "-";
		if (status == Status.BUSY) {
			systemStatus = "正在加载";
			if (loadStartTime > 0) {
				loadStartTimeStr = Tool.getDateTime(loadStartTime);
			}
			double progress = DataCenter.getProgress();
			progressStr = DF.format(progress * 100) + "%";
			double leftTime = getLeftTime();
			if (leftTime >= 0) {
				if (progress > 0.01) {
					leftTimeStr = DF.format(leftTime) + "min";
				} else {
					leftTimeStr = "预估中...";
				}
			}
		}
		root.put("systemStatus", systemStatus);
		root.put("loadStartTime", loadStartTimeStr);
		root.put("progress", progressStr);
		root.put("leftTime", leftTimeStr);
		return TemplateReader.getInstance()
				.readTemplate("load.info.html", root);
	}

	@GET
	@Path("/metadata")
	@Produces("text/html")
	public String metadata() {
		Map<String, Object> root = MapFactory.newHashMap();
		try {
			List<List<String>> tables = DbQuery.getTableDetails(Parameter
					.getInstance().getDbDatabase());
			if (tables.size() > 0) {
				List<String> tableHeads = tables.remove(0);
				root.put("tableHeads", tableHeads);
				root.put("tableValues", tables);
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return TemplateReader.getInstance().readTemplate("load.metadata.html",
				root);
	}

	@GET
	@Path("/start")
	public void start() {
		if (status == Status.IDLE) {
			new DataStatTask().start();
		}
	}

	private double getLeftTime() {
		double leftTime = -1.0;
		double progress = DataCenter.getProgress();
		if (progress > 0 && progress <= 1) {
			leftTime = (DateTime.now().getMillis() - loadStartTime)
					* (1 - progress) / (progress * 1000 * 60);
		}
		return leftTime;
	}

	private enum Status {
		IDLE, BUSY;
	}

	private class DataStatTask extends Thread {

		public void run() {
			Log.info("data stat task start");
			status = Status.BUSY;
			loadStartTime = DateTime.now().getMillis();
			oldClear();
			DataCenter.load();
			DataStat.analyze(20);
			status = Status.IDLE;
			Log.info("data stat task finish");
		}

		private void oldClear() {
			DbBase.clearDatabase(Parameter.getInstance().getDbDatabase());
			FileHelper.delete(new File("stat"));
		}

	}

}
