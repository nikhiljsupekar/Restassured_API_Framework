package com.automationexercise.api.utils;

import io.restassured.response.Response;
import org.testng.Assert;

/**
 * automationexercise.com always returns HTTP 200 and encodes the real status
 * in a JSON "responseCode" field, served under a text/html Content-Type.
 * RestAssured's body(path, matcher) DSL resolves JSON vs XML from the
 * Content-Type header, and that resolution breaks once any logging filter
 * has touched the response stream -- so assertions here read the body
 * through jsonPath() explicitly instead, which always parses as JSON
 * regardless of the declared content type.
 */
public final class ApiAssertions {

    private ApiAssertions() {
    }

    public static void assertResponseCode(Response response, int expected) {
        int actual = response.jsonPath().getInt("responseCode");
        Assert.assertEquals(actual, expected, "Unexpected responseCode. Body: " + response.asString());
    }

    public static void assertMessage(Response response, String expected) {
        String actual = response.jsonPath().getString("message");
        Assert.assertEquals(actual, expected, "Unexpected message. Body: " + response.asString());
    }
}
