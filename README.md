# SPRING BOOT

## Getting Started
- **Install Necessary Packages:** PostgreSQL, FlywayDB, etc.
- **Docker Setup:** Create a `docker-compose` file to install and run a PostgreSQL container.
- **Database Creation:** Set up the database inside the PostgreSQL container.
- **Configuration:** Configure `application.properties` to connect to the database.

## Connecting to the Database
- **FlywayDB:** Use FlywayDB to create all necessary tables and constraints.
- **Entities:** Create the customer entity.

## Error Handling
- **Error Handling Classes:** Develop error handling classes that return appropriate messages and statuses based on errors, to be used in the Service and Controller layers.

## Customer Data Access Service
- **CRUD Operations:** Utilize JDBC Template or JPA to create functions for CRUD operations and other necessary tasks.
- **Unit Tests:** Set up unit tests to ensure functions work as intended.

## Customer Service and Controller
- **Controller Setup:** Create the Customer Controller to implement all required REST APIs for retrieving, adding, updating, etc.
- **Service Layer:** Develop the Customer Service layer to handle the logic necessary to provide correct data to the Controller layer.
- **Unit Tests:** Set up unit tests to ensure functions work as intended.

## Integration Tests
- **Simulated Usage:** Create integration tests to simulate user interaction and test the cooperation of all layers.

## Spring Security
- **API Security:** Secure API endpoints by configuring Spring Security.
- **JWT Authentication:** Implement JWT for authentication.
- **User Authentication:** Create sign-up and login functions for user registration and access.
- **Filters:** Add filters to allow unauthenticated sign-up requests.
- **Token Management:** Develop functions to return authorization tokens on login and verify tokens from other requests.

## Integrating with Amazon S3
- **Configuration:** Set up connections to S3 buckets, including a fake S3 bucket for testing.
- **Functions:** Create functions to put and get objects from S3 buckets.
- **Rest Controller:** Develop a Rest Controller for users to add profile pictures to their accounts.
- **Unit Tests:** Set up unit tests to ensure functions work as intended.
- **Integration Tests:** Modify integration tests to include testing for uploading and retrieving profile pictures.

## GitHub Workflows
- **First Workflow:** Build and verify the project by running all unit tests on push.
- **Second Workflow:** Verify all integration tests, package the Spring application inside a Docker container, upload it to DockerHub, and deploy to AWS Elastic Beanstalk.

# REACT JS

> Coming soon...
