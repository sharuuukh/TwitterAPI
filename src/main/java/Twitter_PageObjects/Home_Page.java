package Twitter_PageObjects;

import ReusableLibrary.AbstractClass;
import ReusableLibrary.ReusableActions_PageObjects;
import com.relevantcodes.extentreports.ExtentTest;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class Home_Page extends AbstractClass {

    ExtentTest logger1;
    public Home_Page(WebDriver driver){
        PageFactory.initElements(driver, this);
        this.logger1 = AbstractClass.logger;
    }

    @FindBy(xpath = "//a[@href='/compose/tweet']")
    WebElement ComposeTweetButton;
    @FindBy(xpath = "//div[@aria-label='Tweet text']")
    WebElement TweetTextArea;
    @FindBy(xpath = "//div[@data-testid='tweetButton']")
    WebElement TweetButton;
    @FindBy(xpath = "//a[@aria-label='Profile']")
    WebElement SeeProfile;
    @FindBy(xpath = "//div[@aria-label='Account menu']")
    WebElement AccountMenu;
    @FindBy(xpath = "//a[@href='/logout']")
    WebElement Logout;

    public void setComposeTweetButton(){
        ReusableActions_PageObjects.clickOnElement(driver,ComposeTweetButton,logger,"Compose Tweet Button");
    }
    public void setTweetTextArea(String userValue){
        ReusableActions_PageObjects.sendKeysMethod(driver, TweetTextArea, userValue, logger, "Tweet Text Area");
    }
    public void setTweetButton(){
        ReusableActions_PageObjects.clickOnElement(driver,TweetButton,logger,"Tweet Button");
    }
    public void setSeeProfile(){
        ReusableActions_PageObjects.clickOnElement(driver, SeeProfile, logger, "See Profile");
    }
    public void setAccountMenu(){
        ReusableActions_PageObjects.clickOnElement(driver, AccountMenu, logger, "Account Menu");
    }
    public void setLogout(){
        ReusableActions_PageObjects.clickOnElement(driver, Logout, logger, "Log Out");
    }
}
