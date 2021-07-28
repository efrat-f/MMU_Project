package main.java.com.hit.dao;

import com.google.gson.Gson;
import main.java.com.hit.dm.DataModel;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class DaoFileImpl<T> implements IDao<Long, DataModel<T>> {

    private String filePath;
    private int capacity;
    private Map<Long, DataModel<T>> hardMemory;

    public DaoFileImpl(String filePath) throws FileNotFoundException {
        this.filePath = "src/main/resources/" + filePath;
        this.capacity = 1000;
        this.hardMemory = new HashMap<Long, DataModel<T>>(capacity);
        loadDataFile();
    }

    public DaoFileImpl(String filePath, int capacity) throws FileNotFoundException {
        this.filePath = "src/main/resources/" + filePath;
        this.capacity = capacity;
        this.hardMemory = new HashMap<Long, DataModel<T>>(capacity);
        loadDataFile();
    }

    @Override
    public void delete(DataModel<T> entity) throws IOException {
        hardMemory.remove(entity.getId());
        saveDataFile();
        capacity++;
    }

    @Override
    public DataModel<T> find(Long id) {
        return hardMemory.get(id);
    }

    @Override
    public void save(DataModel<T> entity) throws IOException {
        if (capacity != 0) {
            hardMemory.put(entity.getId(), entity);
            saveDataFile();
            capacity--;
            return;
        }
        System.out.print("error! hard memory full!");
    }

    /**
     * save the data in file
     *
     * @throws IOException
     */
    private void saveDataFile() throws IOException {
        Gson gson = new Gson();
        String data = gson.toJson(hardMemory);
        FileWriter fileWriter = new FileWriter(filePath);
        fileWriter.write(data);
        fileWriter.close();

    }

    /**
     * load the data file to memoryMap
     *
     * @throws FileNotFoundException
     */
    private void loadDataFile() throws FileNotFoundException {

        //get the content of the JSON file
        HashMap<String, Object> memoryMap = new Gson().fromJson(new FileReader(this.filePath), HashMap.class);

        if (memoryMap == null) {
            memoryMap = new HashMap<String, Object>(capacity);
        }
        //convert the items to DataModel type
        for (Map.Entry<String, Object> entry : memoryMap.entrySet()) {
            DataModel<T> dataModel = new Gson().fromJson(entry.getValue().toString(), DataModel.class);

            this.hardMemory.put(dataModel.getId(), dataModel);
            capacity--;
        }
    }
}