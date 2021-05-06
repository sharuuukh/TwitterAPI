import ReusableLibrary.AbstractClass;
import Twitter_PageObjects.Base_Class;
import com.relevantcodes.extentreports.LogStatus;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class TwitterUseCase2 extends AbstractClass {

    String consumerKey = "AMkBNO0BPUcucKZeH9Fsox96p";
    String consumerSecret = "mOdWFixy6rsnQWgnvRrDIqb97Xfv5LHHr54QQzeYzyHqPIhuxN";
    String accessToken = "2615120031-FteEghS1Ldk7DJcESahvyySLkK6cWa24uCdDiwY";
    String tokenSecret = "FuRPf7xax8C5mwBtkkluOm9jhfrlbT4nRKXy4QBBnecl9";
    String TweetID;
    String MultipleTweets;

    @BeforeClass
    public void setup (){
        RestAssured.baseURI = "https://api.twitter.com/1.1/statuses/";
    }

    @Test(priority = 1)
    public void Case2_GenerateTweets () throws InterruptedException {

        //Generate 20 tweets via API automation
        for (int i = 0; i < 20; i++) {
            MultipleTweets = "This is auto-generated tweet number: " + (i + 1);
            Response Resp = given()
                    .auth().oauth(consumerKey, consumerSecret, accessToken, tokenSecret)
                    .queryParam("status", MultipleTweets)
                    .when()
                    .post("update.json")
                    .then()
                    .extract()
                    .response();

            if (Resp.statusCode() == 200){
                System.out.println("Status code is 200 and successful. Tweet number: " +i+ " has been generated.");
                logger.log(LogStatus.PASS, "Status code is 200 and successful. Tweet number: " +i+ " has been generated.");
            } else {
                System.out.println("Status code is not successful: " + Resp.statusCode());
                logger.log(LogStatus.FAIL, "Status code is not successful: " + Resp.statusCode());
            }
        }


        //Log in to Twitter and verify if first 2 tweets are present
        driver.navigate().to("https://www.twitter.com");
        Base_Class.TwitterLoginPage().setLoginButton();
        Base_Class.TwitterLoginPage().setUsernameField("sharuuukh");
        Base_Class.TwitterLoginPage().setPasswordField("@Brolly38");
        Base_Class.TwitterLoginPage().setLoginFinal();
        Thread.sleep(2000);
        Base_Class.TwitterHomePage().setSeeProfile();
        Thread.sleep(2000);

        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("scroll(0,2000)");

        Response Resp = given()
                .auth().oauth(consumerKey, consumerSecret, accessToken, tokenSecret)
                .queryParam("screen_name", "@sharuuukh")
                .when()
                .get("user_timeline.json")
                .then()
                .extract()
                .response();

        String getTweet = Resp.asString();
        JsonPath js = new JsonPath(getTweet);
        String Tweet2 = js.get("text[18]").toString();
        String Tweet1 = js.get("text[19]").toString();

        System.out.println("The first tweet is: " + Tweet1);
        logger.log(LogStatus.INFO, "The first tweet is: " + Tweet1);
        System.out.println("The second tweet is: " + Tweet2);
        logger.log(LogStatus.INFO, "The second tweet is: " + Tweet2);
    }

    @Test(priority = 2)
    public void Case2_DeleteTweets () throws InterruptedException {

        //Delete the generated tweets via API automation
        for (int i = 0; i < 20; i++) {
            Response Resp = given()
                    .auth().oauth(consumerKey, consumerSecret, accessToken, tokenSecret)
                    .queryParam("screen_name", "@sharuuukh")
                    .when()
                    .get("user_timeline.json")
                    .then()
                    .extract()
                    .response();

            String getTweet = Resp.asString();
            JsonPath js = new JsonPath(getTweet);
            TweetID = (js.get("id[0]")).toString();

            given()
                    .auth().oauth(consumerKey, consumerSecret, accessToken, tokenSecret)
                    .queryParam("id", TweetID)
                    .when()
                    .post("destroy.json")
                    .then()
                    .extract()
                    .response();

            if (Resp.statusCode() == 200){
                System.out.println("Status code is 200 and successful. Tweet number: " +i+ " has been deleted.");
                logger.log(LogStatus.PASS, "Status code is 200 and successful. Tweet number: " +i+ " has been deleted.");
            } else {
                System.out.println("Status code is not successful: " + Resp.statusCode());
                logger.log(LogStatus.FAIL, "Status code is not successful: " + Resp.statusCode());
            }
        }

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
        String Tweet2 = js2.get("text[3]").toString();
        String Tweet1 = js2.get("text[4]").toString();

        if (Tweet1.equals("This is auto-generated tweet number: 1")){
            System.out.println("The first tweet has not been successfully deleted.");
            logger.log(LogStatus.FAIL, "The first tweet has not been successfully deleted.");
        } else {
            System.out.println("The first tweet has been successfully deleted from timeline.");
            logger.log(LogStatus.PASS, "The first tweet has been successfully deleted from timeline.");
        }
        if (Tweet2.equals("This is auto-generated tweet number: 2")){
            System.out.println("The second tweet has not been successfully deleted.");
            logger.log(LogStatus.FAIL, "The second tweet has not been successfully deleted.");
        } else {
            System.out.println("The second tweet has been successfully deleted from timeline.");
            logger.log(LogStatus.PASS, "The second tweet has been successfully deleted from timeline.");
        }

        driver.navigate().refresh();
        Thread.sleep(2000);
        Base_Class.TwitterHomePage().setAccountMenu();
        Base_Class.TwitterHomePage().setLogout();
    }
}
