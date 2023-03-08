import java.net.*;
import java.io.*;
import tcpclient.TCPClient;

public class HTTPAsk {
    public static void main( String[] args) throws IOException {
        
        int portnr = Integer.parseInt(args[0]);
        ServerSocket serverSocket = new ServerSocket(portnr);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            InputStream in = clientSocket.getInputStream();
            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

            StringBuilder requestline = new StringBuilder();
            int read;
            String request;

            while((read = in.read()) != '\n' ) {
                requestline.append((char)read);
            }
            request = requestline.toString();

            if (request != null && request.startsWith("GET /ask")) {
                String queryString = request.substring(request.indexOf('?') + 1, request.indexOf(" HTTP"));

                String[] params = queryString.split("&");
                String hostname = "";
                byte[] toServerBytes = new byte[0];
                String string = "";
                Boolean shutdown = false;
                Integer timeout = null;
                Integer limit = null;
                int port = 0;

                for (String param : params) {
                    String[] pair = param.split("=");
                    if (pair[0].equals("hostname")) {
                        hostname = pair[1];
                    } else if (pair[0].equals("string")) {
                       toServerBytes = pair[1].getBytes();
                    } else if (pair[0].equals("shutdown")) {
                        shutdown = Boolean.parseBoolean(pair[1]);
                    } else if (pair[0].equals("timeout")) {
                        timeout = Integer.parseInt(pair[1]);
                    } else if (pair[0].equals("limit")) {
                        limit = Integer.parseInt(pair[1]);
                    } else if (pair[0].equals("port")) {
                        port = Integer.parseInt(pair[1]);
                    }
                }

                if (!hostname.isEmpty() && port != 0) {
                    TCPClient tcpClient = new tcpclient.TCPClient(shutdown, timeout, limit);
                    byte[] clientBytes = tcpClient.askServer(hostname, port, toServerBytes);
                    String serveroutput = new String(clientBytes);
                    out.writeBytes("HTTP/1.1 200 OK\r\n\r\n");
                    out.writeBytes(serveroutput);
                } else if (hostname.isEmpty() || port == 0) {
                    out.writeBytes("HTTP/1.1 400 Bad Request\r\n");
                }

            } else if(request.startsWith("GET")){
                out.writeBytes("HTTP/1.1 404 Not Found\r\n");
            } else{
                out.writeBytes("HTTP/1.1 400 Bad Request\r\n");
            }
            
            clientSocket.close();
            out.close();
            in.close();
        }
    }
}

