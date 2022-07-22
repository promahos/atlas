package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Objects;

public class AttractionsPage extends BasePage {

    public static final By SEARCH_FIELD = By.xpath("//input[@type='search']");
    public static final By SEARCH_SUGGESTION = By.xpath("//a[@data-testid='search-bar-result']");
    public static final By SEARCH_BUTTON = By.xpath("//button[@type='submit']");

    public AttractionsPage(WebDriver driver) {
        super(driver);
    }

    public AttractionsPage enterCityName(String cityName) {
        driver.findElement(SEARCH_FIELD).sendKeys(cityName);
        return this;
    }

    public boolean waitForSuggestions() {
        WebElement results = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(SEARCH_SUGGESTION));

        return Objects.nonNull(results);
    }

    public AttractionsResultsPage clickSearch() {
        driver.findElement(SEARCH_BUTTON).click();
        return new AttractionsResultsPage(driver);
    }
}
