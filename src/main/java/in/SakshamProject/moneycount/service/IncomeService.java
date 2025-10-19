package in.SakshamProject.moneycount.service;



import in.SakshamProject.moneycount.dto.ExpenseDTO;
import in.SakshamProject.moneycount.dto.IncomeDTO;
import in.SakshamProject.moneycount.entity.CategoryEntity;


import in.SakshamProject.moneycount.entity.ExpenseEntity;
import in.SakshamProject.moneycount.entity.IncomeEntity;
import in.SakshamProject.moneycount.entity.ProfileEntity;

import in.SakshamProject.moneycount.repository.CategoryRepo;
import in.SakshamProject.moneycount.repository.IncomeRepo;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor

public class IncomeService {
    private  final CategoryRepo categoryRepo;
    private  final IncomeRepo incomeRepo;
    private final ProfileService profileService;


    //helper methods

    public IncomeDTO addIncome(IncomeDTO dto){
        ProfileEntity profile=profileService.getCurrentProfile();
        CategoryEntity category=  categoryRepo.findById(dto.getCategoryId())
                .orElseThrow(()-> new RuntimeException("Category not found "));
        IncomeEntity incomeEntity= toEntity(dto,profile,category);
        incomeRepo.save(incomeEntity);
        return  toDto(incomeEntity);
    }
    // read all income from the start and end date for the curr user
    public List<IncomeDTO> getCurrentMonthIncomesFortheCurrentProfile(){
        ProfileEntity profile=   profileService.getCurrentProfile();
        LocalDate now=LocalDate.now();
        LocalDate startDate=now.withDayOfMonth(1);
        LocalDate endDate=now.withDayOfMonth(now.lengthOfMonth());
        List<IncomeEntity> list=incomeRepo.findByProfileIdAndDateBetween(profile.getId(),startDate,endDate);
        return list.stream().map(this::toDto).toList();
    }

    // delete income by id for curr user
    public void deleteIncome(Long incomeId) {
        ProfileEntity profile = profileService.getCurrentProfile();

      IncomeEntity entity = incomeRepo.findById(incomeId)
                .orElseThrow(() -> new RuntimeException("Income not found"));

        if (!entity.getProfile().getId().equals(profile.getId())) {
            throw new RuntimeException("Unauthorized to delete this Income");
        }
        incomeRepo.delete(entity);
    }

    // Get latest 5 incomes for current user
    public List<IncomeDTO> getLatest5IncomeForCurrentUser() {
        ProfileEntity profile = profileService.getCurrentProfile();
        List<IncomeEntity> list = incomeRepo.findTop5ByProfileIdOrderByDateDesc(profile.getId());
        return list.stream().map(this::toDto).toList();
    }

    // Get total income for current user
    public BigDecimal getTotalIncomeForCurrentUser() {
        ProfileEntity profile = profileService.getCurrentProfile();
        BigDecimal total = incomeRepo.findTotalIncomeByProfileId(profile.getId());
        return total != null ? total : BigDecimal.ZERO;
    }

    //filter incomes
    public  List<IncomeDTO> filterIncome(LocalDate startDate, LocalDate endDate, String keyword, Sort sort){
        ProfileEntity profile = profileService.getCurrentProfile();
        List<IncomeEntity> list =  incomeRepo.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(profile.getId(), startDate,endDate,keyword,sort);
        return list.stream().map(this::toDto).toList();

    }


        private IncomeEntity toEntity(IncomeDTO dto, ProfileEntity profile, CategoryEntity categoryEntity){
        return IncomeEntity.builder()
                .name(dto.getName())
                .icon(dto.getIcon())
                .amount(dto.getAmmount())
                .date(dto.getDate())
                .profile(profile)
                .category(categoryEntity)
                .build();
    }
    private  IncomeDTO toDto(IncomeEntity entity){
        return IncomeDTO.builder()
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
