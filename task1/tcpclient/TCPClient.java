package tcpclient;
import java.net.*;
import java.io.*;

public class TCPClient {
    
    public TCPClient() {
    }

    public byte[] askServer(String hostname, int port, byte [] toServerBytes) throws IOException {
        
        Socket clientSocket = new Socket(hostname, port);
        OutputStream toServer = clientSocket.getOutputStream();
        InputStream fromServer = clientSocket.getInputStream();
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        toServer.write(toServerBytes);

        byte[] buffer = new byte[1024];
        int bytesRead;

        while((bytesRead = fromServer.read(buffer)) != -1){
            byteArray.write(buffer, 0, bytesRead);
        }

        clientSocket.close();
        return byteArray.toByteArray();
    }

    public byte[] askServer(String hostname, int port) throws IOException {
        return askServer(hostname, port, new byte[0]);
    }
}
