package web.page;

import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import util.MapFactory;
import web.TemplateReader;

@Path("/guide")
public class Guide {

	@GET
	@Produces("text/html")
	public String running() {
		Map<String, Object> root = MapFactory.newHashMap();
		return TemplateReader.getInstance().readTemplate("guide.html", root);
	}

}
