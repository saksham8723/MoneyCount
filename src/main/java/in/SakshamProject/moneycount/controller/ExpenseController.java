package in.SakshamProject.moneycount.controller;

import in.SakshamProject.moneycount.dto.ExpenseDTO;
import in.SakshamProject.moneycount.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ExpenseDTO> addExpense(@RequestBody ExpenseDTO expenseDTO){
      ExpenseDTO expense=  expenseService.addExpense(expenseDTO);
      return ResponseEntity.status(HttpStatus.CREATED).body(expense);
    }
    @GetMapping
    public  ResponseEntity<List<ExpenseDTO>> getExpenses(){
        List<ExpenseDTO> expenses=expenseService.getCurrentMonthExpensesFortheCurrentProfile();
        return ResponseEntity.ok(expenses);
    }
    @DeleteMapping("/{id}")
 public ResponseEntity<Void> deleteExpense(@PathVariable Long id){
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }
}
