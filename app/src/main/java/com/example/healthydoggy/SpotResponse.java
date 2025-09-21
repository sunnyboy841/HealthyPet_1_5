package com.example.healthydoggy; // 包名

import java.util.List;

public class SpotResponse {
    private int status;
    private String message;
    private List<Spot> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Spot> getData() {
        return data;
    }

    public void setData(List<Spot> data) {
        this.data = data;
    }
}