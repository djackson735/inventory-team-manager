package com.example.inventory_team_manager.models;

public class Part {
    private String partId;
    private String date;
    private String partLocation;
    private int quantity;

    public Part(String partId, String date, String partLocation, int quantity) {
        this.partId = partId;
        this.date = date;
        this.partLocation = partLocation;
        this.quantity = quantity;
    }

    public String getPartId() {
        return partId;
    }

    public void setPartId(String partId) {
        this.partId = partId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPartLocation() {
        return partLocation;
    }

    public void setPartLocation(String partLocation) {
        this.partLocation = partLocation;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
}