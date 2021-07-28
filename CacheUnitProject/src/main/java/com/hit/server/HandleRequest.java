package main.java.com.hit.server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import main.java.com.hit.dm.DataModel;
import main.java.com.hit.services.CacheUnitController;

import java.io.*;
import java.lang.reflect.Type;
import java.net.Socket;

public class HandleRequest<T> implements Runnable {

    Socket s;
    CacheUnitController<T> controller;

    public HandleRequest(Socket s, CacheUnitController<T> controller) {
        this.s = s;
        this.controller = controller;
    }

    /**
     * wait request from client, then send him match response till client close socket
     */
    @Override
    public void run() {
        try {
            DataInputStream reader = new DataInputStream(s.getInputStream());
            DataOutputStream writer = new DataOutputStream(s.getOutputStream());
            Gson gson = new Gson();
            try {
                while (!s.isClosed()) {
                    String req = reader.readUTF();
                    if (req.equals("get statistics")) {
                        writer.writeUTF(controller.statistics());
                        writer.flush();
                    } else {
                        Type ref = new TypeToken<Request<DataModel<T>[]>>() {
                        }.getType();
                        Request<DataModel<T>[]> request = new Gson().fromJson(req, ref);
                        switch (request.getHeaders().get("action")) {
                            case "UPDATE":
                                writer.writeUTF(gson.toJson(controller.update(request.getBody())));
                                writer.flush();
                                break;
                            case "GET":
                                writer.writeUTF(gson.toJson(controller.get(request.getBody())));
                                writer.flush();
                                break;
                            case "DELETE":
                                writer.writeUTF(gson.toJson(controller.delete(request.getBody())));
                                writer.flush();
                                break;
                            default:
                                writer.writeUTF(gson.toJson("illegal request"));
                        }
                    }
                }
                reader.close();
                writer.close();
                s.close();
                controller.updated();
            }catch(Exception e){
                reader.close();
                writer.close();
                s.close();
                controller.updated();
                System.out.println("client disconnect");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
