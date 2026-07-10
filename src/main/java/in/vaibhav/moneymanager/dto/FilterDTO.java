package in.vaibhav.moneymanager.dto;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class FilterDTO {



    private String type;
    private LocalDate startDate;
    private LocalDate endDate;
    private String Keyword;
    private String sortField;
    private String sortOrder;

}
