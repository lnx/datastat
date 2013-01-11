package db;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import util.Parameter;
import data.model.Data;

public class DbUpdate {

	public static void updateGWAT(String table, List<Data> dataList)
			throws ClassNotFoundException {
		String sql = "insert into "
				+ table
				+ " (job_id, "
				+ "wait_time, "
				+ "n_procs, "
				+ "avg_cpu_time_used, "
				+ "used_mem, "
				+ "req_n_procs, "
				+ "req_time, "
				+ "req_memory, "
				+ "status, "
				+ "user_id, "
				+ "group_id, "
				+ "executable_id, "
				+ "queue_id, "
				+ "partition_no, "
				+ "orig_site_id, "
				+ "last_run_site_id, "
				+ "job_str, "
				+ "job_str_param, "
				+ "used_network, "
				+ "used_disk, "
				+ "used_res, "
				+ "req_platform, "
				+ "req_network, "
				+ "req_disk, "
				+ "req_res, "
				+ "vvoid, "
				+ "proj_id, "
				+ "day_of_week, "
				+ "week_no, "
				+ "hour_of_day, "
				+ "run_time) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			PreparedStatement ps = DbBase.getPreparedStatement(Parameter
					.getInstance().getDbDatabase(), sql);
			if (ps != null) {
				for (Data data : dataList) {
					ps.setDouble(1, data.getJobId());
					ps.setDouble(2, data.getWaitTime());
					ps.setDouble(3, data.getNProcs());
					ps.setDouble(4, data.getAvgCpuTimeUsed());
					ps.setDouble(5, data.getUsedMem());
					ps.setDouble(6, data.getReqNProcs());
					ps.setDouble(7, data.getReqTime());
					ps.setDouble(8, data.getReqMemory());
					ps.setString(9, data.getStatus());
					ps.setString(10, data.getUserId());
					ps.setString(11, data.getGroupId());
					ps.setString(12, data.getExecutableId());
					ps.setString(13, data.getQueueId());
					ps.setString(14, data.getPartitionNo());
					ps.setString(15, data.getOrigSiteId());
					ps.setString(16, data.getLastRunSiteId());
					ps.setString(17, data.getJobStr());
					ps.setString(18, data.getJobStrParam());
					ps.setDouble(19, data.getUsedNetwork());
					ps.setDouble(20, data.getUsedDisk());
					ps.setString(21, data.getUsedRes());
					ps.setString(22, data.getReqPlatform());
					ps.setDouble(23, data.getReqNetwork());
					ps.setDouble(24, data.getReqDisk());
					ps.setString(25, data.getReqRes());
					ps.setString(26, data.getVvoid());
					ps.setString(27, data.getProjId());
					ps.setDouble(28, data.getDayOfWeek());
					ps.setDouble(29, data.getWeekNo());
					ps.setDouble(30, data.getHourOfDay());
					ps.setDouble(31, data.getRunTime());
					ps.addBatch();
				}
				ps.executeBatch();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
