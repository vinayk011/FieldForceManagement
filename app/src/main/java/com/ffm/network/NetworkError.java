package com.ffm.network;



public class NetworkError {

    private String description;
    private String recommendation;

    public NetworkError(String description, String recommendation) {
        this.description = description;
        this.recommendation = recommendation;
    }

    public String getDescription() {
        return description;
    }

    public String getRecommendation() {
        return recommendation;
    }
}
