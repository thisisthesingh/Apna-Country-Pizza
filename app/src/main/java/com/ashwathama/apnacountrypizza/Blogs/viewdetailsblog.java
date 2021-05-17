package com.ashwathama.apnacountrypizza.Blogs;

public class viewdetailsblog {

    String name;
    int PriceSmall;
    int PriceMedium;
    int MQuantity;
    int SQuantity;


    public viewdetailsblog(String name, int priceSmall, int priceMedium, int MQuantity, int SQuantity) {
        this.name = name;
        PriceSmall = priceSmall;
        PriceMedium = priceMedium;
        this.MQuantity = MQuantity;
        this.SQuantity = SQuantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getMQuantity() {
        return MQuantity;
    }

    public void setMQuantity(int MQuantity) {
        this.MQuantity = MQuantity;
    }

    public int getSQuantity() {
        return SQuantity;
    }

    public void setSQuantity(int SQuantity) {
        this.SQuantity = SQuantity;
    }

    public viewdetailsblog(){

    }
}
