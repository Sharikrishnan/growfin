package com.example.growfin.Request;

public class CustomerResponseRequest {
    private String customerResponse;

    public String getCustomerResponse() {
        return customerResponse;
    }

    public void setCustomerResponse(String customerResponse) {
        this.customerResponse = customerResponse;
    }

    @Override
    public String toString() {
        return "CommentRequest{" +
                "response='" + customerResponse + '\'' +
                '}';
    }
}
