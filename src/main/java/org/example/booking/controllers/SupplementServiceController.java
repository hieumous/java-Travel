package org.example.booking.controllers;

import org.example.booking.models.SupplementService;
import org.example.booking.services.SupplementServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/supplements")
@Validated
public class SupplementServiceController {

    private final SupplementServiceService supplementService;

    @Autowired
    public SupplementServiceController(SupplementServiceService supplementService) {
        this.supplementService = supplementService;
    }

    @GetMapping
    public ResponseEntity<List<SupplementService>> getAllSupplements() {
        List<SupplementService> supplements = supplementService.getAllSupplements();
        return ResponseEntity.ok(supplements);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplementService> getSupplementById(@PathVariable Long id) {
        SupplementService supplement = supplementService.getSupplementById(id);
        return ResponseEntity.ok(supplement);
    }

    @PostMapping
    public ResponseEntity<SupplementService> createSupplement(@RequestBody SupplementService.NewSupplementServiceRequest supplementRequest) {
        SupplementService createdSupplement = supplementService.createSupplement(supplementRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSupplement);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplementService> updateSupplement(@PathVariable Long id, @RequestBody SupplementService.UpdateSupplementServiceRequest supplementRequest) {
        SupplementService updatedSupplement = supplementService.updateSupplement(id, supplementRequest);
        return ResponseEntity.ok(updatedSupplement);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplement(@PathVariable Long id) {
        supplementService.deleteSupplement(id);
        return ResponseEntity.noContent().build();
    }

    // Xử lý lỗi cho các phương thức (nếu cần)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}