package com.example.growfin.repository;

import com.example.growfin.model.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Queue;

@Repository
public interface AgentRepository extends JpaRepository<Agent, Long> {
    Agent findTopByOrderByTaskCountAscLastmodifiedAsc();
}
