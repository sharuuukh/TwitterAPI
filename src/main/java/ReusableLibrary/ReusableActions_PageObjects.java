package ReusableLibrary;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;

public class ReusableActions_PageObjects {

    //create a reusable method to navigate to a page
    public static WebDriver defineTheDriver() {

        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("incognito");
        WebDriver driver = new ChromeDriver(options);

        return driver;
    } //end of navigate method

    public static void uploadFile(String fileLocation, ExtentTest logger) {
        try {
            //Setting clipboard with file location
            StringSelection stringSelection = new StringSelection(fileLocation);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
            //native key strokes for CTRL, V and ENTER keys
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            logger.log(LogStatus.PASS, "Successfully uploaded file!");
        } catch (Exception exp) {
            exp.printStackTrace();
            logger.log(LogStatus.FAIL, "Failed to upload file.");
        }
    }//end of uploadFile method

    public static void verifyTitle(WebDriver driver, String userValue, ExtentTest logger){
        String actualTitle = driver.getTitle();
        if (actualTitle.equals(userValue)) {
            //System.out.println("Actual title is: " + actualTitle);
            logger.log(LogStatus.PASS, "Successfully verified that title matches expected: " + actualTitle);
        } else {
            //System.out.println("Failed! Actual title is: " + actualTitle);
            logger.log(LogStatus.FAIL, "Failed! Title did not match expected. Actual title is: " + actualTitle);
        }
    }


    public static void sendKeysMethod(WebDriver driver, WebElement xpathLocator, String userValue, ExtentTest logger, String elementName){

        //define by explicit wait
        WebDriverWait wait = new WebDriverWait(driver,10);

        //use try catch to locate an element and click on it
        try{
            WebElement element = wait.until(ExpectedConditions.visibilityOf(xpathLocator));
            element.clear();
            element.sendKeys(userValue);
            logger.log(LogStatus.PASS,"Successfully entered a value on " + elementName);
        } catch (Exception e) {
            logger.log(LogStatus.FAIL,"Unable to enter value on element " + elementName + " " + e);
            getScreenShot(driver, elementName, logger);
            System.out.println("Unable to enter value on element " + elementName + " " + e);
        }
    }


    public static void submitElement(WebDriver driver, WebElement xpathLocator, ExtentTest logger, String elementName){

        //define by explicit wait
        WebDriverWait wait = new WebDriverWait(driver, 10);
        logger.log(LogStatus.PASS,"Successfully submit on " + elementName);
        //use try and catch to locate an element and submit it
        try {
            wait.until(ExpectedConditions.visibilityOf(xpathLocator)).submit();
        } catch (Exception e){
            logger.log(LogStatus.FAIL,"Unable to submit on element " + elementName + " " + e);
            getScreenShot(driver, elementName, logger);
            System.out.println("Unable to submit element " + elementName + " " + e);
        }
    }


    public static void clickOnElement(WebDriver driver, WebElement xpathLocator, ExtentTest logger,  String elementName) {

        //define by explicit wait
        WebDriverWait wait = new WebDriverWait(driver, 10);

        //use try and catch to locate an element and submit it
        try {
            wait.until(ExpectedConditions.visibilityOf(xpathLocator)).click();
            logger.log(LogStatus.PASS, "Successfully clicked on element " + elementName);
        } catch (Exception e) {
            logger.log(LogStatus.FAIL, "Unable to click on element " + elementName + " " + e);
            getScreenShot(driver, elementName, logger);
            System.out.println("Unable to click element " + elementName + " " + e);
        }
    }

    public static void popUpMessage(WebDriver driver, WebElement xpathLocator, ExtentTest logger, String elementName) {
        WebDriverWait wait = new WebDriverWait(driver, 10);

        try {
            wait.until(ExpectedConditions.visibilityOf(xpathLocator)).click();
            logger.log(LogStatus.PASS, "Successfully closed pop-up.");
        } catch (Exception e) {
            logger.log(LogStatus.INFO, "Pop-up not available on this iteration. Proceeding to next step.");
        }

    }


    public static void dropdownByText(WebDriver driver, WebElement xpath, String userValue, ExtentTest logger, String elementName){

        WebDriverWait wait = new WebDriverWait(driver, 10);
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOf(xpath));
            Select dropDown = new Select(element);
            dropDown.selectByVisibleText(userValue);
            logger.log(LogStatus.PASS, "Successfully selected item from drop down " + elementName);
        } catch (Exception e) {
            logger.log(LogStatus.FAIL, "Unable to select dropdown " + elementName + " " + e);
            getScreenShot(driver, elementName, logger);
            System.out.println("Unable to select dropdown " + elementName + " " + e);
        }
    }

    public static void mouseHover(WebDriver driver, WebElement xpath, ExtentTest logger, String elementName) {

        Actions mouseAction = new Actions(driver);

        WebDriverWait wait = new WebDriverWait(driver, 10);
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOf(xpath));
            mouseAction.moveToElement(element).perform();
            logger.log(LogStatus.PASS, "Successfully hovered on element " + elementName);
        } catch (Exception e) {
            logger.log(LogStatus.FAIL, "Unable to hover on element " + elementName + " " +e);
            getScreenShot(driver, elementName, logger);
            System.out.println("Unable to locate 'Men's tab' element." + e);
        }

    }


    public static String captureText(WebDriver driver, WebElement xpath, int index, ExtentTest logger, String elementName) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        String result = "";
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfAllElements(xpath)).get(index);
            result = element.getText();
            logger.log(LogStatus.PASS,"Successfully captured a text on " + elementName + ". Result is: " + result);
        } catch (Exception e) {
            logger.log(LogStatus.FAIL,"Unable to select a value from " + elementName + " " + e);
            getScreenShot(driver, elementName, logger);
            System.out.println("Unable to select a value from " + elementName + " " + e);
        }
        return result;

    }

    public static void getScreenShot(WebDriver driver, String imageName, ExtentTest logger) {
        try {
            String fileName = imageName + ".png";
            String directory = "src/main/java/HTML_Reports/Screenshots/";
            String snPath = "Screenshots/";
            File sourceFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(sourceFile, new File(directory + fileName));
            String image = logger.addScreenCapture(snPath + fileName);
            logger.log(LogStatus.FAIL, "", image);
        } catch (Exception e) {
            logger.log(LogStatus.FAIL, "Error occurred while taking screenshot.");
            e.printStackTrace();
        }
    }


} //end of class
