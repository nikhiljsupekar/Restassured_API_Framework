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
@Feature("Search Product")
public class SearchProductApiTest extends BaseTest {

    @Test(description = "API 5: POST /api/searchProduct with search_product returns matching products")
    @Story("Search Product")
    @Severity(SeverityLevel.CRITICAL)
    public void searchProduct_withValidParam_returnsMatchingProducts() {
        Response response = given(requestSpec)
                .formParam("search_product", "top")
                .when()
                .post(Endpoints.SEARCH_PRODUCT)
                .then()
                .statusCode(200)
                .extract().response();

        ApiAssertions.assertResponseCode(response, 200);
        Assert.assertFalse(response.jsonPath().getList("products").isEmpty(),
                "Expected at least one matching product");
    }

    @Test(description = "API 6: POST /api/searchProduct without search_product returns 400")
    @Story("Search Product Without Parameter")
    @Severity(SeverityLevel.NORMAL)
    public void searchProduct_withoutParam_returnsBadRequest() {
        Response response = given(requestSpec)
                .when()
                .post(Endpoints.SEARCH_PRODUCT)
                .then()
                .statusCode(200)
                .extract().response();

        ApiAssertions.assertResponseCode(response, 400);
        ApiAssertions.assertMessage(response, "Bad request, search_product parameter is missing in POST request.");
    }
}
