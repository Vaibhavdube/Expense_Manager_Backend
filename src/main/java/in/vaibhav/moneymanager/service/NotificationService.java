package in.vaibhav.moneymanager.service;

import in.vaibhav.moneymanager.dto.ExpenseDTO;
import in.vaibhav.moneymanager.entity.ProfileEntity;
import in.vaibhav.moneymanager.repository.ProfileRepository;
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

    private final ProfileRepository profileRepository;
    private final EmailService emailService;
    private final ExpenseService expenseService;

    @Value("${money.manager.frontend.url}")
    private String frontendUrl;

    // Every day at 10 PM
    @Scheduled(cron = "0 0 22 * * *", zone = "Asia/Kolkata")
    public void sendDailyIncomeExpenseReminder() {

        log.info("Job started: sendDailyIncomeExpenseReminder");

        List<ProfileEntity> profiles = profileRepository.findAll();

        for (ProfileEntity profile : profiles) {

            String body = "Hi " + profile.getFullName() + ",\n\n"
                    + "This is a reminder to record today's income and expenses in your Money Manager account.\n\n"
                    + "Click below to open the application:\n"
                    + frontendUrl + "\n\n"
                    + "Regards,\n"
                    + "Money Manager Team";

            emailService.sendEmail(
                    profile.getEmail(),
                    "Daily Reminder: Add Your Income and Expense",
                    body
            );
        }

        log.info("Job completed: sendDailyIncomeExpenseReminder");
    }

    // Every day at 11 PM
    @Scheduled(cron = "0 0 23 * * *", zone = "Asia/Kolkata")
    public void sendDailyExpenseSummary() {

        log.info("Job started: sendDailyExpenseSummary");

        List<ProfileEntity> profiles = profileRepository.findAll();

        for (ProfileEntity profile : profiles) {

            List<ExpenseDTO> todaysExpenses =
                    expenseService.getExpensesForUserOnDate(
                            profile.getId(),
                            LocalDate.now(ZoneId.of("Asia/Kolkata"))
                    );

            if (!todaysExpenses.isEmpty()) {

                StringBuilder table = new StringBuilder();

                table.append("<table style='border-collapse:collapse;width:100%;'>");

                table.append("<tr style='background-color:#f2f2f2;'>")
                        .append("<th style='border:1px solid #ddd;padding:8px;'>S.No</th>")
                        .append("<th style='border:1px solid #ddd;padding:8px;'>Title</th>")
                        .append("<th style='border:1px solid #ddd;padding:8px;'>Category</th>")
                        .append("<th style='border:1px solid #ddd;padding:8px;'>Amount</th>")
                        .append("</tr>");

                int i = 1;

                for (ExpenseDTO expense : todaysExpenses) {

                    table.append("<tr>");

                    table.append("<td style='border:1px solid #ddd;padding:8px;'>")
                            .append(i++)
                            .append("</td>");

                    table.append("<td style='border:1px solid #ddd;padding:8px;'>")
                            .append(expense.getName())
                            .append("</td>");

                    table.append("<td style='border:1px solid #ddd;padding:8px;'>")
                            .append(expense.getCategoryId())
                            .append("</td>");

                    table.append("<td style='border:1px solid #ddd;padding:8px;'>")
                            .append(expense.getAmount())
                            .append("</td>");

                    table.append("</tr>");
                }

                table.append("</table>");

                String body = "Hi " + profile.getFullName()
                        + ",<br/><br/>Here is a summary of your expenses for today.<br/><br/>"
                        + table;

                emailService.sendEmail(
                        profile.getEmail(),
                        "Your Daily Expense Summary",
                        body
                );
            }
        }

        log.info("Job completed: sendDailyExpenseSummary");
    }
}

