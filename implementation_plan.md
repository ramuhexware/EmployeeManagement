# Implementation Plan - Spring Boot Employee Management Application

We will build a clean, production-ready Spring Boot application for employee management containing 8 Java source files. The application connects to the existing PostgreSQL database `employee_db` on port 5432 and maps to the existing `employees` table schema.

## User Review Required
> [!IMPORTANT]
> - **Database connection parameters**: Host: `localhost`, Port: `5432`, Database: `employee_db`, Username: `postgres`, Password: `postgres`. We detected that the table `employees` already exists with columns: `id`, `name`, `email`, `phone`, `department`, `role`, `salary`, `joiningdate` (mapped to `joiningDate`), and `status`. We will map our `Employee` JPA Entity to this schema.

## Proposed Changes

We will create a Maven project in the workspace root `c:\ramu\Project_Assignment\RapidX\FreddeMac_Project_RapidX\Work\Simple_Repo_EmployeeManagement`.

### Project Configuration
#### [NEW] [pom.xml](file:///c:/ramu/Project_Assignment/RapidX/FreddeMac_Project_RapidX/Work/Simple_Repo_EmployeeManagement/pom.xml)
Contains dependencies:
- `spring-boot-starter-web`
- `spring-boot-starter-data-jpa`
- `postgresql` (driver)
- `spring-boot-starter-test` (for verification)

#### [NEW] [application.properties](file:///c:/ramu/Project_Assignment/RapidX/FreddeMac_Project_RapidX/Work/Simple_Repo_EmployeeManagement/src/main/resources/application.properties)
Configures connection parameters to `employee_db` with `update` DDL auto mode to preserve existing schema.

### Core Java Source Files (8 Files)

#### [NEW] [EmployeeApplication.java](file:///c:/ramu/Project_Assignment/RapidX/FreddeMac_Project_RapidX/Work/Simple_Repo_EmployeeManagement/src/main/java/com/example/employeemanagement/EmployeeApplication.java)
Main entry point for Spring Boot.

#### [NEW] [Employee.java](file:///c:/ramu/Project_Assignment/RapidX/FreddeMac_Project_RapidX/Work/Simple_Repo_EmployeeManagement/src/main/java/com/example/employeemanagement/model/Employee.java)
JPA Entity mapping to the existing `employees` table.

#### [NEW] [EmployeeRepository.java](file:///c:/ramu/Project_Assignment/RapidX/FreddeMac_Project_RapidX/Work/Simple_Repo_EmployeeManagement/src/main/java/com/example/employeemanagement/repository/EmployeeRepository.java)
Spring Data Repository interface defining DB queries (includes custom `findByDepartmentAndStatus`).

#### [NEW] [EmployeeService.java](file:///c:/ramu/Project_Assignment/RapidX/FreddeMac_Project_RapidX/Work/Simple_Repo_EmployeeManagement/src/main/java/com/example/employeemanagement/service/EmployeeService.java)
Service class encapsulating the business logic and orchestrating validator, repository, and auditing services.

#### [NEW] [EmployeeController.java](file:///c:/ramu/Project_Assignment/RapidX/FreddeMac_Project_RapidX/Work/Simple_Repo_EmployeeManagement/src/main/java/com/example/employeemanagement/controller/EmployeeController.java)
REST Controller exposing HTTP endpoints for the 4 functional flows.

#### [NEW] [EmployeeValidator.java](file:///c:/ramu/Project_Assignment/RapidX/FreddeMac_Project_RapidX/Work/Simple_Repo_EmployeeManagement/src/main/java/com/example/employeemanagement/validator/EmployeeValidator.java)
Performs business validation (e.g., verifying salary > 0, email format, promotion eligibility).

#### [NEW] [AuditLogService.java](file:///c:/ramu/Project_Assignment/RapidX/FreddeMac_Project_RapidX/Work/Simple_Repo_EmployeeManagement/src/main/java/com/example/employeemanagement/service/AuditLogService.java)
Service to log modifications/transitions of employee states.

#### [NEW] [EmployeeNotFoundException.java](file:///c:/ramu/Project_Assignment/RapidX/FreddeMac_Project_RapidX/Work/Simple_Repo_EmployeeManagement/src/main/java/com/example/employeemanagement/exception/EmployeeNotFoundException.java)
Custom runtime exception thrown when requesting non-existent employees.

---

## Architectural & Flow Mapping

### Functional Flows (4)
1. **Hire Employee (POST)**: Validates details -> Sets status to "ACTIVE" -> Saves -> Logs action.
2. **Promote Employee (PUT)**: Fetches by ID -> Validates promotion parameters -> Updates salary & role -> Saves -> Logs action.
3. **Deactivate Employee (DELETE)**: Fetches by ID -> Sets status to "INACTIVE" -> Saves -> Logs action.
4. **List Active Employees by Dept (GET)**: Queries DB for specific department & status "ACTIVE" -> Returns list.

### DB Calls (3)
1. `EmployeeRepository.save(employee)`
2. `EmployeeRepository.findById(id)`
3. `EmployeeRepository.findByDepartmentAndStatus(department, status)`

### Execution Flows (4)
1. **Success Creation Flow**: Controller -> Validator -> Service -> Repository.save() -> AuditLog -> Returns HTTP 201.
2. **Success Promotion Flow**: Controller -> Service -> Repository.findById() -> Validator -> Repository.save() -> AuditLog -> Returns HTTP 200.
3. **Failure Path Flow**: Controller -> Service -> Repository.findById() -> throws EmployeeNotFoundException -> Returns HTTP 404.
4. **Custom Filtering Flow**: Controller -> Service -> Repository.findByDepartmentAndStatus() -> Returns list.

### Callees (17 Invocations)
1. `EmployeeController.createEmployee(..)` -> `EmployeeService.createEmployee(..)`
2. `EmployeeService.createEmployee(..)` -> `EmployeeValidator.validateNewEmployee(..)`
3. `EmployeeService.createEmployee(..)` -> `EmployeeRepository.save(..)`
4. `EmployeeService.createEmployee(..)` -> `AuditLogService.logCreation(..)`
5. `EmployeeController.getEmployeeById(..)` -> `EmployeeService.getEmployeeById(..)`
6. `EmployeeService.getEmployeeById(..)` -> `EmployeeRepository.findById(..)`
7. `EmployeeController.promoteEmployee(..)` -> `EmployeeService.promoteEmployee(..)`
8. `EmployeeService.promoteEmployee(..)` -> `EmployeeRepository.findById(..)`
9. `EmployeeService.promoteEmployee(..)` -> `EmployeeValidator.validatePromotion(..)`
10. `EmployeeService.promoteEmployee(..)` -> `EmployeeRepository.save(..)`
11. `EmployeeService.promoteEmployee(..)` -> `AuditLogService.logPromotion(..)`
12. `EmployeeController.deactivateEmployee(..)` -> `EmployeeService.deactivateEmployee(..)`
13. `EmployeeService.deactivateEmployee(..)` -> `EmployeeRepository.findById(..)`
14. `EmployeeService.deactivateEmployee(..)` -> `EmployeeRepository.save(..)`
15. `EmployeeService.deactivateEmployee(..)` -> `AuditLogService.logDeactivation(..)`
16. `EmployeeController.getActiveEmployeesByDept(..)` -> `EmployeeService.getEmployeesByDepartmentAndStatus(..)`
17. `EmployeeService.getEmployeesByDepartmentAndStatus(..)` -> `EmployeeRepository.findByDepartmentAndStatus(..)`

---

## Verification Plan

### Automated Tests
- Run `mvn clean test` to compile and verify all functionality. We will write unit tests using Mockito/JUnit 5.

### Manual Verification
- Run the Spring Boot application using `mvn spring-boot:run`.
- Verify the HTTP endpoints using PowerShell `Invoke-RestMethod` commands.
