# Invoice Management Microservice
This microservice provides a simple RESTful API for managing a list of invoices. It allows users to perform CRUD (Create, Read, Update, Delete) operations on invoices and implements error handling and unit tests for the service layer.

## Swagger
After starting the application locally, here you can reach out to the swagger: http://localhost:8080/swagger-ui/index.html

## Requirements
- Java 8 or higher
- Docker
- PostgreSQL
## Technologies Used
- Spring Boot
- PostgreSQL Database
- Docker
## Setup Instructions
Clone the Repository:

```bash
git clone https://github.com/CodeLikeAlexito/invoice-taulia.git
```
### Start the Database:
Start the PostgreSQL database using Docker Compose:

```bash
docker-compose up -d postgres
```
### Run the Application:
Navigate to the root directory of the project and run the Spring Boot application:

```
./gradlew bootRun
```

## REST Endpoints
### Create a New Invoice
```
POST /invoices
``` 
### Create a new invoice with the provided JSON payload:

```json
{
  "invoiceNumber": "25",
  "buyerId": "9ad76928-a991-4f9d-b208-de70dc3dcc5b",
  "supplierId": "b460755d-2863-44b7-9948-e4f5fe6c2415",
  "dueDate": "2023-05-10",
  "items": [
    {
      "description": "fsafa",
      "quantity": 1,
      "price": 125.2
    }
  ]
}
```
### Retrieve a Single Invoice by ID
```
GET /invoices/{id}
```
### Retrieve All Invoices
```
GET /invoices
```
### Partially update an existing invoice with the provided JSON payload:
```
PATCH /invoices/{id}
```
```json
[
  { "op": "replace", "path": "/invoiceNumber", "value": "24515125" }
]
```
### Delete an Invoice by ID
```
DELETE /invoices/{id}
```
## Error Handling
The microservice handles errors such as invalid input, non-existing invoices, etc., and returns appropriate error responses with status codes.