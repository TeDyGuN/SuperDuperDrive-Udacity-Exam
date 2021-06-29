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
public class NoteTests {

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
        this.createNote();
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    //Write a Selenium test that logs in an existing user, creates a note and verifies that the note details are visible in the note list.
    @Test
    @Order(1)
    public void createNoteTest() {

        Assertions.assertDoesNotThrow(() -> {
            this.driver.findElement(By.xpath("//th[text()='Testing']"));
            this.driver.findElement(By.xpath("//td[text()='Testing my App']"));
        });

    }

    //Write a Selenium test that logs in an existing user with existing notes, clicks the edit note button on an existing note, changes the note data, saves the changes, and verifies that the changes appear in the note list.
    @Test
    @Order(2)
    public void updateNote() {

        Assertions.assertDoesNotThrow(() -> {
            this.driver.findElement(By.xpath("//th[text()='Testing']"));
            this.driver.findElement(By.xpath("//td[text()='Testing my App']"));
        });

        WebElement editButton = this.driver.findElement(
                By.xpath("//*[@id='userTable']/tbody/tr/td[1]/button"));

        this.webDriverWait.until(ExpectedConditions.elementToBeClickable(editButton));

        editButton.click();

        WebElement noteTitleField = this.driver.findElement(By.id("noteTitle"));

        this.webDriverWait.until(ExpectedConditions.visibilityOf(noteTitleField));

        noteTitleField.clear();
        noteTitleField.sendKeys("Editing my note");

        WebElement noteDescriptionField = this.driver.findElement(By.id("noteDescription"));

        noteDescriptionField.clear();
        noteDescriptionField.sendKeys("Editing my description note");

        WebElement noteForm = this.driver.findElement(By.id("noteSubmitButton"));

        noteForm.click();

        this.webDriverWait.until(ExpectedConditions.titleContains("Home"));

        Assertions.assertEquals("Home", driver.getTitle());

        WebElement noteCreationMessage = driver.findElement(By.id("success-msg"));

        Assertions.assertEquals("Note updated correctly!", noteCreationMessage.getText());

        WebElement notesTab = this.driver.findElement(By.id("nav-notes-tab"));

        notesTab.click();

        Assertions.assertDoesNotThrow(() -> {
            this.driver.findElement(By.xpath("//th[text()='Editing my note']"));
            this.driver.findElement(By.xpath("//td[text()='Editing my description note']"));
        });
    }

    //Write a Selenium test that logs in an existing user with existing notes, clicks the delete note button on an existing note, and verifies that the note no longer appears in the note list.
    @Test
    @Order(3)
    public void DeleteNote() throws InterruptedException {

        Assertions.assertDoesNotThrow(() -> {
            this.driver.findElement(By.xpath("//th[text()='Testing']"));
            this.driver.findElement(By.xpath("//td[text()='Testing my App']"));
        });

        WebElement deleteButton = this.driver.findElement(
                By.xpath("//*[@id=\"userTable\"]/tbody/tr/td[1]/a"));

        this.webDriverWait.until(ExpectedConditions.elementToBeClickable(deleteButton));

        deleteButton.click();

        this.webDriverWait.until(ExpectedConditions.titleContains("Home"));

        Assertions.assertEquals("Home", driver.getTitle());

        WebElement noteCreationMessage = driver.findElement(By.id("success-msg"));

        Assertions.assertEquals("Note deleted correctly!", noteCreationMessage.getText());

        WebElement notesTab = this.driver.findElement(By.id("nav-notes-tab"));

        notesTab.click();

        Assertions.assertThrows(NoSuchElementException.class, () -> {
            this.driver.findElement(By.xpath("//th[text()='Testing']"));
            this.driver.findElement(By.xpath("//td[text()='Testing my App']"));
        });

    }

    private void backToHomeFromResultPage() throws InterruptedException {

        this.webDriverWait.until(ExpectedConditions.titleContains("Home"));

        Assertions.assertEquals("Home", driver.getTitle());

        WebElement noteCreationMessage = driver.findElement(By.id("success-msg"));

        Assertions.assertEquals("Note created correctly!", noteCreationMessage.getText());

        WebElement notesTab = this.driver.findElement(By.id("nav-notes-tab"));

        notesTab.click();
    }

    private void createNote() throws InterruptedException {

        this.driver.get("http://localhost:" + this.port + "/home");

        WebElement notesTab = this.driver.findElement(By.id("nav-notes-tab"));

        notesTab.click();

        this.webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("createNoteButton")));

        WebElement noteCreationBtn = driver.findElement(By.id("createNoteButton"));

        Assertions.assertNotNull(noteCreationBtn);

        noteCreationBtn.click();

        WebElement noteTitleField = this.driver.findElement(By.id("noteTitle"));

        this.webDriverWait.until(ExpectedConditions.visibilityOf(noteTitleField));

        noteTitleField.sendKeys("Testing");

        WebElement noteDescriptionField = this.driver.findElement(By.id("noteDescription"));

        noteDescriptionField.sendKeys("Testing my App");

        WebElement noteForm = this.driver.findElement(By.id("noteSubmitButton"));

        noteForm.click();

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
