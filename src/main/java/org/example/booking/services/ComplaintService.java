package org.example.booking.services;

import org.example.booking.models.Complaint;
import org.example.booking.enums.ComplaintStatus;
import org.example.booking.models.User;
import org.example.booking.repositories.ComplaintRepository;
import org.example.booking.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ComplaintService {
    private final ComplaintRepository complaintRepository;
    private final UserRepository userRepository;

    public Complaint createComplaint(Long userId, String complaintText) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
        Complaint complaint = new Complaint();
        complaint.setUser(user);
        complaint.setComplaintText(complaintText);
        return complaintRepository.save(complaint);
    }

    public Complaint updateComplaint(Long complaintId, ComplaintStatus status, String adminResponse) {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new RuntimeException("Complaint không tồn tại"));
        complaint.setStatus(status);
        complaint.setAdminResponse(adminResponse);
        complaint.setUpdatedAt(LocalDateTime.now());
        return complaintRepository.save(complaint);
    }
}
