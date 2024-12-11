package com.example.peaceful_land.Repository;

import com.example.peaceful_land.Entity.RequestReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestReportRepository extends JpaRepository<RequestReport, Long> {
    List<RequestReport> findAllByOrderByIdDesc();
    List<RequestReport> findAllByHideEqualsOrderByIdDesc(Boolean hide);
}
