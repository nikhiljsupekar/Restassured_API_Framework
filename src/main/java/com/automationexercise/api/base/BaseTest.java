package com.automationexercise.api.base;

import com.automationexercise.api.config.ConfigManager;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.parsing.Parser;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;

public abstract class BaseTest {

    static {
        // automationexercise.com serves JSON bodies under a text/html Content-Type header,
        // so RestAssured must be told explicitly to parse that content type as JSON.
        RestAssured.registerParser("text/html", Parser.JSON);
    }

    protected RequestSpecification requestSpec;

    @BeforeClass(alwaysRun = true)
    public void setUpBase() {
        RestAssuredConfig config = RestAssuredConfig.config()
                .httpClient(HttpClientConfig.httpClientConfig()
                        .setParam("http.connection.timeout", ConfigManager.connectionTimeout())
                        .setParam("http.socket.timeout", ConfigManager.connectionTimeout()));

        requestSpec = new RequestSpecBuilder()
                .setBaseUri(ConfigManager.baseUri())
                .setConfig(config)
                .addFilter(new AllureRestAssured())
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build();
    }
}
