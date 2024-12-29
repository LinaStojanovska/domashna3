package com.example.project1.repository;

import com.example.project1.model.CompanyIssuerModel;
import com.example.project1.model.CompanyDataModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EnterpriseDataHandler extends JpaRepository<CompanyDataModel, Long> {
    Optional<CompanyDataModel> fetchByDateAndIssuer(LocalDate date, CompanyIssuerModel issuer);
    List<CompanyDataModel> retrieveByIssuerIdAndDateRange(Long issuerId, LocalDate startDate, LocalDate endDate);
    List<CompanyDataModel> retrieveByIssuerId(Long issuerId);
}
