package com.example.growfin.Request;

public class FilterTicketRequest {
    private Long assignedToAgent;
    private String customer;
    private String status;

    public Long getAssignedToAgent() {
        return assignedToAgent;
    }

    public void setAssignedToAgent(Long assignedToAgent) {
        this.assignedToAgent = assignedToAgent;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
       this.customer = customer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "filterTicketRequest{" +
                "assignedToAgent='" + assignedToAgent + '\'' +
                ", Customer='" + customer + '\'' +
                ", Status='" + status + '\'' +
                '}';
    }
}
