package tests;

import base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

/**
 * Tasks 3 & 4: LinkedIn Search and Click Search Result automation.
 */
public class SearchTest extends BaseTest {

    private final String VALID_EMAIL = "adeeshadilmira2@gmail.com";
    private final String VALID_PASSWORD = "Adeesha@2003";

    private WebElement findEmailInput() {
        return wait.until(driver -> {
            List<WebElement> inputs = driver.findElements(By.tagName("input"));
            for (WebElement input : inputs) {
                String id = input.getAttribute("id");
                String name = input.getAttribute("name");
                String type = input.getAttribute("type");
                if ("username".equals(id) || "session_key".equals(id) || "session_key".equals(name) || "email".equals(type) || "text".equals(type)) {
                    if (input.isDisplayed()) return input;
                }
            }
            return null;
        });
    }

    private WebElement findPasswordInput() {
        return wait.until(driver -> {
            List<WebElement> inputs = driver.findElements(By.tagName("input"));
            for (WebElement input : inputs) {
                String type = input.getAttribute("type");
                String id = input.getAttribute("id");
                if ("password".equals(type) || "password".equals(id) || "session_password".equals(id)) {
                    if (input.isDisplayed()) return input;
                }
            }
            return null;
        });
    }

    private void enterValue(WebElement input, String value) {
        input.click();
        input.clear();
        input.sendKeys(value);
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].value = arguments[1];" +
            "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
            "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
            input, value
        );
    }

    private void loginToAccount() {
        driver.get("https://www.linkedin.com/login");

        WebElement emailInput = findEmailInput();
        enterValue(emailInput, VALID_EMAIL);

        WebElement passwordInput = findPasswordInput();
        enterValue(passwordInput, VALID_PASSWORD);

        try {
            passwordInput.sendKeys(Keys.ENTER);
        } catch (Exception e) {
            WebElement btn = driver.findElement(By.xpath("//button[@type='submit' or contains(text(),'Sign in')]"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        }

        if (driver.getCurrentUrl().contains("checkpoint") || driver.getCurrentUrl().contains("challenge")) {
            System.out.println("Security Checkpoint detected! Please solve it manually in the browser...");
            new WebDriverWait(driver, Duration.ofSeconds(90))
                .until(ExpectedConditions.not(ExpectedConditions.urlContains("checkpoint")));
        }

        wait.until(ExpectedConditions.or(
            ExpectedConditions.urlContains("/feed"),
            ExpectedConditions.presenceOfElementLocated(By.xpath("//input[contains(@class,'search-global-typeahead__input') or contains(@placeholder,'Search')]")),
            ExpectedConditions.presenceOfElementLocated(By.id("global-nav"))
        ));
    }

    /**
     * Task 3: Automate Search
     */
    @Test(priority = 1)
    public void testSearchKeyword() {
        loginToAccount();

        driver.get("https://www.linkedin.com/search/results/all/?keywords=Software%20Testing");

        wait.until(ExpectedConditions.urlContains("/search/results/"));
        Assert.assertTrue(driver.getCurrentUrl().contains("/search/results/"), "Search URL mismatch!");

        WebElement resultsContainer = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//main | //div[contains(@class,'search-results-container')] | //ul[contains(@class,'reusable-search')] | //div[contains(@class,'entity-result')]")
        ));
        Assert.assertTrue(resultsContainer.isDisplayed(), "Search results container not visible!");
    }

    /**
     * Task 4: Click a Search Result
     */
    @Test(priority = 2)
    public void testClickFirstSearchResult() {
        loginToAccount();

        driver.get("https://www.linkedin.com/search/results/all/?keywords=Software%20Testing");
        wait.until(ExpectedConditions.urlContains("/search/results/"));

        WebElement firstResult = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath(
                "//div[contains(@class,'search-results-container')]//a[contains(@href,'/')] | " +
                "//li[contains(@class,'reusable-search__result-container')]//a | " +
                "//div[contains(@class,'entity-result')]//a | " +
                "//main//a[contains(@href,'/company/') or contains(@href,'/in/') or contains(@href,'/feed/')] | " +
                "(//main//a)[1]"
            )
        ));

        String targetUrl = firstResult.getAttribute("href");
        if (targetUrl != null && !targetUrl.isEmpty() && !targetUrl.startsWith("#") && !targetUrl.contains("search/results")) {
            driver.get(targetUrl);
        } else {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true); arguments[0].click();", firstResult);
        }

        boolean isNavigated = wait.until(driverInstance -> {
            String url = driverInstance.getCurrentUrl();
            return !url.contains("/search/results/") && driverInstance.getTitle().length() > 0;
        });

        Assert.assertTrue(isNavigated, "Destination page did not load after clicking search result!");
    }
}