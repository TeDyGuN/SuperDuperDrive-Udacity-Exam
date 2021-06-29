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
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class CredentialTests {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private WebDriverWait webDriverWait;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() throws InterruptedException {

        this.driver = new ChromeDriver();
        this.webDriverWait = new WebDriverWait (driver, 1000);

        this.signupUser();
        this.loginUser();
        this.createCredential();
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    //Write a Selenium test that logs in an existing user, creates a credential and verifies that the credential details are visible in the credential list.
    @Test
    @Order(1)
    public void createCredentialTest() {

        Assertions.assertDoesNotThrow(() -> {
            this.driver.findElement(By.xpath("//th[text()='facebook.com']"));
            this.driver.findElement(By.xpath("//td[text()='admin']"));
        });

    }

    //Write a Selenium test that logs in an existing user with existing credentials, clicks the edit credential button on an existing credential, changes the credential data, saves the changes, and verifies that the changes appear in the credential list.
    @Test
    @Order(2)
    public void updateCredentialTest() {

        Assertions.assertDoesNotThrow(() -> {
            this.driver.findElement(By.xpath("//th[text()='facebook.com']"));
            this.driver.findElement(By.xpath("//td[text()='admin']"));
        });

        WebElement editButton = this.driver.findElement(
                By.xpath("//*[@id='credentialTable']/tbody/tr/td[1]/button"));

        this.webDriverWait.until(ExpectedConditions.elementToBeClickable(editButton));

        editButton.click();

        WebElement urlField = this.driver.findElement(By.id("url"));

        this.webDriverWait.until(ExpectedConditions.visibilityOf(urlField));

        urlField.clear();
        urlField.sendKeys("google.com.bo");

        WebElement usernameField = this.driver.findElement(By.id("username"));
        this.webDriverWait.until(ExpectedConditions.visibilityOf(usernameField));
        usernameField.clear();
        usernameField.sendKeys("usertesting");

        WebElement passwordField = this.driver.findElement(By.id("password"));
        this.webDriverWait.until(ExpectedConditions.visibilityOf(passwordField));
        Assertions.assertEquals("12345", passwordField.getAttribute("value"));

        passwordField.clear();
        passwordField.sendKeys("admin");

        WebElement credentialForm = this.driver.findElement(By.id("credentialCreateButton"));

        credentialForm.click();

        this.webDriverWait.until(ExpectedConditions.titleContains("Home"));

        Assertions.assertEquals("Home", driver.getTitle());

        WebElement noteCreationMessage = driver.findElement(By.id("success-msg"));

        Assertions.assertEquals("Credential updated correctly!", noteCreationMessage.getText());

        WebElement notesTab = this.driver.findElement(By.id("nav-credentials-tab"));

        notesTab.click();

        Assertions.assertDoesNotThrow(() -> {
            this.driver.findElement(By.xpath("//th[text()='google.com.bo']"));
            this.driver.findElement(By.xpath("//td[text()='usertesting']"));
        });
    }

//   //Write a Selenium test that logs in an existing user with existing credentials, clicks the delete credential button on an existing credential, and verifies that the credential no longer appears in the credential list.
    @Test
    @Order(3)
    public void DeleteCredential() throws InterruptedException {

        Assertions.assertDoesNotThrow(() -> {
            this.driver.findElement(By.xpath("//th[text()='facebook.com']"));
            this.driver.findElement(By.xpath("//td[text()='admin']"));
        });

        WebElement deleteButton = this.driver.findElement(
                By.xpath("//*[@id=\"credentialTable\"]/tbody/tr/td[1]/a"));

        this.webDriverWait.until(ExpectedConditions.elementToBeClickable(deleteButton));

        deleteButton.click();

        this.webDriverWait.until(ExpectedConditions.titleContains("Home"));

        Assertions.assertEquals("Home", driver.getTitle());

        WebElement noteCreationMessage = driver.findElement(By.id("success-msg"));

        Assertions.assertEquals("Credential deleted correctly!", noteCreationMessage.getText());

        WebElement credentialTab = this.driver.findElement(By.id("nav-credentials-tab"));

        credentialTab.click();

        Assertions.assertThrows(NoSuchElementException.class, () -> {
            this.driver.findElement(By.xpath("//th[text()='facebook.com']"));
            this.driver.findElement(By.xpath("//td[text()='admin']"));
        });

    }

    private void backToHomeFromResultPage() throws InterruptedException {

        this.webDriverWait.until(ExpectedConditions.titleContains("Home"));

        Assertions.assertEquals("Home", driver.getTitle());

        WebElement credentialCreationMessage = driver.findElement(By.id("success-msg"));

        Assertions.assertEquals("Credential created correctly!", credentialCreationMessage.getText());

        WebElement credentialTab = this.driver.findElement(By.id("nav-credentials-tab"));

        credentialTab.click();
    }

    private void createCredential() throws InterruptedException {

        this.driver.get("http://localhost:" + this.port + "/home");

        WebElement credentialTab = this.driver.findElement(By.id("nav-credentials-tab"));

        credentialTab.click();

        this.webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("createCredentialButton")));

        WebElement credentialCreationBtn = driver.findElement(By.id("createCredentialButton"));

        Assertions.assertNotNull(credentialCreationBtn);

        credentialCreationBtn.click();

        WebElement credentialURLField = this.driver.findElement(By.id("url"));

        this.webDriverWait.until(ExpectedConditions.visibilityOf(credentialURLField));

        credentialURLField.sendKeys("facebook.com");

        WebElement credentialUsernameField = this.driver.findElement(By.id("username"));

        this.webDriverWait.until(ExpectedConditions.visibilityOf(credentialUsernameField));

        credentialUsernameField.sendKeys("admin");

        WebElement credentialPasswordField = this.driver.findElement(By.id("password"));

        this.webDriverWait.until(ExpectedConditions.visibilityOf(credentialPasswordField));

        credentialPasswordField.sendKeys("12345");

        WebElement credentialForm = this.driver.findElement(By.id("credentialCreateButton"));

        credentialForm.click();

        this.backToHomeFromResultPage();
    }

    private void loginUser() {

        driver.get("http://localhost:" + this.port + "/login");

        WebElement usernameField = driver.findElement(By.id("inputUsername"));

        usernameField.sendKeys("mark");

        WebElement passwordField = driver.findElement(By.id("inputPassword"));

        passwordField.sendKeys("zuckerberg");

        WebElement submitButton = driver.findElement(By.id("loginSubmit"));

        Assertions.assertEquals("Login", submitButton.getText());

        submitButton.click();
    }

    private void signupUser() {

        this.driver.get("http://localhost:" + this.port + "/signup");

        WebElement firstNameField = this.driver.findElement(By.id("inputFirstName"));

        firstNameField.sendKeys("Mark");

        WebElement lastNameField = this.driver.findElement(By.id("inputLastName"));

        lastNameField.sendKeys("zuckerberg");

        WebElement usernameField = driver.findElement(By.id("inputUsername"));

        usernameField.sendKeys("mark");

        WebElement passwordField = driver.findElement(By.id("inputPassword"));

        passwordField.sendKeys("zuckerberg");

        WebElement submitButton = driver.findElement(By.id("signupSubmit"));

        submitButton.click();
    }
}
