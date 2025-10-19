package in.SakshamProject.moneycount.controller;

import in.SakshamProject.moneycount.service.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")  // 👈 simpler base path
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendEmail(@RequestParam String email) {
        try {
            String smtpResponse = emailService.sendEmail(
                    email,
                    "Verify your email",
                    "Click here to verify your account!"
            );
            return ResponseEntity.ok("✅ Email sent to " + email + " | " + smtpResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Failed to send email: " + e.getMessage());
        }
    }
}