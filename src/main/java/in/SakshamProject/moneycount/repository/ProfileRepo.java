package in.SakshamProject.moneycount.repository;

import in.SakshamProject.moneycount.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepo extends JpaRepository<ProfileEntity,Long> {



    //Behind the seens Jpa will exceute the query select * from tbl_profiles where email=?
    Optional<ProfileEntity> findByEmail(String email);

    //behing the seen jpa will exceute the query select * fromt tbl_profiles where activationtoken=?
     Optional<ProfileEntity> findByActivationToken(String activationtoker);

}
