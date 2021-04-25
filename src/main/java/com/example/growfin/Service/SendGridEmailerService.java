package com.example.growfin.Service;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.*;
import org.springframework.stereotype.Service;

import static com.example.growfin.common.Constants.fromMail;
import static com.example.growfin.common.Constants.ticketSubject;

@Service
public class SendGridEmailerService {

    private static final String sendGridKey = System.getenv("SENDGRID_KEY");

    public void SendMail(String mailAddress,String ticket,String comments) throws Exception {

        try {
            Email from = new Email(fromMail);
            Email to = new Email(mailAddress);

            String subject = ticketSubject;
            String contentText;

            if (comments.isEmpty()) {
                contentText = "Ticket " + ticket + " has been updated";
            } else {
                contentText = comments;
            }
            Content content = new Content("text/html", contentText);

            Mail mail = new Mail(from, subject, to, content);

            SendGrid sg = new SendGrid(sendGridKey);
            Request request = new Request();

            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);
        } catch (Exception e) {
            System.out.println("Send Grid throws Exception "+e);
            throw new Exception();
        }

    }
}
