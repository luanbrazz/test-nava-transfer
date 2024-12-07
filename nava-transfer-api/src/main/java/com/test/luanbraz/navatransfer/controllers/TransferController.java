package com.test.luanbraz.navatransfer.controllers;

import com.test.luanbraz.navatransfer.dto.TransferRequest;
import com.test.luanbraz.navatransfer.dto.TransferResponse;
import com.test.luanbraz.navatransfer.services.TransferService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PATCH})
@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    public ResponseEntity<TransferResponse> createTransfer(@RequestBody TransferRequest request) {
        return ResponseEntity.ok(transferService.createTransfer(request));
    }

    @GetMapping
    public ResponseEntity<List<TransferResponse>> getAllTransfers() {
        return ResponseEntity.ok(transferService.getAllTransfers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransferResponse> getTransferById(@PathVariable Long id) {
        return ResponseEntity.ok(transferService.getTransferById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TransferResponse> cancelTransfer(@PathVariable Long id) {
        return ResponseEntity.ok(transferService.cancelTransferById(id));
    }
}
