package main.java.com.hit.memory;

import main.java.com.hit.algorithm.IAlgoCache;
import main.java.com.hit.dm.DataModel;

public class CacheUnit<T> {

    private IAlgoCache<Long, DataModel<T>> algo;

    public CacheUnit(IAlgoCache<Long, DataModel<T>> algo) {
        this.algo = algo;
    }

    public IAlgoCache<Long, DataModel<T>> getAlgo() {
        return algo;
    }

    /**
     * @param ids
     * @return array of values -data models according given ids
     */
    public DataModel<T>[] getDataModels(Long[] ids){
        DataModel<T>[] dataModels = new DataModel[ids.length];
                for(int i = 0; i< ids.length;i++) {
                    dataModels[i] = algo.getElement(ids[i]);
                }
        return dataModels;
    }

    /**
     * put data models at cache
     * @param dataModels
     * @return array of removes data models from cache by algorithm(or null if not remove)
     */
    public DataModel<T>[] putDataModels(DataModel<T>[] dataModels){
        DataModel<T>[] dataModelsRemove = new DataModel[dataModels.length];
        for(int i=0;i<dataModels.length;i++){
           dataModelsRemove[i] = algo.putElement(dataModels[i].getId(),dataModels[i]);
        }
        return dataModelsRemove;
    }

    /**
     * remove data models from cache according by given ids
     * @param ids
     */
    public void removeDataModels(Long[] ids){
        for(Long id: ids){
            algo.removeElement(id);
        }
    }
}
