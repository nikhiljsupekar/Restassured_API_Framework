package com.automationexercise.api.utils;

import com.automationexercise.api.pojo.UserPayload;

public final class TestDataFactory {

    private TestDataFactory() {
    }

    public static String uniqueEmail() {
        return "qa.user." + System.currentTimeMillis() + "@example.com";
    }

    public static UserPayload defaultUser() {
        return defaultUser(uniqueEmail());
    }

    public static UserPayload defaultUser(String email) {
        return UserPayload.builder()
                .name("QA Automation")
                .email(email)
                .password("P@ssw0rd123")
                .title("Mr")
                .birthDate("15")
                .birthMonth("6")
                .birthYear("1995")
                .firstname("QA")
                .lastname("Automation")
                .company("Acme Inc")
                .address1("221B Baker Street")
                .address2("Suite 4")
                .country("United States")
                .zipcode("10001")
                .state("New York")
                .city("New York")
                .mobileNumber("9876543210")
                .build();
    }
}
