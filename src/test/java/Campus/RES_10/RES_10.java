package Campus.RES_10;

import Campus.RES_10.Model.Grade_Levels;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.*;


public class RES_10 {

    public Cookies cookies;
    public String gradeID;
    public String gradeName;
    public String gradeShortName;
    public String orderInt;


    Grade_Levels gL=new Grade_Levels();

    @BeforeClass
    public void loginToMersys(){
        baseURI = "https://demo.mersys.io/";
        Map<String, String> credential = new HashMap<>();
        credential.put("username","richfield.edu");
        credential.put("password","Richfield2020!");
        credential.put("rememberMe","true");

        cookies =given()
                .contentType(ContentType.JSON)
                .body(credential)
                .when()
                .post("auth/login")
                .then()
                .statusCode(200)
                .extract().response().getDetailedCookies();
    }

    public String randomName (){
        return RandomStringUtils.randomAlphabetic(5).toLowerCase();
    }
    public String randomCode(){
        return  RandomStringUtils.randomNumeric(4);
    }

    @Test
    public void createGradeLevels(){
        gradeShortName =randomName();
        orderInt =randomCode    ();
        gradeName=randomName();

        gL.setName(gradeName);
        gL.setShortName(gradeShortName);
        gL.setOrder(orderInt);

        gL = given()
                .contentType(ContentType.JSON)
                .cookies(cookies)
                .body(gL)
                .when()
                .post("school-service/api/grade-levels")
                .then()
                .statusCode(201)
                .log().body()
                .extract().response().as(Grade_Levels.class);

    }

    @Test(dependsOnMethods = "createGradeLevels", priority = 1)
    public void createGradeLevelsNegative(){
        Grade_Levels gL=new Grade_Levels();

        gL.setName(gradeName);
        gL.setShortName(gradeShortName);
        gL.setOrder(orderInt);


        given()
                .contentType(ContentType.JSON)
                .cookies(cookies)
                .body(gL)
                .when()
                .post("school-service/api/grade-levels")
                .then()
                .statusCode(400);
        Assert.assertNull(gL.getId());
                //.log().body()
                //.extract().response().as(Grade_Levels.class);

    }

    @Test(dependsOnMethods = "createGradeLevels", priority = 2)
    public void updateGradeLevels(){

        gradeName=randomName();
        gL.setName(gradeName);

        given()
                .contentType(ContentType.JSON)
                .cookies(cookies)
                .body(gL)
                .when()
                .put("school-service/api/grade-levels")
                .then()
                .statusCode(200)
                .log().body();

    }

    @Test(dependsOnMethods = "createGradeLevels", priority = 3)
    public void deleteGradeLevels(){
        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .pathParam("deleteID",gL.getId())
                .when()
                .delete("school-service/api/grade-levels/{deleteID}")
                .then()
                .statusCode(200);

    }

    @Test(dependsOnMethods = {"createGradeLevels","deleteGradeLevels"} ,priority = 4)
    public void deleteGradeLevelsNegative(){
        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .pathParam("deleteID",gL.getId())
                .when()
                .delete("school-service/api/grade-levels/{deleteID}")
                .then()
                .statusCode(400);

    }

    @Test(dependsOnMethods = {"createGradeLevels","deleteGradeLevels"} ,priority = 5)
    public void updateGradeLevelsNegative(){
        gL.setName("update");

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(gL)
                .when()
                .put("school-service/api/grade-levels")
                .then()
                .body("message", equalTo("GradeLevels not found"))
                .statusCode(400);

    }

}
