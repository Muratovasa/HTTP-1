import java.io.BufferedOutputStream;

public interface Heandler {
    public void handler(Request request, BufferedOutputStream stream);
}
