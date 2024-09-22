package org.example;

public class ServiceAlreadyExistsException extends RuntimeException {
    public ServiceAlreadyExistsException(String serviceName) {
        super("Service " + serviceName + " already exists");
    }
}
