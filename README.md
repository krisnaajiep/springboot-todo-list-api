# Spring Boot Todo List API

> Simple Todo List RESTful API built with Spring Boot to allow users to manage their to-do list. 

## Table of Contents

- [General Info](#general-information)
- [Technologies Used](#technologies-used)
- [Features](#features)
- [Setup](#setup)
- [Usage](#usage)
- [HTTP Response Codes](#http-response-codes)
- [Project Status](#project-status)
- [Acknowledgements](#acknowledgements)
- [License](#license)

## General Information

Spring Boot Todo List API is a simple RESTful API that allows users to manage their to-do list.
It supports pagination, sorting, and filtering.
This API uses [JWT](https://jwt.io/) for authentication.
This project is designed to explore and practice working with the CRUD operation,
RESTful API, data modeling, authentication, and database in Spring Boot.

## Technologies Used

- Java 21.0.6 LTS
- Maven 3.9.9
- Microsoft SQL Server 2022
- Spring Boot 3.4.5
- Spring Cloud 2024.0.1
- Lombok 1.18.38
- [JJWT](https://github.com/jwtk/jjwt) 0.12.6

## Features

- **User Registration**: Register a new user using the `POST` method.
- **User Login**: Authenticate the user using the `POST` method.
- **Create a To-Do Item**: Create a new to-do item using the `POST` method.
- **Update a To-Do Item**: Update an existing to-do item using the `PUT` method.
- **Delete a To-Do Item**: Delete an existing to-do item using the `DELETE` method.
- **Get To-Do Items**: Get the list of to-do items with pagination using the `GET` method.
- **Filtering and Sorting**: Get the list of to-do items by status or sort by specific field using the `keyword`, `sortBy`, and `sortDir` query params.
- **Refresh Token**: Get a new access token using the `POST` method.

## Setup

To run this API, you’ll need:

* **Java**: Version 21 or higher
* **Maven**: Version 3.x
* **Microsoft SQL Server** 2022 or higher

How to install:

1. Clone the repository

   ```bash
   git clone https://github.com/krisnaajiep/springboot-todo-list-api.git
   ```

2. Change the current working directory

   ```bash
   cd springboot-todo-list-api
   ```

3. Import the database

   ```bash
   sqlcmd -S <host> -U <username> -P <password> -No -i "db/TodoListAPI.sql"
   ```

4. Set environment variables for databases and JWT secret configuration

   ```bash
   export DB_HOST=<host>
   export DB_PORT=<port>
   export DB_NAME=TodoListAPI
   export DB_USERNAME=<username>
   export DB_PASSWORD=<password>
   export JWT_SECRET=<jwt_secret>
   ```

5. Build the project

   ```bash
   mvn clean package
   ```

6. Copy the JAR file from the `target/` directory

   ```bash
   cp target/todo-list-api-0.0.1-SNAPSHOT.jar todo-list-api.jar 
   ```

7. Run the JAR file

   ```bash
   java -jar todo-list-api.jar
   ```

## Usage

Example API Endpoints:

1. **User Registration**

    - Method: `POST`
    - Endpoint: `/register`
    - Request Header:

        - `Content-Type` (string)—The content type of request body (must be `application/json`).

    - Request Body:

        - `name` (string)—The name of the user.
        - `email` (string)—The email address of the user.
        - `password` (string)—The password of the user account.

    - Example Request:

      ```http
      POST /register
      Content-Type: application/json
      
      {
        "name": "John Doe",
        "email": "john@doe.com",
        "password": "example_password",
      }
      ```

    - Response:

        - Status: `201 Created`
        - Content-Type: `application/json`

    - Example Response:

      ```json
      {
        "access_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9_access",
        "refresh_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9_refresh"
      }
      ```

2. **User Login**

    - Method: `POST`
    - Endpoint: `/login`
    - Request Header:

        - `Content-Type` (string)—The content type of request body (must be `application/json`).
   
    - Request Body:

        - `email` (string)—The email address of the user.
        - `password` (string)—The password of the user account.

    - Example Request:

      ```http
      POST /login
      Content-Type: application/json
      
      {
        "email": "john@doe.com",
        "password": "example_password",
      }
      ```

    - Response:

        - Status: `200 OK`
        - Content-Type: `application/json`

    - Example Response:

      ```json
      {
        "access_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9_access",
        "refresh_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9_refresh"
      }
      ```

3. **Refresh Token**

    - Method: `POST`
    - Endpoint: `/refresh`
    - Request Header:
   
        - `Content-Type` (string)—The content type of request body (must be `application/json`).
        - `Authorization` (string)—The access token with `Bearer` type.
      
    - Request Body:
   
        - `refreshToken` (string)—The refresh token.

   - Example Request:

     ```http
     POST /refresh
     Content-Type: application/json
     Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9_access
     
     {
       "refreshToken": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9_refresh"
     }
     ```

    - Response:

        - Status: `200 OK`
        - Content-Type: `application/json`

    - Example Response:

      ```json
      {
        "access_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9_access"
      }
      ```

4. **Create a To-Do Item**

    - Method: `POST`
    - Endpoint: `/todos`
    - Request Header:
 
        - `Content-Type` (string)—The content type of request body (must be `application/json`).
        - `Authorization` (string)—The access token with `Bearer` type.

    - Request Body:

        - `title` (string)—The title of the todo item.
        - `description` (string)—The description of the todo item.

    - Example Request:

      ```http
      POST /todos
      Content-Type: application/json
      Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9_access
      
      {
       "title": "Buy groceries",
       "description": "Buy milk, eggs, and bread"
      }
      ```

    - Response:

        - Status: `201 Created`
        - Content-Type: `application/json`

    - Example Response:

      ```json
      {
        "id": 1,
        "title": "Buy groceries",
        "description": "Buy milk, eggs, and bread"
      }
      ```

5. **Update an Existing To-Do Item**

    - Method: `PUT`
    - Endpoint: `/todos/{id}`
    - Request Header:

        - `Content-Type` (string)—The content type of request body (must be `application/json`).
        - `Authorization` (string)—The access token with `Bearer` type.

    - Request Body:

        - `title` (string)—The title of the todo item.
        - `description` (string)—The description of the todo item.

    - Example Request:

      ```http
      POST /todos/1
      Content-Type: application/json
      Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9_access
      
      {
       "title": "Buy groceries",
       "description": "Buy milk, eggs, bread, and cheese"
      }
      ```

    - Response:

        - Status: `200 OK`
        - Content-Type: `application/json`

    - Example Response:

      ```json
      {
        "id": 1,
        "title": "Buy groceries",
        "description": "Buy milk, eggs, bread, and cheese"
      }
      ```

6. **Delete an Existing To-Do Item**

    - Method: `DELETE`
    - Endpoint: `/todos/{id}`
    - Request Header:

       - `Authorization` (string)—The access token with `Bearer` type.

    - Response:

        - Status: `204 No Content`
        - Content-Type: `text/xml`

7. **Get To-Do Items**

    - Method: `GET`
    - Endpoint: `/todos`
    - Request Header:

       - `Content-Type` (string)—The content type of request body (must be `application/json`).
       - `Authorization` (string)—The access token with `Bearer` type.

    - Response:

        - Status: `200 OK`
        - Content-Type: `application/json`

    - Example Response:

      ```json
      {
         "data": [
           {
             "id": 1,
             "title": "Buy groceries",
             "description": "Buy milk, eggs, bread, and cheese"
           },
           {
             "id": 2,
             "title": "Pay bills",
             "description": "Pay electricity and water bills"
           }
         ],
         "page": 1,
         "limit": 10,
         "total": 2
      }
      ```

        - Params:

           - `page` - The page number to retrieve in a paginated list of results.
           - `limit` - Specifies the maximum number of items to be returned in the response.
           - `keyword` - Search keyword to filter todo items by title or description
           - `sortBy` - Field name to sort the results by (e.g., title, description, created_at)
           - `sortDir` - Sort direction, either 'asc' for ascending or 'desc' for descending

## Authentication

This API uses Bearer Token for authentication. You can generate an access token by registering a new user or login.

You must include an access token in each request to the API with the Authorization request header.

### Authentication error response

If an API key is missing, malformed, or invalid, you will receive an HTTP 401 Unauthorized response code.

## Rate and Usage Limits

API access rate limits apply on a per-API key basis in unit time.
The limit is 60 requests per minute.
Also, depending on your plan, you may have usage limits.
If you exceed either limit, your request will return an HTTP 429 `Too Many Requests` status code.

Each API response returns the following set of headers to help you identify your use status:

| Header                  | Description                                                                       |
|-------------------------|-----------------------------------------------------------------------------------|
| `X-RateLimit-Limit`     | The maximum number of requests that the consumer is permitted to make per minute. |
| `X-RateLimit-Remaining` | The number of requests remaining in the current rate limit window.                |
| `X-RateLimit-Reset`     | The time at which the current rate limit window resets in UTC epoch seconds.      |

## HTTP Response Codes

The API returns the following status codes depending on the success or failure of the request.

| Status Code                | Description                                                                                  |
|----------------------------|----------------------------------------------------------------------------------------------|
| 200 OK                     | The request was processed successfully.                                                      |
| 201 Created                | The new resource was created successfully.                                                   |
| 400 Bad Request            | The server could not understand the request due to invalid syntax.                           |
| 401 Unauthorized           | Authentication is required or the access token is invalid.                                   |
| 403 Forbidden              | Access to the requested resource is forbidden.                                               |
| 404 Not Found              | The requested resource was not found.                                                        |
| 409 Conflict               | Indicates a conflict between the request and the current state of a resource on a web server |
| 415 Unsupported Media Type | The media format of the requested data is not supported by the server.                       |
| 429 Too Many Request       | The client has sent too many requests in a given amount of time (rate limiting).             |
| 500 Internal Server Error  | An unexpected server error occurred.                                                         |

## Project Status

Project is: _complete_.

## Acknowledgements

This project was inspired by [roadmap.sh](https://roadmap.sh/projects/todo-list-api).

## License

This project is licensed under the MIT License—see the [LICENSE](./LICENSE) file for details.
