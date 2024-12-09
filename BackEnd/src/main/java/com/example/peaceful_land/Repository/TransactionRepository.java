package com.example.peaceful_land.Repository;

import com.example.peaceful_land.Entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
