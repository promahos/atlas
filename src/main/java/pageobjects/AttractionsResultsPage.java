package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class AttractionsResultsPage extends BasePage {

    public AttractionsResultsPage(WebDriver driver) {
        super(driver);
    }

    public AttractionsResultsPage setFirstFilterByPrice() {
        var filter = getFirstFilterByPrice();
        filter.click();

        return this;
    }

    public String getFirstFilterByPriceText() {
        var filter = getFirstFilterByPrice();

        return filter.getText();
    }

    public int getFirstFilterByPriceExpectedQuantity() {
        var filter = getFirstFilterByPriceText();
        var filterQuantityString = filter.substring(filter.indexOf("(") + 1, filter.indexOf(")"));

        return Integer.parseInt(filterQuantityString);
    }

    public Double getFirstFilterByPricePriceFrom() {
        var filter = getFirstFilterByPriceText().split("\n")[0];
        var priceFromString = filter.substring(filter.indexOf(" "), filter.indexOf("-")).trim();

       return Double.parseDouble(priceFromString);
    }

    public Double getFirstFilterByPricePriceTo() {
        var filter = getFirstFilterByPriceText().split("\n")[0];
        var priceToString = filter.substring(filter.lastIndexOf(" ")).trim();

        return Double.parseDouble(priceToString);
    }

    public int getResultsQuantity() {
        var showingText = driver.findElement(By.xpath("//span[contains(text(),'Showing')]")).getText();
        var quantityString = showingText.substring(showingText.indexOf("of") + 3);

        return Integer.parseInt(quantityString);
    }

    public boolean waitForResultsQuantity() {
        WebElement results = new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//h2[contains(text(),'results')]")));

        return Objects.nonNull(results);
    }

    public List<Double> getResultsPrices() {
        var prices = driver.findElements(By.xpath("//a[@data-testid='card']//div[contains(text(),'From')]/following-sibling::div"));

        if (prices.size() == 0) {
            return new ArrayList<>();
        } else {
            var pricesString = prices // collect prices as strings
                    .stream()
                    .map(WebElement::getText)
                    .filter(Predicate.not(String::isBlank))
                    .collect(Collectors.toList());

            pricesString.replaceAll(s -> s.substring(s.indexOf(" "))); // get rid of currency text

            return pricesString
                    .stream()
                    .map(Double::parseDouble)
                    .collect(Collectors.toList());
        }
    }

    private WebElement getFirstFilterByPrice() {
        var filtersByPrice = driver.findElements(By.xpath("//*[@name = 'filter_by_price']/following-sibling::label"));
        return filtersByPrice.get(0);
    }
}
