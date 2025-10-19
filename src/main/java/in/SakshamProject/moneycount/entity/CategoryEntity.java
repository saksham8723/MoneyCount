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
@Table(name="tbl_categories")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class CategoryEntity  {



    //automatic gernation for the values of the coloum

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     private  Long id;
    private  String name;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private String type;
    //many profile related to one profile
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="profile_id",nullable = false)
    private ProfileEntity profile;
    // we are going to store the url of icon we will use  emoji picker in frontend
    private  String icon;


}
