package com.library.steps;
import static io.restassured.RestAssured.*;

import com.library.pages.BasePage;
import com.library.pages.BookPage;
import com.library.pages.LoginPage;
import com.library.utility.BrowserUtil;
import com.library.utility.DB_Util;
import com.library.utility.LibraryAPI_Util;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import java.util.LinkedHashMap;
import java.util.Map;

public class US03_Steps {
    static RequestSpecification reqSpec;
    static Response response;
    static JsonPath jsonPath;
    Map<String,String> randomBookLoad;
    static Map<String,Object> randomUserLoad;

    LoginPage loginPage = new LoginPage();
    BasePage basePage = new BookPage();
    BookPage bookPage = new BookPage();
    static String actualID;
    static String fullName;
    static String email;
    public static Map<String, Object> apiGetBookwIdResponse;
   public static Map<String, Object> dbGetBookwId;
    public static Map<String,Object> userMap;

    @Given("Request Content Type header is {string}")
    public void request_content_type_header_is(String header) {
        given().contentType(header);

    }
    @Given("I create a random {string} as request body")
    public void i_create_a_random_as_request_body(String payLoadType) {
        switch (payLoadType){
            case "book":
                randomBookLoad = LibraryAPI_Util.randomBookCreate();
                reqSpec = given().formParams(randomBookLoad).log().all();
                break;
            case "user":
                randomUserLoad = LibraryAPI_Util.getRandomUserMap();
                reqSpec = given().formParams(randomUserLoad).log().all();
                break;

        }


    }
    @When("I send POST request to {string} endpoint")
    public void i_send_post_request_to_endpoint(String endpoint) {
        if (endpoint.contains("decode")) {
            response = given().spec(US05_Steps.reqSpec)
                    .post(endpoint);
        }else {
            response = US01_Steps.addRandomBookFunctionHandler(endpoint);
        }

    }
    @Then("the field value for {string} path should be equal to {string}")
    public void the_field_value_for_path_should_be_equal_to(String fieldToCheck, String expectedFieldValue) {
        if (expectedFieldValue.contains("user")){
            jsonPath = response.jsonPath();
            actualID = jsonPath.getString("user_id");
            userMap = new LinkedHashMap<>();
            userMap.put("email",randomUserLoad.get("email"));
            userMap.put("full_name",randomUserLoad.get("full_name"));
            userMap.put("id",actualID);
        }else{
            jsonPath = response.jsonPath();
            actualID = jsonPath.getString("book_id");

            String actualMessageField = jsonPath.getString(fieldToCheck);
            Assert.assertEquals(expectedFieldValue,actualMessageField);
        }


    }

    @And("I logged in Library UI as {string}")
    public void iLoggedInLibraryUIAs(String userType) {
        loginPage.login(userType);
    }

    @And("I navigate to {string} page")
    public void iNavigateToPage(String page) {
        basePage.navigateModule(page);
    }

    @And("UI, Database and API created book information must match")
    public void uiDatabaseAndAPICreatedBookInformationMustMatch() {
        System.out.println(actualID);
        apiGetBookwIdResponse = LibraryAPI_Util.pullBodyFromAPI(actualID);
        dbGetBookwId = DB_Util.pullBookDataDB(actualID,"book");
        Map<String, String> uiGetBookInfo = BrowserUtil.mapper();

        Assert.assertEquals(uiGetBookInfo,dbGetBookwId);
        Assert.assertEquals(apiGetBookwIdResponse,uiGetBookInfo);





    }
}
