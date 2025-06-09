# Opendota Playwright Web Automation
This project provides an automated test suite for validating the OpenDota website. It uses the Playwright framework to interact with the web pages and compares the data displayed there against the data retrieved directly from the OpenDota API. It follows the Page Object Model design pattern, organizing web interactions by page.

## Technologies Used
|                   |                       |
| ----------------- | --------------------- |
| Language          | **Java**              |
| Build Tool        | **Maven**             |
| UI Framework      | **Playwright**        |
| Testing Framework | **TestNG**            |
| Reporting         | **ExtentReports**     |
| Logging           | **Log4j**             |
| Design Pattern    | **Page Object Model** |
| Remote Execution  | **Selenium Grid**     |

## Key Features

- **Page Object Model**: Organizes web interactions by page, making the code more maintainable and readable.
- **Report Generation**: Generates detailed HTML reports with ExtentReports, including screenshots of failed tests, test log IDs for tracking, and comprehensive insights into test execution.
- **Logging**: Uses Log4j for logging test execution details, which helps in debugging and understanding test flow.
- **Remote Execution**: Supports running tests on a Selenium Grid, allowing for parallel execution and cross-browser testing.

## Getting Started
### Prerequisites
- Java Development Kit (JDK) 21 or higher
- Maven 3.8.6 or higher for build and dependency management
- Playwright installed in your project (can be done via Maven dependencies)
- Selenium Grid setup (optional, for remote execution)
- Docker (optional, for running Selenium Grid in containers)

### Running Tests
#### Locally
- Open a terminal and navigate to the project directory.
- Enter the command below:
   ```shell
   mvn clean test -DsuiteXmlFile="<selected testing xml file>"
   ```
#### Remotely (using Selenium Grid and Docker)
- (to be continued)
