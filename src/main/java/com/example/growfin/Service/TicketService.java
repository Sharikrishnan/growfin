package com.example.growfin.Service;

import com.example.growfin.Request.FilterTicketRequest;
import com.example.growfin.model.Ticket;
import com.example.growfin.model.Agent;
import com.example.growfin.repository.AgentRepository;
import com.example.growfin.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class TicketService {
    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    AgentRepository agentRepository;
    @Autowired
    SendGridEmailer sendGridEmailer;

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
        if(filterTicketRequest.getAssignedToAgent() != null
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
        } else if(filterTicketRequest.getCustomer() != null
                && filterTicketRequest.getStatus() != null ) {
            return ticketRepository.findByStatusAndCustomer(
                    filterTicketRequest.getStatus(),
                    filterTicketRequest.getCustomer()
            );
        } else if(filterTicketRequest.getAssignedToAgent() != null) {
            Agent agent = agentRepository.findById(filterTicketRequest.getAssignedToAgent()).orElseThrow(FileNotFoundException::new);
            return ticketRepository.findByAssignedto(agent);
        } else if(filterTicketRequest.getCustomer() != null) {
            return  ticketRepository.findByCustomer(filterTicketRequest.getCustomer());
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
        ticketToUpdate.setLastmodified(ticketFields.getLastmodified());
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
        sb.append(ticket.getComments());
        sb.append("\n");
        sb.append(responseMessage);
        ticket.setComments(sb.toString());
        sendGridEmailer.SendMail(
                ticket.getCustomerMail(),
                ticket.getTitle(),
                "Ticket has been updated with the Response" + responseMessage);
        Ticket updatedTicket = ticketRepository.save(ticket);
        return updatedTicket;
    }

    public List<Ticket> assignTicket() {
        List<Ticket> tickets = ticketRepository.findAllByStatusContainsAndAssignedtoIsNull("Open");
        for (Ticket ticket : tickets) {
            Agent agent = agentRepository.findTopByOrderByTaskCountAscLastmodifiedAsc();
            ticket.setAssignedto(agent);
            agent.setTaskCount(agent.getTaskCount() + 1);
            agentRepository.save(agent);
        }
        ticketRepository.saveAll(tickets);
        return tickets;
    }

    public Ticket assignTickettoAgent(Long ticketId, Long AgentId) throws FileNotFoundException {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(FileNotFoundException::new);
        Agent agent = agentRepository.findById(AgentId).orElseThrow(FileNotFoundException::new);
        ticket.setAssignedto(agent);
        agent.setTaskCount(agent.getTaskCount()+1);
        agentRepository.save(agent);
        ticketRepository.save(ticket);
        return ticket;
    }

    public Ticket deleteTicket(Long id) throws FileNotFoundException {
        Ticket ticket = ticketRepository.findById(id).orElseThrow(FileNotFoundException::new);
        ticketRepository.delete(ticket);
        return ticket;
    }

    @Scheduled(cron = "0 0 1 * * *")
    public void ResolveTicket() throws IllegalAccessException, FileNotFoundException {
        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        cal.add(Calendar.DATE, -30);
        Date expireDate = cal.getTime();
        List<Ticket> tickets = ticketRepository.findByLastmodifiedBeforeAndStatusEquals(expireDate, "Resolved");
        for (Ticket ticket : tickets) {
            ticket.setStatus("Closed");

        }
        ticketRepository.saveAll(tickets);

    }
}
