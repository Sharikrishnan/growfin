package com.example.growfin.Request;

public class CommentRequest {
    private String response;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "CommentRequest{" +
                "response='" + response + '\'' +
                '}';
    }
}
