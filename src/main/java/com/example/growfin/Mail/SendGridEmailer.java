package com.example.growfin.Mail;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
@Service
public class SendGridEmailer {

    public void SendMail(String customerMail,String ticket) throws IOException {

        Email from = new Email("kicha191@gmail.com");
        Email to = new Email(customerMail); // use your own email address here

        String subject = "Ticket has been updated";
        String contentText = "Ticket " + ticket +" has been updated";
        Content content = new Content("text/html", contentText);

        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid("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");
        Request request = new Request();

        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());

        Response response = sg.api(request);

        System.out.println("status code"+response.getStatusCode());
        System.out.println(response.getHeaders());
        System.out.println(response.getBody());
    }

    public void sendEmail() {
    }
}
