package org.example;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceRegistry {

    private Map<String, Service> services = new ConcurrentHashMap<>();

    public void registerService(String serviceName) throws ServiceAlreadyExistsException {

        Service service = new Service(serviceName, Service.SERVICE_STATUS.ACTIVE);
        if (services.putIfAbsent(serviceName, service) != null) {
            throw new ServiceAlreadyExistsException(serviceName);
        }
    }

    public Optional<Service> getService(String serviceName) {
        return Optional.ofNullable(services.get(serviceName));
    }

    public void removeService(String serviceName) throws ServiceNotFoundException {
        if (services.remove(serviceName) == null) {
            throw new ServiceNotFoundException(serviceName);
        }
    }
}
