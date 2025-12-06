package server;
import com.sun.net.httpserver.HttpServer;
import handlers.ScheduleHandler;
import utils.TemplateRenderer;

import java.io.IOException;
import java.net.InetSocketAddress;


public class Server {

    public Server(int port) throws IOException {
        TemplateRenderer renderer = new TemplateRenderer();
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/", new ScheduleHandler(renderer));

        server.setExecutor(null);
        server.start();
        System.out.printf("Server started on port %s%n", port);
    }
}
