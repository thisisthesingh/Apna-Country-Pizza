package com.ashwathama.apnacountrypizza.Blogs;

public class OrderBlog {

    private String DateandTime;
    private String DeliveryAddress;
    private String ModeOfPayment;
    private String OrderStatus;
    private String AmountStatus;
    private int Amounttopaid;
    private String Oid;


    public OrderBlog( String oid,String dateandTime, String deliveryAddress, String modeOfPayment, int amounttopaid,String orderStatus) {
        DateandTime = dateandTime;
        DeliveryAddress = deliveryAddress;
        OrderStatus=orderStatus;
        ModeOfPayment = modeOfPayment;
        Amounttopaid = amounttopaid;
        Oid=oid;

    }

    public String getOid() {
        return Oid;
    }

    public void setOid(String oid) {
        Oid = oid;
    }
    public String getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        OrderStatus = orderStatus;
    }

    public String getAmountStatus() {
        return AmountStatus;
    }

    public void setAmountStatus(String amountStatus) {
        AmountStatus = amountStatus;
    }

    public String getDateandTime() {
        return DateandTime;
    }

    public void setDateandTime(String dateandTime) {
        DateandTime = dateandTime;
    }

    public String getDeliveryAddress() {
        return DeliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        DeliveryAddress = deliveryAddress;
    }

    public String getModeOfPayment() {
        return ModeOfPayment;
    }

    public void setModeOfPayment(String modeOfPayment) {
        ModeOfPayment = modeOfPayment;
    }

    public int getAmounttopaid() {
        return Amounttopaid;
    }

    public void setAmounttopaid(int amounttopaid) {
        Amounttopaid = amounttopaid;
    }

    public OrderBlog(){

    }
}
