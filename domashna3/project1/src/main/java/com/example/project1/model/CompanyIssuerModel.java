package com.example.project1.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "issuers")
@Data
@NoArgsConstructor
class CompanyIssuer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long issuerId;

    @Column(name = "issuer_code")
    private String issuerCode;

    @Column(name = "last_update_date")
    private LocalDate lastUpdateDate;

    @OneToMany(mappedBy = "issuer", fetch = FetchType.EAGER)
    private List<HistoricalRecord> records;

    public CompanyIssuer(String issuerCode) {
        this.issuerCode = issuerCode;
    }
}

