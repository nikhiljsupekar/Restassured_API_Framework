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
@Feature("Products List")
public class ProductsApiTest extends BaseTest {

    @Test(description = "API 1: GET /api/productsList returns 200 with a product list")
    @Story("Get All Products List")
    @Severity(SeverityLevel.BLOCKER)
    public void getAllProductsList_returnsOk() {
        Response response = given(requestSpec)
                .when()
                .get(Endpoints.PRODUCTS_LIST)
                .then()
                .statusCode(200)
                .extract().response();

        ApiAssertions.assertResponseCode(response, 200);
        Assert.assertFalse(response.jsonPath().getList("products").isEmpty(),
                "Expected at least one product in the response");
    }

    @Test(description = "API 2: POST /api/productsList is unsupported and returns 405")
    @Story("Post To All Products List")
    @Severity(SeverityLevel.NORMAL)
    public void postToProductsList_returnsMethodNotSupported() {
        Response response = given(requestSpec)
                .when()
                .post(Endpoints.PRODUCTS_LIST)
                .then()
                .statusCode(200)
                .extract().response();

        ApiAssertions.assertResponseCode(response, 405);
        ApiAssertions.assertMessage(response, "This request method is not supported.");
    }
}
