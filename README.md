# API-BDD-Framework (by: Nisar Ahmed)

This is a robust **BDD (Behavior-Driven Development)** test automation framework designed for **REST API testing**. It's built with **Java, Rest-Assured, Cucumber, and TestNG** to provide a powerful, scalable, and maintainable solution for validating web services.

-----

## ✨ Features

  * **Behavior-Driven Development:** Uses Cucumber and Gherkin syntax (`.feature` files) for clear, business-readable API test scenarios.
  * **Powerful API Validation:** Leverages **Rest-Assured** for fluent and expressive API request building and response validation.
  * **Multi-Environment Support:** Seamlessly switch between test environments (e.g., INT, CERT) using a single configuration property or a Maven command-line argument.
  * **Detailed Reporting:** Generates beautiful, interactive HTML reports with **ExtentReports**, logging every request, header, body, and response for easy debugging.
  * **JSON Schema Validation:** Includes a utility to validate API response structures against predefined JSON schemas.
  * **Dependency Injection:** Uses Cucumber's built-in DI (**PicoContainer**) to share state (TestContext) and manage factories across step definition files, promoting clean code.
  * **Request & Response Factories:** Simplifies test creation with factories for building `RequestSpecification` and `ResponseSpecification`.
  * **POJO-Based Modeling:** Uses Plain Old Java Objects (POJOs) for request/response serialization and deserialization, enabling type-safe and readable tests.
  * **Parallel Execution:** Supports running scenarios in parallel using TestNG's data provider to significantly reduce execution time.
  * **Rerun Failed Tests:** Automatically generates a `rerun_data.txt` file to easily re-execute only the failed scenarios.
  * **Rich Logging:** Provides detailed, color-coded console logs via a custom `TestListener` and cURL command logging for every API call.

-----

## 🛠️ Tech Stack

  * **Language:** Java 17+
  * **API Automation:** Rest-Assured
  * **BDD Tool:** Cucumber
  * **Test Runner:** TestNG
  * **Build Tool:** Maven
  * **Reporting:** ExtentReports
  * **Logging:** SLF4J & Logback
  * **JSON Processing:** Jackson & `org.json`

-----

## 📂 Project Structure

The framework is organized with a clear separation of concerns, making it easy to navigate and extend.

```
API-BDD-Framework/
├── reports/                          # Output directory for logs and reports
│   ├── cucumber/
│   ├── extent/
│   └── logs/
├── src/
│   ├── main/java/com/nahmed/
│   │   └── models/
│   │       ├── request/              # Request POJOs
│   │       ├── response/             # Response POJOs
│   │       └── schema/               # JSON schema files for validation
│   └── test/
│       ├── java/
│       │   └── com/nahmed/
│       │       ├── constants/        # Framework-level constants (e.g., file paths)
│       │       ├── enums/            # Enumerations for type-safe configuration
│       │       ├── events/           # Cucumber Hooks (Before/After scenarios)
│       │       ├── exceptions/       # Custom exception classes
│       │       ├── factories/        # Factories for building requests, responses, etc.
│       │       ├── features/         # Gherkin feature files
│       │       ├── listeners/        # Custom test listeners for logging and reporting
│       │       ├── reports/          # ExtentReports configuration and management
│       │       ├── runners/          # TestNG test runners for Cucumber
│       │       ├── stepdefinitions/  # Step implementation for Gherkin features
│       │       └── utils/            # Helper and utility classes
│       └── resources/
│           ├── config.properties     # Main configuration file for the framework
│           ├── cucumber.properties   # Cucumber-specific settings
│           └── logback.xml           # Logging configuration
└── pom.xml                           # Maven project configuration
```

-----

## 🚀 Getting Started

### Prerequisites

Before you begin, ensure you have the following installed:

  * **Java JDK 17** or higher
  * **Apache Maven**
  * An IDE like **IntelliJ IDEA** or **Eclipse**

### Setup

1.  **Clone the repository:**

    ```bash
    git clone https://github.com/nisarahmedqae/API-BDD-Framework.git
    ```

2.  **Navigate to the project directory:**

    ```bash
    cd API-BDD-Framework
    ```

3.  **Install dependencies:** Maven will automatically download all the required dependencies defined in `pom.xml`.

    ```bash
    mvn clean install
    ```

-----

## ⚙️ Configuration

All major framework settings are managed in `src/test/resources/config.properties`.

### Switching Environments

You can override the default environment set in `config.properties` by passing a system property during the Maven build.

To run tests on the **CERT** environment:

```bash
mvn clean test -Denv=CERT
```

-----

## ▶️ How to Run Tests

### Running via Maven

You can execute tests from the command line using Maven. The `TestRunner.java` class controls which tests are run via its `tags` attribute.

1.  **Run a specific tag (e.g., `@createBooking`):** Modify the `tags` attribute in `src/test/java/com/nahmed/runners/TestRunner.java` and then run:

    ```bash
    mvn clean test
    ```

2.  **Run all tests:** To run all feature files tagged with `@bookerAPI`, use the default configuration in `TestRunner.java`.

### Running Failed Tests

The framework is configured to automatically capture failed scenarios in `reports/cucumber/rerun_data.txt`. You can run only these failed tests using the `FailedTestRunner.java`.

To run only the failed tests from the last run:

```bash
mvn clean test -Dcucumber.features="@reports/cucumber/rerun_data.txt"
```

### Parallel Execution

Parallel execution is enabled by default in the runners. The number of parallel threads can be configured directly in the runner class.

-----

## 📊 Reporting and Logging

After a test run, the following artifacts are generated in the `reports/` directory:

  * **ExtentReports:** A detailed, interactive HTML report with full request/response logs for each step.
      * **Location:** `reports/extent/extent_report.html`
  * **Cucumber HTML Report:** A standard report generated by Cucumber.
      * **Location:** `reports/cucumber/bdd_report.html`
  * **Execution Log:** A text file containing detailed logs from the test run, as configured by `logback.xml`.
      * **Location:** `reports/logs/test_execution.log`

-----

## ✍️ Writing New Tests

Follow these steps to add a new API test case:

1.  **Create a Feature File:** Add a new `.feature` file in `src/test/java/com/nahmed/features/`. Write your API scenarios using Gherkin syntax.
2.  **Define JSON Schemas (Optional):** If you want to validate the response structure, add a new `.json` schema file in `src/main/java/com/nahmed/models/schema/`.
3.  **Create POJOs (Optional but Recommended):** For complex JSON bodies, create request/response POJO classes in `com.nahmed.models` to enable type-safe data handling.
4.  **Implement Step Definitions:**
      * Create a new class in `com.nahmed.stepdefinitions`.
      * Use **Dependency Injection** in your constructor to get instances of `TestContext` and any required factories (e.g., `RequestSpecBuilderFactory`).
      * Implement the Java methods that map to your Gherkin steps.
      * Use the `TestContext` to store and retrieve shared data like endpoints, the response object, and generated IDs.
      * Use `RequestSpecBuilderFactory` to create your `RequestSpecification`.
      * Use `ResponseHandler` to deserialize the JSON response into your POJOs.
      * Use `ValidationUtils` to perform JSON schema validation.

-----
