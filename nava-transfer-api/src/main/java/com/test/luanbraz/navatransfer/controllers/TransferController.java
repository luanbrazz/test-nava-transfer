package com.test.luanbraz.navatransfer.controllers;

import com.test.luanbraz.navatransfer.dto.TransferRequest;
import com.test.luanbraz.navatransfer.dto.TransferResponse;
import com.test.luanbraz.navatransfer.services.TransferService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @GetMapping("/test")
    public String teste() {
        return "testing api...";
    }

    @PostMapping
    public ResponseEntity<TransferResponse> createTransfer(@RequestBody TransferRequest request) {
        return ResponseEntity.ok(transferService.createTransfer(request));
    }
}