import org.apache.commons.cli.*;
import java.io.IOException;

public class Iperfer {
    public static void main(String[] args) throws IOException {

        // process your command line args
        // if you want to use the apache commons library
        Options options = new Options();

        Client client = new Client();
        Server server = new Server();

        client.run();
        server.run();
    }
}