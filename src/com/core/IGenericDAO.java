package com.core;

import java.util.List;

public interface IGenericDAO<T>  {  
    /** 
     * @param instance 
     */  
    public boolean save(T instance) throws Throwable;  
      
    /** 
     * @param id 
     * @return 
     */  
    public T findById(long id) throws Throwable;  
      
    /** 
     * @return 
     */  
    public List<T> findAll() throws Throwable;  
      
    /** 
     * @param begin 
     * @param end 
     * @return 
     */  
    public List<T> findAll(Integer begin, Integer end) throws Throwable;  
        
    /** 
     * @param isntance 
     */  
    public boolean remove(T instance) throws Throwable;    
    
    public boolean unique(T instance);
}  