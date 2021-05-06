package Twitter_PageObjects;

import ReusableLibrary.AbstractClass;
import ReusableLibrary.ReusableActions_PageObjects;
import com.relevantcodes.extentreports.ExtentTest;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class Login_Page extends AbstractClass {

    ExtentTest logger1;
    public Login_Page(WebDriver driver){
        PageFactory.initElements(driver, this);
        this.logger1 = AbstractClass.logger;
    }

    @FindBy(xpath = "//a[@href='/login']")
    WebElement LoginButton;
    @FindBy(xpath = "//input[@name='session[username_or_email]']")
    WebElement usernameField;
    @FindBy(xpath = "//input[@name='session[password]']")
    WebElement passwordField;
    @FindBy(xpath = "//div[@data-testid='LoginForm_Login_Button']")
    WebElement LoginFinal;

    public void setLoginButton(){
        ReusableActions_PageObjects.clickOnElement(driver,LoginButton,logger,"Login Button");
    }
    public void setUsernameField(String userValue){
        ReusableActions_PageObjects.sendKeysMethod(driver,usernameField,userValue,logger,"Username Field");
    }
    public void setPasswordField(String userValue){
        ReusableActions_PageObjects.sendKeysMethod(driver,passwordField,userValue,logger,"Password Field");
    }
    public void setLoginFinal(){
        ReusableActions_PageObjects.clickOnElement(driver,LoginFinal,logger,"Login Button Final");
    }
}