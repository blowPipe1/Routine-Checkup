package server;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;


public class Server {

    public Server(int port) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.setExecutor(null);
        server.start();
        System.out.printf("Server started on port %s%n", port);
    }
}
