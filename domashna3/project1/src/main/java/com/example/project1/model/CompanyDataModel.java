package com.example.project1.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "historical_records")
@Data
@NoArgsConstructor
public class HistoricalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recordId;

    @Column(name = "record_date")
    private LocalDate recordDate;

    @Column(name = "closing_price")
    private Double closingPrice;

    @Column(name = "highest_price")
    private Double highestPrice;

    @Column(name = "lowest_price")
    private Double lowestPrice;

    @Column(name = "mean_price")
    private Double meanPrice;

    @Column(name = "price_variation")
    private Double priceVariation;

    @Column(name = "units_traded")
    private Integer unitsTraded;

    @Column(name = "top_turnover")
    private Integer topTurnover;

    @Column(name = "total_turnover_amount")
    private Integer totalTurnoverAmount;

    @ManyToOne
    @JoinColumn(name = "issuer_id")
    private CompanyIssuer issuer;

    public HistoricalRecord(LocalDate recordDate, Double closingPrice, Double highestPrice, Double lowestPrice, Double meanPrice, Double priceVariation, Integer unitsTraded, Integer topTurnover, Integer totalTurnoverAmount) {
        this.recordDate = recordDate;
        this.closingPrice = closingPrice;
        this.highestPrice = highestPrice;
        this.lowestPrice = lowestPrice;
        this.meanPrice = meanPrice;
        this.priceVariation = priceVariation;
        this.unitsTraded = unitsTraded;
        this.topTurnover = topTurnover;
        this.totalTurnoverAmount = totalTurnoverAmount;
    }
}
