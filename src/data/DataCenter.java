package data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import util.FileHelper;
import util.ListFactory;
import util.Log;
import util.Parameter;
import data.model.Data;
import db.DbBase;
import db.DbUpdate;

public class DataCenter {

	private static final int batchSize = 1000;

	private static final String dataDir = "data";

	private static double total = 0;

	private static double progress = 0;

	public static double getProgress() {
		double ret = 0;
		if (total != 0) {
			ret = progress / total;
		}
		return ret;
	}

	public static void load() {
		if (FileHelper.isDirectoryExist(dataDir)) {
			DbBase.clearDatabase(Parameter.getInstance().getDbDatabase());
			total = progress = 0;
			try {
				String[] filenames = new File(dataDir).list();
				for (String filename : filenames) {
					total += FileHelper.countLines(dataDir + "/" + filename);
					DbBase.createTable(filename);
				}
				for (String filename : filenames) {
					load(filename);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			Log.error("can not find data directory");
		}
		total = progress = 0;
	}

	private static void load(String filename) throws IOException {
		String filepath = dataDir + "/" + filename;
		Log.info("load " + filepath);
		if (FileHelper.isFileExist(filepath)) {
			FileReader fr = new FileReader(new File(filepath));
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
			progress++;
			while (line != null && !line.contains("@data")) {
				line = br.readLine();
				progress++;
			}
			int count = 0;
			List<Data> dataList = ListFactory.newArrayList();
			while ((line = br.readLine()) != null) {
				Data gwat = new Data(line);
				if (gwat.isCorrect()) {
					dataList.add(gwat);
					count++;
				} else {
					Log.error(filepath + " data format error: "
							+ gwat.getLine());
				}
				if (count >= batchSize) {
					store(filename, dataList);
					dataList.clear();
					count = 0;
				}
				progress++;
			}
			if (dataList.size() > 0) {
				store(filename, dataList);
				dataList.clear();
				count = 0;
			}
			br.close();
			fr.close();
		} else {
			Log.error("can not find " + filepath);
		}
	}

	private static void store(String table, List<Data> dataList) {
		try {
			DbUpdate.updateGWAT(table, dataList);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
