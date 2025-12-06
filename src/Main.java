import server.Server;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            Server server = new Server(8089);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}