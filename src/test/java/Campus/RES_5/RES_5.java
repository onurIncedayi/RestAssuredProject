package Campus.RES_5;

import Campus.RES_5.Model.Position;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.*;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class RES_5 {

    Cookies cookies;
    @BeforeClass
    public void loginCampus() {
        baseURI = "https://demo.mersys.io/";

        Map<String, String> credential = new HashMap<>();
        credential.put("username", "richfield.edu");
        credential.put("password", "Richfield2020!");
        credential.put("rememberMe", "true");

        cookies=
                given()
                        .contentType(ContentType.JSON)
                        .body(credential)

                        .when()
                        .post("auth/login")

                        .then()
                        //.log().cookies()
                        .statusCode(200)
                        .extract().response().getDetailedCookies()
        ;
    }

    String positionName;
    String positionShortName;
    String positionId;

    @Test
    public void addPosition() {
        positionName = getRandomName();
        positionShortName = getRandomShortName();

        Position position = new Position();
        position.setName(positionName);
        position.setShortName(positionShortName);

        positionId =
                given()
                        .cookies(cookies)
                        .contentType(ContentType.JSON)
                        .body(position)

                        .when()
                        .post("school-service/api/employee-position")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().jsonPath().getString("id")
        ;
    }

    public String getRandomName() {
        return RandomStringUtils.randomAlphabetic(8).toLowerCase();
    }
    public String getRandomShortName() {
        return RandomStringUtils.randomAlphabetic(3).toLowerCase();
    }

    @Test(dependsOnMethods = "addPosition")
    public void addPositionNegative()
    {
        Position position = new Position();
        position.setName(positionName);
        position.setShortName(positionShortName);

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(position)

                .when()
                .post("school-service/api/employee-position")

                .then()
                .log().body()
                .statusCode(400)
                .body("message",equalTo("The Position with Name \""+positionName+"\" already exists."))
        ;

    }

    @Test(dependsOnMethods = "addPosition")
    public void editPosition()
    {
        positionName = getRandomName();

        Position position=new Position();

        position.setId(positionId);
        position.setName(positionName);
        position.setShortName(positionShortName);

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(position)

                .when()
                .put("school-service/api/employee-position")

                .then()
                .log().body()
                .statusCode(200)
                .body("name",equalTo(positionName))
        ;

    }


}
