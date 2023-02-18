package com.academy.techcenture;
import com.relevantcodes.extentreports.LogStatus;
import org.openqa.selenium.*;
import org.testng.Assert;
import org.testng.annotations.*;

public class GoogleTest extends BaseTest {

    @Test
    public void googleSearchTest() {
        extentTest = reports.startTest("Google Search Engine Positive Test");

        driver.get("https://google.com");
        extentTest.log(LogStatus.INFO, "Navigate to Google.com");

        String actualTitle = driver.getTitle();
       Assert.assertEquals(actualTitle, "Google");
        extentTest.log(LogStatus.PASS, "google titles matched");

        WebElement googleLogo = driver.findElement(By.xpath("//img[@alt='Google']"));
        Assert.assertTrue(!googleLogo.isDisplayed()); //специально добавили <!>, чтобы получить скриншот ошибки
        extentTest.log(LogStatus.PASS, "Google Logo is displayed");

        String searchKey = "Mechanical Keyboard";
        driver.findElement(By.name("q")).sendKeys(searchKey + Keys.ENTER);
        extentTest.log(LogStatus.INFO, "Entered " + searchKey + " in search input box and pressed Enter");

        WebElement resultStat = driver.findElement(By.id("result-stats"));
        Assert.assertTrue(resultStat.isDisplayed());
        extentTest.log(LogStatus.PASS, "Result stats were displayed");
        extentTest.log(LogStatus.INFO, "Test Scenario ended");
    }



}
