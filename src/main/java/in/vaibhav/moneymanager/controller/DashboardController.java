package in.vaibhav.moneymanager.controller;


import in.vaibhav.moneymanager.service.DashboardService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;




    @GetMapping
    public ResponseEntity<Map<String, Object>> getDashboardDate() {
        Map<String, Object> dashboardData =dashboardService.getDashboardData();
      return ResponseEntity.ok(dashboardData);
    }

}
