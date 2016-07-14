package com.cnu2016.assignment04.Controllers;

import com.cnu2016.assignment04.Application;
import com.cnu2016.assignment04.Models.*;
import com.cnu2016.assignment04.Repositories.OrderLineRepository;
import com.cnu2016.assignment04.Repositories.OrderRepository;
import com.cnu2016.assignment04.Repositories.ProductRepository;
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
public class OrderControllerTest {

    @Autowired
    OrderRepository orderRepository;
    
    @Autowired
    ProductRepository productRepository;
    
    @Autowired
    OrderLineRepository orderLineRepository;

    private order testOrderA;

    @Before
    public void setUp() {
        RestAssured.port = 8080;
        testOrderA = new order();
        testOrderA = orderRepository.save(testOrderA);
    }

    @After
    public void tearDown() {
        orderRepository.delete(testOrderA.getOrderId());
    }


    @Test
    public void listAllOrdersTest() {
        RestAssured.when()
                .get("/api/orders").
                then().
                statusCode(HttpStatus.SC_OK);
    }


    @Test
    public void listOneOrderTest() {
        RestAssured.when()
                .get("/api/orders/{id}", testOrderA.getOrderId()).
                then().
                statusCode(HttpStatus.SC_OK);

        RestAssured.when()
                .get("/api/orders/{id}", 0).
                then().
                statusCode(HttpStatus.SC_NOT_FOUND);

    }

    @Test
    public void createOrderTest() {

        String orderId =
            RestAssured.when()
                .post("/api/orders")
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .extract().path("id").toString();

//        System.out.println(orderId);

    }

    @Test
    public void addProductTestWrongFields() {

        String orderId = RestAssured.when()
                .post("/api/orders")
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .extract().path("id").toString();
        Integer id = Integer.parseInt(orderId);

        /* Not giving the specified fields */
        RestAssured.given()
                .contentType("application/json")
                .body("{}")
                .when()
                .post("/api/orders/{id}/orderLineItem", id)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void addProductTestMoreThanStock() {

        /* Adding more quantity than in stock */

        String orderId = RestAssured.when()
                .post("/api/orders")
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .extract().path("id").toString();

        Integer id = Integer.parseInt(orderId);

        product tempProduct = new product();
        tempProduct.setQuantityInStock(99);
        tempProduct = productRepository.save(tempProduct);
        Integer productId = tempProduct.getId();

        RestAssured.given()
                .contentType("application/json")
                .body("{\"product_id\":" + productId + ",\"qty\":\"100\"}")
                .when()
                .post("/api/orders/{id}/orderLineItem", id)
                .then()
                .statusCode(HttpStatus.SC_CREATED);
    }

    @Test
    public void addProductTestWrongIdCheck() {

        /* Wrong product_id check */

        String orderId = RestAssured.when()
                .post("/api/orders")
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .extract().path("id").toString();

        Integer id = Integer.parseInt(orderId);
        product tempProduct = new product();
        tempProduct.setQuantityInStock(99);
        tempProduct = productRepository.save(tempProduct);
        Integer productId = tempProduct.getId();

        RestAssured.given()
                .contentType("application/json")
                .body("{\"product_id\":\"wrongInput\",\"qty\":\"100\"}")
                .when()
                .post("/api/orders/{id}/orderLineItem", id)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void addProductTestWrongIdTest2() {

        /* Wrong order_id test */
        product tempProduct = new product();
        tempProduct.setQuantityInStock(99);
        tempProduct = productRepository.save(tempProduct);
        Integer productId = tempProduct.getId();

        RestAssured.given()
                .contentType("application/json")
                .body("{\"product_id\":" + productId + ",\"qty\":\"100\"}")
                .when()
                .post("/api/orders/{id}/orderLineItem", "wrongInput")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);

        productRepository.delete(productId);
        //orderRepository.delete(id);
    }

    @Test
    public void EmptyUserCheckOutTest() {

        String orderId = RestAssured.when()
                .post("/api/orders")
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .extract().path("id").toString();

        Integer id = Integer.parseInt(orderId);
        product tempProduct2 = new product();
        tempProduct2.setQuantityInStock(99);
        tempProduct2.setCode("testProductCheckout");
        tempProduct2 = productRepository.save(tempProduct2);
        Integer productId2 = tempProduct2.getId();

        System.out.println("productId=" + productId2);

        RestAssured.given()
                .contentType("application/json")
                .body("{\"product_id\":" + productId2 + ",\"qty\":\"98\"}")
                .when()
                .post("/api/orders/{id}/orderLineItem", id)
                .then()
                .statusCode(HttpStatus.SC_CREATED);

        RestAssured.given()
                .contentType("application/json")
                .when()
                .patch("/api/orders/{id}", id)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);

    }

    @Test
    public void perfectCheckOutTest() {

        String orderId = RestAssured.when()
                .post("/api/orders")
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .extract().path("id").toString();

        Integer id = Integer.parseInt(orderId);
        product tempProduct2 = new product();
        tempProduct2.setQuantityInStock(99);
        tempProduct2.setCode("testProductCheckout");
        tempProduct2 = productRepository.save(tempProduct2);
        Integer productId2 = tempProduct2.getId();

        System.out.println("productId=" + productId2);

        RestAssured.given()
                .contentType("application/json")
                .body("{\"product_id\":" + productId2 + ",\"qty\":\"98\"}")
                .when()
                .post("/api/orders/{id}/orderLineItem", id)
                .then()
                .statusCode(HttpStatus.SC_CREATED);

        RestAssured.given()
                .contentType("application/json")
                .body("{\"status\":\"checkout\",\"user_name\":\"testUser\",\"address\":\"Dubai\"}")
                .when()
                .patch("/api/orders/{id}", id)
                .then()
                .statusCode(HttpStatus.SC_OK);

    }

    @Test
    public void checkoutTestMoreThanStock() {

        String orderId = RestAssured.when()
                .post("/api/orders")
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .extract().path("id").toString();

        Integer id = Integer.parseInt(orderId);

        System.out.println("orderId=" + id);

        /*Checkout more than in quantity */

        product tempProduct = new product();
        tempProduct.setQuantityInStock(99);
        tempProduct = productRepository.save(tempProduct);
        Integer productId = tempProduct.getId();

        RestAssured.given()
                .contentType("application/json")
                .body("{\"product_id\":" + productId + ",\"qty\":\"100\"}")
                .when()
                .post("/api/orders/{id}/orderLineItem", id)
                .then()
                .statusCode(HttpStatus.SC_CREATED);

        RestAssured.given()
                .contentType("application/json")
                .body("{\"status\":\"checkout\",\"user_name\":\"testUser\",\"address\":\"Dubai\"}")
                .when()
                .patch("/api/orders/{id}", id)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void addProductTestWrongFields2() {


        String orderId = RestAssured.when()
                .post("/api/orders")
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .extract().path("id").toString();

        Integer id = Integer.parseInt(orderId);

        product tempProduct2 = new product();
        tempProduct2.setQuantityInStock(99);
        tempProduct2.setCode("testProductCheckout");
        tempProduct2 = productRepository.save(tempProduct2);
        Integer productId2 = tempProduct2.getId();

        System.out.println("productId=" + productId2);

        RestAssured.given()
                .contentType("application/json")
                .body("{\"product_id\":" + productId2 + ",\"qty\":\"98\"}")
                .when()
                .post("/api/orders/{id}/orderLineItem", id)
                .then()
                .statusCode(HttpStatus.SC_CREATED);


        /*User name not given */
        RestAssured.given()
                .contentType("application/json")
                .body("{\"status\":\"checkout\",\"address\":\"Dubai\"}")
                .when()
                .patch("/api/orders/{id}", id)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);

        /* Address not given */
        RestAssured.given()
                .contentType("application/json")
                .body("{\"status\":\"checkout\",\"user_name\":\"testUser\"}")
                .when()
                .patch("/api/orders/{id}", id)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);

        /*Status not given */
        RestAssured.given()
                .contentType("application/json")
                .body("{\"user_name\":\"testUser\",\"address\":\"Dubai\"}")
                .when()
                .patch("/api/orders/{id}", id)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);

    }


    @Test
    public void deleteOrderTest() {

        RestAssured.when()
                .delete("/api/orders/{id}", testOrderA.getOrderId()).
                then().
                statusCode(HttpStatus.SC_OK);

        RestAssured.when()
                .delete("/api/orders/{id}", testOrderA.getOrderId()).
                then().
                statusCode(HttpStatus.SC_NOT_FOUND);

    }

}