package main.java.com.hit.util;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.util.Locale;
import java.util.Scanner;

/**
 * Responsible for the interface with the user in order to run the server and stop it if necessary
 * use given input and output implement
 */
public class CLI implements Runnable{
    private PropertyChangeSupport support;
    private String m_news;
    private Scanner m_in;
    private DataOutputStream m_out;
    private String status;

    public CLI(InputStream in, OutputStream out){
        support = new PropertyChangeSupport(this);
        m_in = new Scanner(in);
        m_out = new DataOutputStream(out);
        status = "";
    }
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

    /**
     * if user write start/shutdown send it to server(use property change support)
     */
    @Override
    public void run(){
        String command;
        write("write start/shutDown to start/stop server");
        while(true){
            command = m_in.nextLine().toUpperCase(Locale.ROOT);
            if(status.equals(command)){
                try {
                    throw new Exception("Incorrect command(the status are already given)");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                switch (command) {
                    case "START":
                        write("Starting server...");
                        support.firePropertyChange("news", status, command);
                        status = command;
                        break;
                    case "SHUTDOWN":
                        support.firePropertyChange("news", status, command);
                        write("Shutdown server");
                        status = command;
                        break;
                    default:
                        write("Not a valid command");
                        break;
                }
            }
        }
    }

    /**
     * write string to user
     * @param string
     */
    public void write(String string){
        try {
            m_out.writeUTF(string+"\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
