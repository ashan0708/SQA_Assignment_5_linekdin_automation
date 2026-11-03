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

public class LoginTest extends BaseTest {

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

    private void submitForm(WebElement passwordField) {
        try {
            passwordField.sendKeys(Keys.ENTER);
        } catch (Exception e) {
            WebElement btn = driver.findElement(By.xpath("//button[@type='submit' or contains(text(),'Sign in')]"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        }
    }

    @Test(priority = 1)
    public void testValidLogin() {
        driver.get("https://www.linkedin.com/login");

        WebElement emailInput = findEmailInput();
        enterValue(emailInput, VALID_EMAIL);

        WebElement passwordInput = findPasswordInput();
        enterValue(passwordInput, VALID_PASSWORD);

        submitForm(passwordInput);

        if (driver.getCurrentUrl().contains("checkpoint") || driver.getCurrentUrl().contains("challenge")) {
            System.out.println("Security Checkpoint detected! Please solve it manually in browser...");
            new WebDriverWait(driver, Duration.ofSeconds(90))
                .until(ExpectedConditions.not(ExpectedConditions.urlContains("checkpoint")));
        }

        boolean isFeedLoaded = wait.until(ExpectedConditions.or(
            ExpectedConditions.urlContains("/feed"),
            ExpectedConditions.presenceOfElementLocated(By.xpath("//input[contains(@class,'search-global-typeahead__input')]")),
            ExpectedConditions.presenceOfElementLocated(By.id("global-nav"))
        ));

        Assert.assertTrue(isFeedLoaded, "Verification failed: Feed page did not load!");
    }

    @Test(priority = 2)
    public void testInvalidPasswordLogin() {
        driver.get("https://www.linkedin.com/login");

        WebElement emailInput = findEmailInput();
        enterValue(emailInput, VALID_EMAIL);

        WebElement passwordInput = findPasswordInput();
        enterValue(passwordInput, "WrongInvalidPassword_999#");

        submitForm(passwordInput);

        // Verification: Assert error message appears OR user stays on login page with error state
        boolean isErrorDisplayed = wait.until(ExpectedConditions.or(
            ExpectedConditions.presenceOfElementLocated(By.id("error-for-password")),
            ExpectedConditions.presenceOfElementLocated(By.id("error-for-username")),
            ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@role='alert' or contains(@class,'alert') or contains(@class,'error')]")),
            ExpectedConditions.presenceOfElementLocated(By.xpath("//p[contains(@class,'form__label--error')]"))
        ));

        Assert.assertTrue(isErrorDisplayed, "Negative test failed: Error message was not displayed!");
    }
}