package com.example.demo.client;

import com.example.demo.model.StatusResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class TranslationClient {
    private final String baseUrl;
    private final RestTemplate restTemplate;
    private final long initialDelay;
    private final long maxDelay;
    private final long timeout;
    private static final Logger logger = LoggerFactory.getLogger(TranslationClient.class);

    public TranslationClient(String baseUrl, long initialDelay, long maxDelay, long timeout) {
        this.baseUrl = baseUrl;
        this.restTemplate = new RestTemplate();
        this.initialDelay = initialDelay;
        this.maxDelay = maxDelay;
        this.timeout = timeout;
    }

    /**
     * Polls the translation status synchronously.
     *
     * @return the final translation status ("completed", "error", or "pending").
     * @throws Exception if polling times out or an HTTP error occurs.
     */
    public String pollStatusSynchronously() throws Exception {
        logger.info("Polling started");

        long startTime = System.currentTimeMillis();
        long delay = initialDelay;

        while (System.currentTimeMillis() - startTime < timeout) {
            try {
                String status = fetchStatus();
                if (!status.equals("pending")) {
                    return status;
                }
                Thread.sleep(delay);
                delay = Math.min(maxDelay, delay * 2); // Exponential backoff
            } catch (HttpClientErrorException e) {
                throw new Exception("HTTP error while fetching status: " + e.getMessage(), e);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new Exception("Polling interrupted", e);
            }
        }
        throw new Exception("Polling timed out");
    }

    /**
     * Polls the translation status asynchronously.
     *
     * @param callback      called with the final status when the polling succeeds.
     * @param errorCallback called with an exception if polling fails or times out.
     */
    public void pollStatusAsynchronously(Consumer<String> callback, Consumer<Exception> errorCallback) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        CompletableFuture.runAsync(() -> {
            try {
                String result = pollStatusSynchronously();
                callback.accept(result);
            } catch (Exception e) {
                errorCallback.accept(e);
            }
        }, executor).thenRun(executor::shutdown);
    }

    /**
     * Fetches the translation status from the server.
     *
     * @return the current translation status ("pending", "completed", or "error").
     */
    private String fetchStatus() {
        return restTemplate.getForObject(baseUrl + "/status", StatusResponse.class).getResult();
    }
}
