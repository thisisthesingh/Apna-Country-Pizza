package com.ashwathama.apnacountrypizza.Blogs;

import com.google.firebase.database.Exclude;

public class cartblog {


    @Exclude
    int finalam;



    private String name;
    private String desc;
    private String image;
    private int PriceSmall;
    private int PriceMedium;
    private int SQuantity;
    private int MQuantity;
    private int mamount;
    private int samount;
    private int tamount;

    private String id;



    public cartblog(int sQuantity,int mQuantity,int tamount, String id, String name, String desc, String image, int priceSmall, int priceMedium, int samount, int mamount) {

        SQuantity=sQuantity;
        MQuantity=mQuantity;
        this.tamount=tamount;
        this.samount=samount;
        this.mamount=mamount;
        this.id=id;
        this.name = name;
        this.desc = desc;
        this.image = image;
        PriceSmall = priceSmall;
        PriceMedium = priceMedium;
    }


    public int getFinalam() {
        return finalam;
    }

    public void setFinalam(int finalam) {
        this.finalam = finalam;
    }

    public int getSQuantity() {
        return SQuantity;
    }

    public void setSQuantity(int SQuantity) {
        this.SQuantity = SQuantity;
    }

    public int getMQuantity() {
        return MQuantity;
    }

    public void setMQuantity(int MQuantity) {
        this.MQuantity = MQuantity;
    }

    public int getTamount() {
        return tamount;
    }

    public void setTamount(int tamount) {
        this.tamount = tamount;
    }

    public int getMamount() {
        return mamount;
    }

    public void setMamount(int mamount) {
        this.mamount = mamount;
    }

    public int getSamount() {
        return samount;
    }

    public void setSamount(int samount) {
        this.samount = samount;
    }

    public String getId(){
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getPriceSmall() {
        return PriceSmall;
    }

    public void setPriceSmall(int priceSmall) {
        PriceSmall = priceSmall;
    }

    public int getPriceMedium() {
        return PriceMedium;
    }

    public void setPriceMedium(int priceMedium) {
        PriceMedium = priceMedium;
    }
    public cartblog(){

    }
}
