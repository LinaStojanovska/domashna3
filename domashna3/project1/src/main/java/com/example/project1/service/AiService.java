package com.example.project1.service;

import com.example.project1.model.CompanyIssuerModel;
import com.example.project1.model.CompanyDataModel;
import com.example.project1.dto.Response;
import com.example.project1.repository.CompanyDataRepository;
import com.example.project1.repository.CompanyIssuerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsIntegrationService {

    private final RestTemplate apiClient = new RestTemplate();
    private final CompanyDataRepository dataRepository;
    private final CompanyIssuerRepository issuerRepository;

    private final String analysisEndpoint = "http://127.0.0.1:5000/generate_signal";
    private final String sentimentEndpoint = "http://127.0.0.1:5000/analyze";
    private final String forecastEndpoint = "http://127.0.0.1:8000/predict-next-month-price/";

    public String generateTechnicalSignal(Long companyId) {
        List<CompanyDataModel> historicalData = dataRepository.findByCompanyId(companyId);

        List<Map<String, Object>> payload = new ArrayList<>();
        for (CompanyDataModel entry : historicalData) {
            Map<String, Object> record = new HashMap<>();
            record.put("date", entry.getDate().toString());
            record.put("close", entry.getLastTransactionPrice());
            record.put("open", (entry.getMaxPrice() + entry.getMinPrice()) / 2.0);
            record.put("high", entry.getMaxPrice());
            record.put("low", entry.getMinPrice());
            record.put("volume", entry.getQuantity());
            payload.add(record);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<Map<String, Object>>> requestEntity = new HttpEntity<>(payload, headers);

        ResponseEntity<Map> response = apiClient.exchange(
                analysisEndpoint,
                HttpMethod.POST,
                requestEntity,
                Map.class
        );

        Map<String, Object> responseBody = response.getBody();
        if (responseBody != null && responseBody.containsKey("final_signal")) {
            return responseBody.get("final_signal").toString();
        } else {
            throw new RuntimeException("Unable to retrieve a valid signal from the API.");
        }
    }

    public Response performSentimentAnalysis(Long companyId) throws Exception {
        CompanyIssuerModel issuer = issuerRepository.findById(companyId)
                .orElseThrow(() -> new Exception("Issuer not found"));

        String issuerCode = issuer.getCompanyCode();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<Map> response = apiClient.exchange(
                sentimentEndpoint + "?company_code=" + issuerCode,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                Map.class
        );

        Map<String, Object> responseBody = response.getBody();
        if (responseBody != null) {
            if (responseBody.containsKey("error")) {
                String error = (String) responseBody.get("error");
                throw new RuntimeException("API Error: " + error);
            }

            Response analysisResult = new Response();
            analysisResult.sentimentScore = (Double) responseBody.get("sentiment_score");
            analysisResult.recommendation = (String) responseBody.get("recommendation");
            return analysisResult;
        } else {
            throw new RuntimeException("Failed to retrieve sentiment analysis results.");
        }
    }

    public Double predictPriceUsingLSTM(Long companyId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        List<CompanyDataModel> recentData = dataRepository.findByCompanyIdAndDateBetween(
                companyId, LocalDate.now().minusMonths(3), LocalDate.now());

        Map<String, Object> requestBody = Map.of("data", mapDataForRequest(recentData));

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        Map<String, Double> predictionResponse = apiClient.postForObject(
                forecastEndpoint, requestEntity, Map.class);

        return predictionResponse != null ? predictionResponse.get("predicted_next_month_price") : null;
    }

    private static List<Map<String, Object>> mapDataForRequest(List<CompanyDataModel> data) {
        return data.stream().map(record -> {
            Map<String, Object> map = new HashMap<>();
            map.put("date", record.getDate().toString());
            map.put("average_price", record.getAveragePrice());
            return map;
        }).collect(Collectors.toList());
    }
}
