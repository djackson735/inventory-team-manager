package com.example.inventory_team_manager.models;

public class Part {
    private String partId;
    private String initialPartDate;
    private String partLocation;
    private int quantity;

    public Part(String partId, String initialPartDate, String partLocation, int quantity) {
        this.partId = partId;
        this.initialPartDate = initialPartDate;
        this.partLocation = partLocation;
        this.quantity = quantity;
    }

    public String getPartId() {
        return partId;
    }

    public void setPartId(String partId) {
        this.partId = partId;
    }

    public String getInitialPartDate() {
        return initialPartDate;
    }

    public void setInitialPartDate(String initialPartDate) {
        this.initialPartDate = initialPartDate;
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