package com.example.growfin.repository;

import com.example.growfin.model.Agent;
import com.example.growfin.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByAssignedtoAndStatusAndCustomer(Agent assignedto, String status, String customer);
    List<Ticket> findByAssignedtoAndStatus(Agent assignedto,String status);
    List<Ticket> findByAssignedtoAndCustomer(Agent assignedto,String customer);
    List<Ticket> findByStatusAndCustomer(String status,String customer);
    List<Ticket> findByAssignedto(Agent assignedto);
    List<Ticket> findByStatus(String status);
    List<Ticket> findByCustomer(String customer);
    List<Ticket> findAllByStatusContainsAndAssignedtoIsNull(String status);
    List<Ticket> findByLastmodifiedBeforeAndStatusEquals(Date expiryDate,String status);
}
