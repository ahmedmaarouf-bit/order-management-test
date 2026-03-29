# Order Management System

A Spring Boot REST API for managing customers, products, and orders.

## Tech Stack

- Java 17
- Spring Boot 3.2
- Spring Data JPA
- H2 Database (in-memory)
- MapStruct
- Spring Security
- Maven

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+

### Running the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`.

### H2 Console

Access the H2 database console at: `http://localhost:8080/h2-console`

- JDBC URL: `jdbc:h2:mem:orderdb`
- Username: `sa`
- Password: (leave empty)

## API Documentation

### Swagger UI

Access the interactive API documentation at: `http://localhost:8080/swagger-ui.html`

You can test all endpoints directly from the Swagger UI interface.

### OpenAPI Spec

The OpenAPI specification is available at: `http://localhost:8080/api-docs`

A static `openapi.yaml` file is also included in the project root for import into Postman or other API clients.

## API Endpoints

### Customers

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/customers` | Get all customers |
| GET | `/api/customers/{id}` | Get customer by ID |
| POST | `/api/customers` | Create a new customer |
| PUT | `/api/customers/{id}` | Update a customer |
| DELETE | `/api/customers/{id}` | Delete a customer |

### Products

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/products` | Get all products |
| GET | `/api/products/{id}` | Get product by ID |
| POST | `/api/products` | Create a new product |
| PUT | `/api/products/{id}` | Update a product |
| DELETE | `/api/products/{id}` | Delete a product |
| PUT | `/api/products/stock` | Bulk update stock |

### Orders

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/orders` | Get paginated orders |
| GET | `/api/orders/all` | Get all orders |
| GET | `/api/orders/{id}` | Get order by ID |
| POST | `/api/orders` | Create a new order |
| DELETE | `/api/orders/{id}` | Delete an order (admin only) |

## Example Requests

### Create a Customer

```bash
curl -X POST http://localhost:8080/api/customers \
  -H "Content-Type: application/json" \
  -d '{"name": "Test User", "email": "test@example.com", "phone": "555-0000"}'
```

### Create an Order

```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": 1,
    "items": [
      {"productId": 2, "quantity": 2},
      {"productId": 3, "quantity": 1}
    ]
  }'
```

### Get Paginated Orders

```bash
curl "http://localhost:8080/api/orders?page=0&size=10"
```

---

## Your Task

This project has **several bugs** that you need to find and fix. 

### Instructions

1. Run the application and explore the API
2. Review the codebase to identify issues
3. For each bug you find:
   - Explain what the issue is
   - Explain why it's a problem
   - Propose and implement a fix
4. You can use curl, Postman, or write tests to verify your fixes

### Tips

- Read the code carefully
- Test the API endpoints
- Check the logs for SQL queries
- Don't assume everything works as expected

Good luck!