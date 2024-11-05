package com.example.peaceful_land.Repository;

import com.example.peaceful_land.Entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
