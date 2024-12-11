package com.example.peaceful_land.Repository;

import com.example.peaceful_land.Entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findById(Long id);
    Optional<Account> findByEmail(String email);
    Optional<Account> findByAvatarUrl(String avatarUrl);
    List<Account> findAllByRoleExpirationBefore(LocalDate expiration);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);

    @Query("SELECT a.avatarUrl FROM Account a")
    List<String> findAllAvatarUrls();

}
