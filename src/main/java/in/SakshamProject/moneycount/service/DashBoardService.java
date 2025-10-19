package in.SakshamProject.moneycount.service;


import in.SakshamProject.moneycount.dto.ExpenseDTO;
import in.SakshamProject.moneycount.dto.IncomeDTO;
import in.SakshamProject.moneycount.dto.RecentTransactionDto;
import in.SakshamProject.moneycount.entity.ProfileEntity;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Stream.concat;

@Service
@RequiredArgsConstructor
public class DashBoardService {
    private  final IncomeService incomeService;
    private  final ExpenseService expenseService;
    private  final ProfileService profileService;

    public Map<String,Object> getDashBoardData(){
        ProfileEntity profile=profileService.getCurrentProfile();
        Map<String ,Object> returnValue=new LinkedHashMap<>();
        List<IncomeDTO> latestIncomes=incomeService.getLatest5IncomeForCurrentUser();
        List<ExpenseDTO> latestExpense=expenseService.getLatest5ExpenseForCurrentUser();
      List<RecentTransactionDto> recentTransactions =  concat(latestIncomes.stream().map(income->
                RecentTransactionDto.builder()
                        .id(income.getId())
                        .profileId(profile.getId())
                        .icon(income.getIcon())
                        .name(income.getName())
                        .ammount(income.getAmmount())
                        .date(income.getDate())
                        .createdAt(income.getCreatedAt())
                        .updateAt(income.getUpdatedAt())
                        .type("income").build()
        ),latestExpense.stream().map(expense->
                RecentTransactionDto.builder()
                        .id(expense.getId())
                        .profileId(profile.getId())
                        .icon(expense.getIcon())
                        .name(expense.getName())
                        .ammount(expense.getAmmount())
                        .date(expense.getDate())
                        .createdAt(expense.getCreatedAt())
                        .updateAt(expense.getUpdatedAt())
                        .type("expense").build()))
              .sorted((a,b)->{
                  int cmp=b.getDate().compareTo(a.getDate());
                  if (cmp==0&&a.getCreatedAt()!=null && b.getCreatedAt()!=null){
                      return b.getCreatedAt().compareTo(a.getCreatedAt());
                  }
                  return  cmp;

              }).collect(Collectors.toList());
      returnValue.put("totalBalance",incomeService.getTotalIncomeForCurrentUser()
              .subtract(expenseService.getTotalExpenseForCurrentUser()));
      returnValue.put("totalIncome",incomeService.getTotalIncomeForCurrentUser());
      returnValue.put("totalExpense",expenseService.getTotalExpenseForCurrentUser());
      returnValue.put("recent5Expenses",latestExpense);
      returnValue.put("recent5Income",latestIncomes);
      returnValue.put("recentTransactions",recentTransactions);
      return returnValue;


    }
}
