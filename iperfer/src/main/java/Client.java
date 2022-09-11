import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;


public class Client {

    private Socket socket;
    private long numPacketsSent; //num KB sent 
    private DataOutputStream outStream;
    // private PrintWriter inStream;

    // pass constructor values as required
    public Client(String hostName, int port) {
        // set local variables from constructor
        try {
            socket = new Socket(hostName, port);
        }     
        catch(Exception e) {
            System.out.println("Error creating new client socket:" + e);
        }
        try {
            outStream = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            System.out.println("Error creating socket output stream:" + e);
        }
        
    }

    // run your code
    public long run(double timeInSecs)  throws IOException {
        // Socket clientSocket = new Socket();
        double time = timeInSecs * 1000000000;
        boolean finishSending = false;
        numPacketsSent = 0;
        byte[] packet = new byte[1000];
        double elapsedTime = 0;
        long sendStart, sendFinish;

        while (!finishSending) {
            sendStart = System.nanoTime();
            outStream.write(packet, 0, packet.length);
            sendFinish = System.nanoTime();
            elapsedTime += sendFinish - sendStart;
            finishSending = (elapsedTime >= time);
            numPacketsSent++;
        }

        elapsedTime /= 1000000000;
        outStream.close();
        socket.close();

        summarize(elapsedTime);

        return numPacketsSent;
    }

    private void summarize(double elapsedTime) {
        //Convert to KB -> megabits then megabits / secs -> Mbps
        double trafficRate = (8*(double) numPacketsSent/1000.0) / elapsedTime;

        System.out.println("sent=" + numPacketsSent + " KB rate=" + trafficRate + " Mbps");
    }
}