package com.library.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.junit.Assert;
import java.util.List;

public class US02_Steps {
    static int pathParam ;



    @Given("Path param id is {string}")
    public void path_param_id_is(String param) {

        pathParam = Integer.parseInt(param);

    }
    @Then("{string} field should be same with path param")
    public void field_should_be_same_with_path_param(String fieldToGet) {
        int actualID = Integer.parseInt(US01_Steps.BodyMap.get(fieldToGet));
        Assert.assertEquals(actualID,pathParam);



    }
    @Then("following fields should not be null")
    public void following_fields_should_not_be_null(List<String> fieldsToAssert) {
        for (String eachField : fieldsToAssert) {
            Assert.assertTrue(US01_Steps.BodyMap.get(eachField) != null );
        }

    }

}
