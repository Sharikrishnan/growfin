package com.example.growfin.controller;


import com.example.growfin.Request.CustomerResponseRequest;
import com.example.growfin.Service.SendGridEmailerService;
import com.example.growfin.Request.FilterTicketRequest;
import com.example.growfin.Service.TicketService;
import com.example.growfin.model.Agent;
import com.example.growfin.model.Ticket;
import com.example.growfin.repository.AgentRepository;
import com.example.growfin.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.*;


@RestController
public class TicketController {
    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    AgentRepository agentRepository;
    @Autowired
    SendGridEmailerService sendGridEmailerService;
    @Autowired
    TicketService ticketService;

    @RequestMapping(
            value = "/status",
            produces = "application/json",
            method = RequestMethod.GET)
    public String getStatus() {
        return "status ok";
    }

    @RequestMapping(
            value = "/tickets",
            produces = "application/json",
            method = RequestMethod.GET
    )
    public List<Ticket> getAllTickets() {
        return ticketService.findAllTickets();
    }

    @RequestMapping(
            value = "/ticket",
            produces = "application/json",
            consumes = "application/json",
            method = RequestMethod.POST)
    public Ticket createTicket(@RequestBody Ticket ticket) {
        return ticketService.createTicket(ticket);
    }

    @RequestMapping(
            value = "/agent",
            produces = "application/json",
            consumes = "application/json",
            method = RequestMethod.POST)
    public Agent createAgent(@RequestBody Agent agent) {
        return ticketService.createAgent(agent);
    }

    @RequestMapping(
            value = "/filter/ticket",
            produces = "application/json",
            consumes = "application/json",
            method = RequestMethod.GET)
    public List<Ticket> filterTicket(@RequestBody FilterTicketRequest filterTicketRequest) throws FileNotFoundException {
        return ticketService.filterTicket(filterTicketRequest);
    }
    @RequestMapping(
            value = "/ticket/{id}",
            produces = "application/json",
            consumes = "application/json",
            method = RequestMethod.GET)
    public Ticket findTicketById(@PathVariable(value="id") Long ticketId) throws FileNotFoundException {
        return ticketService.findTicket(ticketId);
    }

    @RequestMapping(
        value = "update/ticket/{id}",
        produces = "application/json",
        consumes = "application/json",
        method = RequestMethod.PUT)
    public Ticket UpdateTicket(@PathVariable(value = "id") Long ticketId, @RequestBody Ticket requestTicket) throws FileNotFoundException, IllegalAccessException {
        return ticketService.updateTicket(ticketId,requestTicket);
    }

    @RequestMapping(
            value = "/ticket/{id}/status/{statusname}",
            produces = "application/json",
            consumes = "application/json",
            method = RequestMethod.PUT)
    public Ticket updateStatus(@PathVariable(value = "id") Long ticketId, @PathVariable(value="statusname") String status) throws IllegalAccessException, FileNotFoundException {
        return ticketService.updateTicketStatus(ticketId,status);
    }

    @RequestMapping(
            value = "/ticket/{id}/Response",
            produces = "application/json",
            method = RequestMethod.PUT)
    public Ticket addResponse(@PathVariable(value = "id") Long ticketId, @RequestBody CustomerResponseRequest message) throws Exception {
        return ticketService.addResponse(ticketId,message.getCustomerResponse());

    }

    @RequestMapping(
            value = "/assign/tickets",
            produces = "application/json",
            method = RequestMethod.PUT)
    public List<Ticket> assignTicket() throws Exception {
        return ticketService.assignTicket();
    }

    @RequestMapping(
            value = "/assign/ticket/{ticketId}/agent/{agentId}",
            produces = "application/json",
            method = RequestMethod.PUT)
    public Ticket assignTickettoAgent(
            @PathVariable(value = "ticketId") Long ticketId,
            @PathVariable(value = "agentId") Long agentId) throws Exception {
        return ticketService.assignTickettoAgent(ticketId,agentId);
    }

    @RequestMapping(
            value = "ticket/delete/{id}",
            produces = "application/json",
            consumes = "application/json",
            method = RequestMethod.DELETE)
    public Ticket deleteTicket(@PathVariable(value = "id") Long ticketId) throws Exception {
        return  ticketService.deleteTicket(ticketId);
    }
}
