package org.example.booking.repositories;


import org.example.booking.models.RevenueReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface RevenueReportRepository extends JpaRepository<RevenueReport, Long> {

    // Lấy báo cáo doanh thu theo tháng (chỉ cần cung cấp ngày đầu tháng)
    List<RevenueReport> findByMonth(LocalDate month);
}
