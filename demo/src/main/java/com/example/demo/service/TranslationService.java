package com.example.demo.service;

import com.example.demo.model.TranslationStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Random;

@Service
public class TranslationService {
    @Value("${translation.processing.min-time:5000}")
    private long minProcessingTime;

    @Value("${translation.processing.max-time:15000}")
    private long maxProcessingTime;

    @Value("${translation.error.probability:0.1}")
    private double errorProbability;

    private final Random random = new Random();
    private Instant startTime;
    private long processingTime;
    private boolean hasError;

    public void initializeTranslation() {
        startTime = Instant.now();
        processingTime = minProcessingTime + (long) (random.nextDouble() * (maxProcessingTime - minProcessingTime));
        hasError = random.nextDouble() < errorProbability;
    }

    public TranslationStatus checkStatus() {
        if (startTime == null) {
            initializeTranslation();
        }

        long elapsedTime = Instant.now().toEpochMilli() - startTime.toEpochMilli();

        if (elapsedTime < processingTime) {
            return TranslationStatus.PENDING;
        }

        return hasError ? TranslationStatus.ERROR : TranslationStatus.COMPLETED;
    }
}
