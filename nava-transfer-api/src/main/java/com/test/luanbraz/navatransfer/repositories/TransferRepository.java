package com.test.luanbraz.navatransfer.repositories;

import com.test.luanbraz.navatransfer.entities.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
}
