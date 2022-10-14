package Campus.RES_8;

import Campus.RES_8.Model.Departments;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class Res_8 {

    public Cookies cookies;
    public String departmentsID;
    public String departmentsName;
    public String departmentsCode;

    public String schoolID="6343bf893ed01f0dc03a509a ";

    Departments dp=new Departments();


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
    public void createDepartments(){
        departmentsName=getRandomName();
        departmentsCode=getRandomCode();


        dp.setName(departmentsName);
        dp.setCode(departmentsCode);
        dp.setSchoolId(schoolID);

        departmentsID=
                given()
                        .cookies(cookies)
                        .contentType(ContentType.JSON)
                        .body(dp)

                        .when()
                        .post("school-service/api/department")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().jsonPath().getString("id")
        ;

        System.out.println("name manual = " + departmentsName);
    }
    public String getRandomName() {
        return RandomStringUtils.randomAlphabetic(5).toLowerCase();
    }

    public String getRandomCode() {
        return RandomStringUtils.randomNumeric(3);
    }

    @Test (dependsOnMethods  ="createDepartments",priority = 1)
    public void createDepartmentsNegative(){
        Departments dp=new Departments();
        dp.setName(departmentsName);
        dp.setCode(departmentsCode);

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(dp)
                .when()
                .post("school-service/api/department")
                .then()
                .statusCode(400);
        Assert.assertNull(dp.getId());

    }

    @Test(dependsOnMethods = "createDepartments", priority = 2)
    public void updateDepartments(){

        departmentsName=getRandomName();
        dp.setName(departmentsName);

        given()
                .contentType(ContentType.JSON)
                .cookies(cookies)
                .body(dp)
                .when()
                .put("school-service/api/department")
                .then()
                .statusCode(200)
                .log().body();

    }

    @Test(dependsOnMethods = "createDepartments", priority = 3)
    public void deleteDepartments(){
        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .pathParam("deleteID",dp.getId())
                .when()
                .delete("school-service/api/department/{deleteID}")
                .then()
                .statusCode(200);

    }

    @Test(dependsOnMethods = {"createDepartments","deleteDepartments"} ,priority = 4)
    public void deleteDepartmentsNegative(){
        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .pathParam("deleteID",dp.getId())
                .when()
                .delete("school-service/api/department/{deleteID}")
                .then()
                .statusCode(400);

    }

    @Test(dependsOnMethods = {"createDepartments","deleteDepartments"} ,priority = 5)
    public void updateDepartmentsNegative(){
        dp.setName("update");

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(dp)
                .when()
                .put("school-service/api/department")
                .then()
                .body("message", equalTo("Departments not found"))
                .statusCode(400);

    }


}
