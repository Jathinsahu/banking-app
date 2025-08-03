# Simple Banking System

A complete Java Spring Boot backend application for a simple banking system with JWT authentication, MySQL database, and RESTful APIs.

## Features

- **User Authentication**: JWT-based signup and login
- **Account Management**: Check balance, credit/debit money
- **Money Transfer**: Transfer money between users by username
- **Transaction History**: View transaction history with date range filters
- **CSV Export**: Download transaction history as CSV file
- **Security**: Spring Security with JWT token authentication
- **Database**: MySQL with Spring Data JPA (Hibernate)

## Technology Stack

- **Java 17**
- **Spring Boot 3.2.1**
- **Spring Security** (JWT Authentication)
- **Spring Data JPA** (Hibernate)
- **MySQL Database**
- **Maven** (Build Tool)
- **BCrypt** (Password Encryption)

## Project Structure

```
simple-banking-system/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── banking/
│   │   │           ├── SimpleBankingSystemApplication.java
│   │   │           ├── controller/
│   │   │           │   ├── AuthController.java
│   │   │           │   └── BankingController.java
│   │   │           ├── dto/
│   │   │           │   ├── ApiResponse.java
│   │   │           │   ├── BalanceResponse.java
│   │   │           │   ├── JwtResponse.java
│   │   │           │   ├── LoginRequest.java
│   │   │           │   ├── SignupRequest.java
│   │   │           │   ├── TransactionRequest.java
│   │   │           │   └── TransactionResponse.java
│   │   │           ├── entity/
│   │   │           │   ├── Transaction.java
│   │   │           │   ├── TransactionType.java
│   │   │           │   └── User.java
│   │   │           ├── repository/
│   │   │           │   ├── TransactionRepository.java
│   │   │           │   └── UserRepository.java
│   │   │           ├── security/
│   │   │           │   ├── AuthEntryPointJwt.java
│   │   │           │   ├── AuthTokenFilter.java
│   │   │           │   ├── JwtUtils.java
│   │   │           │   └── WebSecurityConfig.java
│   │   │           ├── service/
│   │   │           │   ├── AuthService.java
│   │   │           │   ├── BankingService.java
│   │   │           │   ├── UserDetailsServiceImpl.java
│   │   │           │   └── UserPrincipal.java
│   │   │           └── util/
│   │   │               └── CsvExportUtil.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── data.sql
├── pom.xml
└── README.md
```

## Prerequisites

- **Java 17** or higher
- **Maven 3.6+**
- **MySQL 8.0+**

## Setup Instructions

### 1. Clone the Repository

```bash
git clone <repository-url>
cd simple-banking-system
```

### 2. Setup MySQL Database

1. Install MySQL and create a database:
```sql
CREATE DATABASE banking_system;
```

2. Create a MySQL user (optional):
```sql
CREATE USER 'banking_user'@'localhost' IDENTIFIED BY 'banking_password';
GRANT ALL PRIVILEGES ON banking_system.* TO 'banking_user'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Configure Database Connection

Update `src/main/resources/application.properties` with your MySQL credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/banking_system?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=your_mysql_password
```

### 4. Run the Application

```bash
# Using Maven
mvn clean install
mvn spring-boot:run

# Or run the jar file
mvn clean package
java -jar target/simple-banking-system-0.0.1-SNAPSHOT.jar
```

The application will start on `http://localhost:8080`

## API Documentation

### Authentication Endpoints

#### 1. User Signup
```http
POST /auth/signup
Content-Type: application/json

{
    "username": "john_doe",
    "password": "password123",
    "email": "john@example.com"
}
```

**Response:**
```json
{
    "success": true,
    "message": "User registered successfully! Username: john_doe",
    "data": null
}
```

#### 2. User Login
```http
POST /auth/login
Content-Type: application/json

{
    "username": "john_doe",
    "password": "password123"
}
```

**Response:**
```json
{
    "success": true,
    "message": "Login successful",
    "data": {
        "token": "eyJhbGciOiJIUzUxMiJ9...",
        "type": "Bearer",
        "username": "john_doe",
        "email": "john@example.com"
    }
}
```

### Banking Endpoints

**Note:** All banking endpoints require JWT token in Authorization header: `Bearer <token>`

#### 3. Get Account Balance
```http
GET /account/balance
Authorization: Bearer <your_jwt_token>
```

**Response:**
```json
{
    "success": true,
    "message": "Balance retrieved successfully",
    "data": {
        "username": "john_doe",
        "balance": 1000.00
    }
}
```

#### 4. Credit Money
```http
POST /account/credit
Authorization: Bearer <your_jwt_token>
Content-Type: application/json

{
    "amount": 500.00,
    "description": "Salary deposit"
}
```

#### 5. Debit Money
```http
POST /account/debit
Authorization: Bearer <your_jwt_token>
Content-Type: application/json

{
    "amount": 100.00,
    "description": "ATM withdrawal"
}
```

#### 6. Transfer Money
```http
POST /account/transfer
Authorization: Bearer <your_jwt_token>
Content-Type: application/json

{
    "amount": 200.00,
    "targetUsername": "jane_smith",
    "description": "Payment for services"
}
```

#### 7. Get Transaction History
```http
# Get all transactions
GET /account/transactions
Authorization: Bearer <your_jwt_token>

# Get transactions with date filter
GET /account/transactions?from=2024-01-01&to=2024-01-31
Authorization: Bearer <your_jwt_token>
```

#### 8. Download Transaction History (CSV)
```http
# Download all transactions as CSV
GET /account/transactions/download
Authorization: Bearer <your_jwt_token>

# Download transactions with date filter as CSV
GET /account/transactions/download?from=2024-01-01&to=2024-01-31
Authorization: Bearer <your_jwt_token>
```

## Testing with Postman

### Step 1: Import the Collection
Create a new Postman collection and add the following requests:

### Step 2: Test User Registration
1. **POST** `http://localhost:8080/auth/signup`
2. **Body (JSON):**
```json
{
    "username": "testuser",
    "password": "password123",
    "email": "test@example.com"
}
```

### Step 3: Test User Login
1. **POST** `http://localhost:8080/auth/login`
2. **Body (JSON):**
```json
{
    "username": "testuser",
    "password": "password123"
}
```
3. **Copy the JWT token** from the response

### Step 4: Test Banking Operations
1. Set **Authorization** header: `Bearer <your_jwt_token>`
2. Test each banking endpoint as described above

### Sample Users (from data.sql)
- **Username:** `john_doe`, **Password:** `password123`, **Email:** `john@example.com`
- **Username:** `jane_smith`, **Password:** `password123`, **Email:** `jane@example.com`
- **Username:** `bob_wilson`, **Password:** `password123`, **Email:** `bob@example.com`

## Key Commands

### Build and Run
```bash
# Clean and compile
mvn clean compile

# Run tests
mvn test

# Package the application
mvn clean package

# Run the application
mvn spring-boot:run

# Run with specific profile
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Database Commands
```bash
# Connect to MySQL
mysql -u root -p

# Show databases
SHOW DATABASES;

# Use the banking database
USE banking_system;

# Show tables
SHOW TABLES;

# View users
SELECT * FROM users;

# View transactions
SELECT * FROM transactions;
```

## Error Handling

The application includes comprehensive error handling:

- **400 Bad Request**: Invalid input data, insufficient balance, etc.
- **401 Unauthorized**: Invalid or missing JWT token
- **404 Not Found**: User not found, etc.
- **500 Internal Server Error**: Server-side errors

## Security Features

1. **Password Encryption**: BCrypt hashing
2. **JWT Authentication**: Stateless authentication
3. **Input Validation**: Bean validation annotations
4. **SQL Injection Protection**: JPA/Hibernate parameterized queries
5. **CORS Configuration**: Cross-origin request handling

## Future Enhancements

- Account types (Savings, Checking)
- Transaction limits and validation
- Email notifications
- Admin dashboard
- Transaction reversals
- Multi-currency support
- Account statements
- Interest calculations

## Troubleshooting

### Common Issues

1. **MySQL Connection Failed**
   - Check MySQL service is running
   - Verify database credentials in `application.properties`
   - Ensure database exists

2. **Port 8080 Already in Use**
   - Change port in `application.properties`: `server.port=8081`
   - Or kill the process using port 8080

3. **JWT Token Issues**
   - Ensure token is included in Authorization header
   - Check token expiration (default: 24 hours)
   - Verify token format: `Bearer <token>`

### Logs
Check application logs for detailed error information:
```bash
tail -f logs/spring.log
```

## Contact

For questions or support, please open an issue in the repository.