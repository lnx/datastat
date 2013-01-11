package data.model;

public class Data {

	private boolean correct = true;
	private String line;

	private double jobId = -1;
	private double waitTime = -1;
	private double nProcs = -1;
	private double avgCpuTimeUsed = -1;
	private double usedMem = -1;
	private double reqNProcs = -1;
	private double reqTime = -1;
	private double reqMemory = -1;
	private String status = "";
	private String userId = "";
	private String groupId = "";
	private String executableId = "";
	private String queueId = "";
	private String partitionNo = "";
	private String origSiteId = "";
	private String lastRunSiteId = "";
	private String jobStr = "";
	private String jobStrParam = "";
	private double usedNetwork = -1;
	private double usedDisk = -1;
	private String usedRes = "";
	private String reqPlatform = "";
	private double reqNetwork = -1;
	private double reqDisk = -1;
	private String reqRes = "";
	private String vvoid = "";
	private String projId = "";
	private double dayOfWeek = -1;
	private double weekNo = -1;
	private double hourOfDay = -1;
	private double runTime = -1;

	public Data(String line) {
		this.line = line == null ? "" : line;
		String[] parts = line.trim().split(",");
		if (parts.length == 31) {
			try {
				jobId = Double.parseDouble(parts[0]);
				waitTime = Double.parseDouble(parts[1]);
				nProcs = Double.parseDouble(parts[2]);
				avgCpuTimeUsed = Double.parseDouble(parts[3]);
				usedMem = Double.parseDouble(parts[4]);
				reqNProcs = Double.parseDouble(parts[5]);
				reqTime = Double.parseDouble(parts[6]);
				reqMemory = Double.parseDouble(parts[7]);
				status = parts[8];
				userId = parts[9];
				groupId = parts[10];
				executableId = parts[11];
				queueId = parts[12];
				partitionNo = parts[13];
				origSiteId = parts[14];
				lastRunSiteId = parts[15];
				jobStr = parts[16];
				jobStrParam = parts[17];
				usedNetwork = Double.parseDouble(parts[18]);
				usedDisk = Double.parseDouble(parts[19]);
				usedRes = parts[20];
				reqPlatform = parts[21];
				reqNetwork = Double.parseDouble(parts[22]);
				reqDisk = Double.parseDouble(parts[23]);
				reqRes = parts[24];
				vvoid = parts[25];
				projId = parts[26];
				dayOfWeek = Double.parseDouble(parts[27]);
				weekNo = Double.parseDouble(parts[28]);
				hourOfDay = Double.parseDouble(parts[29]);
				runTime = Double.parseDouble(parts[30]);
			} catch (Exception e) {
				correct = false;
			}
		} else {
			correct = false;
		}
	}

	public boolean isCorrect() {
		return correct;
	}

	public String getLine() {
		return line;
	}

	public double getJobId() {
		return jobId;
	}

	public double getWaitTime() {
		return waitTime;
	}

	public double getNProcs() {
		return nProcs;
	}

	public double getAvgCpuTimeUsed() {
		return avgCpuTimeUsed;
	}

	public double getUsedMem() {
		return usedMem;
	}

	public double getReqNProcs() {
		return reqNProcs;
	}

	public double getReqTime() {
		return reqTime;
	}

	public double getReqMemory() {
		return reqMemory;
	}

	public String getStatus() {
		return status;
	}

	public String getUserId() {
		return userId;
	}

	public String getGroupId() {
		return groupId;
	}

	public String getExecutableId() {
		return executableId;
	}

	public String getQueueId() {
		return queueId;
	}

	public String getPartitionNo() {
		return partitionNo;
	}

	public String getOrigSiteId() {
		return origSiteId;
	}

	public String getLastRunSiteId() {
		return lastRunSiteId;
	}

	public String getJobStr() {
		return jobStr;
	}

	public String getJobStrParam() {
		return jobStrParam;
	}

	public double getUsedNetwork() {
		return usedNetwork;
	}

	public double getUsedDisk() {
		return usedDisk;
	}

	public String getUsedRes() {
		return usedRes;
	}

	public String getReqPlatform() {
		return reqPlatform;
	}

	public double getReqNetwork() {
		return reqNetwork;
	}

	public double getReqDisk() {
		return reqDisk;
	}

	public String getReqRes() {
		return reqRes;
	}

	public String getVvoid() {
		return vvoid;
	}

	public String getProjId() {
		return projId;
	}

	public double getDayOfWeek() {
		return dayOfWeek;
	}

	public double getWeekNo() {
		return weekNo;
	}

	public double getHourOfDay() {
		return hourOfDay;
	}

	public double getRunTime() {
		return runTime;
	}

}
