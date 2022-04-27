package com.tampm.todolist;

//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RegistrationBrowserTest {
//    private static HtmlUnitDriver browser;

    @LocalServerPort
    private int port;

    @Autowired
    TestRestTemplate rest;

//    @BeforeAll
//    public static void setup() {
//        browser = new HtmlUnitDriver();
//        browser.manage().timeouts()
//                .implicitlyWait(10, TimeUnit.SECONDS);
//    }
//
//    @AfterAll
//    public static void closeBrowser() {
//        browser.quit();
//    }

//    @Test
//    public void testRegistration() {
//        browser.findElementByLinkText("here").click();
//        assertEquals(registrationPageUrl(), browser.getCurrentUrl());
//        browser.findElementByName("username").sendKeys("test_user");
//        browser.findElementByName("password").sendKeys("test_password");
//        browser.findElementByName("confirm").sendKeys("test_password");
//        browser.findElementByName("fullName").sendKeys("Test Tester");
//    }

    private String homePageUrl() {
        return "http://localhost:" + port + "/";
    }

    private String registrationPageUrl() {
        return homePageUrl() + "register";
    }
}
