package in.SakshamProject.moneycount.service;


import in.SakshamProject.moneycount.dto.ExpenseDTO;
import in.SakshamProject.moneycount.entity.CategoryEntity;
import in.SakshamProject.moneycount.entity.ExpenseEntity;
import in.SakshamProject.moneycount.entity.ProfileEntity;
import in.SakshamProject.moneycount.repository.CategoryRepo;
import in.SakshamProject.moneycount.repository.ExpenseRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {
    private  final CategoryRepo categoryRepo;
    private  final ExpenseRepo expenseRepo;
    private  final ProfileService profileService;






    //adding a new expense to the db
    public  ExpenseDTO addExpense(ExpenseDTO dto){
        ProfileEntity profile=profileService.getCurrentProfile();
      CategoryEntity category=  categoryRepo.findById(dto.getCategoryId())
              .orElseThrow(()-> new RuntimeException("Category not found "));
     ExpenseEntity expenseEntity= toEntity(dto,profile,category);
     expenseRepo.save(expenseEntity);
     return  toDto(expenseEntity);




    }
    //read all the expence for current month based on the start date and end date
    public List<ExpenseDTO> getCurrentMonthExpensesFortheCurrentProfile(){
     ProfileEntity profile=   profileService.getCurrentProfile();
        LocalDate now=LocalDate.now();
        LocalDate startDate=now.withDayOfMonth(1);
        LocalDate endDate=now.withDayOfMonth(now.lengthOfMonth());
        List<ExpenseEntity> list=expenseRepo.findByProfileIdAndDateBetween(profile.getId(),startDate,endDate);
        return list.stream().map(this::toDto).toList();
    }
   // delete expense by id for curr user
   public void deleteExpense(Long expenseId) {
       ProfileEntity profile = profileService.getCurrentProfile();

       ExpenseEntity entity = expenseRepo.findById(expenseId)
               .orElseThrow(() -> new RuntimeException("Expense not found"));

       if (!entity.getProfile().getId().equals(profile.getId())) {
           throw new RuntimeException("Unauthorized to delete this expense");
       }

       expenseRepo.delete(entity);
   }
   // get latest 5 expenses for current user
    public  List<ExpenseDTO> getLatest5ExpenseForCurrentUser(){
        ProfileEntity profile = profileService.getCurrentProfile();
        List<ExpenseEntity>  list=expenseRepo.findTop5ByProfileIdOrderByDateDesc(profile.getId());
        return list.stream().map(this::toDto).toList();

    }
    //get total expense for current user
    public BigDecimal getTotalExpenseForCurrentUser(){
        ProfileEntity profile = profileService.getCurrentProfile();
        BigDecimal total=expenseRepo.findTotalExpenceByProfileId(profile.getId());
        return total!=null?total:BigDecimal.ZERO;

    }
    // filter expense
    public  List<ExpenseDTO> filterExpense(LocalDate startDate, LocalDate endDate, String keyword, Sort sort){
        ProfileEntity profile = profileService.getCurrentProfile();
     List<ExpenseEntity> list =  expenseRepo.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(profile.getId(), startDate,endDate,keyword,sort);
         return list.stream().map(this::toDto).toList();

    }
    public  List<ExpenseDTO> getExpensesForUserOnDate(Long profileId,LocalDate date){
      List<ExpenseEntity> expenseslist=  expenseRepo.findByProfileIdAndDate(profileId,date);
      return expenseslist.stream().map(this::toDto).toList();

    }

    //helper methods
    private ExpenseEntity toEntity(ExpenseDTO dto, ProfileEntity profile, CategoryEntity categoryEntity){
        return ExpenseEntity.builder()
                .name(dto.getName())
                .icon(dto.getIcon())
                .amount(dto.getAmmount())
                .date(dto.getDate())
                .profile(profile)
                .category(categoryEntity)
                .build();
    }
    private  ExpenseDTO toDto(ExpenseEntity entity){
        return ExpenseDTO.builder()
                .name(entity.getName())
                .id(entity.getId())
                .icon(entity.getIcon())
                .categoryId(entity.getCategory()!=null ? entity.getCategory().getId():null)
                .categoryName(entity.getCategory()!=null ? entity.getCategory().getName():null)
                .ammount(entity.getAmount())
                .date(entity.getDate())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

}
