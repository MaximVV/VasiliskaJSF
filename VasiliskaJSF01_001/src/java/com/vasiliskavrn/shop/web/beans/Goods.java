
package com.vasiliskavrn.shop.web.beans;

import java.io.Serializable;


public class Goods implements Serializable{
           
     
    private String article;    
    private String sex;     
    private String name;  
    private String countryMade;    
    private String countryBrand;    
    private String composition;
    private String price;
    private String firme;
    private String color;
    private String size;
    private byte[] image;
    private long id;

    public void setId(long id) {
        this.id = id;
    }
    
    public long getId() {
        return id;
    }
     
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
     
    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCountryMade() {
        return countryMade;
    }

    public void setCountryMade(String countryMade) {
        this.countryMade = countryMade;
    }

    public String getCountryBrand() {
        return countryBrand;
    }

    public void setCountryBrand(String countryBrand) {
        this.countryBrand = countryBrand;
    }

    public String getComposition() {
        return composition;
    }

    public void setComposition(String composition) {
        this.composition = composition;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFirme() {
        return firme;
    }

    public void setFirme(String firme) {
        this.firme = firme;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    
}
