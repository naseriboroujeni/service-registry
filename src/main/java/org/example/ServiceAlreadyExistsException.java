package org.example;

public class ServiceAlreadyExistsException extends Exception {
    public ServiceAlreadyExistsException(String serviceName) {
        super("Service " + serviceName + " already exists");
    }
}
