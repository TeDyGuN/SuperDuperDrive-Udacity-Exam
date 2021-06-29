package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoginAndSignUpTests {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private WebDriverWait webDriverWait;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        this.driver = new ChromeDriver();
        this.webDriverWait = new WebDriverWait (driver, 1000);
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    //Write a Selenium test that verifies that the home page is not accessible without logging in.
    @Test
    @Order(1)
    public void getLoginPage() {
        driver.get("http://localhost:" + this.port + "/home");
        Assertions.assertEquals("Login", driver.getTitle());
    }

    //Write a Selenium test that signs up a new user, logs that user in, verifies that they can access the home page, then logs out and verifies that the home page is no longer accessible.
    @Test
    @Order(2)
    public void signupAndLoginTest() {

        this.signup();

        this.login();

        this.webDriverWait.until(ExpectedConditions.titleContains("Home"));

        Assertions.assertEquals("Home", driver.getTitle());

        WebElement logoutButton = driver.findElement(By.id("logoutBtn"));

        Assertions.assertEquals("Logout", logoutButton.getText());

        logoutButton.click();

        this.webDriverWait.until(ExpectedConditions.titleContains("Login"));

        driver.get("http://localhost:" + this.port + "/home");

        Assertions.assertEquals("Login", driver.getTitle());
    }

    private void login() {

        Assertions.assertEquals("Login", driver.getTitle());

        WebElement usernameField = driver.findElement(By.id("inputUsername"));

        usernameField.sendKeys("mark");

        WebElement passwordField = driver.findElement(By.id("inputPassword"));

        passwordField.sendKeys("zuckerberg");

        WebElement submitButton = driver.findElement(By.id("loginSubmit"));

        submitButton.click();

        Assertions.assertThrows(NoSuchElementException.class, () -> {
            this.driver.findElement(By.id("invalidMessage"));
        });
    }


    private void signup() {

        this.driver.get("http://localhost:" + this.port + "/signup");

        Assertions.assertEquals("Sign Up", driver.getTitle());

        Assertions.assertDoesNotThrow(() -> {

            WebElement firstName = this.driver.findElement(By.id("inputFirstName"));

            firstName.sendKeys("Mark");

            WebElement lastNameField = this.driver.findElement(By.id("inputLastName"));

            lastNameField.sendKeys("Zuckerberg");

            WebElement usernameField = driver.findElement(By.id("inputUsername"));

            usernameField.sendKeys("mark");

            WebElement passwordField = driver.findElement(By.id("inputPassword"));

            passwordField.sendKeys("zuckerberg");

            WebElement submitButton = driver.findElement(By.id("signupSubmit"));

            Assertions.assertEquals("Sign Up", submitButton.getText());

            submitButton.click();
        });

        this.webDriverWait.until(ExpectedConditions.titleContains("Login"));
    }

}
