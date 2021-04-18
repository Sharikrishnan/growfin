package com.example.growfin.controller;


import com.example.growfin.Mail.SendGridEmailer;
import com.example.growfin.exeception.ResourceNotFoundException;
import com.example.growfin.model.agent;
import com.example.growfin.model.ticket;
import com.example.growfin.repository.AgentRepository;
import com.example.growfin.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;


@RestController
@RequestMapping("/api")
public class TicketController {
    public HashMap<Long, Integer> ticketStatus = new HashMap<Long,Integer>();

    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    AgentRepository agentRepository;
    @Autowired
    SendGridEmailer sendGridEmailer;

    @GetMapping("/tickets")
    public List<ticket> getAllComments() {
        return ticketRepository.findAll();
    }

    @GetMapping("/test")
    public String getStatus() {
        return "status ok";
    }

    @PostMapping("/ticket")
    public ticket createTicket(@RequestBody ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @GetMapping("/ticket/{id}")
    public ticket findTicketById(@PathVariable(value="id") Long ticketId) {
        return ticketRepository.findById(ticketId).orElseThrow(() -> new ResourceNotFoundException("Ticket","id",ticketId));
    }

    @GetMapping("/ticket/assignedagent/{id}")
    public List<ticket> findTicketByagent(@PathVariable(value = "id") Integer agentId) {
        List<ticket> tickets = ticketRepository.findAll();
        List<ticket> filteredTickets= new ArrayList<ticket>();
        for(ticket temporaryTicket: tickets) {
             if (temporaryTicket.getAssignedto().equals(agentId)) {
                filteredTickets.add(temporaryTicket);
             }
        }
        return filteredTickets;
    }
    @GetMapping("/ticket/customer/{name}")
    public List<ticket> findTicketByCustomer(@PathVariable(value = "name") String customer) {
        List<ticket> tickets = ticketRepository.findAll();
        List<ticket> filteredTickets= new ArrayList<ticket>();
        for(ticket temporaryTicket: tickets) {
            if (temporaryTicket.getCustomer().equals(customer)) {
                filteredTickets.add(temporaryTicket);
            }
        }
        return filteredTickets;
    }

    @GetMapping("/ticket/status/{name}")
    public List<ticket> findTicketByStatus(@PathVariable(value = "name") String status) {
        List<ticket> tickets = ticketRepository.findAll();
        List<ticket> filteredTickets= new ArrayList<ticket>();
        for(ticket temporaryTicket: tickets) {
            if (temporaryTicket.getStatus().equals(status)) {
                filteredTickets.add(temporaryTicket);
            }
        }
        return filteredTickets;
    }

    @PostMapping("/ticket/{id}")
    public ticket createTicket(@PathVariable(value = "id") Long ticketId,@RequestBody ticket requestTicket) {
        ticket tempticket = ticketRepository.findById(ticketId).orElseThrow(() -> new ResourceNotFoundException("Ticket","id",ticketId));
        if (requestTicket.getCreatedby() != null) {
            tempticket.setCreatedby(requestTicket.getCreatedby());
        }
        if (requestTicket.getAssignedto() != null) {
            tempticket.setAssignedto(requestTicket.getAssignedto());
        }
        if (requestTicket.getCustomer() != null) {
            tempticket.setCustomer(requestTicket.getCustomer());
        }
        if (requestTicket.getDescription() != null) {
            tempticket.setDescription(requestTicket.getDescription());
        }
        tempticket.setLastmodified(requestTicket.getLastmodified());
        if (requestTicket.getPriority() != null) {
            tempticket.setPriority(requestTicket.getPriority());
        }
        if (requestTicket.getTitle() != null) {
            tempticket.setTitle(requestTicket.getTitle());
        }
        if (requestTicket.getType() != null) {
            tempticket.setType(requestTicket.getType());
        }
        return ticketRepository.save(tempticket);
    }

    @PostMapping("/ticket/{id}/status/{statusname}")
    public ticket updateStatus(@PathVariable(value = "id") Long ticketId,@PathVariable(value="statusname") String Status) throws Exception {
        ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new ResourceNotFoundException("Ticket","id",ticketId));
        ticket.setStatus(Status);
        return ticketRepository.save(ticket);
    }

    @PostMapping("/ticket/{id}/Response")
    public ticket addResponse(@PathVariable(value = "id") Long ticketId,@RequestBody ticket requestTicket) throws IOException {
        ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new ResourceNotFoundException("Ticket","id",ticketId));
        String comments = ticket.getComments()+" "+requestTicket.getComments();
        ticket.setComments(comments);
        sendGridEmailer.SendMail(ticket.getCustomerMail(), ticket.getTitle());
        ticket updatedTicket =  ticketRepository.save(ticket);

        return updatedTicket;

    }

    @PostMapping("/ticket/{di}/agent/{agentid}")
    public  ticket assignTicket(@PathVariable(value = "id") Long ticketId,@PathVariable(value = "agentid") Integer agentid) {
        ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new ResourceNotFoundException("Ticket","id",ticketId));
        ticket.setAssignedto(agentid);
        return ticketRepository.save(ticket);
    }

    @PostMapping("ticket/delete/{id}")
    public ResponseEntity<?> deleteTicket(@PathVariable(value = "id") Long ticketId) {
        ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new ResourceNotFoundException("Ticket","id",ticketId));
        ticketRepository.delete(ticket);
        return ResponseEntity.ok().build();
    }

    @PostMapping("ticket/Resolve")
    public ResponseEntity<?> resolveTicket() throws Exception {
        List<ticket> tickets = ticketRepository.findAll();
        for(ticket temporaryTicket: tickets) {

            if (temporaryTicket.getLastmodified().compareTo(new Date()) >= 30) {
                temporaryTicket.setStatus("Closed");
                ticketRepository.save(temporaryTicket);
            }

        }
        return ResponseEntity.ok().build();
    }

}
