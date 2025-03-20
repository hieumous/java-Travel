package org.example.booking.services;

import org.example.booking.models.SupplementService;
import org.example.booking.repositories.SupplementServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplementServiceService {

    private final SupplementServiceRepository supplementServiceRepository;

    @Autowired
    public SupplementServiceService(SupplementServiceRepository supplementServiceRepository) {
        this.supplementServiceRepository = supplementServiceRepository;
    }

    // Lấy tất cả các dịch vụ bổ sung
    public List<SupplementService> getAllSupplements() {
        return supplementServiceRepository.findAll();
    }

    // Lấy dịch vụ bổ sung theo ID
    public SupplementService getSupplementById(Long id) {
        return supplementServiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplement not found with id: " + id));
    }

    // Tạo dịch vụ bổ sung mới
    public SupplementService createSupplement(SupplementService.NewSupplementServiceRequest request) {
        SupplementService supplementService = new SupplementService();
        supplementService.setName(request.getName());
        supplementService.setDescription(request.getDescription());
        supplementService.setPrice(request.getPrice());
        return supplementServiceRepository.save(supplementService);
    }

    // Cập nhật dịch vụ bổ sung
    public SupplementService updateSupplement(Long id, SupplementService.UpdateSupplementServiceRequest request) {
        SupplementService existingSupplement = getSupplementById(id);
        if (request.getName() != null) {
            existingSupplement.setName(request.getName());
        }
        if (request.getDescription() != null) {
            existingSupplement.setDescription(request.getDescription());
        }
        if (request.getPrice() != null) {
            existingSupplement.setPrice(request.getPrice());
        }
        return supplementServiceRepository.save(existingSupplement);
    }

    // Xóa dịch vụ bổ sung
    public void deleteSupplement(Long id) {
        supplementServiceRepository.deleteById(id);
    }
}