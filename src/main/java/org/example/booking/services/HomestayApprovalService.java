package org.example.booking.services;
import org.example.booking.repositories.HomestayRepository;
import org.example.booking.models.Homestay;
import org.example.booking.enums.HomestayStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
@Service
public class HomestayApprovalService {
    @Autowired
    private HomestayRepository homestayRepository;

    public List<Homestay> getPendingHomestays() {
        return homestayRepository.findByStatus(HomestayStatus.PENDING);
    }

    public Homestay approveHomestay(Long homestayId) {
        Optional<Homestay> homestayOpt = homestayRepository.findById(homestayId);
        if (homestayOpt.isPresent()) {
            Homestay homestay = homestayOpt.get();
            homestay.setStatus(HomestayStatus.APPROVED);
            return homestayRepository.save(homestay);
        }
        throw new RuntimeException("Homestay không tồn tại!");
    }
}
