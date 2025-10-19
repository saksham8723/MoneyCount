package in.SakshamProject.moneycount.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name="tbl_profiles")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
private  String fullName;
@Column(unique = true)
private String email;
private  String password;
private  String profileImageUrl;
@Column(updatable = false)
@CreationTimestamp
private LocalDateTime createdAt;
@UpdateTimestamp
private LocalDateTime updateAt;
private Boolean isActive;
private String activationToken;

//we want to run this code  automatically just before saving the  object into the database;
@PrePersist
public void prePersist(){
    if(this.isActive==null){
        isActive=false;
    }
}


}
