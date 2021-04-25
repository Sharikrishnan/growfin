package com.example.growfin.Service;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.*;
import org.springframework.stereotype.Service;

@Service
public class SendGridEmailerService {

    public void SendMail(String mailAddress,String ticket,String comments) throws Exception {
        try {
            Email from = new Email("kicha191@gmail.com");
            Email to = new Email(mailAddress);

            String subject = "Ticket has been updated";
            String contentText;

            if (comments.isEmpty()) {
                contentText = "Ticket " + ticket + " has been updated";
            } else {
                contentText = comments;
            }
            Content content = new Content("text/html", contentText);

            Mail mail = new Mail(from, subject, to, content);

            SendGrid sg = new SendGrid("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");
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
