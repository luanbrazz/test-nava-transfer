package com.test.luanbraz.navatransfer.services;

import com.test.luanbraz.navatransfer.dto.TransferRequest;
import com.test.luanbraz.navatransfer.dto.TransferResponse;

public interface TransferService {
    TransferResponse createTransfer(TransferRequest request);

}
