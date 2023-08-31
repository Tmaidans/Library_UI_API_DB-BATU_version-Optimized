package com.library.utility;

import com.github.javafaker.Faker;
import com.library.steps.US01_Steps;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class LibraryAPI_Util {
    public static RequestSpecification reqSpec;
    public static ResponseSpecification resSpec;
    static JsonPath jsonPath;
    static Faker faker = new Faker();
    /**
     * Return TOKEN as String by using provided username from /token endpoint
     * @param userType
     * @return
     */
    public static String getToken(String userType){


        String email=ConfigurationReader.getProperty(userType+"_username");
        String password="libraryUser";



        return getToken(email,password);


    }

    public static String getToken(String email,String password){


        return given()
                .contentType(ContentType.URLENC)
                .formParam("email" , email)
                .formParam("password" , password).
                when()
                .post(ConfigurationReader.getProperty("library.baseUri")+"/login")
                .prettyPeek()
                .path("token") ;


    }

    public static Map<String,Object> getRandomBookMap(){

        Faker faker = new Faker() ;
        Map<String,Object> bookMap = new LinkedHashMap<>();
        String randomBookName = faker.book().title() + faker.number().numberBetween(0, 10);
        bookMap.put("name", randomBookName);
        bookMap.put("isbn", faker.code().isbn10()   );
        bookMap.put("year", faker.number().numberBetween(1000,2021)   );
        bookMap.put("author",faker.book().author()   );
        bookMap.put("book_category_id", faker.number().numberBetween(1,20)   );  // in library app valid category_id is 1-20
        bookMap.put("description", faker.chuckNorris().fact() );

        return bookMap ;
    }

    public static Map<String,Object> getRandomUserMap(){

        Faker faker = new Faker() ;
        Map<String,Object> bookMap = new LinkedHashMap<>();
        String fullName = faker.name().fullName();
        String email=fullName.substring(0,fullName.indexOf(" "))+"@library";
        System.out.println(email);
        bookMap.put("full_name", fullName );
        bookMap.put("email", email);
        bookMap.put("password", "libraryUser");
        // 2 is librarian as role
        bookMap.put("user_group_id",2);
        bookMap.put("status", "ACTIVE");
        bookMap.put("start_date", "2023-03-11");
        bookMap.put("end_date", "2024-03-11");
        bookMap.put("address", faker.address().cityName());

        return bookMap ;
    }

    public static RequestSpecification idPathParamHandler(int pathParam){
        reqSpec =  given().pathParam("id",pathParam);
        return reqSpec;
    }
    public static Map randomBookCreate(){
        Map<String,Object> bodyLoad = new LinkedHashMap<>();
        bodyLoad.put("name",faker.friends().character());
        bodyLoad.put("isbn",faker.animal().name());
        bodyLoad.put("year",faker.number().digits(4));
        bodyLoad.put("author",faker.book().author());
        bodyLoad.put("book_category_id",1);
        bodyLoad.put("description",faker.rickAndMorty().quote());
        return bodyLoad;
    }
    public static RequestSpecification searchBookWithId(String id){

        reqSpec = given().contentType(ContentType.JSON).header("x-library-token", getToken("librarian"))
                .pathParam("id",id);

        return reqSpec;
    }

    public static Map<String,Object> pullBodyFromAPI(String id){
        jsonPath = searchBookWithId(id)
                //.pathParam("id",id)
                .get("/get_book_by_id/{id}").jsonPath();
        Map<String, Object> apiGetBookById = jsonPath.getMap("");
        apiGetBookById.remove("added_date");
        return apiGetBookById;

    }






}
