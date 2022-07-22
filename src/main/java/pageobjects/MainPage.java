package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDate;
import java.time.YearMonth;

import static java.lang.String.format;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Locale.US;

public class MainPage extends BasePage {

    public MainPage(WebDriver driver) {
        super(driver);
    }

    public MainPage enterCityName(String cityName) {
        WebElement searchField = driver.findElement(By.xpath("//input[@type='search']"));
        searchField.sendKeys(cityName);
        return this;
    }

    public MainPage chooseDates(LocalDate startDate, LocalDate endDate) {

        WebElement datesField = driver.findElement(By.className("xp__dates-inner"));

        datesField.click();

        WebElement forwardButton = driver.findElement(By.xpath("//div[@data-bui-ref='calendar-next']"));

        var formatter = ofPattern("MMMM yyyy", US);
        var monthStart = YearMonth.of(startDate.getYear(), startDate.getMonth());

        YearMonth currentMonth;
        WebElement calendarMonth;
        Boolean moveForward = false;
        do {
            calendarMonth = new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.visibilityOfElementLocated(
                            By.className("bui-calendar__month")));
            currentMonth = YearMonth.parse(calendarMonth.getText(), formatter);
            moveForward = currentMonth.isBefore(monthStart);
            if (moveForward) {
                forwardButton.click();
            }
        } while (moveForward);

        clickDate(startDate, calendarMonth);
        clickDate(endDate, calendarMonth);

        return this;
    }

    public StaysResultsPage executeSearch() {
        WebElement searchButton = driver.findElement(By.className("sb-searchbox__button"));
        searchButton.click();
        return new StaysResultsPage(driver);
    }

    public AttractionsPage openAttractionsPage() {
        var attractions = driver.findElement(By.xpath("//a[@data-decider-header='attractions']"));
        attractions.click();

        return new AttractionsPage(driver);
    }

    private void clickDate(LocalDate date, WebElement calendarMonth) {
        String dateXPath = "//td[@data-date = '%s']";
        String stringDate = date.format(ofPattern("yyyy-MM-dd"));
        var currentDateXPath = format(dateXPath, stringDate);
        var currentDate = calendarMonth.findElement(By.xpath(currentDateXPath));
        currentDate.click();
    }
}
