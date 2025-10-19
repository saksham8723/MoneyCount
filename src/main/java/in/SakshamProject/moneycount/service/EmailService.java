package in.SakshamProject.moneycount.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.properties.mail.smtp.from}")
    private String from;
    public String sendEmail(String toEmail, String subject, String body) throws MessagingException {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(body, false);
            helper.setFrom(from); // optional but recommended

            // ✅ Correct way: use Spring’s mail sender
            mailSender.send(mimeMessage);

            return "✅ Email sent successfully to " + toEmail;
        } catch (MailException e) {
            throw new MessagingException("Spring Mail Error: " + e.getMessage(), e);
        } catch (MessagingException e) {
            throw new MessagingException("SMTP Error: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new MessagingException("Unexpected Error: " + e.getMessage(), e);
        }
    }
}