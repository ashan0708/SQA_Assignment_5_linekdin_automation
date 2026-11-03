# LinkedIn Web UI Automation Test Suite

Automated functional testing suite for key user workflows on LinkedIn using **Java**, **Selenium WebDriver**, and **TestNG**. 

---

## 📌 Deliverable Overview

| Metric | Details |
| :--- | :--- |
| **Target Application** | [LinkedIn](https://www.linkedin.com) |
| **Frameworks** | Selenium WebDriver 4.26.0, TestNG 7.10.2 |
| **Language & Build** | Java JDK 17, Apache Maven |
| **Test Design Pattern** | BaseTest configuration with dynamic explicit waits & anti-bot options |
| **Execution Result** | **4 Total Tests, 4 Passes, 0 Failures** (100% Pass Rate)|

---

## 🚀 Automated Test Scenarios

The suite covers 4 automated end-to-end functional test cases:

1. **`testValidLogin` (Task 1 - Positive Login)**:
   * Enters verified user credentials, dispatches React events, submits form, and asserts navigation to the LinkedIn Feed (`/feed`).
2. **`testInvalidPasswordLogin` (Task 2 - Negative Login)**:
   * Enters a valid email with an incorrect password and verifies that the error banner/alert is displayed and access is denied.
3. **`testSearchKeyword` (Task 3 - Search Functionality)**:
   * Queries `"Software Testing"` and verifies that the results page URL contains `/search/results/` and the search results container renders.
4. **`testClickFirstSearchResult` (Task 4 - Click Search Result)**:
   * Executes a search query, locates the first result entity/profile anchor, executes navigation, and validates transition away from search results to the destination page.

---

## 🛠️ Project Structure

```text
linkedin-automation/
├── src/
│   └── test/
│       └── java/
│           ├── base/
│           │   └── BaseTest.java        # Driver lifecycle, anti-bot flags, screenshot on failure
│           └── tests/
│               ├── LoginTest.java       # Positive & negative login flows
│               └── SearchTest.java      # Keyword search & search result interaction
├── target/
│   └── screenshots/                     # Automated captures upon test failures
├── test-output/
│   ├── emailable-report.html            # TestNG execution summary report
│   └── index.html
├── pom.xml                              # Maven dependencies & build plugins
├── testng.xml                           # XML suite configuration
└── README.md                            # Documentation & execution instructions
```

## ⚙️ Prerequisites & Setup

Ensure the following are installed on your machine:
* **Java JDK 17** (or above)
* **Apache Maven 3.8+**
* **Google Chrome Browser** (latest version)
* **Eclipse IDE** (or IntelliJ IDEA)

---

## 🔐 Credentials Configuration

Test credentials can be updated directly inside the respective test classes:

* `src/test/java/tests/LoginTest.java`:
  ```java
  private final String VALID_EMAIL = "your_email@gmail.com";
  private final String VALID_PASSWORD = "your_password";
  
* `src/test/java/tests/SearchTest.java`:
 ```java
  private final String VALID_EMAIL = "your_email@gmail.com";
  private final String VALID_PASSWORD = "your_password";
```

▶️ How to Run the Tests

Method 1: 
Using Eclipse IDE (Recommended)
1.Clone or import this project into Eclipse as an Existing Maven Project.
2.Right-click on testng.xml in the Package Explorer.   
3.Select Run As ➔ TestNG Suite

Method 2: 
Using Maven Command Line
Open your terminal in the project root directory and execute:
 ```Bash
mvn clean test
```

📊 Test Reports & Screenshots
After test execution completes:
- HTML Test Report: Open test-output/emailable-report.html in any web browser to view detailed execution duration, status, and method-level test results.   
- Failure Screenshots: If any assertion fails, a timestamped PNG screenshot is automatically captured under

