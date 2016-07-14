package com.cnu2016.assignment04.Controllers;

import com.cnu2016.assignment04.Application;
import com.cnu2016.assignment04.Models.*;
import com.cnu2016.assignment04.Repositories.ProductRepository;
import com.jayway.restassured.RestAssured;
import org.apache.http.HttpStatus;
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

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:8080")
@TestPropertySource("classpath:test.properties")
public class ProductControllerTest {

    @Autowired
    ProductRepository productRepository;

    private product testProductA;

    @Before
    public void setUp() {
        RestAssured.port = 8080;
        testProductA = new product();
        testProductA = productRepository.save(testProductA);
    }

    @After
    public void tearDown() {
        productRepository.delete(testProductA.getId());
    }


    @Test
    public void listAllProductsTest() {
        RestAssured.when()
                .get("/api/products").
                then().
                statusCode(HttpStatus.SC_OK);
    }


    @Test
    public void listOneProductTest() {
        RestAssured.when()
                .get("/api/products/{id}", testProductA.getId()).
                then().
                statusCode(HttpStatus.SC_OK);

        RestAssured.when()
                .get("/api/products/{id}", 0).
                then().
                statusCode(HttpStatus.SC_NOT_FOUND);

    }

    @Test
    public void postProductTest() {

        RestAssured.given().contentType("application/json")
                .body("{\"code\":\"testProduct\",\"description\":\"testProduct\",\"productName\":\"testProduct\",\"buyPrice\":100,\"quantityInStock\":100,\"categoryId\":1,\"enabled\":1}")
                .when().
                post("/api/products").
                then().
                statusCode(HttpStatus.SC_CREATED);

        RestAssured.given().contentType("application/json")
                .body("{\"buyPrice\": 100.00}")
                .when().
                post("/api/products").
                then().
                statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void putProductTest() {

        RestAssured.given().contentType("application/json")
                .body("{\"code\":\"testProductChanged\"}")
                .when().
                put("/api/products/{id}", testProductA.getId()).
                then().
                statusCode(HttpStatus.SC_OK);

        product testProductGet = productRepository.findOne(testProductA.getId());
        Assert.assertEquals(testProductGet.getCode(), "testProductChanged");

        RestAssured.given().contentType("application/json")
                .body("{\"buyPrice\": 100.00}")
                .when().
                put("/api/products/{id}", testProductA.getId()).
                then().
                statusCode(HttpStatus.SC_NOT_FOUND);


        RestAssured.given().contentType("application/json")
                .body("{\"code\":\"testProductChanged\"}")
                .when().
                put("/api/products/{id}", 0).
                then().
                statusCode(HttpStatus.SC_NOT_FOUND);

    }

    @Test
    public void patchProductTest() {

        testProductA.setDescription("testDescription");
        testProductA = productRepository.save(testProductA);

        RestAssured.given().contentType("application/json")
                .body("{\"code\":\"testProductChanged\"}")
                .when().
                patch("/api/products/{id}", testProductA.getId()).
                then().
                statusCode(HttpStatus.SC_OK);

        product testProductGet = productRepository.findOne(testProductA.getId());
        Assert.assertEquals(testProductGet.getCode(), "testProductChanged");
        Assert.assertEquals(testProductGet.getDescription(), "testDescription");

        RestAssured.given().contentType("application/json")
                .body("{\"code\":\"testProductChanged\",\"description\":\"updatedDescription\"}")
                .when().
                patch("/api/products/{id}", testProductA.getId()).
                then().
                statusCode(HttpStatus.SC_OK);

        testProductGet = productRepository.findOne(testProductA.getId());
        Assert.assertEquals(testProductGet.getCode(), "testProductChanged");
        Assert.assertEquals(testProductGet.getDescription(), "updatedDescription");

        RestAssured.given().contentType("application/json")
                .body("{\"code\":\"testProductChanged\",\"description\":\"updatedDescription\"}")
                .when().
                patch("/api/products/{id}", 0).
                then().
                statusCode(HttpStatus.SC_NOT_FOUND);

        RestAssured.given().contentType("application/json")
                .body("{\"code\":\"testProduct\",\"description\":\"testProduct\",\"productName\":\"testProduct\",\"buyPrice\":100,\"quantityInStock\":100,\"categoryId\":1,\"enabled\":1}")
                .when().
                patch("/api/products/{id}", testProductA.getId()).
                then().
                statusCode(HttpStatus.SC_OK);

    }

    @Test
    public void deleteProductTest() {

        RestAssured.when()
                .delete("/api/products/{id}", testProductA.getId()).
                then().
                statusCode(HttpStatus.SC_OK);

        RestAssured.when()
                .get("/api/products/{id}", testProductA.getId()).
                then().
                statusCode(HttpStatus.SC_NOT_FOUND);

    }

}