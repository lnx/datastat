package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class FileHelper {

	public static void delete(File file) {
		if (file != null) {
			if (file.isDirectory()) {
				for (File f : file.listFiles()) {
					delete(f);
				}
			}
			file.delete();
			Log.info("delete " + file.getAbsolutePath());
		}
	}

	public static void store(String filepath, List<String> lines) {
		if (lines.size() > 0) {
			try {
				FileWriter fw = new FileWriter(new File(filepath));
				BufferedWriter bw = new BufferedWriter(fw);
				for (String line : lines) {
					bw.append(line + "\n");
				}
				bw.flush();
				bw.close();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static long countLines(String filepath) {
		long count = 0;
		if (isFileExist(filepath)) {
			try {
				FileReader fr = new FileReader(new File(filepath));
				BufferedReader br = new BufferedReader(fr);
				while (br.readLine() != null) {
					count++;
				}
				br.close();
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return count;
	}

	public static boolean isFileExist(String filepath) {
		boolean res = false;
		if (filepath != null) {
			File file = new File(filepath);
			if (file.isFile()) {
				res = true;
			}
		}
		return res;
	}

	public static boolean isDirectoryExist(String filepath) {
		boolean res = false;
		if (filepath != null) {
			File file = new File(filepath);
			if (file.isDirectory()) {
				res = true;
			}
		}
		return res;
	}

	public static String loadFileGetString(String filepath) {
		StringBuilder sb = new StringBuilder();
		try {
			if (isFileExist(filepath)) {
				FileReader fr = new FileReader(filepath);
				BufferedReader br = new BufferedReader(fr);
				for (String line = br.readLine(); line != null; line = br
						.readLine()) {
					sb.append(line + "\n");
				}
				br.close();
				fr.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	public static List<String> loadFileGetList(String filepath) {
		List<String> ret = ListFactory.newArrayList();
		try {
			if (isFileExist(filepath)) {
				FileReader fr = new FileReader(filepath);
				BufferedReader br = new BufferedReader(fr);
				for (String line = br.readLine(); line != null; line = br
						.readLine()) {
					ret.add(line);
				}
				br.close();
				fr.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static Map<String, String> loadProperties(String filepath) {
		Map<String, String> map = MapFactory.newHashMap();
		try {
			if (isFileExist(filepath)) {
				FileReader fr = new FileReader(filepath);
				BufferedReader br = new BufferedReader(fr);
				for (String line = br.readLine(); line != null; line = br
						.readLine()) {
					if (!StringUtils.isBlank(line)) {
						String[] parts = line.trim().split("=");
						if (parts.length == 2 && !StringUtils.isBlank(parts[0])
								&& !StringUtils.isBlank(parts[1])) {
							map.put(parts[0], parts[1]);
						}
					}
				}
				br.close();
				fr.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}

}
