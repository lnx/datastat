package web.page;

import java.io.File;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import util.MapFactory;
import web.TemplateReader;

@Path("/")
public class Index {

	@GET
	@Produces("text/html")
	public String index() {
		Map<String, Object> root = MapFactory.newHashMap();
		return TemplateReader.getInstance().readTemplate("index.html", root);
	}

	@GET
	@Path("/css/{filename}")
	@Produces("text/css")
	public Response css(@PathParam("filename") String filename) {
		return Response.ok(new File("bootstrap/css/" + filename)).build();
	}

	@GET
	@Path("/js/{filename}")
	@Produces("text/javascript")
	public Response js(@PathParam("filename") String filename) {
		return Response.ok(new File("bootstrap/js/" + filename)).build();
	}

	@GET
	@Path("/image/{filename}")
	@Produces("image/png")
	public Response getViewImage(@PathParam("filename") String filename) {
		return Response.ok(new File("bootstrap/img/" + filename)).build();
	}

}
