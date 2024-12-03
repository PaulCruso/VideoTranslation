package com.example.demo.model;

public class StatusResponse {
    private String result;

    public StatusResponse(TranslationStatus status) {
        this.result = status.toString().toLowerCase();
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}