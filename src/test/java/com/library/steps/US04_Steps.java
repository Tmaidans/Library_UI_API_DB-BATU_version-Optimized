package com.library.steps;

import com.library.pages.BasePage;
import com.library.pages.BookPage;
import com.library.pages.LoginPage;
import com.library.utility.BrowserUtil;
import com.library.utility.DB_Util;
import io.cucumber.java.en.Then;
import org.junit.Assert;

import java.util.Map;

import static com.library.steps.US03_Steps.actualID;
import static com.library.steps.US03_Steps.userMap;

public class US04_Steps {
    LoginPage loginPage = new LoginPage();
    BasePage basePage = new BookPage();
    public static Map<String, Object> dbGetBookwId;
    @Then("created user information should match with Database")
    public void created_user_information_should_match_with_database() {
        dbGetBookwId = DB_Util.pullBookDataDB(actualID,"user");
        Assert.assertEquals(dbGetBookwId,userMap);
    }

    @Then("created user should be able to login Library UI")
    public void created_user_should_be_able_to_login_library_ui() {

    loginPage.login((String) US03_Steps.randomUserLoad.get("email"), (String) US03_Steps.randomUserLoad.get("password"));
        BrowserUtil.waitForVisibility(basePage.accountHolderName,5);
        Assert.assertTrue(basePage.accountHolderName.isDisplayed());
    }

    @Then("created user name should appear in Dashboard Page")
    public void created_user_name_should_appear_in_dashboard_page() {
        BrowserUtil.waitForVisibility(basePage.accountHolderName,5);
        Assert.assertEquals(basePage.accountHolderName.getText(),US03_Steps.randomUserLoad.get("full_name"));
    }

}
