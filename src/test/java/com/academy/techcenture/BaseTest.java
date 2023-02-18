package com.academy.techcenture;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.asserts.SoftAssert;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

public class BaseTest {

    protected WebDriver driver;
    protected ExtentReports reports;
    protected ExtentTest extentTest;


    @BeforeTest
    public void beforeTest(){
        reports = new ExtentReports(  System.getProperty("user.dir") + "/test-output/ExtentReports.html", true);
        reports.addSystemInfo( "OS NAME" , System.getProperty("os.name"));
        reports.addSystemInfo("ENGINEER", System.getProperty("user.name"));
        reports.addSystemInfo("ENVIRONMENT", "QA");
        reports.addSystemInfo("JAVA VERSION", System.getProperty("java.version"));
    }

    @AfterTest
    public void afterTest(){
        reports.flush();
        reports.close();
    }

    private String browserName;
    private String browserVersion;

    @BeforeMethod
    public void setUpBefore(){
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        Capabilities browserCap = ((RemoteWebDriver) driver).getCapabilities();
        browserName = browserCap.getBrowserName();
        browserVersion = browserCap.getBrowserVersion();

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(15));
        driver.manage().window().maximize();

        reports.addSystemInfo("BROWSER NAME", browserName);
        reports.addSystemInfo("BROWSER VERSION", browserVersion);


    }

    @AfterMethod
    public void tearDown(ITestResult result) throws IOException {

        if(result.getStatus()==ITestResult.FAILURE){
            extentTest.log(LogStatus.FAIL, "TEST CASE FAILED IS "+result.getName());
            extentTest.log(LogStatus.FAIL, "TEST CASE FAILED IS "+result.getThrowable());

            String screenshotPath = getScreenshot(driver, result.getName());
            extentTest.log(LogStatus.FAIL, extentTest.addScreenCapture(screenshotPath));

        }
        else if(result.getStatus()==ITestResult.SKIP){
            extentTest.log(LogStatus.SKIP, "Test Case SKIPPED IS " + result.getName());
        }
        else if(result.getStatus()==ITestResult.SUCCESS){
            extentTest.log(LogStatus.PASS, "Test Case PASSED IS " + result.getName());

        }

        reports.endTest(extentTest);

        if (driver != null){
            driver.quit();
        }
        }
    public static String getScreenshot(WebDriver driver, String screenshotName) throws IOException {
        String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        String destination = System.getProperty("user.dir") + "/test-output/" + screenshotName + dateName
                + ".png";
        File finalDestination = new File(destination);
        FileUtils.copyFile(source, finalDestination);
        return destination;
    }


    }

