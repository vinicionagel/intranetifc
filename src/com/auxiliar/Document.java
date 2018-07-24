package com.auxiliar;

import java.io.Serializable;

public class Document implements Serializable {

    private static final long serialVersionUID = 10000L;

    private String name;
    private String size;
    private String type;
    private String local;   
    private int id;
    public Document() {
    }    
    
    public Document(String name, String size, String type, String local, int id) {
        this.name = name;
        this.size = size;
        this.type = type;
        this.local = local;
        this.id = id;
    }


    public Document(String name, String size, String type,  int id) {
        this.name = name;
        this.size = size;
        this.type = type;
        this.id = id;
    }
        public Document(String name, String size, String type) {
        this.name = name;
        this.size = size;
        this.type = type;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}