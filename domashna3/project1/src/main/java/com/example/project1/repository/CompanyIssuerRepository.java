package com.example.project1.repository;

import com.example.project1.model.CompanyIssuerModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IssuerDataHandler extends JpaRepository<CompanyIssuerModel, Long> {
    Optional<CompanyIssuerModel> findIssuerByCode(String issuerCode);
    List<CompanyIssuerModel> fetchAllOrderedByIdAsc();
}
