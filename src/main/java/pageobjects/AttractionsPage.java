package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Objects;

public class AttractionsPage extends BasePage {

    public AttractionsPage(WebDriver driver) {
        super(driver);
    }

    public AttractionsPage enterCityName(String cityName) {
        driver.findElement(By.xpath("//input[@type='search']")).sendKeys(cityName);
        return this;
    }

    public boolean waitForSuggestions() {
        WebElement results = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//a[@data-testid='search-bar-result']")));

        return Objects.nonNull(results);
    }

    public AttractionsResultsPage clickSearch() {
        driver.findElement(By.xpath("//button[@type='submit']")).click();
        return new AttractionsResultsPage(driver);
    }
}
