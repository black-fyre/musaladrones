# Drones Service API

## Table of Contents
- [Introduction](#introduction)
- [Task Description](#task-description)
- [Requirements](#requirements)
    - [Functional Requirements](#functional-requirements)
    - [Non-functional Requirements](#non-functional-requirements)
- [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Building](#building)
    - [Running](#running)
    - [Testing](#testing)
## Introduction
The Drones Service API is designed to manage a fleet of drones for delivering medications. Drones can be registered, loaded with medication items, and their status can be checked. This API aims to facilitate communication with drones for medication delivery.

## Task Description
The project involves creating a REST API service, known as the dispatch controller, to interact with a fleet of drones. Each drone has specific attributes such as serial number, model, weight limit, battery capacity, and state. Additionally, medications can be associated with drones, each having a name, weight, code, and image.

## Requirements

### Functional Requirements
- The service prevents drones from being loaded with more weight than they can carry.
- Drones can not be in LOADING state if the battery level is below 25%.
- There is periodic task to check drones' battery levels and create history/audit event logs.

### Non-functional Requirements
- Input/output data must be in JSON format.
- The project is buildable and runnable.
- It uses a database that can be run locally.
- Required data is preloaded in the database.
- JUnit tests are incorporated.

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven

### Building
1. Clone the repository.
2. Navigate to the project root directory.
3. Build the project:
    ```bash
    ./mvnw clean install
    ```

### Running
1. Run the project without building:
    ```bash
    ./mvnw clean spring-boot:run 
    ```
#### OR
2. Run the project:
    ```bash
    java -jar target/MusalaDrones-0.0.1-SNAPSHOT.jar
    ```

### Testing
1. Run tests:
   ```bash
   ./mvnw test
