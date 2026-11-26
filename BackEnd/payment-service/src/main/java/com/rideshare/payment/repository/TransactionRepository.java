package com.rideshare.payment.repository;

import com.rideshare.payment.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    Page<Transaction> findBySolde_IdChauffeurOrderByDateDesc(UUID idChauffeur, Pageable pageable);
}
