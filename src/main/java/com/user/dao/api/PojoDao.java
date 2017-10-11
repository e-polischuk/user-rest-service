package com.user.dao.api;

import java.util.List;

/**
 * Provides flexible CRUD access to configured database for 
 * any mapped POJO that contains special field for primary key.
 * 
 * @author e-polischuk
 *
 * @param <T> type of POJO
 * @param <ID> type of the field that represents POJO's primary key
 */
public interface PojoDao<T, ID> {
    /**
     * By the primary key selects the mapped row that
     * represents POJO's state.
     * 
     * @param id a primary key
     * @return the POJO
     */
    T findOne(ID id);
    
    /**
     * Selects entire mapped table of the POJO.
     * 
     * @return list of all saved POJOs to mapped table
     */
    List<T> findAll();
    
    /**
     * Inserts the POJO's state (except the primary key field)
     * into the mapped table.
     * 
     * @param pojo a POJO with instantiated state
     */
    T save(T pojo);
    
    /**
     * Updates the POJO's state by the primary key in the mapped row.
     * 
     * @param pojo a POJO with changed state
     */
    void update(T pojo);
    
    /**
     * Deletes the mapped row by the primary key.
     * 
     * @param id a primary key of deleting POJO
     */
    void delete(ID id);
    
    /**
     * Closes all available connections to the database.
     */
    void close();
}
