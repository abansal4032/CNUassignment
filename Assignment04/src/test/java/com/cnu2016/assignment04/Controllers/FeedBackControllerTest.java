package com.cnu2016.assignment04.Controllers;

import com.cnu2016.assignment04.Application;
import com.cnu2016.assignment04.Models.*;
import com.cnu2016.assignment04.Repositories.FeedbackRepository;
import com.jayway.restassured.RestAssured;
import org.apache.http.HttpStatus;
import static org.hamcrest.Matchers.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import com.jayway.restassured.path.json.JsonPath.*;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:8080")
@TestPropertySource("classpath:test.properties")
public class FeedBackControllerTest {

    @Autowired
    FeedbackRepository feedbackRepository;

    @Test
    public void contactUsTest() {
        RestAssured.given()
                .contentType("application/json")
                .body("{\"message\":\"test call\"}")
                .when()
                .post("/api/contactus")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }
}