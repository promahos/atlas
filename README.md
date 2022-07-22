# Atlas test task

This is a test task for test automation with following requirements
1. Automation tool: Selenium WebDriver
2. Language: Java
4. Used pattern: Page Object
5. Maven Project with TestNG

Test-cases to implement:
- Stays search:
1) Open website https://www.booking.com
2) Choose city "New York"
3) Choose dates : December 1, 2022 - December 30, 2022
4) Search
5) Verify, that in search results (first page)
   - all cities - "New York"
   - dates match: December 1, 2022 - December 30, 2022

- Attractions filter by  price:
1) Open website https://www.booking.com
2) Go to attractions page
3) Choose city "New York"
4) Search
5) Set filter by price (first filter value)
6) Verify, that in search results (first page)
    - all prices are in range of filter
    - results quantity is equal to expected by  filter

- Optional task:
   Using Java Reflection (or any other method) count quantity of test methods in project (with @Test annotation).
   Save their names as a list with "," delimiter to text file (.txt).

