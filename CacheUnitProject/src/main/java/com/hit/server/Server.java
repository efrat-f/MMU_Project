package main.java.com.hit.server;

import main.java.com.hit.services.CacheUnitController;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Server<T> implements Runnable, PropertyChangeListener {
    private ServerSocket serverSocket;
    Executor executor;

    public Server(){
        serverSocket = null;
        executor = Executors. newCachedThreadPool();
    }

    /**
     * if evt is START starting server socket
     * if evt is SHUTDOWN close server socket
     * @param evt
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String status = ((String)evt.getNewValue());
        switch (status) {
            case "START":
                try {
                    serverSocket = new ServerSocket(12345);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                new Thread(this).start();
                break;
            case "SHUTDOWN":
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    /**
     * waiting for connection from client, then create thread of handle request and socket for it
     */
    @Override
    public void run() {
        while (!serverSocket.isClosed()) {
            Socket socket = null;
            try {
                // socket object to receive incoming client requests
                try {
                    socket = serverSocket.accept();
                }catch (Exception e){
                    System.out.println("socket accept exception");
                    break;
                }
                System.out.println("A new client is connected : " + socket);

                System.out.println("Assigning new thread for this client");

                // create a new thread object
                Thread handleRequest = new Thread(new HandleRequest<String>(socket, new CacheUnitController<String>()));
                executor.execute (handleRequest);
                // Invoking the start() method
                //handleRequest.start();

            } catch (Exception e) {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
                e.printStackTrace();
            }
        }
    }
}