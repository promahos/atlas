package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Locale.US;

public class StaysResultsPage extends BasePage {

    public StaysResultsPage(WebDriver driver) {
        super(driver);
    }

    public List<String> getAddresses() {
        var addresses = driver.findElements(By.xpath("//span[@data-testid='address']"));

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
        var startDate = driver.findElement(By.xpath("//button[@data-testid='date-display-field-start']"));
        String date = startDate.getText().split("\n")[1];
        var formatter = ofPattern("EEEE d MMMM yyyy", US);
        return LocalDate.parse(date, formatter);
    }

    public LocalDate getEndDate() {
        var endDate = driver.findElement(By.xpath("//button[@data-testid='date-display-field-end']"));
        String date = endDate.getText().split("\n")[1];
        var formatter = ofPattern("EEEE d MMMM yyyy", US);
        return LocalDate.parse(date, formatter);
    }
}
