package Campus.RES_5;

import Campus.RES_5.Model.Position;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class RES_5 {

    public Cookies cookies;
    public String positionName;
    public String positionShortName;
    public String positionId;

    public String tenantId = "5fe0786230cc4d59295712cf";
    public Position position = new Position();

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


    @Test
    public void addPosition() {
        positionName = getRandomName();
        positionShortName = getRandomShortName();

        position.setName(positionName);
        position.setShortName(positionShortName);
        position.setTenantId(tenantId);

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

    @Test(dependsOnMethods = "addPosition", priority = 1)
    public void addPositionNegative()
    {
        position.setName(positionName);
        position.setShortName(positionShortName);
        position.setTenantId(tenantId);

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

    @Test(dependsOnMethods = "addPosition", priority = 2)
    public void editPosition()
    {
        positionName = getRandomName();

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
                .body("name",equalTo(positionName));

    }

    @Test (dependsOnMethods = "addPosition", priority = 3)
    public void deletePositionById()
    {
        given()
                .cookies(cookies)
                .pathParam("positionId", positionId)
                .when()
                .delete("https://demo.mersys.io/school-service/api/employee-position/{positionId}")
                .then()
                .log().body()
                .statusCode(204);
    }

    @Test (dependsOnMethods = "addPosition", priority = 4)
    public void deletePositionByIdNegative()
    {
        given()
                .cookies(cookies)
                .pathParam("positionId", positionId)
                .when()
                .delete("https://demo.mersys.io/school-service/api/employee-position/{positionId}")
                .then()
                .log().body()
                .statusCode(204);
    }


}
