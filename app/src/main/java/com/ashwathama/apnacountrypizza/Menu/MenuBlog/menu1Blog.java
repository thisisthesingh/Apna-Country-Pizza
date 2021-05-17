package com.ashwathama.apnacountrypizza.Menu.MenuBlog;

public class menu1Blog {




    private String id;

    private String name;
    private String desc;
    private String image;
    private int PriceSmall;
    private int PriceMedium;


    public menu1Blog(String id, String name, String desc, String image, int priceSmall, int priceMedium) {



        this.id=id;
        this.name = name;
        this.desc = desc;
        this.image = image;
        PriceSmall = priceSmall;
        PriceMedium = priceMedium;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getId(){
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
   public menu1Blog(){

   }
}
