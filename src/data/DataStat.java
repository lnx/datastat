package data;

import java.io.File;
import java.util.List;

import util.FileHelper;
import util.ListFactory;
import util.Log;
import util.Parameter;
import util.Tool;
import db.DbQuery;

public class DataStat {

	private static final String DATABASE = Parameter.getInstance()
			.getDbDatabase();

	public static void analyze(int splitNumber) {
		if (splitNumber > 0) {
			FileHelper.delete(new File("stat"));
			Log.info("start to analyze");
			new File("stat").mkdir();
			List<String> tableNames = DbQuery.getTableNames(DATABASE);
			for (String tableName : tableNames) {
				new File("stat/" + tableName).mkdir();
				jobsNumber(tableName);
				meanNProcsNumber(tableName);
				meanAvgCpuTimeUsed(tableName);
				meanUsedMem(tableName);
				meanReqTime(tableName);
				meanReqMemory(tableName);
				jobsNumberPerProcessors(tableName);
				jobsNumberPerItem(splitNumber, tableName, "avg_cpu_time_used");
				jobsNumberPerItem(splitNumber, tableName, "used_mem");
				jobsNumberPerItem(splitNumber, tableName, "req_time");
				jobsNumberPerItem(splitNumber, tableName, "req_memory");
				meanAccuracy(tableName);
			}
			Log.info("analyze finish");
		} else {
			Log.error("interval number must be positive");
		}
	}

	private static void jobsNumber(String tableName) {
		analyze("select day_of_week, count(*) jobs_number from " + tableName
				+ " group by day_of_week", "stat/" + tableName
				+ "/jobs_number_day_of_week");
		analyze("select hour_of_day, count(*) jobs_number from " + tableName
				+ " group by hour_of_day", "stat/" + tableName
				+ "/jobs_number_hour_of_day");
	}

	private static void meanNProcsNumber(String tableName) {
		analyze("select day_of_week, sum(n_procs) / count(job_id) mean_n_procs from "
				+ tableName + " group by day_of_week", "stat/" + tableName
				+ "/mean_n_procs_day_of_week");
		analyze("select hour_of_day, sum(n_procs) / count(job_id) mean_n_procs from "
				+ tableName + " group by hour_of_day", "stat/" + tableName
				+ "/mean_n_procs_hour_of_day");
	}

	private static void meanAvgCpuTimeUsed(String tableName) {
		analyze("select day_of_week, sum(avg_cpu_time_used) / count(job_id) mean_avg_cpu_time_used from "
				+ tableName + " group by day_of_week", "stat/" + tableName
				+ "/mean_avg_cpu_time_used_day_of_week");
		analyze("select hour_of_day, sum(avg_cpu_time_used) / count(job_id) mean_avg_cpu_time_used from "
				+ tableName + " group by hour_of_day", "stat/" + tableName
				+ "/mean_avg_cpu_time_used_hour_of_day");
	}

	private static void meanUsedMem(String tableName) {
		analyze("select day_of_week, sum(used_mem) / count(job_id) mean_used_mem from "
				+ tableName + " group by day_of_week", "stat/" + tableName
				+ "/mean_used_mem_day_of_week");
		analyze("select hour_of_day, sum(used_mem) / count(job_id) mean_used_mem from "
				+ tableName + " group by hour_of_day", "stat/" + tableName
				+ "/mean_used_mem_hour_of_day");
	}

	private static void meanReqTime(String tableName) {
		analyze("select day_of_week, sum(req_time) / count(job_id) mean_req_time from "
				+ tableName + " group by day_of_week", "stat/" + tableName
				+ "/mean_req_time_day_of_week");
		analyze("select hour_of_day, sum(req_time) / count(job_id) mean_req_time from "
				+ tableName + " group by hour_of_day", "stat/" + tableName
				+ "/mean_req_time_hour_of_day");
	}

	private static void meanReqMemory(String tableName) {
		analyze("select day_of_week, sum(req_memory) / count(job_id) mean_req_memory from "
				+ tableName + " group by day_of_week", "stat/" + tableName
				+ "/mean_req_memory_day_of_week");
		analyze("select hour_of_day, sum(req_memory) / count(job_id) mean_req_memory from "
				+ tableName + " group by hour_of_day", "stat/" + tableName
				+ "/mean_req_memory_hour_of_day");
	}

	private static void jobsNumberPerProcessors(String tableName) {
		analyze("select n_procs, count(job_id) jobs_number from " + tableName
				+ " group by n_procs", "stat/" + tableName
				+ "/jobs_number_per_processors");
	}

	private static void analyze(String sql, String filepath) {
		List<String> analysisResult = ListFactory.newArrayList();
		Log.info("data query: " + sql);
		List<List<String>> queryList = DbQuery.query(DATABASE, sql);
		for (List<String> lineList : queryList) {
			if (lineList.size() > 0) {
				StringBuilder sb = new StringBuilder();
				sb.append(lineList.get(0));
				for (int i = 1, size = lineList.size(); i < size; i++) {
					sb.append("\t" + lineList.get(i));
				}
				analysisResult.add(sb.toString());
			}
		}
		emptyVoid(analysisResult);
		FileHelper.store(filepath, analysisResult);
		Log.info("analysis result stores into " + filepath);
	}

	private static void jobsNumberPerItem(int splitNumber, String tableName,
			String item) {
		double max = DbQuery.getMax(DATABASE, tableName, item);
		if (max < 0) {
			max = 0;
		}
		double section = max / splitNumber;
		List<String> analysisResult = ListFactory.newArrayList();
		analysisResult.add(item + "\tjobs_number");
		if (section > 0) {
			for (int i = 0; i < splitNumber; i++) {
				String sql = "select count(job_id) from " + tableName
						+ " where " + item + " > " + i * section + " and "
						+ item + " <= " + (i + 1) * section;
				Log.info("data query: " + sql);
				List<List<String>> tables = DbQuery.query(DATABASE, sql);
				if (tables.size() > 1 && tables.get(1).size() > 0
						&& Tool.checkNumber(tables.get(1).get(0))) {
					analysisResult.add((i + 1) * section + "\t"
							+ Double.parseDouble(tables.get(1).get(0)));
				}
			}
		}
		String filepath = "stat/" + tableName + "/jobs_number_per_" + item;
		emptyVoid(analysisResult);
		FileHelper.store(filepath, analysisResult);
		Log.info("analysis result stores into " + filepath);
	}

	private static void meanAccuracy(String tableName) {
		String sql = "select sum(abs((req_time - run_time) / run_time)) / count(*) accuracy from "
				+ tableName
				+ " where req_time > 0 and run_time > 0 group by user_id";
		Log.info("data query: " + sql);
		List<List<String>> queryList = DbQuery.query(DATABASE, sql);
		List<String> analysisResult = ListFactory.newArrayList();
		analysisResult.add("user_id\taccuracy");
		for (int i = 1, size = queryList.size(); i < size; i++) {
			analysisResult.add(i + "\t" + queryList.get(i).get(0));
		}
		String filepath = "stat/" + tableName + "/mean_accuracy_of_user";
		emptyVoid(analysisResult);
		FileHelper.store(filepath, analysisResult);
		Log.info("analysis result stores into " + filepath);
	}

	private static void emptyVoid(List<String> analysisResult) {
		int analysisResultSize = analysisResult.size();
		if (analysisResultSize == 0) {
			analysisResult.add("empty\tempty");
			analysisResult.add("0.0\t0.0");
		} else if (analysisResultSize == 1) {
			analysisResult.add("0.0\t0.0");
		}
	}

}
