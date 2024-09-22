package org.example;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Service {

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();

    public enum SERVICE_STATUS {
        ACTIVE,
        INACTIVE
    }

    private final String name;
    private SERVICE_STATUS status;

    public Service(String name, SERVICE_STATUS status) {
        this.name = name;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public SERVICE_STATUS getStatus() {
        try {
            readLock.lock();
            return status;
        } finally {
            readLock.unlock();
        }
    }

    public void setStatus(SERVICE_STATUS status) {
        try {
            writeLock.lock();
            this.status = status;
        } finally {
            writeLock.unlock();
        }
    }
}
