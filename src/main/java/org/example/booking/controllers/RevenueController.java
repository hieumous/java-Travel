package org.example.booking.controllers;

import org.example.booking.models.RevenueReport;
import org.example.booking.services.RevenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/revenues")
@RequiredArgsConstructor
public class RevenueController {
    private final RevenueService revenueService;

    // Lấy báo cáo doanh thu theo tháng (truyền ngày đầu tháng, ví dụ: 2023-01-01)
    @GetMapping
    public ResponseEntity<List<RevenueReport>> getRevenueReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate month) {
        List<RevenueReport> reports = revenueService.getRevenueReportByMonth(month.withDayOfMonth(1));
        return ResponseEntity.ok(reports);
    }
}
