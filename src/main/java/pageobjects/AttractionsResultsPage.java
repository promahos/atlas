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

    public static final By RESULTS_SHOWING = By.xpath("//span[contains(text(),'Showing')]");
    public static final By RESULTS_QUANTITY = By.xpath("//h2[contains(text(),'results')]");
    public static final By PRICE = By.xpath("//a[@data-testid='card']//div[contains(text(),'From')]/following-sibling::div");
    public static final By FILTER_BY_PRICE = By.xpath("//*[@name = 'filter_by_price']/following-sibling::label");

    public AttractionsResultsPage(WebDriver driver) {
        super(driver);
    }

    public AttractionsResultsPage setFirstFilterByPrice() {
        getFirstFilterByPrice().click();
        return this;
    }

    public String getFirstFilterByPriceText() {
        return getFirstFilterByPrice().getText();
    }

    public int getFirstFilterByPriceExpectedQuantity() {
        String filter = getFirstFilterByPriceText();
        String filterQuantityString = filter.substring(filter.indexOf("(") + 1, filter.indexOf(")"));

        return Integer.parseInt(filterQuantityString);
    }

    public double getFirstFilterByPricePriceFrom() {
        String filter = getFirstFilterByPriceText().split("\n")[0];
        String priceFromString = filter.substring(filter.indexOf(" "), filter.indexOf("-")).trim();

       return Double.parseDouble(priceFromString);
    }

    public double getFirstFilterByPricePriceTo() {
        String filter = getFirstFilterByPriceText().split("\n")[0];
        String priceToString = filter.substring(filter.lastIndexOf(" ")).trim();

        return Double.parseDouble(priceToString);
    }

    public int getResultsQuantity() {
        String showingText = driver.findElement(RESULTS_SHOWING).getText();
        String quantityString = showingText.substring(showingText.indexOf("of") + 3);

        return Integer.parseInt(quantityString);
    }

    public boolean waitForResultsQuantity() {
        WebElement results = new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                ExpectedConditions.visibilityOfElementLocated(RESULTS_QUANTITY));

        return Objects.nonNull(results);
    }

    public List<Double> getResultsPrices() {
        List<WebElement> prices = driver.findElements(PRICE);

        if (prices.size() == 0) {
            return new ArrayList<>();
        } else {
            List<String> pricesString = prices // collect prices as strings
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
        List<WebElement> filtersByPrice = driver.findElements(FILTER_BY_PRICE);
        return filtersByPrice.get(0);
    }
}
