package main.java.com.hit.client;

import main.java.com.hit.view.CacheUnitView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class CacheUnitClientObserver implements PropertyChangeListener {

    CacheUnitClient cacheUnitClient;

    public CacheUnitClientObserver() {
        try {
            this.cacheUnitClient = new CacheUnitClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * if evt is load read the file and send match request,
     * else if evt is show send request of show statistic
     * if exit close socket
     * send response to view
     * @param evt
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt){
        String response = null;
        if("load".equals(evt.getPropertyName())) {
            File file = (File) evt.getNewValue();
            try {
                String request = "";
                Scanner myReader = new Scanner(file);
                while (myReader.hasNextLine()) {
                    request += myReader.nextLine();
                }
                myReader.close();
                response = cacheUnitClient.send(request.toString());
                CacheUnitView view=(CacheUnitView) evt.getSource();
            } catch (IOException e) {
                e.printStackTrace();
                response = "fail";
            }
        }else if("show".equals(evt.getPropertyName())){
            try {
                response = cacheUnitClient.send("get statistics");
                CacheUnitView view=(CacheUnitView) evt.getSource();
                view.updateUIData(response);
            } catch (IOException e) {
                e.printStackTrace();
                response = "fail";
            }
        }else if("exit".equals(evt.getPropertyName())){
            cacheUnitClient.closeSocket();
            return;
        }
        CacheUnitView view=(CacheUnitView) evt.getSource();
        view.updateUIData(response);
    }
}
