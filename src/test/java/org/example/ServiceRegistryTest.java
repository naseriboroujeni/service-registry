package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceRegistryTest {

    ServiceRegistry serviceRegistry;

    @BeforeEach
    public void setUp() {
        serviceRegistry = new ServiceRegistry();
    }

    @Test
    public void testRegisterService() {
        serviceRegistry.registerService("MyService");
        assertTrue(serviceRegistry.getService("MyService").isPresent());
    }

    @Test
    public void testRegisterExistingService() {
        serviceRegistry.registerService("MyService");

        assertThrows(ServiceAlreadyExistsException.class, () -> serviceRegistry.registerService("MyService"));
    }

    @Test
    public void testDuplicateServiceInDifferentThreads() throws InterruptedException {
        List<ServiceAlreadyExistsException> catchedExceptions = new CopyOnWriteArrayList<>();

        Runnable registerServiceRunnable = () -> {
            try {
                serviceRegistry.registerService("MyService");
            } catch (ServiceAlreadyExistsException e) {
                catchedExceptions.add(e);
            }
        };

        Thread t1 = new Thread(registerServiceRunnable);
        Thread t2 = new Thread(registerServiceRunnable);
        Thread t3 = new Thread(registerServiceRunnable);
        Thread t4 = new Thread(registerServiceRunnable);

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        t1.join();
        t2.join();
        t3.join();
        t4.join();

        assertEquals(3, catchedExceptions.size());
    }

    @Test
    void testConcurrentServiceRegistrationWithoutSync() throws InterruptedException {
        int numberOfThreads = 100;
        CountDownLatch latch = new CountDownLatch(1); // Ensures all threads start together
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);

        // Create threads to concurrently register the same service
        for (int i = 0; i < numberOfThreads; i++) {
            executor.submit(() -> {
                try {
                    latch.await(); // Wait for the signal to start all threads
                    serviceRegistry.registerService("MyService");
                } catch (ServiceAlreadyExistsException | InterruptedException e) {
                    // Expected if the service is already registered by another thread
                }
            });
        }

        latch.countDown(); // Start all threads
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        // Validate that only one service was registered
        Optional<Service> service = serviceRegistry.getService("MyService");
        assertTrue(service.isPresent(), "Service should be registered.");

        // Check if the map contains exactly one service
        assertEquals(1, serviceRegistry.getServiceCount(), "Service count should be exactly 1.");
    }
}
