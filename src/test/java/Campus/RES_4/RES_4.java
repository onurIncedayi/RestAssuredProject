package Campus.RES_4;

import Campus.RES_4.Model.Field;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class RES_4 {

    public Cookies cookies;
    public String fieldID;
    public String fieldName;
    public String fieldCode;
    Field field = new Field();

    @BeforeClass
    public void loginCampus() {

        baseURI = "https://demo.mersys.io/";

        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("username", "richfield.edu");
        userInfo.put("password", "Richfield2020!");
        userInfo.put("rememberMe", "true");

        cookies = given()
                .contentType(ContentType.JSON)
                .body(userInfo)
                .when()
                .post("auth/login")
                .then()
                .statusCode(200)
                .extract().response().getDetailedCookies();
    }

    @Test
    public void createField() {
        fieldName = randomName();
        fieldCode = randomCode();

        field.setName(fieldName);
        field.setCode(fieldCode);
        field.setType("STRING");

        field = given()
                .contentType(ContentType.JSON)
                .cookies(cookies)
                .body(field)
                .when()
                .post("school-service/api/entity-field")
                .then()
                .statusCode(201)
                .log().body()
                .extract().response().as(Field.class);

    }

    public String randomName() {
        return RandomStringUtils.randomAlphabetic(6).toLowerCase();
    }

    public String randomCode() {
        return RandomStringUtils.randomNumeric(4);
    }

    @Test(dependsOnMethods = "createField", priority = 1)
    public void createFieldNegative() {
        Field field = new Field();
        field.setName(fieldName);
        field.setCode(fieldCode);
        field.setType("STRING");

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(field)
                .when()
                .post("school-service/api/entity-field")
                .then()
                .statusCode(400)
        //.log().body()
        ;
        Assert.assertNull(field.getId());
    }

    @Test(dependsOnMethods = "createField", priority = 2)
    public void updateField() {
        fieldName = randomName();
        field.setName(fieldName);
        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(field)
                .when()
                .put("school-service/api/entity-field")
                .then()
                .statusCode(200)
                .log().body()
        ;
    }

    @Test(dependsOnMethods = "createField", priority = 3)
    public void deleteField() {
        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .pathParam("deleteID", field.getId())
                .when()
                .delete("school-service/api/entity-field/{deleteID}")
                .then()
                .statusCode(204);
    }

    @Test(dependsOnMethods = {"createField", "deleteField"}, priority = 4)
    public void deleteFieldNegative() {
        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .pathParam("deleteID", field.getId())
                .when()
                .delete("school-service/api/entity-field/{deleteID}")
                .then()
                .statusCode(400);
    }

    @Test(dependsOnMethods = {"createField", "deleteField"}, priority = 5)
    public void updateFieldNegative() {
        field.setName("update");
        field.setCode("0000");

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(field)
                .when()
                .put("school-service/api/entity-field")
                .then()
                //.log().body()
                .body("code", equalTo(null))
                .statusCode(400);
    }


    @Test
    public void searchFields() {

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body("{\n" + "  \"schoolId\": \"" + field.getSchoolId() + "\"\n" + "}")
                //.log().body()
                .when()
                .post("school-service/api/entity-field/search")
                .then()
                .statusCode(200)
        ;
    }
}
