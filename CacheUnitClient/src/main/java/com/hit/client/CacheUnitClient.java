package main.java.com.hit.client;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class CacheUnitClient {

    Socket socket;
    DataOutputStream writer;
    DataInputStream input;

    public CacheUnitClient() throws IOException {
        this.socket = new Socket("127.0.0.1", 12345);
        this.writer = new DataOutputStream(socket.getOutputStream());
        this.input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
    }

    /**
     * send request to server and accept response
     * @param request
     * @return response
     * @throws IOException
     */
    public String send(String request) throws IOException {
        try {
            writer.writeUTF(request);
            writer.flush();
            StringBuilder sb = new StringBuilder();
            String content = "";
            do{
                content = input.readUTF();
                sb.append(content);
            }
            while (input.available() != 0);
            content = sb.toString();
            return content;


        } catch ( IOException e) {
            try {
                socket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
                writer.close();
                input.close();
                return "fail";
            }
            e.printStackTrace();
            return "fail";
        }
    }

    /**
     * close socket ant in/output
     */
    public void closeSocket() {
        try {
            socket.close();
            writer.close();
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
