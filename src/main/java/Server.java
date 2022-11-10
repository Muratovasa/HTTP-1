import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public Socket start() throws IOException {
        final var serverSocket = new ServerSocket(9999);
        return serverSocket.accept();
    }


    public String[] connect() throws IOException {
        while (true) {
            try {
                var in = new BufferedReader(new InputStreamReader(start().getInputStream()));
                final var requestLine = in.readLine();
                final var parts = requestLine.split(" ");

                if (parts.length != 3) {
                    // just close socket
                    continue;
                }
                return parts;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
