package com.example.demo.controller;

import com.example.demo.model.StatusResponse;
import com.example.demo.service.TranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TranslationController {
    private final TranslationService translationService;

    @Autowired
    public TranslationController(TranslationService translationService) {
        this.translationService = translationService;
    }

    @GetMapping("/status")
    public StatusResponse getStatus() {
        return new StatusResponse(translationService.checkStatus());
    }
}
