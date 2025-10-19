package in.SakshamProject.moneycount.controller;

import in.SakshamProject.moneycount.dto.ExpenseDTO;
import in.SakshamProject.moneycount.dto.FilterDTO;
import in.SakshamProject.moneycount.dto.IncomeDTO;
import in.SakshamProject.moneycount.service.ExpenseService;
import in.SakshamProject.moneycount.service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/filter")
public class FilterController {
    private final IncomeService incomeService;
    private  final ExpenseService expenseService;
    @PostMapping
    public ResponseEntity<?> filterTransactions(@RequestBody FilterDTO filterDTO) {
        // Prepare data or validation
        LocalDate startDate = filterDTO.getStartDate() != null ? filterDTO.getStartDate() : LocalDate.MIN;
        LocalDate endDate = filterDTO.getEndDate() != null ? filterDTO.getEndDate() : LocalDate.now();
        String keyword = filterDTO.getKeyword() != null ? filterDTO.getKeyword() : "";
        String sortField = filterDTO.getSortField() != null ? filterDTO.getSortField() : "date";

        Sort.Direction direction =
                "desc".equalsIgnoreCase(filterDTO.getSortOrder())
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        Sort sort = Sort.by(direction, sortField);

        if ("income".equalsIgnoreCase(filterDTO.getType())) {
            List<IncomeDTO> incomes = incomeService.filterIncome(startDate, endDate, keyword, sort);
            return ResponseEntity.ok(incomes);

        } else if ("expense".equalsIgnoreCase(filterDTO.getType())) {
            List<ExpenseDTO> expenses = expenseService.filterExpense(startDate, endDate, keyword, sort);
            return ResponseEntity.ok(expenses);

        } else {
            return ResponseEntity.badRequest().body("Invalid type. Must be 'income' or 'expense'.");
        }
    }
}
