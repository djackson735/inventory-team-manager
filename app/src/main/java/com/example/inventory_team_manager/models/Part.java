package com.example.inventory_team_manager.models;

public class Part {
    private String partId;
    private String initialPartDate;
    private String partLocation;
    private int partQuantity;

    public Part(String partId, String initialPartDate, String partLocation, int partQuantity) {
        this.partId = partId;
        this.initialPartDate = initialPartDate;
        this.partLocation = partLocation;
        this.partQuantity = partQuantity;
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

    public int getPartQuantity() {
        return partQuantity;
    }

    public void setPartQuantity(int partQuantity) {
        this.partQuantity = partQuantity;
    }
    
}