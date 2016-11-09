package com.vasiliskavrn.shop.web.beans;

public class Cloth {

    public Cloth() {
    }

    public Cloth(String name, long id) {        
        this.name = name;
        this.id = id;
    }
    
    private String name;
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

 

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
