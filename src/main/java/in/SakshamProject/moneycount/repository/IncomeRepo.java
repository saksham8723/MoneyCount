package in.SakshamProject.moneycount.repository;

import in.SakshamProject.moneycount.entity.ExpenseEntity;
import in.SakshamProject.moneycount.entity.IncomeEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface IncomeRepo extends JpaRepository<IncomeEntity,Long> {
    List<IncomeEntity> findByProfileIdOrderByDateDesc(Long profileid);
    List<IncomeEntity> findTop5ByProfileIdOrderByDateDesc(Long profileid);

    @Query("Select sum(i.amount) from IncomeEntity i where i.profile.id=:profileId")
    BigDecimal findTotalIncomeByProfileId(@Param("profileId")Long profileId);

    //select * from tbl_incomes where profile_id=?1 and date between ?2 and ?3 and name like %?4%
    List<IncomeEntity> findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(
            Long profileId,
            LocalDate startDate,
            LocalDate endDate,
            String keyword,
            Sort sort

    );
    //select * from tbl_incomes  where profileId=?1 and date between ?2 and ?3
    List<IncomeEntity> findByProfileIdAndDateBetween(Long profileId,LocalDate startDate,LocalDate endDate);
}
