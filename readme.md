# Spring Boot JWT Authentication Example

This is a sample Spring Boot application demonstrating JWT (JSON Web Token) authentication. The application allows users to register, log in, and access protected resources using JWT.

## Table of Contents

- [Prerequisites](#prerequisites)
- [Running the Project](#running-the-project)
- [Sending JWT Authorization Headers](#sending-jwt-authorization-headers)
- [Swagger Documentation](#swagger-documentation)
- [Endpoints](#endpoints)
- [License](#license)

## Prerequisites

Before you begin, ensure you have met the following requirements:

- Java 11 or higher
- Maven 3.6 or higher
- An IDE (like IntelliJ IDEA, Eclipse, etc.) or a text editor
- Postman or any API client (optional for testing)

## Running the Project

1. **Clone the Repository**

   bash
   git clone https://github.com/yourusername/your-repo-name.git
   cd your-repo-name

2. **Build the Project**

   You can build the project using Maven:

   bash
   mvn clean install

3. **Run the Application**

   You can run the application using the following command:

   bash
   mvn spring-boot:run

   Alternatively, you can run the generated JAR file:

   bash
   java -jar target/social-media-app.jar

4. **Access the Application**

   The application will run on http://localhost:8080 by default.

## Sending JWT Authorization Headers

To access protected endpoints, you need to include a JWT in your request headers. Here’s how you can do it:

1. **Obtain a JWT Token**

   Send a POST request to the /auth/login endpoint with your credentials:

   - **URL**: http://localhost:8080/api/auth/login
   - **Method**: POST
   - **Body** (JSON):

     json
     {
     "email": "your_email",
     "password": "your_password"
     }

   If the credentials are valid, you'll receive a response containing the JWT token:

   json
   {
   "accessToken": "",
   "refreshToken": ""
   }

2. **Use the JWT Token in Subsequent Requests**

   For all subsequent requests to protected endpoints, include the token in the Authorization header:

   - **Header**:

     Authorization: Bearer your_jwt_token

### Example with cURL

Here’s an example of how to send a request with JWT using cURL:

bash
curl -X GET http://localhost:8080/protected/resource \\
-H "Authorization: Bearer your_jwt_token"

## Swagger Documentation

The application includes Swagger for API documentation. You can access it at:
http://localhost:8080/swagger-ui/index.html

## Endpoints

| Method | Endpoint            | Description                   |
| ------ | ------------------- | ----------------------------- |
| POST   | /auth/login         | Authenticate user and get JWT |
| GET    | /protected/resource | Access protected resource     |

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
