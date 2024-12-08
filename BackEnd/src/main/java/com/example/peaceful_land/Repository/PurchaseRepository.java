package com.example.peaceful_land.Repository;

import com.example.peaceful_land.Entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
}
