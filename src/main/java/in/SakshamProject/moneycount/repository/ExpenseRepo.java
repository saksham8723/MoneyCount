package in.SakshamProject.moneycount.repository;

import in.SakshamProject.moneycount.entity.ExpenseEntity;

import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepo extends JpaRepository<ExpenseEntity,Long> {
   List<ExpenseEntity> findByProfileIdOrderByDateDesc(Long profileid);
   List<ExpenseEntity> findTop5ByProfileIdOrderByDateDesc(Long profileid);

   @Query("Select sum(e.amount) from ExpenseEntity e where e.profile.id=:profileId")
   BigDecimal findTotalExpenceByProfileId(@Param("profileId")Long profileId);

   //select * from tbl_expense where profile_id=?1 and date between ?2 and ?3 and name like %?4%
   List<ExpenseEntity> findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(
           Long profileId,
           LocalDate startDate,
           LocalDate endDate,
           String keyword,
          Sort sort

   );
   //select * from tbl_expense where profileId=?1 and date between ?2 and ?3
   List<ExpenseEntity> findByProfileIdAndDateBetween(Long profileId,LocalDate startDate,LocalDate endDate);


   //select * from expense where profileId=? and date=?
   List<ExpenseEntity>findByProfileIdAndDate(Long profileId, LocalDate date);
}
