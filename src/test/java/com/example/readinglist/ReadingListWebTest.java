package com.example.readinglist;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= WebEnvironment.RANDOM_PORT)
public class ReadingListWebTest {

    private static ChromeDriver browser;

    @LocalServerPort
    private int port;

    @BeforeClass
    public static void openBrowser() {
        browser = new ChromeDriver();
        browser.manage().timeouts()
                .implicitlyWait(10, TimeUnit.SECONDS);
    }

    @AfterClass
    public static void closeBrowser() {
        browser.quit();
    }

    @Test
    public void addBookToEmptyList() {
        String baseUrl = "http://localhost:" + port;

        browser.get(baseUrl);
        browser.findElementByName("username").sendKeys("craig");
        browser.findElementByName("password").sendKeys("password");
        browser.findElementByTagName("form").submit();

        assertEquals("Add a book", browser.findElementByTagName("h3").getText());

        browser.findElementByName("title").sendKeys("BOOK TITLE");
        browser.findElementByName("author").sendKeys("BOOK AUTHOR");
        browser.findElementByName("isbn").sendKeys("1234567890");
        browser.findElementByName("description").sendKeys("DESCRIPTION");
        browser.findElementByClassName("addBook").submit();

        WebElement dl = browser.findElementByCssSelector("dt.bookHeadline");
        assertEquals("BOOK TITLE by BOOK AUTHOR (ISBN: 1234567890)", dl.getText());

        WebElement dt = browser.findElementByCssSelector("dd.bookDescription");
        assertEquals("DESCRIPTION", dt.getText());
    }
}
