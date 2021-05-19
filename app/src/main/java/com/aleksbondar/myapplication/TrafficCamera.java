package com.aleksbondar.myapplication;

public class TrafficCamera {
    String camId;
    String description;
    String imageUrl;
    double[] coordinates;

    public TrafficCamera(String camId, String description, String imageUrl, double[] coordinates) {
        this.camId = camId;
        this.description = description;
        this.imageUrl = imageUrl;
        this.coordinates = coordinates;
    }

    public double[] getCoordinates() {
        return this.coordinates;
    }

    public String getDescription() {
        return this.description;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }
}
