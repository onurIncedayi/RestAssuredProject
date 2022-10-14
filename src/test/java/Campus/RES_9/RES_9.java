package Campus.RES_9;

import Campus.RES_9.Model.BankAccount;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.*;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class RES_9 {

    Cookies cookies;
    BankAccount ba = new BankAccount();
    
    @BeforeClass
    public void setup() {
        Map<String, String> creditentials = new HashMap<>();
        creditentials.put("username", "richfield.edu");
        creditentials.put("password", "Richfield2020!");
        creditentials.put("rememberMe", "true");

        baseURI = "https://demo.mersys.io/school-service/api/bank-accounts";

        cookies = given()
                .contentType(ContentType.JSON)
                .body(creditentials)
                .when()
                .post("https://demo.mersys.io/auth/login")
                .then()
                .statusCode(200)
                .extract().detailedCookies();

        ba.setSchoolId("6343bf893ed01f0dc03a509a");
    }

    @Test
    public void createBankAccount() {
        ba.setName(randomAccName());
        ba.setIban("TR" + randomIBAN());
        ba.setCurrency("TRY");
        ba.setIntegrationCode(randomCode());

        ba = given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(ba)
                .when()
                .post()
                .then()
                .log().body()
                .statusCode(201)
                .extract().response().as(BankAccount.class);

    }

    public String randomAccName() {
        return RandomStringUtils.randomAlphabetic(4).toUpperCase();
    }

    public String randomIBAN() {
        return RandomStringUtils.randomNumeric(20);
    }

    public String randomCode() {
        return RandomStringUtils.randomNumeric(3);
    }

    @Test(dependsOnMethods = "createBankAccount", priority = 1)
    public void createBankAccNegative() {
        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(ba)
                .given()
                .post()
                .then()
                .statusCode(400);
    }

    @Test(dependsOnMethods = "createBankAccount", priority = 2)
    public void editBankAcc() {
        ba.setName("update");
        ba.setIntegrationCode("000");
        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(ba)
                .when()
                .put()
                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo("update"))
                .body("integrationCode", equalTo("000"));
    }

    @Test(dependsOnMethods = "createBankAccount", priority = 3)
    public void deleteBankAcc() {
        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .pathParam("accID", ba.getId())
                .when()
                .delete("{accID}")
                .then()
                .log().body()
                .statusCode(200);
    }

    @Test(dependsOnMethods = {"createBankAccount", "deleteBankAcc"}, priority = 4)
    public void deleteBankAccNegative() {
        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .pathParam("accID", ba.getId())
                .when()
                .delete("{accID}")
                .then()
                .log().body()
                .body("message",containsString("must be exist"))
                .body("code",equalTo(null))
                .statusCode(400);
    }

    @Test(dependsOnMethods = {"createBankAccount", "deleteBankAcc"}, priority = 5)
    public void editBankAccNegative(){
        ba.setName("editNegative");

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(ba)
                .when()
                .put()
                .then()
                .log().body()
                .body("detail",containsString("is null"))
                .statusCode(400);
    }

    @Test
    public void searchBankAcc(){

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "  \"name\": \"\",\n" +
                        "  \"schoolId\": \""+ba.getSchoolId()+"\"\n" +
                        "}")
                .given()
                .post("/search")
                .then()
                .log().body()
                .statusCode(200);
    }


}
