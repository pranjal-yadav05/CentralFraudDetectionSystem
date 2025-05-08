package org.example.centralserver.entities.DTO;



public class DashboardOverviewDTO {

    private Long totalAccounts;
    private Long totalTransactions;
    private Long totalSuspiciousAccounts;
    private Long totalSuspiciousTransactions;

    public DashboardOverviewDTO() {}

    public DashboardOverviewDTO(Long totalAccounts, Long totalTransactions, Long totalSuspiciousAccounts, Long totalSuspiciousTransactions) {
        this.totalAccounts = totalAccounts;
        this.totalTransactions = totalTransactions;
        this.totalSuspiciousAccounts = totalSuspiciousAccounts;
        this.totalSuspiciousTransactions = totalSuspiciousTransactions;
    }


    public Long getTotalAccounts() {
        return totalAccounts;
    }

    public void setTotalAccounts(Long totalAccounts) {
        this.totalAccounts = totalAccounts;
    }

    public Long getTotalTransactions() {
        return totalTransactions;
    }

    public void setTotalTransactions(Long totalTransactions) {
        this.totalTransactions = totalTransactions;
    }

    public Long getTotalSuspiciousAccounts() {
        return totalSuspiciousAccounts;
    }

    public void setTotalSuspiciousAccounts(Long totalSuspiciousAccounts) {
        this.totalSuspiciousAccounts = totalSuspiciousAccounts;
    }

    public Long getTotalSuspiciousTransactions() {
        return totalSuspiciousTransactions;
    }

    public void setTotalSuspiciousTransactions(Long totalSuspiciousTransactions) {
        this.totalSuspiciousTransactions = totalSuspiciousTransactions;
    }
}
