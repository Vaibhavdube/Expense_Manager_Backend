package in.vaibhav.moneymanager.dto;


import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IncomeDTO {

    private Long id;

    private String name;

    private String icon;

    private LocalDate date;

    private BigDecimal amount;

    private Long categoryId;

    private String categoryName;

    private Long profileId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}