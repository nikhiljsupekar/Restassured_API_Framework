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
`AccountApiTest` chains create -> get -> update -> delete via `dependsOnMethods`.
`testng.xml` runs sequentially (`parallel="none"`) — automationexercise.com is a
public demo site behind Cloudflare and returns sporadic 520s under concurrent load.

## Prerequisites

- JDK 17+ (JDK 25 is installed and works fine via `maven.compiler.release=17`)
- Maven 3.9+ — not currently installed on this machine. `winget install Apache.Maven`
  does **not** work (no such package in the winget repository as of this writing),
  and Chocolatey/Scoop aren't installed either. Pick one:

  **Option A — manual install (most reliable on Windows):**
  1. Download the binary zip from https://maven.apache.org/download.cgi
     (e.g. `apache-maven-3.9.16-bin.zip`).
  2. Extract it somewhere permanent, e.g. `C:\Tools\apache-maven-3.9.16`.
  3. Add `C:\Tools\apache-maven-3.9.16\bin` to your user `PATH`
     (Settings → System → About → Advanced system settings → Environment Variables).
  4. Open a **new** terminal and confirm with `mvn -version`.

  **Option B — if you already use a package manager:**
  ```powershell
  choco install maven      # Chocolatey
  scoop install maven      # Scoop
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

`.github/workflows/api-tests.yml` runs the suite on every push/PR to `main`,
generates the Allure report via `mvn allure:report`, and deploys it straight
to GitHub Pages using `actions/deploy-pages` (no `gh-pages` branch involved)
plus uploads raw Surefire results as a build artifact.

**One-time setup:** in the repo's Settings → Pages, set Source to
**"GitHub Actions"** (not "Deploy from a branch") — the workflow's `deploy`
job needs that to publish successfully.

## Configuration

`src/main/resources/config.properties` holds `base.uri` (defaults to
`https://automationexercise.com`). Override per-run without editing the file:

```powershell
mvn clean test "-Dbase.uri=https://staging.automationexercise.com"
```
