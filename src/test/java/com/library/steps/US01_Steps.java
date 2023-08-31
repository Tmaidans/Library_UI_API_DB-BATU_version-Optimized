package com.library.steps;

import com.library.utility.LibraryAPI_Util;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;
import org.junit.Assert;

import java.util.Map;

import static io.restassured.RestAssured.*;

public class US01_Steps {
    static String token;
    public static ResponseSpecification resSpec;
     static  Response response;
    JsonPath jsonPath;
    static Map<String, String> BodyMap;
    @Given("I logged Library api as a {string}")
    public void i_logged_library_api_as_a(String user) {
         token = LibraryAPI_Util.getToken(user);
    }
    @Given("Accept header is {string}")
    public void accept_header_is(String acceptHeader) {
        given().accept(acceptHeader);
    }
    @When("I send GET request to {string} endpoint")
    public void i_send_get_request_to_endpoint(String endpoint) {
        if (endpoint.contains("{")){
           response = LibraryAPI_Util.idPathParamHandler(US02_Steps.pathParam)
                    .accept(ContentType.JSON)
                    .header("x-library-token",token).when()
                    .get(endpoint);
            jsonPath = response.jsonPath();
            BodyMap = jsonPath.getMap("");
        } else if (!endpoint.contains("{")) {
            response =  given().accept(ContentType.JSON).header("x-library-token",token).when()
                    .get(endpoint);
        }}

    @Then("status code should be {int}")
    public void status_code_should_be(int expectedStatusCode) {
        if (response == null){
            response = US03_Steps.response;
        }
        Assert.assertEquals(expectedStatusCode,response.getStatusCode());

    }
    @Then("Response Content type is {string}")
    public void response_content_type_is(String expectedContenType) {


        Assert.assertEquals(expectedContenType,response.getContentType());

    }
    @Then("{string} field should not be null")
    public void field_should_not_be_null(String field) {

        Assert.assertTrue(response.getHeader(field) != "NULL");

    }

    public static Response addRandomBookFunctionHandler(String endpoint){
       if (endpoint.contains("user")){
            System.out.println(token);
            response = given().spec(US03_Steps.reqSpec).header("x-library-token",token)
                    .post(endpoint);
            response.prettyPrint();
            return response;

        } else if (endpoint.contains("decode")) {
           response = given().spec(US05_Steps.reqSpec)
                   .post(endpoint);
           return response;

       } else {
            response = given().spec(US03_Steps.reqSpec).header("x-library-token",token)
                    .post(endpoint);
            return response;
        }


    }}

