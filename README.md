# Employee Management Application

A simple Spring Boot application built with Java 21, Spring Data JPA, and PostgreSQL database integration. It exposes REST API endpoints for employee life cycle operations.

## Database Prerequisites
- **Database Name**: `employee_db`
- **Port**: `5432`
- **Username**: `postgres`
- **Password**: `postgres`
- **Table name**: `employees` (auto-mapped to entity)

---

## Running the Application
From the workspace root directory, run:
```bash
mvn spring-boot:run
```
The application will start on port `8080`.

---

## Postman Execution Steps

Import the following steps or configure them manually in Postman to interact with the application.

### 1. Hire Employee (POST)
Creates a new active employee in the database and triggers the validator and audit logger.

- **Method**: `POST`
- **URL**: `http://localhost:8080/api/employees`
- **Headers**:
  - `Content-Type`: `application/json`
- **Body** (Raw JSON):
```json
{
  "name": "Alice Smith",
  "email": "alice.smith@example.com",
  "phone": "555-0199",
  "department": "Engineering",
  "role": "Software Engineer",
  "salary": 85000.0,
  "joiningDate": "2026-07-23"
}
```
- **Expected Response**: `201 Created`
- **Response Example**:
```json
{
  "id": 13,
  "name": "Alice Smith",
  "email": "alice.smith@example.com",
  "phone": "555-0199",
  "department": "Engineering",
  "role": "Software Engineer",
  "salary": 85000.0,
  "joiningDate": "2026-07-23",
  "status": "ACTIVE"
}
```

---

### 2. Retrieve Employee Details (GET)
Retrieves the details of a specific employee using their ID.

- **Method**: `GET`
- **URL**: `http://localhost:8080/api/employees/13`
- **Expected Response**: `200 OK`
- **Response Example**:
```json
{
  "id": 13,
  "name": "Alice Smith",
  "email": "alice.smith@example.com",
  "phone": "555-0199",
  "department": "Engineering",
  "role": "Software Engineer",
  "salary": 85000.0,
  "joiningDate": "2026-07-23",
  "status": "ACTIVE"
}
```

---

### 3. Promote Employee (PUT)
Promotes an active employee, raising their salary and updating their role.

- **Method**: `PUT`
- **URL**: `http://localhost:8080/api/employees/13/promote?newSalary=95000.0&newRole=Senior Software Engineer`
- **Query Params**:
  - `newSalary`: `95000.0`
  - `newRole`: `Senior Software Engineer`
- **Expected Response**: `200 OK`
- **Response Example**:
```json
{
  "id": 13,
  "name": "Alice Smith",
  "email": "alice.smith@example.com",
  "phone": "555-0199",
  "department": "Engineering",
  "role": "Senior Software Engineer",
  "salary": 95000.0,
  "joiningDate": "2026-07-23",
  "status": "ACTIVE"
}
```

---

### 4. Deactivate/Offboard Employee (DELETE)
Sets an employee's status to `INACTIVE`.

- **Method**: `DELETE`
- **URL**: `http://localhost:8080/api/employees/13`
- **Expected Response**: `200 OK`
- **Response Example**:
```json
{
  "id": 13,
  "name": "Alice Smith",
  "email": "alice.smith@example.com",
  "phone": "555-0199",
  "department": "Engineering",
  "role": "Senior Software Engineer",
  "salary": 95000.0,
  "joiningDate": "2026-07-23",
  "status": "INACTIVE"
}
```

---

### 5. Get Active Employees by Department (GET)
Queries all active employees belonging to a specific department.

- **Method**: `GET`
- **URL**: `http://localhost:8080/api/employees/department/Engineering`
- **Expected Response**: `200 OK`
- **Response Example** (If the employee was deactivated, it will return an empty list `[]`; if active, it lists matching employees):
```json
[
  {
    "id": 13,
    "name": "Alice Smith",
    "email": "alice.smith@example.com",
    "phone": "555-0199",
    "department": "Engineering",
    "role": "Senior Software Engineer",
    "salary": 95000.0,
    "joiningDate": "2026-07-23",
    "status": "ACTIVE"
  }
]
```

---

### 6. Error Flow: Retrieve Non-existent Employee (GET)
Verifies error handler execution flow for a non-existent ID.

- **Method**: `GET`
- **URL**: `http://localhost:8080/api/employees/999`
- **Expected Response**: `404 Not Found`
