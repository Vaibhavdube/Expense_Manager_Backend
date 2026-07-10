package in.vaibhav.moneymanager.controller;

import in.vaibhav.moneymanager.dto.IncomeDTO;
import in.vaibhav.moneymanager.service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/income")
public class IncomeController {

    private final IncomeService incomeService;

    @PostMapping
    public ResponseEntity<IncomeDTO> addIncome(@RequestBody IncomeDTO incomeDTO) {

        IncomeDTO saved = incomeService.addIncome(incomeDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<IncomeDTO>> getIncome() {

        List<IncomeDTO> income =
                incomeService.getCurrentMonthIncomeForCurrentUser();

        return ResponseEntity.ok(income);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncome(@PathVariable Long id) {

        incomeService.deleteIncome(id);

        return ResponseEntity.noContent().build();
    }
}