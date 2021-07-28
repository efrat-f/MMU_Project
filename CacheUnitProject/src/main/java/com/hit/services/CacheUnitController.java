package main.java.com.hit.services;

import main.java.com.hit.dm.DataModel;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * manager cache unit service calls
 * @param <T>
 */
public class CacheUnitController<T> {
    CacheUnitService cacheUnitService;

    public CacheUnitController() throws FileNotFoundException {
        cacheUnitService = new CacheUnitService<T>();
    }
    public boolean delete(DataModel<T>[] dataModels) throws IOException {
        return cacheUnitService.delete(dataModels);
    }
    public DataModel<T>[] get(DataModel<T>[] dataModels){
        return cacheUnitService.get(dataModels);
    }
    public boolean update(DataModel<T>[] dataModels){
        return cacheUnitService.update(dataModels);
    }

    public void updated() {
        cacheUnitService.updated();
    }
    public String statistics(){
        return cacheUnitService.statistics();
    }
}
