package org.example.booking.services;
import org.example.booking.repositories.HomestayRepository;
import org.example.booking.repositories.BookingRepository;
import org.example.booking.models.Homestay;
import org.example.booking.models.Booking;
import org.example.booking.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class HomestayManagementService {
    @Autowired
    private HomestayRepository homestayRepository;
    @Autowired
    private BookingRepository bookingRepository;

    //Lấy danh sách homestay theo chủ sở hữu
    public List<Homestay> getHomestaysByOwner(User owner) {
        return homestayRepository.findByOwner(owner);
    }
    //Cập nhật thông tin homestay
    public Homestay updateHomestay(Homestay homestay) {
        return homestayRepository.save(homestay);
    }
    //Xem danh sách đặt phòng theo tháng
    public List<Booking> getMonthlyBookings(Long homestayId, int month) {
        return bookingRepository.findByHomestayIdAndMonth(homestayId, month);
    }
}
