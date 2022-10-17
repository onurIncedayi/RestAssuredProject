package Campus.RES_3;

import Campus.RES_3.Model.Document;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import org.apache.commons.lang3.RandomStringUtils;
import java.util.HashMap;
import java.util.Map;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;



public class DocumentTest {


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
                        // .log().cookies()
                        .statusCode(200)
                        .extract().response().getDetailedCookies()

        ;
    }


    String documentID;
    String documentName;

    String documentDescription;


    @Test
    public void createDocument() {

        documentName = getRandomName();
        documentDescription = getRandomName() + " " + getRandomName();

        Document document = new Document();
        document.setName(documentName);
        document.setAttachmentStages(new String[]{"STUDENT_REGISTRATION"});
        document.setSchoolId("6343bf893ed01f0dc03a509a");
        document.setDescription(documentDescription);


        documentID =
                given()

                        .cookies(cookies)
                        .contentType(ContentType.JSON)
                        .body(document)

                        .when()
                        .post("school-service/api/attachments")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().jsonPath().getString("id")

        ;
    }

    public String getRandomName() {
        return RandomStringUtils.randomAlphabetic(6).toLowerCase();
    }


    @Test(dependsOnMethods = "createDocument")
    public void createDocumentNegative() {

        Document document = new Document();
        document.setName(documentName);
        document.setAttachmentStages(new String[]{"STUDENT_REGISTRATION"});


        given()

                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(document)

                .when()
                .post("school-service/api/attachments")

                .then()
                .log().body()
                .statusCode(400)


        ;
    }

    @Test(dependsOnMethods = "createDocument")
    public void updateDocument() {

        documentName = getRandomName();

        Document document = new Document();
        document.setId(documentID);
        document.setName(documentName);
        document.setAttachmentStages(new String[]{"STUDENT_REGISTRATION"});
        document.setSchoolId("6343bf893ed01f0dc03a509a");
        document.setDescription(documentDescription);

        given()

                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(document)

                .when()
                .put("school-service/api/attachments")

                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo(documentName))


        ;
    }

    @Test(dependsOnMethods = "updateDocument")
    public void deleteDocumentById() {
        given()
                .cookies(cookies)
                .pathParam("documentID", documentID)

                .when()
                .delete("school-service/api/attachments/{documentID}")

                .then()
                .log().body()
                .statusCode(200)
        ;
    }

    @Test(dependsOnMethods = "deleteDocumentById")
    public void deleteDocumentByIdNegative() {
        given()
                .cookies(cookies)
                .pathParam("documentID", documentID)
                .log().uri()

                .when()
                .delete("school-service/api/attachments/{documentID}}")

                .then()
                .log().body()
                .statusCode(400)
        ;
    }

    @Test(dependsOnMethods = "deleteDocumentById")
    public void updateDocumentNegative() {
        documentName = getRandomName();

        Document document = new Document();
        document.setId(documentID);
        document.setName(documentName);
        document.setAttachmentStages(new String[]{"STUDENT_REGISTRATION"});
        document.setSchoolId("6343bf893ed01f0dc03a509a");
        document.setDescription(documentDescription);

        given()

                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(document)

                .when()
                .put("school-service/api/attachments")

                .then()
                .log().body()
                .statusCode(400)
                .body("message", equalTo("GENERAL.ERROR.ATTACHMENT_TYPE_NOT_FOUND"))


        ;
    }
}















