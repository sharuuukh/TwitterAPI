import ReusableLibrary.AbstractClass;
import ReusableLibrary.ReusableActions_PageObjects;
import Twitter_PageObjects.Base_Class;
import Twitter_PageObjects.Login_Page;
import com.relevantcodes.extentreports.LogStatus;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class TwitterUseCase1 extends AbstractClass {

    String consumerKey = "AMkBNO0BPUcucKZeH9Fsox96p";
    String consumerSecret = "mOdWFixy6rsnQWgnvRrDIqb97Xfv5LHHr54QQzeYzyHqPIhuxN";
    String accessToken = "2615120031-FteEghS1Ldk7DJcESahvyySLkK6cWa24uCdDiwY";
    String tokenSecret = "FuRPf7xax8C5mwBtkkluOm9jhfrlbT4nRKXy4QBBnecl9";
    String TweetID;

    @BeforeClass
    public void setup (){
        RestAssured.baseURI = "https://api.twitter.com/1.1/statuses/";
    }

    @Test
    public void Case1 () throws InterruptedException {

        //Log in to Twitter and create a Tweet
        driver.navigate().to("https://www.twitter.com");
        Base_Class.TwitterLoginPage().setLoginButton();
        Base_Class.TwitterLoginPage().setUsernameField("Username");
        Base_Class.TwitterLoginPage().setPasswordField("Password");
        Base_Class.TwitterLoginPage().setLoginFinal();
        Thread.sleep(4000);
        Base_Class.TwitterHomePage().setComposeTweetButton();
        Base_Class.TwitterHomePage().setTweetTextArea("This tweet was generated automatically for testing purposes.");
        Base_Class.TwitterHomePage().setTweetButton();
        Thread.sleep(2000);
        Base_Class.TwitterHomePage().setSeeProfile();
        Thread.sleep(2000);

        //Verify most recent Tweet has been created
            Response Resp =
                    given()
                            .auth().oauth(consumerKey, consumerSecret, accessToken, tokenSecret)
                            .queryParam("screen_name", "@sharuuukh")
                            .when()
                            .get("user_timeline.json")
                            .then()
                            .extract()
                            .response();

        if (Resp.statusCode() == 200){
            System.out.println("Status code is 200 and successful.");
            logger.log(LogStatus.PASS, "Status code is 200 and successful.");
        } else {
            System.out.println("Status code is not successful: " + Resp.statusCode());
            logger.log(LogStatus.FAIL, "Status code is not successful: " + Resp.statusCode());
        }

        String getTweet = Resp.asString();
        JsonPath js = new JsonPath(getTweet);
        String text1 = js.get("text[0]").toString();

        if (text1.equals("This tweet was generated automatically for testing purposes.")) {
            System.out.println("The recent tweet is: " + text1);
            logger.log(LogStatus.PASS, "The recent tweet is: " + text1);
        } else {
            System.out.println("Recent tweet does not match tweet from .json body.");
            logger.log(LogStatus.INFO, "Recent tweet does not match tweet from .json body.");
        }

        //Capture the recent Tweet's ID
        TweetID = (js.get("id[0]")).toString();
        System.out.println("ID of most recent tweet is: "+ TweetID);
        logger.log(LogStatus.INFO, "ID of most recent tweet is: "+ TweetID);

        //Delete the recent Tweet
                given()
                        .auth().oauth(consumerKey, consumerSecret, accessToken, tokenSecret)
                        .queryParam("id", TweetID)
                        .when()
                        .post("destroy.json")
                        .then()
                        .extract()
                        .response();

        System.out.println("Recent tweet with ID: " + TweetID + " has been deleted.");
        logger.log(LogStatus.INFO, "Recent tweet with ID: " + TweetID + " has been deleted.");

        //Verify Tweet has been deleted and sign off
        Response Resp2 = given()
                .auth().oauth(consumerKey, consumerSecret, accessToken, tokenSecret)
                .queryParam("screen_name", "@sharuuukh")
                .when()
                .get("user_timeline.json")
                .then()
                .extract()
                .response();

        String getTweet2 = Resp2.asString();
        JsonPath js2 = new JsonPath(getTweet2);
        String text2 = js2.get("text[0]").toString();

        if (text2.equals("This tweet was generated automatically for testing purposes.")){
            System.out.println("Tweet has not been successfully deleted.");
            logger.log(LogStatus.FAIL, "Tweet has not been successfully deleted.");
        } else {
            System.out.println("Tweet has been successfully deleted from timeline.");
            logger.log(LogStatus.PASS, "Tweet has been successfully deleted from timeline.");
        }

        driver.navigate().refresh();
        Thread.sleep(2000);
        Base_Class.TwitterHomePage().setAccountMenu();
        Base_Class.TwitterHomePage().setLogout();
    }

}
