import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start();
        server.putHeandler("GET", "/message", ((request, stream) -> {
            try {
                stream.write(("GET request \r\n" +
                        "parametrs: " + request.getQueryParams() + "\r\n").getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
        server.putHeandler("POST", "/message", new Heandler() {
            @Override
            public void handler(Request request, BufferedOutputStream stream) {
                try {
                    stream.write((request.getBody() + "\r\n" +
                            "parametrs: " + request.getQueryParam("name").getValue() + "\r\n").getBytes(StandardCharsets.UTF_8));
                    System.out.println("POST request");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
