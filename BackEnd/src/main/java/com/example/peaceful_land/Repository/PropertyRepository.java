package com.example.peaceful_land.Repository;

import com.example.peaceful_land.Entity.Account;
import com.example.peaceful_land.Entity.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.List;

public interface PropertyRepository extends JpaRepository<Property, Long>, JpaSpecificationExecutor<Property> {
    long countByUserEquals(Account account);
    long countByDateBeginBetweenAndUserEquals(LocalDateTime dateBegin, LocalDateTime dateEnd, Account account);
    List<Property> findByUserEquals(Account account);
    Page<Property> findAll(Specification<Property> spec, Pageable pageable);
}
