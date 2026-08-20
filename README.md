# automationexercise.com API Automation Framework

Rest Assured + TestNG + Allure framework covering all 14 endpoints listed at
[automationexercise.com/api_list](https://automationexercise.com/api_list).

> Quirk of this API: every response is returned with **HTTP 200**, and the real
> status lives in the JSON body's `responseCode` field (200/201/400/404/405).
> Every test asserts on `responseCode`, not just the HTTP status line.

## Coverage

| # | Endpoint | Test class |
|---|----------|------------|
| 1 | GET `/api/productsList` | `ProductsApiTest` |
| 2 | POST `/api/productsList` (405) | `ProductsApiTest` |
| 3 | GET `/api/brandsList` | `BrandsApiTest` |
| 4 | PUT `/api/brandsList` (405) | `BrandsApiTest` |
| 5 | POST `/api/searchProduct` | `SearchProductApiTest` |
| 6 | POST `/api/searchProduct` no param (400) | `SearchProductApiTest` |
| 7 | POST `/api/verifyLogin` valid | `LoginApiTest` |
| 8 | POST `/api/verifyLogin` missing email (400) | `LoginApiTest` |
| 9 | DELETE `/api/verifyLogin` (405) | `LoginApiTest` |
| 10 | POST `/api/verifyLogin` invalid (404) | `LoginApiTest` |
| 11 | POST `/api/createAccount` (201) | `AccountApiTest` |
| 12 | DELETE `/api/deleteAccount` (200) | `AccountApiTest` |
| 13 | PUT `/api/updateAccount` (200) | `AccountApiTest` |
| 14 | GET `/api/getUserDetailByEmail` (200) | `AccountApiTest` |

## Project layout

```
src/main/java/com/automationexercise/api/
  config/       ConfigManager      - reads base.uri etc. from config.properties
  constants/    Endpoints          - endpoint path constants
  base/         BaseTest           - shared RequestSpecification + Allure/log filters
  pojo/         UserPayload        - form-param payload for create/update account
  utils/        TestDataFactory    - builds valid test users with unique emails

src/test/java/com/automationexercise/api/tests/
  ProductsApiTest, BrandsApiTest, SearchProductApiTest, LoginApiTest, AccountApiTest
```

`LoginApiTest` provisions its own throwaway user in `@BeforeClass`/`@AfterClass`.
`AccountApiTest` chains create -> get -> update -> delete via `dependsOnMethods`
so the lifecycle always runs in order even under `testng.xml`'s `parallel="classes"`.

## Prerequisites

- JDK 17+ (JDK 25 is installed and works fine via `maven.compiler.release=17`)
- Maven 3.9+ — not currently installed on this machine. Install with either:
  ```powershell
  winget install Apache.Maven
  ```
  or
  ```powershell
  choco install maven
  ```
  Then open a new terminal so `mvn` is on PATH.
- (Optional, for viewing reports locally) [Allure commandline](https://allurereport.org/docs/install/):
  ```powershell
  winget install --id AllureReport.Allure
  ```

## Running the tests

```powershell
mvn clean test
```

This runs `testng.xml`, and drops raw Allure result JSON in `target/allure-results`.

## Viewing the Allure report

```powershell
mvn allure:serve
```

or, with the Allure CLI installed directly:

```powershell
allure serve target/allure-results
```

## CI

`.github/workflows/api-tests.yml` runs the suite on every push/PR to `main` and
publishes the Allure report to the `gh-pages` branch (served via GitHub Pages)
plus uploads raw Surefire results as a build artifact.

## Configuration

`src/main/resources/config.properties` holds `base.uri` (defaults to
`https://automationexercise.com`). Override per-run without editing the file:

```powershell
mvn clean test "-Dbase.uri=https://staging.automationexercise.com"
```
