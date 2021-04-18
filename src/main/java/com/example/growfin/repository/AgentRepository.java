package com.example.growfin.repository;

import com.example.growfin.model.agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgentRepository extends JpaRepository<agent, Long> {

}
