import java.net.*;
import java.io.*;
import tcpclient.TCPClient;

public class ConcHTTPAsk {
    public static void main(String[] args) throws IOException {
        int portnr = Integer.parseInt(args[0]);
        ServerSocket serverSocket = new ServerSocket(portnr);
        while (true) {
            Socket clientSocket = serverSocket.accept();
            MyRunnable r = new MyRunnable(clientSocket);
            new Thread(r).start();
        }
    }
}


