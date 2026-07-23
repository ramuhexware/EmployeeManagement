package com.example.employeemanagement.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AuditLogService {

    private static final Logger logger = LoggerFactory.getLogger(AuditLogService.class);

    public void logCreation(Integer employeeId, String name, String department) {
        logger.info("AUDIT - Hired new employee [ID: {}, Name: {}, Department: {}]", employeeId, name, department);
    }

    public void logPromotion(Integer employeeId, String oldRole, String newRole, double oldSalary, double newSalary) {
        logger.info("AUDIT - Promoted employee [ID: {}]. Role transition: {} -> {}. Salary transition: {} -> {}", 
                employeeId, oldRole, newRole, oldSalary, newSalary);
    }

    public void logDeactivation(Integer employeeId, String name) {
        logger.info("AUDIT - Deactivated employee [ID: {}, Name: {}]", employeeId, name);
    }
}
