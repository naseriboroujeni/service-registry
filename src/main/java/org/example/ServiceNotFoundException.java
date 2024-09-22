package org.example;

public class ServiceNotFoundException extends RuntimeException {
    public ServiceNotFoundException(String serviceName) {
        super("Service " + serviceName + " not found");
    }
}
