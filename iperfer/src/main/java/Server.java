import java.io.IOException;
import java.io.DataInputStream;
import java.net.*;

public class Server {

    private ServerSocket socket;
    private long numPacketsReceived; //num KB received 

    // pass any variables to the constructor
    public Server(int portNum) {
        // set any local variables
        try {
            socket = new ServerSocket(portNum);
        } catch (Exception e) {
            System.out.println("Error creating new server socket on " + str(portNum) + ": " + e);
        }
    }

    // run your server code
    public void run() throws IOException {
        // ServerSocket serverSocket = new ServerSocket();

        boolean connectionAlive = true;
        numPacketsReceived = 0;
        byte[] packet = new byte[1000];
        double elapsedTime = 0;
        long receiveStart, receiveFinish;
        double numBytesReceived = 0;

        try {
            // Block until client requests connection
            Socket client = socket.accept();

            inStream = new DataInputStream(client.getInputStream());
            receiveStart = System.nanoTime();
            while (connectionAlive) {
                numBytesReceived = inStream.read(packet, 0, packet.length);
                connectionAlive = (numBytesReceived != -1);
                numPacketsReceived += numBytesReceived/1000;
            }
            receiveFinish = System.nanoTime();
            
            inStream.close();
            client.close();
            socket.close();
        } catch (Exception e) {
            System.out.println("Failed to read all bytes sent by client connected to port " + str(socket.getPort()) + ": " + e);
        }

        elapsedTime = (double)(receiveFinish - receiveStart) / 1000000000;

        summarize(elapsedTime);
        return numPacketsReceived;
    }

    private void summarize(double elapsedTime) {
        //Convert to KB -> megabits then megabits / secs -> Mbps
        double trafficRate = (8*(double) numPacketsReceived/1000.0) / elapsedTime;

        System.out.println("received=" + str(numPacketsSent) + " KB rate=" + str(trafficRate) + " Mbps");
    }
}
