package org.example;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceRegistry {

    private Map<String, Service> services = new ConcurrentHashMap<>();

    public void registerService(String serviceName) {

        Service service = new Service(serviceName, Service.SERVICE_STATUS.ACTIVE);
        if (services.putIfAbsent(serviceName, service) != null) {
            throw new ServiceAlreadyExistsException(serviceName);
        }
    }

    public Optional<Service> getService(String serviceName) {
        return Optional.ofNullable(services.get(serviceName));
    }

    public void removeService(String serviceName) {
        if (services.remove(serviceName) == null) {
            throw new ServiceNotFoundException(serviceName);
        }
    }

    public int getServiceCount() {
        return services.size();
    }
}
