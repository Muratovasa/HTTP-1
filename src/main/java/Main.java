import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        final var validPaths = List.of("/index.html", "/spring.svg", "/spring.png", "/resources.html", "/styles.css", "/app.js", "/links.html", "/forms.html", "/classic.html", "/events.html", "/events.js");
        Server server = new Server();
        var out = new BufferedOutputStream(server.start().getOutputStream());
        String[] parts = server.connect();
        final var path = parts[1];
        if (!validPaths.contains(path)) {
            out.write((Request.code_404()
            ).getBytes());
            out.flush();
            //continue;    подсвечивает красным говорит вне цикла
        }

        final var filePath = Path.of(".", "public", path);
        final var mimeType = Files.probeContentType(filePath);

        // special case for classic
        if (path.equals("/classic.html")) {
            final var template = Files.readString(filePath);
            final var content = template.replace(
                    "{time}",
                    LocalDateTime.now().toString()
            ).getBytes();
            out.write((Request.code_200(mimeType, content.length)
            ).getBytes());
            out.write(content);
            out.flush();
            //continue;    подсвечивает красным говорит вне цикла
        }

        final var length = Files.size(filePath);
        out.write((Request.code_200(mimeType, length)
        ).getBytes());
        Files.copy(filePath, out);
        out.flush();
    }
}
