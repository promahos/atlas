import io.github.bonigarcia.wdm.managers.ChromeDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.internal.ConstructorOrMethod;
import pageobjects.AttractionsResultsPage;
import pageobjects.MainPage;
import pageobjects.StaysResultsPage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;

public class BookingTest {

    private WebDriver driver;

    @BeforeClass
    public void setUp(ITestContext context) {
        Optional<Path> browserPath = ChromeDriverManager.chromedriver().getBrowserPath();
        assertThat(browserPath)
                .as("Chrome browser should be installed to run the tests")
                .isPresent();

        ChromeDriverManager.chromedriver().setup();

        driver = new ChromeDriver();
        driver.manage().timeouts().pageLoadTimeout(ofSeconds(100));
        driver.manage().timeouts().implicitlyWait(ofSeconds(100));
        driver.manage().window().maximize();

        var testMethods = context.getAllTestMethods();
        String methodsNames = Arrays.stream(testMethods)
                .filter(b -> b.getRealClass() == this.getClass())
                .map(ITestNGMethod::getConstructorOrMethod)
                .map(ConstructorOrMethod::getName)
                .collect(Collectors.joining(", "));
        Path targetFilePath = Paths.get("results", "testMethodsNamesBeforeExecution.txt");
        try {
            Files.write(targetFilePath, methodsNames.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException("Failed to write file " + targetFilePath, e);
        }
    }

    @Test
    public void verifyStaysSearchResults() {

        // 1. Open page https://www.booking.com
        driver.get("https://www.booking.com");
        MainPage mainPage = new MainPage(driver);

        // 2. Choose city
        String city = "New York";
        mainPage.enterCityName(city);

        // 3. Choose dates
        LocalDate startDate = LocalDate.of(2022, 12, 1);
        LocalDate endDate = LocalDate.of(2022, 12, 30);
        mainPage.chooseDates(startDate, endDate);

        // 4. Search
        StaysResultsPage resultsPage = mainPage.executeSearch();

        // Verify
        // City
        var addresses = resultsPage.getAddresses();
        assertThat(addresses)
                .as("All addresses should contain chosen city")
                .isNotEmpty()
                .allMatch(e -> e.contains(city));

        // Dates
        var resultsStartDate = resultsPage.getStartDate();
        assertThat(resultsStartDate)
                .as("Start date on results page should be equal to chosen value")
                .isEqualTo(startDate);

        var resultsEndDate = resultsPage.getEndDate();
        assertThat(resultsEndDate)
                .as("End date on results page should be equal to chosen value")
                .isEqualTo(endDate);
    }

    @Test
    public void verifyAttractionsFilterByPrice() {

        // 1. Open page https://www.booking.com
        driver.get("https://www.booking.com");
        MainPage mainPage = new MainPage(driver);

        // 2. Go to attractions page
        var attractionsPage = mainPage.openAttractionsPage();

        // 3. Choose city
        String city = "New York";
        attractionsPage.enterCityName(city);

        attractionsPage.waitForSuggestions();

        // 4. Search
        AttractionsResultsPage resultsPage = attractionsPage.clickSearch();

        var totalQuantity = resultsPage.getResultsQuantity();

        // 5. Find price filter, pick first value, remember it, set filter
        var priceFrom = resultsPage.getFirstFilterByPricePriceFrom();
        var priceTo = resultsPage.getFirstFilterByPricePriceTo();
        var filterQuantity = resultsPage.getFirstFilterByPriceExpectedQuantity();

        resultsPage.setFirstFilterByPrice();
        resultsPage.waitForResultsQuantity();

        // 6. Check results: quantity should be equal to expected (and less than all results),
        // prices should be in range of filter
        var resultsQuantity = resultsPage.getResultsQuantity();

        assertThat(resultsQuantity)
                .isLessThan(totalQuantity)
                .isEqualTo(filterQuantity);

        var prices = resultsPage.getResultsPrices();

        assertThat(prices)
                .isNotEmpty()
                .allMatch(d -> d <= priceTo)
                .allMatch(d -> d >= priceFrom);
    }

    @AfterClass
    public void tearDown() {
        driver.close();
        driver.quit();
    }
}
