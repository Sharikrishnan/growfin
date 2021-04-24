package com.example.growfin.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "agents")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = { "created", "lastmodified" }, allowGetters = true)
public class Agent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String createdby;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date created;

    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date lastmodified;


    private String lastmodifiedby;

    @NotBlank
    private  String name;

    @NotBlank
    private String agentMail;

    @OneToMany(mappedBy = "assignedto")
    @JsonManagedReference
    private List<Ticket> tickets;

    private int taskCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreatedby() {
        return createdby;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getLastmodified() {
        return lastmodified;
    }

    public void setLastmodified(Date lastmodified) {
        this.lastmodified = lastmodified;
    }

    public String getLastmodifiedby() {
        return lastmodifiedby;
    }

    public void setLastmodifiedby(String lastmodifiedby) {
        this.lastmodifiedby = lastmodifiedby;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAgentMail() {
        return agentMail;
    }

    public void setAgentMail(String agentMail) {
        this.agentMail = agentMail;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public int getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(int taskCount) {
        this.taskCount = taskCount;
    }

    @Override
    public String toString() {
        return "Agent{" +
                "id=" + id +
                ", createdby='" + createdby + '\'' +
                ", created=" + created +
                ", lastmodified=" + lastmodified +
                ", lastmodifiedby='" + lastmodifiedby + '\'' +
                ", name='" + name + '\'' +
                ", agentMail='" + agentMail + '\'' +
                ", tickets=" + tickets +
                ", taskCount=" + taskCount +
                '}';
    }
}
