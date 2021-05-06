package Twitter_PageObjects;

import ReusableLibrary.AbstractClass;

public class Base_Class extends AbstractClass {

    public static Login_Page TwitterLoginPage(){
        Login_Page TwitterLoginPage = new Login_Page(driver);
        return TwitterLoginPage;
    }

    public static Home_Page TwitterHomePage(){
        Home_Page TwitterHomePage = new Home_Page(driver);
        return TwitterHomePage;
    }
}
