Invoice Management Microservice
This microservice provides a simple RESTful API for managing a list of invoices. It allows users to perform CRUD (Create, Read, Update, Delete) operations on invoices and implements error handling and unit tests for the service layer.

Requirements
Java 8 or higher
Docker
PostgreSQL
Technologies Used
Spring Boot
PostgreSQL Database
Docker
Setup Instructions
Clone the Repository:

bash
Copy code
git clone <repository_url>
cd invoice-management-microservice
Start the Database:
Start the PostgreSQL database using Docker Compose:

bash
Copy code
docker-compose up -d postgres
Run the Application:
Navigate to the root directory of the project and run the Spring Boot application:

bash
Copy code
./mvnw spring-boot:run
REST Endpoints
Create a New Invoice
bash
Copy code
POST /invoices
Create a new invoice with the provided JSON payload.

Retrieve a Single Invoice by ID
bash
Copy code
GET /invoices/{id}
Retrieve details of a single invoice by its ID.

Retrieve All Invoices
bash
Copy code
GET /invoices
Retrieve details of all invoices.

Update an Existing Invoice
bash
Copy code
PUT /invoices/{id}
Update an existing invoice with the provided JSON payload.

Patch an Existing Invoice
bash
Copy code
PATCH /invoices/{id}
Partially update an existing invoice with the provided JSON payload.

Request Body:

json
Copy code
[
{ "op": "replace", "path": "/invoiceNumber", "value": "24515125" }
]
Delete an Invoice by ID
bash
Copy code
DELETE /invoices/{id}
Delete an existing invoice by its ID.

Invoice JSON Payload
json
Copy code
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
Error Handling
The microservice handles errors such as invalid input, non-existing invoices, etc., and returns appropriate error responses with status codes.