package org.example.booking.services;
import org.example.booking.repositories.HomestayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class HomestayDeletionService {
    @Autowired
    private HomestayRepository homestayRepository;

    public void deleteHomestay(Long id) {
        homestayRepository.deleteById(id);
    }
}
