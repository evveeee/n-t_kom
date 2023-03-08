package tcpclient;
import java.net.*;
import java.io.*;

public class TCPClient {
    private boolean shutdown;
    private Integer timeout;
    private Integer limit;

    public TCPClient(boolean shutdown, Integer timeout, Integer limit) {
        this.shutdown = shutdown;
        this.timeout = timeout;
        this.limit = limit;
    }

    public byte[] askServer(String hostname, int port, byte [] toServerBytes) throws IOException {
        Socket clientSocket = new Socket(hostname, port);
        OutputStream toServer = clientSocket.getOutputStream();
        InputStream fromServer = clientSocket.getInputStream();
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();

        toServer.write(toServerBytes);

        if(shutdown)
            clientSocket.shutdownOutput();

        byte[] buffer = new byte[1024];
        int bytesRead;
        int bytesReceived = 0;

        if(timeout != null) {
            clientSocket.setSoTimeout(timeout);
            fromServer = new BufferedInputStream(fromServer);
        }

        try {
            while (true) {
                bytesRead = fromServer.read(buffer);
                if (bytesRead == -1)
                    break;
                if ((limit != null) && (bytesReceived == limit))
                    break;
                byteArray.write(buffer, 0, bytesRead);
                bytesReceived += bytesRead;
            }
        } catch(SocketTimeoutException e) {
            //nothing
        }

        clientSocket.close();
        return byteArray.toByteArray();
    }
}
