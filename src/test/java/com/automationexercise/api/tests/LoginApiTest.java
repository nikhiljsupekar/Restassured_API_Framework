package com.automationexercise.api.tests;

import com.automationexercise.api.base.BaseTest;
import com.automationexercise.api.constants.Endpoints;
import com.automationexercise.api.pojo.UserPayload;
import com.automationexercise.api.utils.ApiAssertions;
import com.automationexercise.api.utils.TestDataFactory;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

/**
 * verifyLogin needs an account that already exists, so this class provisions
 * one via createAccount in @BeforeClass and removes it in @AfterClass rather
 * than depending on test execution order against AccountApiTest.
 */
@Epic("automationexercise.com API")
@Feature("Verify Login")
public class LoginApiTest extends BaseTest {

    private UserPayload user;

    @BeforeClass(alwaysRun = true, dependsOnMethods = "setUpBase")
    public void createTestUser() {
        user = TestDataFactory.defaultUser();
        Response response = given(requestSpec)
                .formParams(user.toFormParams())
                .when()
                .post(Endpoints.CREATE_ACCOUNT)
                .then()
                .statusCode(200)
                .extract().response();

        ApiAssertions.assertResponseCode(response, 201);
    }

    @AfterClass(alwaysRun = true)
    public void deleteTestUser() {
        Response response = given(requestSpec)
                .formParam("email", user.getEmail())
                .formParam("password", user.getPassword())
                .when()
                .delete(Endpoints.DELETE_ACCOUNT)
                .then()
                .statusCode(200)
                .extract().response();

        ApiAssertions.assertResponseCode(response, 200);
    }

    @Test(description = "API 7: POST /api/verifyLogin with valid credentials returns 200")
    @Story("Verify Login with valid details")
    @Severity(SeverityLevel.BLOCKER)
    public void verifyLogin_withValidCredentials_returnsOk() {
        Response response = given(requestSpec)
                .formParam("email", user.getEmail())
                .formParam("password", user.getPassword())
                .when()
                .post(Endpoints.VERIFY_LOGIN)
                .then()
                .statusCode(200)
                .extract().response();

        ApiAssertions.assertResponseCode(response, 200);
        ApiAssertions.assertMessage(response, "User exists!");
    }

    @Test(description = "API 8: POST /api/verifyLogin without email returns 400")
    @Story("Verify Login without email parameter")
    @Severity(SeverityLevel.NORMAL)
    public void verifyLogin_withoutEmail_returnsBadRequest() {
        Response response = given(requestSpec)
                .formParam("password", user.getPassword())
                .when()
                .post(Endpoints.VERIFY_LOGIN)
                .then()
                .statusCode(200)
                .extract().response();

        ApiAssertions.assertResponseCode(response, 400);
        ApiAssertions.assertMessage(response, "Bad request, email or password parameter is missing in POST request.");
    }

    @Test(description = "API 9: DELETE /api/verifyLogin is unsupported and returns 405")
    @Story("Delete To Verify Login")
    @Severity(SeverityLevel.NORMAL)
    public void deleteVerifyLogin_returnsMethodNotSupported() {
        Response response = given(requestSpec)
                .when()
                .delete(Endpoints.VERIFY_LOGIN)
                .then()
                .statusCode(200)
                .extract().response();

        ApiAssertions.assertResponseCode(response, 405);
        ApiAssertions.assertMessage(response, "This request method is not supported.");
    }

    @Test(description = "API 10: POST /api/verifyLogin with invalid/unregistered email returns 404")
    @Story("Verify Login with invalid details")
    @Severity(SeverityLevel.CRITICAL)
    public void verifyLogin_withInvalidCredentials_returnsNotFound() {
        Response response = given(requestSpec)
                .formParam("email", "no_such_user_" + System.currentTimeMillis() + "@example.com")
                .formParam("password", "wrongPassword")
                .when()
                .post(Endpoints.VERIFY_LOGIN)
                .then()
                .statusCode(200)
                .extract().response();

        ApiAssertions.assertResponseCode(response, 404);
        ApiAssertions.assertMessage(response, "User not found!");
    }
}
