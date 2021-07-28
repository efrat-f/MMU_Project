package main.java.com.hit.view;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;

/**
 * Responsible the interface with the user in order to send him request
 */
public class CacheUnitView extends Component implements ActionListener {
    private JFileChooser fc;
    private PropertyChangeSupport support;
    private JTextArea textArea;

    public CacheUnitView() {
        this.fc = new JFileChooser();
        support = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl){
        support.addPropertyChangeListener(pcl);
    }
    public void	removePropertyChangeListener(PropertyChangeListener pcl){
        support.removePropertyChangeListener(pcl);
    }

    public void	start(){
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
    private void createAndShowGUI() {
        //Create and set up the window.
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("CacheUnitUi");
        frame.setSize(700, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                support.firePropertyChange("exit", null, null);
            }
        });

        JButton loadButton = new JButton(" 📂 Load a Request ");
        loadButton.setActionCommand("load");
        loadButton.setVerticalTextPosition(AbstractButton.CENTER);
        loadButton.setHorizontalTextPosition(AbstractButton.LEADING); //aka LEFT, for left-to-right locales
        loadButton.setEnabled(true);
        Border border = new LineBorder(Color.CYAN, 2, true);
        loadButton.setBorder(border);
        loadButton.setBackground(Color.WHITE);
        loadButton.setForeground(Color.CYAN);
        loadButton.setOpaque(true);
        loadButton.setFont(new Font(null, Font.PLAIN, 20));

        loadButton.addActionListener(this);
        //BasicOptionPaneUI.ButtonActionListener buttonActionListener = new BasicOptionPaneUI.ButtonActionListener(this);
        JButton showStatisticsButton = new JButton(" 👁‍ Show Statistics ");
        showStatisticsButton.setActionCommand("show");
        showStatisticsButton.setVerticalTextPosition(AbstractButton.CENTER);
        showStatisticsButton.setHorizontalTextPosition(AbstractButton.LEADING); //aka LEFT, for left-to-right locales
        showStatisticsButton.setEnabled(true);
        showStatisticsButton.setBorder(border);
        showStatisticsButton.setBackground(Color.WHITE);
        showStatisticsButton.setForeground(Color.CYAN);
        showStatisticsButton.setOpaque(true);
        showStatisticsButton.setFont(new Font(null, Font.PLAIN, 20));


        showStatisticsButton.addActionListener(this);

        JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout());

        //We create a sub-panel. Notice, that we don't use any layout-manager,
        //Because we want it to use the default FlowLayout
        JPanel subPanel = new JPanel();
        subPanel.add(loadButton);
        subPanel.add(showStatisticsButton);
        subPanel.setBackground(Color.BLACK);
        subPanel.setOpaque(true);


        //Now we simply add it to your main panel.
        panel1.add(subPanel, BorderLayout.NORTH);
        textArea = new JTextArea("");
        panel1.add(textArea, BorderLayout.CENTER);
        textArea.setBackground(Color.cyan);
        textArea.setOpaque(true);
        textArea.setFont(new Font("Arial", Font.PLAIN, 20));

        frame.getContentPane().add(panel1);
        frame.setVisible(true);
    }

    /**
     * send the type request and file(if necessary) to client, according user asks
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()){
            case "load":
                fc.setCurrentDirectory(new File("src/main/resources"));
                FileNameExtensionFilter f = new FileNameExtensionFilter("JSON FILES", "json","text"); // here you choose which files to show
                fc.setFileFilter(f);
                int i = fc.showOpenDialog(this);
                if(i == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    support.firePropertyChange("load", null, file);
                }
                break;
            case "show":
                support.firePropertyChange("show", null, null);
                break;
            default:
                break;
        }
    }

    /**
     * show data to client
     * @param t
     * @param <T> type of data
     */
   public <T> void updateUIData(T t) {
       if ("true".equals(t)) {
           textArea.setText("success");
       } else if ("false".equals(t)) {
           textArea.setText("fail");
       }else if(t.toString().contains("[")){
           Gson gson = new GsonBuilder().setPrettyPrinting().create();
           JsonElement je = JsonParser.parseString((String) t);
           String prettyJsonString = gson.toJson(je);
           textArea.setText(prettyJsonString);
       }else{
           textArea.setText((String) t);
       }
   }
}

