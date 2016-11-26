package server;

import java.io.IOException;

/**
 * Created by Jordan on 11/14/2016.
 */
public class ServerApplication {

    public static final int PORT = 6969; // Ayyyyy lmao

    public static void main(String[] args) throws IOException {
        final Server server = new Server(PORT);
        server.start();
    }

}
