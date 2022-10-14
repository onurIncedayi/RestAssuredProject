package Campus.RES_12;

import Campus.RES_12.Model.Nationality;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.*;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class NationalityTest {

    Cookies cookies;

    @BeforeClass
    public void loginCampus() {


        baseURI = "https://demo.mersys.io/";

        Map<String, String> credential = new HashMap<>();
        credential.put("username", "richfield.edu");
        credential.put("password", "Richfield2020!");
        credential.put("rememberMe", "true");

        cookies =
                given()

                        .contentType(ContentType.JSON)
                        .body(credential)


                        .when()
                        .post("auth/login")


                        .then()
                        .log().cookies()
                        .log().body()
                        .statusCode(200)
                        .extract().response().getDetailedCookies()

        ;
    }

    String nationalityID;
    String nationalityName;


    @Test
    public void createNationality() {
        nationalityName = getRandomName();

        Nationality nationality = new Nationality();
        nationality.setName(nationalityName);

        nationalityID =
                given()

                        .cookies(cookies)
                        .contentType(ContentType.JSON)
                        .body(nationality)

                        .when()
                        .post("school-service/api/nationality")

                        .then()
                        .statusCode(201)
                        .extract().jsonPath().getString("id")


        ;
    }

    public String getRandomName() {
        return RandomStringUtils.randomAlphabetic(6).toLowerCase();
    }


    @Test(dependsOnMethods = "createNationality")
    public void createNationalityNegative() {


        Nationality nationality = new Nationality();
        nationality.setName(nationalityName);

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(nationality)

                .when()
                .post("school-service/api/nationality")

                .then()
                .log().body()
                .statusCode(400)
                .body("message", equalTo("The Nationality with Name \"" + nationalityName + "\" already exists."))
        ;
    }


    @Test(dependsOnMethods = "createNationality")
    public void updateNationality() {

        nationalityName = getRandomName();

        Nationality nationality = new Nationality();
        nationality.setId(nationalityID);
        nationality.setName(nationalityName);


        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(nationality)

                .when()
                .put("school-service/api/nationality")

                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo(nationalityName))
        ;
    }


    @Test(dependsOnMethods = "updateNationality")
    public void deleteNationalityById() {
        given()
                .cookies(cookies)
                .pathParam("nationalityID", nationalityID)

                .when()
                .delete("school-service/api/nationality/{nationalityID}")

                .then()
                .log().body()
                .statusCode(200)
        ;
    }


    @Test(dependsOnMethods = "deleteNationalityById")
    public void deleteNationalityByIdNegative() {
        given()
                .cookies(cookies)
                .pathParam("nationalityID", nationalityID)
                .log().uri()
                .when()
                .delete("school-service/api/nationality/{nationalityID}")

                .then()
                .log().body()
                .statusCode(400)
        ;
    }

    @Test(dependsOnMethods = "deleteNationalityById")
    public void updateNationalityNegative() {
        nationalityName = getRandomName();

        Nationality nationality = new Nationality();
        nationality.setId(nationalityID);
        nationality.setName(nationalityName);


        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(nationality)

                .when()
                .put("school-service/api/nationality")

                .then()
                .log().body()
                .statusCode(400)
              //  .body("message", equalTo("Nationality not found"))
        ;
    }


}







