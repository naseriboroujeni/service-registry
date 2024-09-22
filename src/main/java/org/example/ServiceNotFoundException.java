package org.example;

public class ServiceNotFoundException extends Exception{
    public ServiceNotFoundException(String serviceName) {
        super("Service " + serviceName + " not found");
    }
}
