package com.example.growfin.repository;

import com.example.growfin.model.ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<ticket, Long> {

}
