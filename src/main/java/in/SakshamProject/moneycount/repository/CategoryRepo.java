package in.SakshamProject.moneycount.repository;

import in.SakshamProject.moneycount.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepo extends JpaRepository<CategoryEntity,Long> {
    //Behind the secene select * from  tbl_categories where profile_id=?
    List<CategoryEntity> findByProfileId(Long profileId);
    //select * from tbl_categories where  profile_id=? AND id=?
   Optional<CategoryEntity> findByIdAndProfileId(Long id, Long profileId);



    List<CategoryEntity>     findByTypeAndProfileId(String type,Long profileId);



      Boolean  existsByNameAndProfileId(String name,Long profileId);
}
