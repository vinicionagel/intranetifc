package com.core;

public interface IGenericBO<T> {
	
    public boolean save(T obj) throws Throwable;
    
    public boolean remove(T obj) throws Throwable;
    
    public boolean update(T obj) throws Throwable;
}