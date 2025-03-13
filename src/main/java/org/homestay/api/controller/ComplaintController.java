package org.homestay.api.controller;

import org.homestay.api.model.Complaint;
import org.homestay.api.model.ComplaintStatus;
import org.homestay.api.service.ComplaintService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/complaints")
@RequiredArgsConstructor
public class ComplaintController {
    private final ComplaintService complaintService;

    // Người dùng tạo khiếu nại
    @PostMapping
    public ResponseEntity<Complaint> createComplaint(@RequestParam String complaintText,
                                                     @AuthenticationPrincipal UserDetails user) {
        Long userId = ((org.homestay.api.model.User) user).getId();
        Complaint complaint = complaintService.createComplaint(userId, complaintText);
        return ResponseEntity.ok(complaint);
    }

    // Admin cập nhật trạng thái khiếu nại
    @PutMapping("/{complaintId}")
    public ResponseEntity<Complaint> updateComplaint(@PathVariable Long complaintId,
                                                     @RequestParam ComplaintStatus status,
                                                     @RequestParam String adminResponse) {
        Complaint complaint = complaintService.updateComplaint(complaintId, status, adminResponse);
        return ResponseEntity.ok(complaint);
    }
}
