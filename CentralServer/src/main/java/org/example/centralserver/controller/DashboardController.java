package org.example.centralserver.controller;


import org.example.centralserver.entities.DTO.DashboardOverviewDTO;
import org.example.centralserver.entities.Transection;
import org.example.centralserver.repo.mongo.AccountRepo;
import org.example.centralserver.repo.mongo.TransectionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/dashboard")
public class DashboardController {


    @Autowired
    AccountRepo accountRepo;


    @Autowired
    TransectionRepo transectionRepo;

    @GetMapping
    public ResponseEntity<DashboardOverviewDTO> getDashboard() {

        DashboardOverviewDTO dashboardDTO = new DashboardOverviewDTO();

        long totalAccounts = accountRepo.count();
        long totalSuspiciousAccounts = accountRepo.countByIsSuspiciousTrue();

        // Count transactions
        long totalTransactions = transectionRepo.count();
        long suspiciousTransactions = transectionRepo.countBySuspiciousTrue();

        // Set data in DTO
        dashboardDTO.setTotalAccounts(totalAccounts);
        dashboardDTO.setTotalSuspiciousAccounts(totalSuspiciousAccounts);
        dashboardDTO.setTotalTransactions(totalTransactions);
        dashboardDTO.setTotalSuspiciousTransactions(suspiciousTransactions);



        return ResponseEntity.ok(dashboardDTO);

    }

}
