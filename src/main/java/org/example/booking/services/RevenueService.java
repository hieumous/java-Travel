package org.example.booking.services;

import org.example.booking.models.Homestay;
import org.example.booking.models.RevenueReport;
import org.example.booking.repositories.RevenueReportRepository;
import org.example.booking.repositories.HomestayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RevenueService {
    private final RevenueReportRepository revenueReportRepository;
    private final HomestayRepository homestayRepository;

    // Tính doanh thu vào ngày đầu tiên của tháng (ví dụ: 0 0 0 1 * ?)
    @Scheduled(cron = "0 0 0 1 * ?")
    public void calculateMonthlyRevenue() {
        List<Homestay> homestays = homestayRepository.findAll();
        LocalDate currentMonth = LocalDate.now().withDayOfMonth(1);

        for (Homestay homestay : homestays) {
            // Giả lập tính toán doanh thu, số lượt đặt, tỷ lệ lấp đầy
            double totalIncome = 0.0; // Thay bằng logic tính tổng thu nhập thật sự
            int numberOfBookings = 0; // Số lượt đặt phòng
            double occupancyRate = 0.0; // Tỷ lệ lấp đầy

            double serviceFee = totalIncome * 0.20;

            RevenueReport report = new RevenueReport();
            report.setHomestay(homestay);
            report.setTotalIncome(totalIncome);
            report.setNumberOfBookings(numberOfBookings);
            report.setOccupancyRate(occupancyRate);
            report.setServiceFee(serviceFee);
            report.setMonth(currentMonth);

            revenueReportRepository.save(report);
        }
    }

    public List<RevenueReport> getRevenueReportByMonth(LocalDate month) {
        return revenueReportRepository.findByMonth(month);
    }
}
