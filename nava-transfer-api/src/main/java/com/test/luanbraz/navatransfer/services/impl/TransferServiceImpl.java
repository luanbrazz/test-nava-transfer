package com.test.luanbraz.navatransfer.services.impl;

import com.test.luanbraz.navatransfer.dto.TransferRequest;
import com.test.luanbraz.navatransfer.dto.TransferResponse;
import com.test.luanbraz.navatransfer.dto.errors.CustomErrorResponse;
import com.test.luanbraz.navatransfer.entities.Transfer;
import com.test.luanbraz.navatransfer.entities.enuns.TransferStatus;
import com.test.luanbraz.navatransfer.exceptions.CustomException;
import com.test.luanbraz.navatransfer.repositories.CustomerRepository;
import com.test.luanbraz.navatransfer.repositories.TransferRepository;
import com.test.luanbraz.navatransfer.services.TransferService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class TransferServiceImpl implements TransferService {

    private final TransferRepository transferRepository;
    private final CustomerRepository userRepository;

    public TransferServiceImpl(TransferRepository transferRepository, CustomerRepository userRepository) {
        this.transferRepository = transferRepository;
        this.userRepository = userRepository;
    }

    @Override
    public TransferResponse createTransfer(TransferRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(
                        createErrorResponse("Erro de autenticação", "Usuário não encontrado."),
                        HttpStatus.UNAUTHORIZED
                )).getId();

        LocalDate today = LocalDate.now();
        long daysDifference = ChronoUnit.DAYS.between(today, request.getTransferDate());

        BigDecimal fee = calculateFee(request.getAmount(), daysDifference);

        if (fee == null) {
            throw new CustomException(
                    createErrorResponse("Erro de negócio", "Nenhuma taxa aplicável para a data de transferência determinada."),
                    HttpStatus.UNPROCESSABLE_ENTITY
            );
        }

        Transfer transfer = buildTransfer(request, today, fee, userId);
        transfer.setStatus(request.getTransferDate().isEqual(today) ? TransferStatus.COMPLETED : TransferStatus.PENDING);

        Transfer savedTransfer = transferRepository.save(transfer);

        return toTransferResponse(savedTransfer);
    }

    @Override
    public List<TransferResponse> getAllTransfers() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(
                        createErrorResponse("Erro de autenticação", "Usuário não encontrado."),
                        HttpStatus.UNAUTHORIZED
                )).getId();

        return transferRepository.findByUserId(userId).stream()
                .map(this::toTransferResponse)
                .toList();
    }

    @Override
    public TransferResponse getTransferById(Long id) {
        Transfer transfer = findTransferByIdOrThrow(id, "Erro na recuperação da transferência", "Transferência não encontrada.");
        return toTransferResponse(transfer);
    }

    @Override
    public TransferResponse cancelTransferById(Long id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(
                        createErrorResponse("Erro de autenticação", "Usuário não encontrado."),
                        HttpStatus.UNAUTHORIZED
                )).getId();

        Transfer transfer = findTransferByIdOrThrow(id, "Erro de cancelamento de transferência", "Transferência não encontrada.");

        if (!transfer.getUserId().equals(userId)) {
            throw new CustomException(createErrorResponse(
                    "Erro de autorização", "Você não tem permissão para cancelar esta transferência."
            ), HttpStatus.FORBIDDEN);
        }

        if (!transfer.getTransferDate().isAfter(LocalDate.now())) {
            throw new CustomException(createErrorResponse(
                    "Erro de negócio",
                    "Não é possível cancelar uma transferência agendada para hoje ou para uma data anterior."
            ), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        transfer.setStatus(TransferStatus.CANCELLED);
        Transfer updatedTransfer = transferRepository.save(transfer);
        return toTransferResponse(updatedTransfer);
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

    private Transfer buildTransfer(TransferRequest request, LocalDate today, BigDecimal fee, Long userId) {
        Transfer transfer = new Transfer();
        transfer.setSourceAccount(request.getSourceAccount());
        transfer.setDestinationAccount(request.getDestinationAccount());
        transfer.setAmount(request.getAmount());
        transfer.setFee(fee);
        transfer.setTransferDate(request.getTransferDate());
        transfer.setScheduleDate(today);
        transfer.setUserId(userId);
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

    private Transfer findTransferByIdOrThrow(Long id, String errorTitle, String errorMessage) {
        return transferRepository.findById(id)
                .orElseThrow(() -> new CustomException(
                        createErrorResponse(errorTitle, errorMessage),
                        HttpStatus.NOT_FOUND
                ));
    }

}
