package com.library.steps;

import com.library.utility.LibraryAPI_Util;
import io.cucumber.java.en.Given;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class US05_Steps {
    static RequestSpecification reqSpec;
    static String token;

    @Given("I logged Library api with credentials {string} and {string}")
    public void i_logged_library_api_with_credentials_and(String username, String password) {
        token = LibraryAPI_Util.getToken(username, password);
    }
    @Given("I send token information as request body")
    public void i_send_token_information_as_request_body() {
        reqSpec = given().formParam("token",token);
    }

}
