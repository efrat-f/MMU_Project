package main.java.com.hit.services;

import main.java.com.hit.algorithm.IAlgoCache;
import main.java.com.hit.algorithm.LRUAlgoCacheImpl;
import main.java.com.hit.dao.DaoFileImpl;
import main.java.com.hit.dao.IDao;
import main.java.com.hit.dm.DataModel;
import main.java.com.hit.memory.CacheUnit;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

public class CacheUnitService<T> {
    IAlgoCache<Long, DataModel<T>> IAlg;
    CacheUnit<T> cache;
    private IDao dao;
    private int capacity;
    private int swap;
    private int countDataModels;
    private int countRequest;

    public CacheUnitService() throws FileNotFoundException {
        capacity = 3;
        swap = 0;
        countDataModels = 0;
        countRequest = 0;
        IAlg = new LRUAlgoCacheImpl<>(capacity);
        cache = new CacheUnit<>(IAlg);
        dao = new DaoFileImpl<T>("hardMemory.json");
    }

    /**
     * remove data models from cache and hard memory
     * @param dataModels
     * @return
     */
    public boolean delete(DataModel<T>[] dataModels) {
        countDataModels += dataModels.length;
        countRequest++;
        for (DataModel<T> dataModel: dataModels) {
            try {
                dao.delete(dataModel);
            } catch (IOException e) {
                System.out.println(e);
                return false;
            }
        }
        Long[] ids = new Long[dataModels.length];
        for(int i=0;i<dataModels.length;i++){
            ids[i] = dataModels[i].getId();
        }
        cache.removeDataModels(ids);
        return true;
    }

    /**
     * get data models value, if not in cache search it at hard memory and put it at cache
     * @param dataModels
     * @return data models value
     */
    public DataModel<T>[] get(DataModel<T>[] dataModels){
        countDataModels += dataModels.length;
        countRequest++;
        Long[] ids = new Long[dataModels.length];
        for(int i=0;i<dataModels.length;i++){
            ids[i] = dataModels[i].getId();
        }
        DataModel<T>[] get_arr = cache.getDataModels(ids);
        for(int i=0;i<dataModels.length;i++){
            if(get_arr[i] == null){
                get_arr[i] = (DataModel<T>) dao.find(ids[i]);
                if (get_arr[i] != null) {
                    DataModel<T>[] data = new DataModel[1];
                    System.arraycopy(get_arr, i, data, 0, 1);
                    cache.putDataModels(data);
                }
            }
        }return get_arr;
    }

    /**
     * put data models in cache by algorithm, update remove items in file
     * @param dataModels
     * @return if success
     */
    public boolean update(DataModel<T>[] dataModels){
        countDataModels += dataModels.length;
        countRequest++;
        DataModel<T>[] removedData = cache.putDataModels(dataModels);
        for (DataModel<T> removedItem: removedData) {
            if(removedItem != null) {
                swap++;
                try {
                    dao.save(removedItem);
                } catch (IOException e) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * update all cache when server finish
     * @return if success
     */
    public boolean updated() {
            Map<Long, DataModel<T>> memory = cache.getAlgo().getMemory();
            for (Map.Entry<Long, DataModel<T>> entry: memory.entrySet()) {
                try {
                    dao.save(entry.getValue());
                } catch (IOException e) {
                    return false;
                }
        }return true;
    }

    /**
     * @return statistics
     */
    public String statistics() {
        return "Capacity: " + capacity + "\n" +
                "Algorithm: LRU" + "\n" +
                "Total number of request: " + countRequest + "\n" +
                "Total number of DataModels (GET/UPDATE/DELETE):" + countDataModels + "\n" +
                "Total number of DataModels swap: " + swap;
    }
}
