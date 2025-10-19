package in.SakshamProject.moneycount.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IncomeDTO {
    private Long id;
    @NotBlank
    private String name;
    private String icon;
    private  String categoryName;
    @NotNull
    private Long categoryId;
    @NotNull
    @Positive
    private BigDecimal ammount;
    @NotNull
    @PastOrPresent
    private LocalDate date;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
