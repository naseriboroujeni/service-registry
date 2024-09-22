package org.example;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceRegistry {

    private Map<String, Service> services = new ConcurrentHashMap<>();

    public void registerService(String serviceName) throws Exception {

        Service service = new Service(serviceName, Service.SERVICE_STATUS.ACTIVE);
        if (services.putIfAbsent(serviceName, service) != null) {
            throw new Exception("Service " + serviceName + " already registered.");
        }
    }

    public Optional<Service> getService(String serviceName) {
        return Optional.ofNullable(services.get(serviceName));
    }

    public boolean removeService(String serviceName) throws Exception {
        return services.remove(serviceName) != null;
    }
}
