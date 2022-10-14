package Campus.RES_2;

import Campus.RES_2.Model.Attestations;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class RES_2 {

    Cookies cookies;

    String attestationName;
    String attestationsID;


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
                        .statusCode(200)
                        .extract().response().getDetailedCookies()
        ;
    }


    @Test
    public void addAttestations() {

        attestationName = getRandomNameInput();

        Attestations attestations = new Attestations();
        attestations.setNameInput(attestationName);

        attestationsID =
                given()
                        .cookies(cookies)
                        .contentType(ContentType.JSON)
                        .body(attestations)
                        .when()
                        .post("school-service/api/attestation")
                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().jsonPath().getString("id")
        ;


    }

    public String getRandomNameInput() {
        return RandomStringUtils.randomAlphabetic(9).toLowerCase();
    }

    @Test(dependsOnMethods = "addAttestations")
    public void addAttestationsNegative() {
        Attestations attestations = new Attestations();
        attestations.setNameInput(attestationName);
        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(attestations)
                .when()
                .post("school-service/api/attestation")
                .then()
                .log().body()
                .statusCode(400)
                .body("message", equalTo("Name \"" + getRandomNameInput() + "\" already exists."))
        ;
    }

    @Test(dependsOnMethods = "addAttestations")
    public void editAttestation() {

        attestationName = getRandomNameInput();

        Attestations attestations = new Attestations();
        attestations.setNameInput(attestationName);
        attestations.setId(attestationsID);


        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(attestations)

                .when()
                .put("school-service/api/attestation")

                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo(attestationName))
        ;
    }

    @Test(dependsOnMethods = "editAttestation")
    public void deleteAttestationById() {
        given()
                .cookies(cookies)
                .pathParam("attestationID", attestationsID)

                .when()
                .delete("school-service/api/attestation/{attestationsID}")

                .then()
                .log().body()
                .statusCode(204)
        ;
    }
}

