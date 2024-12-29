package com.example.project1.service;

import com.example.project1.model.IssuerModel;
import com.example.project1.repository.IssuerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IssuerManagementService {

    private final IssuerRepository issuerRepository;

    public List<IssuerModel> fetchAllIssuers() {
        return issuerRepository.findAllByOrderByIdAsc();
    }

    public IssuerModel fetchIssuerById(Long id) throws Exception {
        return issuerRepository.findById(id)
                .orElseThrow(() -> new Exception("Issuer with ID " + id + " not found"));
    }
}
