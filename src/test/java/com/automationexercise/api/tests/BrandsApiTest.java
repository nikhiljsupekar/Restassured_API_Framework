package com.automationexercise.api.tests;

import com.automationexercise.api.base.BaseTest;
import com.automationexercise.api.constants.Endpoints;
import com.automationexercise.api.utils.ApiAssertions;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

@Epic("automationexercise.com API")
@Feature("Brands List")
public class BrandsApiTest extends BaseTest {

    @Test(description = "API 3: GET /api/brandsList returns 200 with a brand list")
    @Story("Get All Brands List")
    @Severity(SeverityLevel.CRITICAL)
    public void getAllBrandsList_returnsOk() {
        Response response = given(requestSpec)
                .when()
                .get(Endpoints.BRANDS_LIST)
                .then()
                .statusCode(200)
                .extract().response();

        ApiAssertions.assertResponseCode(response, 200);
        Assert.assertFalse(response.jsonPath().getList("brands").isEmpty(),
                "Expected at least one brand in the response");
    }

    @Test(description = "API 4: PUT /api/brandsList is unsupported and returns 405")
    @Story("Put To All Brands List")
    @Severity(SeverityLevel.NORMAL)
    public void putToBrandsList_returnsMethodNotSupported() {
        Response response = given(requestSpec)
                .when()
                .put(Endpoints.BRANDS_LIST)
                .then()
                .statusCode(200)
                .extract().response();

        ApiAssertions.assertResponseCode(response, 405);
        ApiAssertions.assertMessage(response, "This request method is not supported.");
    }
}
