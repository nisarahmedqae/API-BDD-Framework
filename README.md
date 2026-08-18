# restassured-bdd-framework (by: Nisar Ahmed)

A Java 17 API automation framework built with **Rest-Assured**, **Cucumber**, **TestNG**, and **ExtentReports**.

This project is designed for readable BDD-style API testing with:
- scenario-based state sharing through **Cucumber PicoContainer**
- parallel execution through **TestNG DataProvider**
- detailed request/response logging
- JSON schema validation
- environment-based configuration
- local and CI-specific runners

---

## Features

- **BDD API testing** with Gherkin feature files and Cucumber step definitions
- **Rest-Assured request specification factory** for reusable API setup
- **POJO serialization/deserialization** for request and response models
- **Parallel scenario execution** with configurable thread count
- **Environment switching** using config plus runtime overrides
- **HTML reporting** through Cucumber HTML and Extent Spark reports
- **Detailed request/response logging** into console, file logs, and Extent reports
- **JSON schema validation** for response contracts
- **Failed-scenario rerun support** using a dedicated rerun runner
- **Cross-platform path handling** for schemas, config, and report output

---

## Tech Stack

- **Java:** 17+
- **Build Tool:** Maven
- **BDD:** Cucumber 7
- **Test Runner:** TestNG
- **API Testing:** Rest-Assured
- **Reporting:** ExtentReports
- **Logging:** SLF4J + Logback
- **JSON Mapping:** Jackson

---

## Project Structure

```text
API-BDD-Framework-master/
├── pom.xml
├── README.md
├── reports/
│   ├── cucumber/
│   ├── extent/
│   └── logs/
└── src/
    ├── main/java/com/nahmed/
    │   ├── constants/         # framework constants and report/config paths
    │   ├── enums/             # config enums
    │   ├── exceptions/        # custom exceptions
    │   ├── factories/         # request/response specification factories
    │   ├── listeners/         # Cucumber listener + Rest-Assured request filter
    │   ├── models/            # request/response POJOs and JSON schemas
    │   ├── reports/           # Extent report management
    │   └── utils/             # configuration, schema validation, context, helpers
    └── test/
        ├── java/com/nahmed/
        │   ├── events/        # Cucumber hooks
        │   ├── features/      # .feature files
        │   ├── runners/       # local, CI, and failed-test runners
        │   └── stepdefinitions/ # Cucumber step definitions
        └── resources/
            ├── config.properties
            ├── cucumber.properties
            └── logback.xml
└── target/
```

---

## Current Execution Model

This framework currently uses **three runners**:

### `TestRunner`
Used for **local execution**.

Current behavior:
- runs from Surefire by default (`mvn test`)
- uses `@add_place` as the active tag filter in code
- supports parallel execution

File:
- `src/test/java/com/nahmed/runners/TestRunner.java`

### `CiTestRunner`
Used for **CI/CD execution**.

Current behavior:
- runs all matching scenarios unless tags are supplied through profile/system/env values
- supports parallel execution
- is used by the `ci`, `smoke`, `sanity`, and `regression` Maven profiles

File:
- `src/test/java/com/nahmed/runners/CiTestRunner.java`

### `FailedTestRunner`
Used to rerun scenarios listed in:
- `target/cucumber/rerun.txt`

File:
- `src/test/java/com/nahmed/runners/FailedTestRunner.java`

---

## Prerequisites

Make sure the following are installed:

- **JDK 17** or higher
- **Maven 3.8+**
- Optional IDE: IntelliJ IDEA / Eclipse

You can verify quickly with:

```powershell
java -version
mvn -version
```

---

## Getting Started

### 1. Clone the repository

```powershell
git clone https://github.com/nisarahmedqae/API-BDD-Framework.git
Set-Location API-BDD-Framework
```

### 2. Build the project

```powershell
mvn clean install -DskipTests
```

---

## Configuration

Primary configuration file:
- `src/test/resources/config.properties`

Current config keys:
- `environment`
- `base_url_int`
- `token_url_int`
- `client_id_int`
- `client_secret_int`
- `base_url_cert`
- `token_url_cert`
- `client_id_cert`
- `client_secret_cert`
- `invalid_token`
- `expired_token`
- `dataprovider_thread_count`

### Environment Resolution
`ConfigurationManager` resolves the current environment as:
1. `-Denv=...` system property
2. `environment` value from `config.properties`
3. fallback to `INT` if config cannot be resolved

Examples:

```powershell
mvn test -Denv=CERT
mvn -Pci test -Denv=INT
```

### Runtime Thread Resolution

Thread count is resolved by `RuntimeConfigResolver` in this order:
1. JVM system property (for example `-Dthreads=4`)
2. environment variable (`THREADS`)
3. `dataprovider_thread_count` from `config.properties`

For failed-rerun execution, `FailedTestRunner` uses:
- `-DfailedThreads=...`
- then `FAILEDTHREADS` env variable
- then `dataprovider_thread_count`

---

## How to Run Tests

## Local Run

Default local run uses `TestRunner`, which currently targets `@add_place`.

```powershell
mvn test
```

## CI Run

Use the CI profile to run `CiTestRunner`.

```powershell
mvn -Pci test
```

## Run with More Threads

```powershell
mvn -Pci test -Dthreads=4
```

## Run on a Specific Environment

```powershell
mvn -Pci test -Denv=CERT
```

## Run with Explicit Cucumber Tag Filter

You can pass tags directly without changing runner code:

```powershell
mvn -Pci test -Dcucumber.filter.tags="@get_place"
mvn -Pci test -Dtags="@delete_place_positive"
```

## Profile-Based Runs

Available Maven profiles:
- `ci`
- `smoke`
- `sanity`
- `regression`

Examples:

```powershell
mvn -Pci test
mvn -Psmoke test
mvn -Psanity test
mvn -Pregression test
```

> Note: the profile wiring is active, but the current feature files do **not** contain `@smoke`, `@sanity`, or `@regression` tags. Until those tags are added to feature files, running these profile commands will not match any scenarios.

## Rerun Failed Scenarios

The framework writes rerun entries to:
- `target/cucumber/rerun.txt`

You can execute the failed runner directly with Maven:

```powershell
mvn -Dtest=FailedTestRunner test
```

> Note: this runner only executes scenarios if `target/cucumber/rerun.txt` exists and contains failed scenario entries from a prior run.

---

## Parallel Execution

Parallel execution is enabled in all three runners using TestNG's DataProvider:
- `TestRunner`
- `CiTestRunner`
- `FailedTestRunner`

Current state:
- verified working with multiple threads
- hooks were updated to avoid global `RestAssured.reset()` during parallel execution
- `TestContext` is used for scenario-level shared data
- request specs are built fresh per request

Example:

```powershell
mvn -Pci test -Dthreads=4
```

---

## Reporting and Logs

### Cucumber HTML Report
Generated at:
- `reports/cucumber/bdd_report.html`

### Rerun File
Generated at:
- `target/cucumber/rerun.txt`

### Extent Report
Generated under a date-based folder structure:
- `reports/extent/yyyy-MM-dd/yyyy-MM-dd_HH-mm-ss.html`

Example:
- `reports/extent/2026-08-18/2026-08-18_22-31-37.html`

### Log Files
Generated under a date-based folder structure:
- `reports/logs/yyyy-MM-dd/yyyy-MM-dd_HH-mm-ss.log`

### Retention Behavior
`FrameworkConstants` automatically purges old report/log files older than **3 months**.

### Report Opening Behavior
Extent report browser auto-open is skipped in Linux CI environments.

---

## Logging Behavior

The framework logs:
- scenario start and finish events
- step start and finish events
- request method, URI, headers, body
- response status and response body
- schema validation outcomes
- step pass/fail/skip details

Logging destinations:
- console
- Logback file appender
- Extent report nodes

Relevant classes:
- `com.nahmed.listeners.TestListener`
- `com.nahmed.listeners.RestAssuredRequestFilter`
- `src/test/resources/logback.xml`

---

## Request / Response Flow

Typical scenario flow:
1. Cucumber starts the scenario
2. `Hooks` logs environment and scenario details
3. Step definitions build requests through `RequestSpecBuilderFactory`
4. `RestAssuredRequestFilter` logs request/response details
5. Responses are deserialized through `ResponseHandler`
6. Schemas are validated through `ValidationUtils`
7. `TestListener` updates Extent report nodes and step outcomes
8. `Hooks` logs scenario completion

---

## Dependency Injection and Shared State

The framework uses:
- `io.cucumber:cucumber-picocontainer`

This enables constructor injection across step definition classes.

Typical injected objects include:
- `TestContext`
- `RequestSpecBuilderFactory`

`TestContext` is used to share scenario-scoped data such as:
- response
- request
- generated IDs like `place_id`
- arbitrary scenario values

---

## API Models and Schema Validation

### Request / Response Models
POJOs are organized under:
- `src/main/java/com/nahmed/models/request/...`
- `src/main/java/com/nahmed/models/response/...`

### JSON Schemas
Schema files are stored under:
- `src/main/java/com/nahmed/models/schema/`

Examples:
- `addPlaceSchema.json`
- `deletePlaceSchema.json`
- `getPlaceSchema.json`
- `updatePlaceSchema.json`

Validation is performed through:
- `ValidationUtils.validateResponseAgainstSchema(...)`

---

## Authentication Note

`AuthManager` is currently **OAuth-ready but mocked**.

Current behavior:
- `getBearerToken()` returns `Bearer mockToken`

So at present:
- authenticated flows can be structurally exercised
- real token generation is not active until the commented OAuth implementation is restored/adapted

If you plan to enable real OAuth:
- update `AuthManager`
- provide valid values for:
  - `token_url_<env>`
  - `client_id_<env>`
  - `client_secret_<env>`

---

## Current Tags in Feature Files

Current feature/scenario tags include:
- `@add_place`
- `@get_place`
- `@delete_place`
- `@delete_place_positive`
- `@delete_place_negative`
- `@update_place`
- `@update_place_positive`
- `@update_place_negative`
- `@google_maps`

This matters because:
- local run currently uses `@add_place`
- CI can be filtered dynamically using `-Dcucumber.filter.tags=...`

---

## Writing New Tests

### 1. Add or update a feature file
Create/update files under:
- `src/test/java/com/nahmed/features/`

### 2. Add request/response models if needed
Create POJOs under:
- `src/main/java/com/nahmed/models/request/...`
- `src/main/java/com/nahmed/models/response/...`

### 3. Add schema files if needed
Place schema JSON files under:
- `src/main/java/com/nahmed/models/schema/`

### 4. Implement step definitions
Create step definition classes under:
- `src/test/java/com/nahmed/stepdefinitions/`

Use constructor injection where needed, for example:
- `TestContext`
- `RequestSpecBuilderFactory`

### 5. Choose how the test should run
- local only via `TestRunner`
- CI via `CiTestRunner`
- rerun through `FailedTestRunner`

### 6. Add appropriate tags
If you want profile-based execution, add tags that match the profiles you intend to use, for example:
- `@smoke`
- `@sanity`
- `@regression`

---

## Useful Commands

### Local

```powershell
mvn test
```

### CI

```powershell
mvn -Pci test
```

### CI with 4 threads

```powershell
mvn -Pci test -Dthreads=4
```

### Run only one tag in CI

```powershell
mvn -Pci test -Dcucumber.filter.tags="@get_place"
```

### Use CERT environment

```powershell
mvn -Pci test -Denv=CERT
```

### Rerun failed scenarios

```powershell
mvn -Dtest=FailedTestRunner test
```

---

## Known Caveats

- `TestRunner` is currently hardcoded to `@add_place` for local execution.
- `smoke`, `sanity`, and `regression` Maven profiles are wired, but current feature files do not yet include matching tags.
- `AuthManager` currently returns a mock bearer token.
- Extent reports are timestamped and stored under date-based directories, not a single fixed filename.
- Failed rerun depends on `target/cucumber/rerun.txt` being generated by a previous run.

---

## Recommended Next Improvements

If you want to extend the framework further, the most practical next steps are:
- add `@smoke`, `@sanity`, and `@regression` tags to features/scenarios
- enable real OAuth token retrieval in `AuthManager`
- move test-only dependencies in `pom.xml` to `test` scope where appropriate
- add CI pipeline configuration (`.github/workflows`, Jenkins, Azure DevOps, etc.)
- document team conventions for adding new APIs, tags, and schemas
