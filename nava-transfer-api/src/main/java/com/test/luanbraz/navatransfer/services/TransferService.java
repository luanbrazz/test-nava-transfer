package com.test.luanbraz.navatransfer.services;

import com.test.luanbraz.navatransfer.dto.TransferRequest;
import com.test.luanbraz.navatransfer.dto.TransferResponse;

import java.util.List;

public interface TransferService {
    TransferResponse createTransfer(TransferRequest request);

    List<TransferResponse> getAllTransfers();

    TransferResponse getTransferById(Long id);
}
