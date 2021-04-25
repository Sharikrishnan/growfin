package com.example.growfin.Service;

import com.example.growfin.model.Ticket;
import com.example.growfin.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class ResolveTicketService {

    @Autowired
    TicketRepository ticketRepository;

    @Scheduled(cron = "0 0 1 * * *")
    public void ResolveTicket() throws IllegalAccessException {
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
