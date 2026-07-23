package com.example.employeemanagement.repository;

import com.example.employeemanagement.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    
    // Custom query to find employees by department and status (DB Call 3)
    List<Employee> findByDepartmentAndStatus(String department, String status);
}
