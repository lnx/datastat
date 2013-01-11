package web;

import java.io.IOException;
import java.net.URI;
import java.util.Set;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.grizzly.http.server.HttpServer;

import util.Log;
import util.SetFactory;

import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;

public class Server {

	private static final String[] packages = { "web.page" };

	private static final String encoding = "UTF-8";

	private static final String ip = "localhost";

	private static final int port = 1709;

	private static void start() {
		try {
			System.setProperty("file.encoding", encoding);
			String url = "http://" + ip;
			URI uri = UriBuilder.fromUri(url).port(port).build();
			ResourceConfig rc = new PackagesResourceConfig(packages);
			rc.add(new ApplicationHandler());
			HttpServer hs = GrizzlyServerFactory.createHttpServer(uri, rc);
			Log.info("Listening " + url + ":" + port);
			Log.info("Press enter key to exit...");
			System.in.read();
			hs.stop();
			Log.info("Bye!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static class ApplicationHandler extends Application {

		public Set<Class<?>> getClasses() {
			Set<Class<?>> set = SetFactory.newHashSet();
			set.add(web.page.Data.class);
			set.add(web.page.Guide.class);
			set.add(web.page.Index.class);
			set.add(web.page.Load.class);
			set.add(web.page.Statistics.class);
			return set;
		}

	}

	public static void main(String[] args) {
		Server.start();
	}

}
