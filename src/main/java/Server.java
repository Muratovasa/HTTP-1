import org.apache.hc.core5.net.URIBuilder;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    final static int PORT = 6788;
    private final ConcurrentHashMap<String, HashMap<String, Heandler>> handlers = new ConcurrentHashMap<>();

    public void start() throws IOException {
        var countThread = 64;
        final ExecutorService threadPool = Executors.newFixedThreadPool(countThread);
        try (final var serverSocket = new ServerSocket(PORT)) {
            while (true) {
                try {
                    final var socket = serverSocket.accept();
                    connect(socket, threadPool);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void connect(Socket socket, ExecutorService threadPool) {
        threadPool.submit(() -> {
            try (final var in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 final var out = new BufferedOutputStream(socket.getOutputStream());
            ) {
                while (true) {
                    final var requestLine = in.readLine();
                    final var parts = requestLine.split(" ");

                    if (parts.length != 3) {
                        continue;
                    }
                    Request request = new Request(parts[0], parts[1], parts[0].equals("GET") ? null : requestLine, new URIBuilder());
                    handlers.get(request.getMethod()).get(request.getPathRequest()).handler(request, out);
                    out.flush();
                    continue;
                }

            }
        });
    }

    public void putHeandler(String method, String path, Heandler heandler) {
        handlers.put(method, new HashMap<>() {{
            put(path, heandler);
        }});
    }
}
