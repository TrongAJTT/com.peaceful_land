package com.example.peaceful_land.Repository;

import com.example.peaceful_land.Entity.Account;
import com.example.peaceful_land.Entity.RequestWithdraw;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RequestWithdrawRepository extends JpaRepository<RequestWithdraw, Long> {
    boolean existsByAccountEqualsAndDateBeginBetween(Account account, LocalDateTime dateBegin, LocalDateTime dateEnd);
    List<RequestWithdraw> findAllByOrderByDateBeginDesc();
    List<RequestWithdraw> findAllByStatusEqualsOrderByDateBeginDesc(Byte status);
}
