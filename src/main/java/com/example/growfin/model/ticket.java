package com.example.growfin.model;

import com.example.growfin.exeception.StatusException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

@Entity
@Table(name = "tickets")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = { "created", "lastmodified" }, allowGetters = true)
public class ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date created;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date lastmodified;

    @NotBlank
    private String type;

    @NotBlank
    private String description;

    @NotBlank
    private String title;

    @NotBlank
    private String createdby;


    @NotBlank
    private String lastmodifiedby;

    @NotBlank
    private String customer;

    @NotBlank
    private String customerMail;

    @Column(nullable = true)
    private Integer assignedto;

    @Column(nullable = true)
    private  String priority;

    @Column(nullable = true)
    private String status;

    @Column(nullable = true)
    private String comments;

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getCustomerMail() {
        return customerMail;
    }

    public void setCustomerMail(String customerMail) {
        this.customerMail = customerMail;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
    public String getLastmodifiedby() {
        return lastmodifiedby;
    }

    public void setLastmodifiedby(String lastmodifiedby) {
        this.lastmodifiedby = lastmodifiedby;
    }

    public Date getLastmodified() {
        return lastmodified;
    }

    public void setLastmodified(Date lastmodified) {
        this.lastmodified = lastmodified;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreatedby() {
        return createdby;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public Integer getAssignedto() {
        return assignedto;
    }

    public void setAssignedto(Integer assignedto) {
        this.assignedto = assignedto;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) throws Exception {
        if (checkStatus(status)) {
            status = status;
        } else {
            throw new StatusException();
        }
    }

    public Boolean checkStatus(String status) {
        ArrayList<String> fixedstatus = new ArrayList<String>();
        fixedstatus.add("Open");
        fixedstatus.add("waiting for customer");
        fixedstatus.add("customer responded");
        fixedstatus.add("resolved");
        fixedstatus.add("Closed");
        return fixedstatus.contains(status);
    }
}
