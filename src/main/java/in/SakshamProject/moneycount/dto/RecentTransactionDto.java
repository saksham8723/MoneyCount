package in.SakshamProject.moneycount.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class RecentTransactionDto {
    private  Long id;
    private Long profileId;
    private  String icon;
    private  String name;
    private BigDecimal ammount;
    private LocalDate date;
    private LocalDateTime createdAt;
    private  LocalDateTime updateAt;
    private  String type;



}
