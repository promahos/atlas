# Atlas test task

This is a test task for test automation with the following requirements
1. Automation tool: Selenium WebDriver
2. Language: Java
4. Used pattern: Page Object
5. Maven Project with TestNG

Test-cases to implement:
- Stays search:
1) Open website https://www.booking.com
2) Choose city "New York"
3) Choose dates: December 1, 2022 - December 30, 2022
4) Search
5) Verify, that in search results (first page)
   - all cities - "New York"
   - dates match: December 1, 2022 - December 30, 2022

- Attractions filter by  price:
1) Open website https://www.booking.com
2) Go to the attractions page
3) Choose city "New York"
4) Search
5) Set filter by price (first filter value)
6) Verify, that in search results (first page)
   - all prices are in the range of the filter
   - results quantity is equal to expected by the filter

- Optional task:
  Using Java Reflection (or any other method) count the quantity of test methods in the project (with @Test annotation).
  Save their names as a list with "," delimiter to a text file (.txt).

# Important notes

- Tests execution requires Chrome browser to be installed.
There is a known bug in version 103 of ChromeDriver which corresponds to 103 version of browser.
It sometimes results in an error with message:
"cannot determine loading status".
If you encounter this problem and your Chrome version is 103 - downgrade to 102 version is suggested as a temporary measure.
  (https://www.globalnerdy.com/2022/06/24/why-your-selenium-chromedriver-chrome-setup-stopped-working/)

- Optional task is implemented in three different ways, as it's not clear from the task, what we need these methods' names for.
All three options write to the project directory 'results', but in different files.
  - As a separate test. Implemented as a separate test class, needs direct naming of test classes to find methods in.
    Can be executed as any other test (with mvn or from IDE). Writes to file "testMethodsNamesAsTest.txt" as a result.
  - As a separate (not test) class. Requires 'org.reflections' dependency, gets all test classes from project system folders.
    Can be executed from IDE (class TestCounter, method main). Writes to file "testMethodsNamesAll.txt" as a result.
  - As a part of test execution. Uses TestNG context, allows getting test methods that are going to be executed. Writes to file "testMethodsNamesBeforeExecution.txt".
