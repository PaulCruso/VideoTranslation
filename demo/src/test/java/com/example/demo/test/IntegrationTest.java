package com.example.demo.test;

import com.example.demo.DemoApplication;
import com.example.demo.client.TranslationClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class IntegrationTest {

    @Test
    public void integrationTest() {
        Thread serverThread = new Thread(() -> SpringApplication.run(DemoApplication.class));
        serverThread.start();

        try {
            Thread.sleep(3000);

            TranslationClient client = new TranslationClient(
                    "http://localhost:8080",
                    1000,
                    5000,
                    20000
            );

            // Use the client library synchronously
            String status = client.pollStatusSynchronously();
            System.out.println("Synchronous Final Status: " + status);

            // Use the client library asynchronously
            client.pollStatusAsynchronously(
                    result -> System.out.println("Asynchronous Final Status: " + result),
                    error -> System.err.println("Error in async polling: " + error.getMessage())
            );

        } catch (Exception e) {
            System.err.println("Integration Test Error: " + e.getMessage());
        } finally {
            serverThread.interrupt();
        }
    }
}
