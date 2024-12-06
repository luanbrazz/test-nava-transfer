package com.test.luanbraz.navatransfer.services.impl;

import com.test.luanbraz.navatransfer.dto.TransferRequest;
import com.test.luanbraz.navatransfer.dto.TransferResponse;
import com.test.luanbraz.navatransfer.dto.errors.CustomErrorResponse;
import com.test.luanbraz.navatransfer.entities.Transfer;
import com.test.luanbraz.navatransfer.entities.enuns.TransferStatus;
import com.test.luanbraz.navatransfer.exceptions.CustomException;
import com.test.luanbraz.navatransfer.repositories.TransferRepository;
import com.test.luanbraz.navatransfer.services.TransferService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class TransferServiceImpl implements TransferService {

    private final TransferRepository transferRepository;

    public TransferServiceImpl(TransferRepository transferRepository) {
        this.transferRepository = transferRepository;
    }

    @Override
    public TransferResponse createTransfer(TransferRequest request) {
        LocalDate today = LocalDate.now();
        long daysDifference = ChronoUnit.DAYS.between(today, request.getTransferDate());

        BigDecimal fee = calculateFee(request.getAmount(), daysDifference);

        if (fee == null) {
            throw new CustomException(createErrorResponse("Erro na transferência", "Nenhuma taxa aplicável para a data de transferência determinada."));
        }

        Transfer transfer = buildTransfer(request, today, fee);
        transfer.setStatus(request.getTransferDate().isEqual(today) ? TransferStatus.COMPLETED : TransferStatus.PENDING);

        Transfer savedTransfer = transferRepository.save(transfer);

        return toTransferResponse(savedTransfer);
    }

    private BigDecimal calculateFee(BigDecimal amount, long daysDifference) {
        if (daysDifference == 0) return BigDecimal.valueOf(3).add(amount.multiply(BigDecimal.valueOf(0.025)));
        if (daysDifference >= 1 && daysDifference <= 10) return BigDecimal.valueOf(12);
        if (daysDifference >= 11 && daysDifference <= 20) return amount.multiply(BigDecimal.valueOf(0.082));
        if (daysDifference >= 21 && daysDifference <= 30) return amount.multiply(BigDecimal.valueOf(0.069));
        if (daysDifference >= 31 && daysDifference <= 40) return amount.multiply(BigDecimal.valueOf(0.047));
        if (daysDifference >= 41 && daysDifference <= 50) return amount.multiply(BigDecimal.valueOf(0.017));
        return null;
    }

    private CustomErrorResponse createErrorResponse(String error, String message) {
        return new CustomErrorResponse(
                LocalDateTime.now(),
                error,
                message
        );
    }

    private Transfer buildTransfer(TransferRequest request, LocalDate today, BigDecimal fee) {
        Transfer transfer = new Transfer();
        transfer.setSourceAccount(request.getSourceAccount());
        transfer.setDestinationAccount(request.getDestinationAccount());
        transfer.setAmount(request.getAmount());
        transfer.setFee(fee);
        transfer.setTransferDate(request.getTransferDate());
        transfer.setScheduleDate(today);
        return transfer;
    }

    private TransferResponse toTransferResponse(Transfer transfer) {
        return new TransferResponse(
                transfer.getId(),
                transfer.getSourceAccount(),
                transfer.getDestinationAccount(),
                transfer.getAmount(),
                transfer.getFee(),
                transfer.getTransferDate(),
                transfer.getScheduleDate(),
                transfer.getStatus()
        );
    }
}
