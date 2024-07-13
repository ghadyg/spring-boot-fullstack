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

## Description

### Components

- **Login/Login.jsx:** Handles user login functionality.
- **Signup/Signup.jsx:** Manages user registration.

### Context

- **AuthContext.jsx:** Provides authentication context for managing user sessions.

### Customer

- **Card.jsx:** Displays customer information.
- **CreateCustomerForm.jsx:** Form to create new customers.
- **DrawerForm.jsx:** Drawer form component.
- **UpdateCustomerForm.jsx:** Form to update existing customers.
- **UpdateDrawerForm.jsx:** Drawer form for updating customers.

### Shared

- **ProtectedRoute.jsx:** Ensures routes are accessible only to authenticated users.
- **SideBar.jsx:** Sidebar navigation component.

### Services

- **client.js:** Axios client setup for making HTTP requests.
- **notification.js:** Notification service for displaying alerts and messages.

### Other Files

- **Customer.jsx:** Main customer component that implements infinite scroll functionality to load customers dynamically as the user scrolls, preventing application overload..
- **Home.jsx:** Home component for the landing page.
- **index.css:** Global CSS styles.
- **main.jsx:** Main entry point for the React application.

## Usage

- **Login:** Navigate to `/login` to log in.
- **Signup:** Navigate to `/signup` to register.
- **Customer Management:** Use the customer forms to create, update, and view customer information.
- **Protected Routes:** Ensure routes are secure and only accessible to authenticated users.

