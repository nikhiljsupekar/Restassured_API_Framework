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
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

/**
 * Account lifecycle must run in order: a created account is required before
 * it can be fetched, updated, and finally deleted, so each step declares the
 * previous one as a hard TestNG dependency instead of relying on file order.
 */
@Epic("automationexercise.com API")
@Feature("Account Lifecycle")
public class AccountApiTest extends BaseTest {

    private final UserPayload user = TestDataFactory.defaultUser();

    @Test(description = "API 11: POST /api/createAccount creates a new user and returns 201")
    @Story("Create/Register User Account")
    @Severity(SeverityLevel.BLOCKER)
    public void createAccount_returnsCreated() {
        Response response = given(requestSpec)
                .formParams(user.toFormParams())
                .when()
                .post(Endpoints.CREATE_ACCOUNT)
                .then()
                .statusCode(200)
                .extract().response();

        ApiAssertions.assertResponseCode(response, 201);
        ApiAssertions.assertMessage(response, "User created!");
    }

    @Test(description = "API 14: GET /api/getUserDetailByEmail returns the created user's details",
            dependsOnMethods = "createAccount_returnsCreated")
    @Story("Get User Detail by Email")
    @Severity(SeverityLevel.CRITICAL)
    public void getUserDetailByEmail_returnsUser() {
        Response response = given(requestSpec)
                .queryParam("email", user.getEmail())
                .when()
                .get(Endpoints.GET_USER_DETAIL_BY_EMAIL)
                .then()
                .statusCode(200)
                .extract().response();

        ApiAssertions.assertResponseCode(response, 200);
        Assert.assertEquals(response.jsonPath().getString("user.email"), user.getEmail());
        Assert.assertEquals(response.jsonPath().getString("user.name"), user.getName());
    }

    @Test(description = "API 13: PUT /api/updateAccount updates the created user and returns 200",
            dependsOnMethods = "getUserDetailByEmail_returnsUser")
    @Story("Update User Account")
    @Severity(SeverityLevel.CRITICAL)
    public void updateAccount_returnsOk() {
        UserPayload updated = TestDataFactory.defaultUser(user.getEmail());

        Response response = given(requestSpec)
                .formParams(updated.toFormParams())
                .when()
                .put(Endpoints.UPDATE_ACCOUNT)
                .then()
                .statusCode(200)
                .extract().response();

        ApiAssertions.assertResponseCode(response, 200);
        ApiAssertions.assertMessage(response, "User updated!");
    }

    @Test(description = "API 12: DELETE /api/deleteAccount removes the created user and returns 200",
            dependsOnMethods = "updateAccount_returnsOk", alwaysRun = true)
    @Story("Delete User Account")
    @Severity(SeverityLevel.BLOCKER)
    public void deleteAccount_returnsOk() {
        Response response = given(requestSpec)
                .formParam("email", user.getEmail())
                .formParam("password", user.getPassword())
                .when()
                .delete(Endpoints.DELETE_ACCOUNT)
                .then()
                .statusCode(200)
                .extract().response();

        ApiAssertions.assertResponseCode(response, 200);
        ApiAssertions.assertMessage(response, "Account deleted!");
    }
}
