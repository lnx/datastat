package web;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class TemplateReader {

	private static final String TEMPLATE_DIR = "web";

	public static TemplateReader getInstance() {
		return TemplateReaderHolder.INSTANCE;
	}

	private static class TemplateReaderHolder {
		public static final TemplateReader INSTANCE = new TemplateReader();
	}

	private final Configuration cfg;

	public TemplateReader() {
		cfg = new Configuration();
		try {
			cfg.setObjectWrapper(new DefaultObjectWrapper());
			cfg.setDirectoryForTemplateLoading(new File(TEMPLATE_DIR));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String readTemplate(String templateName, Map<String, Object> root) {
		String res = "";
		try {
			Template template = cfg.getTemplate(templateName);
			StringWriter sw = new StringWriter();
			template.process(root, sw);
			res = sw.getBuffer().toString();
		} catch (IOException | TemplateException e) {
			e.printStackTrace();
		}
		return res;
	}

}
