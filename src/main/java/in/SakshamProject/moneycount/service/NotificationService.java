package in.SakshamProject.moneycount.service;


import in.SakshamProject.moneycount.dto.ExpenseDTO;
import in.SakshamProject.moneycount.entity.ProfileEntity;
import in.SakshamProject.moneycount.repository.ProfileRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final ProfileRepo profileRepo;
    private final EmailService emailService;
    private final ExpenseService expenseService;
    @Value("${money.count.frontend.url}")
    private String frontendUrl;




    //* * * * * Every minutes
    //0 * *  * * Every hour
    //0 0 * * * Every day

    @Scheduled(cron = "0 0 22 * * * ",zone="IST") //daily at 10 PM
    public void sendDailyIncomeExpenseRemainder() {
        log.info("Job started: sendDailyIncomeExpenseRemainder()");

        List<ProfileEntity> profiles = profileRepo.findAll();

        profiles.parallelStream().forEach(profile -> {
            try {
                if (profile.getEmail() == null || profile.getEmail().isEmpty()) return;

                String fullName = profile.getFullName() != null ? profile.getFullName() : "User";

                StringBuilder body = new StringBuilder();
                body.append("Hi ").append(fullName).append(",<br><br>")
                        .append("This is a friendly reminder to add your income and expenses for the day in Money Count.<br><br>")
                        .append("<a href='").append(frontendUrl)
                        .append("' style='display:inline-block;padding:10px 20px;background-color:#4CAF50;color:#fff;text-decoration:none;border-radius:5px;font-weight:bold;'>Go to Money Count</a>")
                        .append("<br><br>Best regards,<br>Money Count Team");

                emailService.sendEmail(
                        profile.getEmail(),
                        "Daily Reminder: Add your income and expenses",
                        body.toString()
                );

                log.info("Reminder sent to {}", profile.getEmail());

            } catch (Exception e) {
                log.error("Failed to send reminder to {}: {}", profile.getEmail(), e.getMessage());
            }
        });
    }
//    @Scheduled(cron = "0 * * * * * ",zone="IST") // every minutes
@Scheduled(cron = "0 0 23 * * *", zone = "Asia/Kolkata") // Runs every day at 11 PM IST
public void sendDailyExpenseSummary() {
    log.info("Job started: sendDailyExpenseSummary()");

    List<ProfileEntity> profiles = profileRepo.findAll();

    profiles.parallelStream().forEach(profile -> {
        try {
            String fullName = profile.getFullName() != null ? profile.getFullName() : "User";
            List<ExpenseDTO> todayExpenses = expenseService.getExpensesForUserOnDate(profile.getId(), LocalDate.now());

            // ✅ Only send if user actually has expenses
            if (!todayExpenses.isEmpty()) {

                StringBuilder table = new StringBuilder();
                table.append("<table style='border-collapse: collapse; width:100%; font-family: Arial, sans-serif;'>");
                table.append("<tr style='background-color:#f2f2f2;'>")
                        .append("<th style='border:1px solid #ddd; padding:8px;'>S.No</th>")
                        .append("<th style='border:1px solid #ddd; padding:8px;'>Name</th>")
                        .append("<th style='border:1px solid #ddd; padding:8px;'>Amount</th>")
                        .append("<th style='border:1px solid #ddd; padding:8px;'>Category</th>")

                        .append("</tr>");

                int i = 1;
                for (ExpenseDTO expenseDTO : todayExpenses) {
                    table.append("<tr>")
                            .append("<td style='border:1px solid #ddd; padding:8px;'>").append(i++).append("</td>")
                            .append("<td style='border:1px solid #ddd; padding:8px;'>").append(expenseDTO.getName()).append("</td>")
                            .append("<td style='border:1px solid #ddd; padding:8px;'>").append(expenseDTO.getAmmount()).append("</td>")
                            .append("<td style='border:1px solid #ddd; padding:8px;'>")
                            .append(expenseDTO.getCategoryId() != null ? expenseDTO.getCategoryId() : "N/A")
                            .append("</td>")
                            .append("<td style='border:1px solid #ddd; padding:8px;'>").append(LocalDate.now()).append("</td>")
                            .append("</tr>");
                }

                table.append("</table>");

                String body = "Hi " + fullName + ",<br/><br/>"
                        + "Here is a summary of your expenses for today:<br/><br/>"
                        + table.toString()
                        + "<br/><br/>Best regards,<br/>Money Count Team";

                emailService.sendEmail(profile.getEmail(), "Your Daily Expense Summary", body);

                log.info("Expense summary sent to {}", profile.getEmail());
            } else {
                log.info("No expenses found for user: {}", profile.getEmail());
            }

        } catch (Exception e) {
            log.error("Failed to send expense summary to {}: {}", profile.getEmail(), e.getMessage());
        }
    });
}

}
