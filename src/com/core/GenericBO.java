package com.core;

import java.io.Serializable;
import java.util.List;

public abstract class GenericBO<T> implements IGenericBO<T>, Serializable {

    private final Class typeClass;
    private GenericDAO<T> dao;
    
    public GenericBO() {
        typeClass = null;
        dao = new GenericDAO<>(typeClass);
    }    
    
    public GenericBO(Class argClass) {
        typeClass = argClass;
        dao = new GenericDAO<>(typeClass);
    }

    @Override
    public boolean save(T obj)  {        
        return dao.save(obj);
    }

    @Override
    public boolean update(T obj)  {        
        return dao.update(obj);
    }

    @Override
    public boolean remove(T obj)  {        
        return dao.remove(obj);
    }

    public boolean unique(T obj){        
        return dao.unique(obj);
    }
    
    public List<T> findAll()  {        
        return dao.findAll();
    }

    public T findById(long id)  {        
        return dao.findById(id);
    }        
}