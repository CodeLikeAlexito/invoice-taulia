# Invoice Management Microservice
This microservice provides a simple RESTful API for managing a list of invoices. It allows users to perform CRUD (Create, Read, Update, Delete) operations on invoices and implements error handling and unit tests for the service layer.

## Requirements
Java 8 or higher
Docker
PostgreSQL
## Technologies Used
Spring Boot
PostgreSQL Database
Docker
## Setup Instructions
Clone the Repository:

```bash
git clone <repository_url>
cd invoice-management-microservice
```
### Start the Database:
Start the PostgreSQL database using Docker Compose:

```bash
docker-compose up -d postgres
```
### Run the Application:
Navigate to the root directory of the project and run the Spring Boot application:

```
bash
./mvnw spring-boot:run
```

## REST Endpoints
### Create a New Invoice
```
POST /invoices
``` 
### Create a new invoice with the provided JSON payload:

```json
{
"invoiceNumber": "INV-001",
"buyer": {
  "name": "Buyer Company"
},
"supplier": {
  "name": "Supplier Inc."
},
"items": [
  {
    "name": "Item 1",
    "quantity": 2,
    "amount": 100
  },
  {
    "name": "Item 2",
    "quantity": 1,
    "amount": 50
  }
],
"invoiceDate": "2024-05-15",
"dueDate": "2024-06-15"
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
json
Copy code
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