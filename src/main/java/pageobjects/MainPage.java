package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

import static java.lang.String.format;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Locale.US;

public class MainPage extends BasePage {

    public static final By SEARCH_FIELD = By.xpath("//input[@type='search']");
    public static final By DATES = By.className("xp__dates-inner");
    public static final By CALENDAR_FORWARD_BUTTON = By.xpath("//div[@data-bui-ref='calendar-next']");
    public static final By CALENDAR_MONTH = By.className("bui-calendar__month");
    public static final By SEARCH_BUTTON = By.className("sb-searchbox__button");
    public static final By ATTRACTIONS_LINK = By.xpath("//a[@data-decider-header='attractions']");
    public static final String DATE_XPATH = "//td[@data-date = '%s']";

    public MainPage(WebDriver driver) {
        super(driver);
    }

    public MainPage enterCityName(String cityName) {
        driver.findElement(SEARCH_FIELD).sendKeys(cityName);
        return this;
    }

    public MainPage chooseDates(LocalDate startDate, LocalDate endDate) {

        driver.findElement(DATES).click();

        WebElement monthElement = getMonth(startDate);
        clickDate(startDate, monthElement);

        monthElement = getMonth(endDate);
        clickDate(endDate, monthElement);

        return this;
    }

    public StaysResultsPage executeSearch() {
        driver.findElement(SEARCH_BUTTON).click();
        return new StaysResultsPage(driver);
    }

    public AttractionsPage openAttractionsPage() {
        driver.findElement(ATTRACTIONS_LINK).click();
        return new AttractionsPage(driver);
    }

    private WebElement getMonth(LocalDate startDate) {
        DateTimeFormatter formatter = ofPattern("MMMM yyyy", US);
        YearMonth monthStart = YearMonth.of(startDate.getYear(), startDate.getMonth());

        WebElement monthElement;
        boolean moveForward;
        do {
            monthElement = new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.visibilityOfElementLocated(CALENDAR_MONTH));

            moveForward = YearMonth.parse(monthElement.getText(), formatter)
                    .isBefore(monthStart);

            if (moveForward) {
                driver.findElement(CALENDAR_FORWARD_BUTTON).click();
            }
        } while (moveForward);

        return monthElement;
    }

    private void clickDate(LocalDate date, WebElement calendarMonth) {
        String stringDate = date.format(ofPattern("yyyy-MM-dd"));
        String currentDateXPath = format(DATE_XPATH, stringDate);
        calendarMonth.findElement(By.xpath(currentDateXPath)).click();
    }
}
