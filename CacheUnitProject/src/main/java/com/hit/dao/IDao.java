package main.java.com.hit.dao;

import java.io.IOException;

public interface IDao<ID extends java.io.Serializable,T>{

    /**
     * Delete a given entity.
     * @param entity
     * @throws IOException
     */
    void delete(T entity) throws IOException;

    /**
     * find a given id.
     * @param id
     * @return
     */
    T find(ID id);

    /**
     * save a given entity.
     * @param entity
     * @throws IOException
     */
    void save(T entity) throws IOException;

}
