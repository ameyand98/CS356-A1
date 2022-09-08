import org.apache.commons.cli.*;
import java.io.IOException;

public class Iperfer {
    public static void main(String[] args) throws IOException {

        // System.out.println(args[2] + "-----------------");
        // System.out.println(args.length + "-----------------");
        // process your command line args
        // if you want to use the apache commons library
        // Options options = new Options();
        Options options = addCLIArgs();
        CommandLineParser parser = new DefaultParser(); 
        CommandLine cmd = null;
        // HelpFormatter formatter = new HelpFormatter();
        try {
            System.out.println("parsing cmds");
            cmd = parser.parse(options, args);
        } catch (Exception e) {
            throwError("Error: parsing inputs");
        }

        checkValidCLIArgs(args);

        // System.out.println(cmd.getOptionValue("port_number") + "********");
        int portNum = Integer.parseInt(cmd.getOptionValue("port_number"));
        if (portNum < 1024 || portNum > 65535) {
            throwError("Error: port number must be in the range 1024 to 65535");
        }

        String runAs = cmd.getOptionValue("client");
        if (runAs == null) {
            // run as server
            Server server = new Server(portNum);
            server.run();
        } else {
            // run as client
            String hostname = cmd.getOptionValue("hostname");
            double timeInSecs = Integer.parseInt(cmd.getOptionValue("time"));
            Client client = new Client(hostname, portNum);
            client.run(timeInSecs);
        }

    }

    public static void throwError(String error) {
        System.out.println(error);
        System.exit(1);
    }

    public static void checkValidCLIArgs(String[] args) {
        if (args[0].equals("-c") || args[0].equals("-s")) {
            if (args[0].equals("-c")) {
                if (args.length == 7) {
                    if (args[1].equals("-h") && args[3].equals("-p") && args[5].equals("-t")) {
                        return;
                    }
                }
            } else {
                if (args.length == 3) {
                    if (args[1].equals("-p")) {
                        return;
                    }
                }
            }
        } 

        // should not reach here
        throwError("Error: invalid arguments");
    }

    public static Options addCLIArgs() {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        Option clientOpt = new Option("c", "client");
        group.addOption(clientOpt);
        Option serverOpt = new Option("s", "server");
        group.addOption(serverOpt);
        options.addOptionGroup(group);

        options.addOption(new Option("h", "hostname", true, "my_host"));
        options.addOption(new Option("t", "time", true, "10"));
        Option port = new Option("p", "port_number", true, "1024");
        port.setRequired(true);
        options.addOption(port);
        return options;
    }
}