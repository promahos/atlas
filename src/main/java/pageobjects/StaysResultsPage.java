package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Locale.US;

public class StaysResultsPage extends BasePage {

    public static final By ADDRESS = By.xpath("//span[@data-testid='address']");
    public static final By START_DATE = By.xpath("//button[@data-testid='date-display-field-start']");
    public static final By END_DATE = By.xpath("//button[@data-testid='date-display-field-end']");

    public StaysResultsPage(WebDriver driver) {
        super(driver);
    }

    public List<String> getAddresses() {
        List<WebElement> addresses = driver.findElements(ADDRESS);

        if (addresses.size() == 0) {
            return new ArrayList<>();
        } else {
            return addresses
                    .stream()
                    .map(WebElement::getText)
                    .collect(Collectors.toList());
        }
    }

    public LocalDate getStartDate() {
        return getDate(START_DATE);
    }

    public LocalDate getEndDate() {
        return getDate(END_DATE);
    }

    private LocalDate getDate(By dateBy) {
        WebElement startDateElement = driver.findElement(dateBy);
        String date = startDateElement.getText().split("\n")[1];
        DateTimeFormatter formatter = ofPattern("EEEE d MMMM yyyy", US);

        return LocalDate.parse(date, formatter);
    }
}
