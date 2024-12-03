Using the Client Library

Initialization
Create a TranslationClient instance by providing:

Base URL: URL of the translation server.
Initial Delay: Starting delay for polling (in milliseconds).
Maximum Delay: Maximum allowable delay for polling (in milliseconds).
Timeout: Maximum time to wait for a response (in milliseconds).

java

TranslationClient client = new TranslationClient(
    "http://localhost:8080", // Server URL
    1000,                    // Initial delay in ms
    5000,                    // Maximum delay in ms
    20000                    // Timeout in ms
);

Synchronous Usage
The synchronous method blocks until the polling completes or times out:

try {
    String status = client.pollStatusSynchronously();
    System.out.println("Final Status: " + status);
} catch (Exception e) {
    System.err.println("Error: " + e.getMessage());
}

Asynchronous Usage
The asynchronous method allows non-blocking polling with callback handlers:

client.pollStatusAsynchronously(
    status -> System.out.println("Final Status: " + status), // Success callback
    error -> System.err.println("Error: " + error.getMessage()) // Error callback
);

Configurable Parameters
You can adjust polling behavior by changing:

Delays: Tune initialDelay and maxDelay for desired polling frequency.
Timeout: Set a reasonable timeout to prevent long waits.
Base URL: Change to point to your server's URL.