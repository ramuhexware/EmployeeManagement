package com.example.employeemanagement.service;

import com.example.employeemanagement.exception.EmployeeNotFoundException;
import com.example.employeemanagement.model.Employee;
import com.example.employeemanagement.repository.EmployeeRepository;
import com.example.employeemanagement.validator.EmployeeValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Spy
    private EmployeeValidator employeeValidator;

    @Mock
    private AuditLogService auditLogService;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee mockEmployee;

    @BeforeEach
    public void setup() {
        mockEmployee = new Employee(
                "John Doe",
                "john.doe@example.com",
                "123-456-7890",
                "Engineering",
                "Software Engineer",
                75000.0,
                "2026-07-23",
                "ACTIVE"
        );
        mockEmployee.setId(1);
    }

    @Test
    public void testCreateEmployee_Success() {
        when(employeeRepository.save(any(Employee.class))).thenReturn(mockEmployee);
        doNothing().when(auditLogService).logCreation(any(), any(), any());

        Employee created = employeeService.createEmployee(mockEmployee);

        assertNotNull(created);
        assertEquals("ACTIVE", created.getStatus());
        assertEquals("John Doe", created.getName());
        verify(employeeRepository, times(1)).save(mockEmployee);
        verify(auditLogService, times(1)).logCreation(1, "John Doe", "Engineering");
    }

    @Test
    public void testGetEmployeeById_Success() {
        when(employeeRepository.findById(1)).thenReturn(Optional.of(mockEmployee));

        Employee found = employeeService.getEmployeeById(1);

        assertNotNull(found);
        assertEquals(1, found.getId());
        verify(employeeRepository, times(1)).findById(1);
    }

    @Test
    public void testGetEmployeeById_NotFound() {
        when(employeeRepository.findById(2)).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class, () -> {
            employeeService.getEmployeeById(2);
        });
        verify(employeeRepository, times(1)).findById(2);
    }

    @Test
    public void testPromoteEmployee_Success() {
        when(employeeRepository.findById(1)).thenReturn(Optional.of(mockEmployee));
        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));
        doNothing().when(auditLogService).logPromotion(any(), any(), any(), any(Double.class), any(Double.class));

        Employee promoted = employeeService.promoteEmployee(1, 90000.0, "Senior Software Engineer");

        assertNotNull(promoted);
        assertEquals(90000.0, promoted.getSalary());
        assertEquals("Senior Software Engineer", promoted.getRole());
        verify(employeeRepository, times(1)).save(mockEmployee);
        verify(auditLogService, times(1)).logPromotion(1, "Software Engineer", "Senior Software Engineer", 75000.0, 90000.0);
    }

    @Test
    public void testDeactivateEmployee_Success() {
        when(employeeRepository.findById(1)).thenReturn(Optional.of(mockEmployee));
        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));
        doNothing().when(auditLogService).logDeactivation(any(), any());

        Employee deactivated = employeeService.deactivateEmployee(1);

        assertNotNull(deactivated);
        assertEquals("INACTIVE", deactivated.getStatus());
        verify(employeeRepository, times(1)).save(mockEmployee);
        verify(auditLogService, times(1)).logDeactivation(1, "John Doe");
    }
}
