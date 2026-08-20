package com.automationexercise.api.pojo;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * automationexercise.com's createAccount/updateAccount endpoints consume
 * application/x-www-form-urlencoded fields, not a JSON body -- toFormParams()
 * mirrors that contract for use with RestAssured's .formParams(...).
 */
public class UserPayload {

    private String name;
    private String email;
    private String password;
    private String title;
    private String birthDate;
    private String birthMonth;
    private String birthYear;
    private String firstname;
    private String lastname;
    private String company;
    private String address1;
    private String address2;
    private String country;
    private String zipcode;
    private String state;
    private String city;
    private String mobileNumber;

    public static Builder builder() {
        return new Builder();
    }

    public Map<String, Object> toFormParams() {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("name", name);
        params.put("email", email);
        params.put("password", password);
        params.put("title", title);
        params.put("birth_date", birthDate);
        params.put("birth_month", birthMonth);
        params.put("birth_year", birthYear);
        params.put("firstname", firstname);
        params.put("lastname", lastname);
        params.put("company", company);
        params.put("address1", address1);
        params.put("address2", address2);
        params.put("country", country);
        params.put("zipcode", zipcode);
        params.put("state", state);
        params.put("city", city);
        params.put("mobile_number", mobileNumber);
        return params;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public static final class Builder {
        private final UserPayload payload = new UserPayload();

        public Builder name(String name) {
            payload.name = name;
            return this;
        }

        public Builder email(String email) {
            payload.email = email;
            return this;
        }

        public Builder password(String password) {
            payload.password = password;
            return this;
        }

        public Builder title(String title) {
            payload.title = title;
            return this;
        }

        public Builder birthDate(String birthDate) {
            payload.birthDate = birthDate;
            return this;
        }

        public Builder birthMonth(String birthMonth) {
            payload.birthMonth = birthMonth;
            return this;
        }

        public Builder birthYear(String birthYear) {
            payload.birthYear = birthYear;
            return this;
        }

        public Builder firstname(String firstname) {
            payload.firstname = firstname;
            return this;
        }

        public Builder lastname(String lastname) {
            payload.lastname = lastname;
            return this;
        }

        public Builder company(String company) {
            payload.company = company;
            return this;
        }

        public Builder address1(String address1) {
            payload.address1 = address1;
            return this;
        }

        public Builder address2(String address2) {
            payload.address2 = address2;
            return this;
        }

        public Builder country(String country) {
            payload.country = country;
            return this;
        }

        public Builder zipcode(String zipcode) {
            payload.zipcode = zipcode;
            return this;
        }

        public Builder state(String state) {
            payload.state = state;
            return this;
        }

        public Builder city(String city) {
            payload.city = city;
            return this;
        }

        public Builder mobileNumber(String mobileNumber) {
            payload.mobileNumber = mobileNumber;
            return this;
        }

        public UserPayload build() {
            return payload;
        }
    }
}
