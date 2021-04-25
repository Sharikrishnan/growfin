package com.example.growfin.Service;

import com.example.growfin.Request.FilterTicketRequest;
import com.example.growfin.model.Ticket;
import com.example.growfin.model.Agent;
import com.example.growfin.repository.AgentRepository;
import com.example.growfin.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.List;

import static com.example.growfin.common.Constants.*;

@Service
public class TicketService {
    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    AgentRepository agentRepository;
    @Autowired
    SendGridEmailerService sendGridEmailerService;

    public List<Ticket> findAllTickets() {
        return ticketRepository.findAll();
    }

    public Ticket createTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    public Agent createAgent(Agent agent) {
        return agentRepository.save(agent);
    }

    public Ticket findTicket(Long id) throws FileNotFoundException {
        return ticketRepository.findById(id).orElseThrow(FileNotFoundException::new);
    }

    public List<Ticket> filterTicket(FilterTicketRequest filterTicketRequest) throws FileNotFoundException {
        if (filterTicketRequest.getAssignedToAgent() != null
                && filterTicketRequest.getCustomer() != null
                && filterTicketRequest.getStatus() != null) {
            Agent agent = agentRepository.findById(filterTicketRequest.getAssignedToAgent()).orElseThrow(FileNotFoundException::new);
            return ticketRepository.findByAssignedtoAndStatusAndCustomer(
                    agent,
                    filterTicketRequest.getStatus(),
                    filterTicketRequest.getCustomer());
        } else if (filterTicketRequest.getAssignedToAgent() != null
                && filterTicketRequest.getCustomer() != null) {
            Agent agent = agentRepository.findById(filterTicketRequest.getAssignedToAgent()).orElseThrow(FileNotFoundException::new);
            return ticketRepository.findByAssignedtoAndCustomer(
                    agent,
                    filterTicketRequest.getCustomer()
            );
        } else if (filterTicketRequest.getAssignedToAgent() != null
                && filterTicketRequest.getStatus() != null) {
            Agent agent = agentRepository.findById(filterTicketRequest.getAssignedToAgent()).orElseThrow(FileNotFoundException::new);
            return ticketRepository.findByAssignedtoAndStatus(
                    agent,
                    filterTicketRequest.getStatus()
            );
        } else if (filterTicketRequest.getCustomer() != null
                && filterTicketRequest.getStatus() != null) {
            return ticketRepository.findByStatusAndCustomer(
                    filterTicketRequest.getStatus(),
                    filterTicketRequest.getCustomer()
            );
        } else if (filterTicketRequest.getAssignedToAgent() != null) {
            Agent agent = agentRepository.findById(filterTicketRequest.getAssignedToAgent()).orElseThrow(FileNotFoundException::new);
            return ticketRepository.findByAssignedto(agent);
        } else if (filterTicketRequest.getCustomer() != null) {
            return ticketRepository.findByCustomer(filterTicketRequest.getCustomer());
        } else {
            return ticketRepository.findByStatus(filterTicketRequest.getStatus());
        }
    }

    public Ticket updateTicket(Long ticketId, Ticket ticketFields) throws FileNotFoundException, IllegalAccessException {
        Ticket ticketToUpdate = ticketRepository.findById(ticketId).orElseThrow(FileNotFoundException::new);
        if (ticketFields.getCreatedby() != null) {
            ticketToUpdate.setCreatedby(ticketFields.getCreatedby());
        }
        if (ticketFields.getAssignedto() != null) {
            ticketToUpdate.setAssignedto(ticketFields.getAssignedto());
        }
        if (ticketFields.getCustomer() != null) {
            ticketToUpdate.setCustomer(ticketFields.getCustomer());
        }
        if (ticketFields.getDescription() != null) {
            ticketToUpdate.setDescription(ticketFields.getDescription());
        }
        if (ticketFields.getPriority() != null) {
            ticketToUpdate.setPriority(ticketFields.getPriority());
        }
        if (ticketFields.getTitle() != null) {
            ticketToUpdate.setTitle(ticketFields.getTitle());
        }
        if (ticketFields.getType() != null) {
            ticketToUpdate.setType(ticketFields.getType());
        }
        if (ticketFields.getStatus() != null) {
            ticketToUpdate.setStatus(ticketFields.getStatus());
            Agent agent = agentRepository.findById(
                    ticketToUpdate.getAssignedto().getId()).orElseThrow(FileNotFoundException::new);
            agent.setTaskCount(agent.getTaskCount() - 1);
            agentRepository.save(agent);

        }
        return ticketRepository.save(ticketToUpdate);
    }

    public Ticket updateTicketStatus(Long id, String status) throws FileNotFoundException, IllegalAccessException {
        Ticket ticketToUpdate = ticketRepository.findById(id).orElseThrow(FileNotFoundException::new);
        ticketToUpdate.setStatus(status);
        Agent agent = agentRepository.findById(
                ticketToUpdate.getAssignedto().getId()).orElseThrow(FileNotFoundException::new);
        agent.setTaskCount(agent.getTaskCount() - 1);
        agentRepository.save(agent);
        return ticketRepository.save(ticketToUpdate);
    }

    public Ticket addResponse(Long id, String responseMessage) throws Exception {
        Ticket ticket = ticketRepository.findById(id).orElseThrow(FileNotFoundException::new);
        StringBuilder sb = new StringBuilder();
        if (ticket.getComments() != null) {
            sb.append(ticket.getComments());
            sb.append("\n");
        }
        sb.append(responseMessage);
        ticket.setComments(sb.toString());
        sendGridEmailerService.SendMail(
                ticket.getCustomerMail(),
                ticket.getTitle(),
                responseComment + responseMessage);
        Ticket updatedTicket = ticketRepository.save(ticket);
        return updatedTicket;
    }

    public List<Ticket> assignTicket() throws Exception {
        List<Ticket> tickets = ticketRepository.findAllByStatusContainsAndAssignedtoIsNull(openStatus);
        for (Ticket ticket : tickets) {
            Agent agent = agentRepository.findTopByOrderByTaskCountAscLastmodifiedAsc();
            ticket.setAssignedto(agent);
            agent.setTaskCount(agent.getTaskCount() + 1);
            agentRepository.save(agent);
            sendGridEmailerService.SendMail(
                    ticket.getCustomerMail(),
                    ticket.getTitle(),
                    ticketBody);
        }
        ticketRepository.saveAll(tickets);
        return tickets;
    }

    public Ticket assignTickettoAgent(Long ticketId, Long AgentId) throws Exception {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(FileNotFoundException::new);
        Agent agent = agentRepository.findById(AgentId).orElseThrow(FileNotFoundException::new);
        ticket.setAssignedto(agent);
        agent.setTaskCount(agent.getTaskCount() + 1);
        agentRepository.save(agent);
        ticketRepository.save(ticket);
        sendGridEmailerService.SendMail(
                ticket.getCustomerMail(),
                ticket.getTitle(),
                ticketBody);
        return ticket;
    }

    public Ticket deleteTicket(Long id) throws Exception {
        Ticket ticket = ticketRepository.findById(id).orElseThrow(FileNotFoundException::new);

        Agent agent = agentRepository.findById(
                ticket.getAssignedto().getId()).orElseThrow(FileNotFoundException::new);
        agent.setTaskCount(agent.getTaskCount() - 1);
        agentRepository.save(agent);
        ticketRepository.delete(ticket);
        sendGridEmailerService.SendMail(
                ticket.getCustomerMail(),
                ticket.getTitle(),
                ticketBody);
        return ticket;
    }

}
