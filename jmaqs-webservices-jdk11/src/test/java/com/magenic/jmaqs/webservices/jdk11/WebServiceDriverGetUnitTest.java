/*
 * Copyright 2021 (C) Magenic, All rights Reserved
 */

package com.magenic.jmaqs.webservices.jdk11;

import com.magenic.jmaqs.utilities.helper.TestCategories;
import com.magenic.jmaqs.webservices.jdk11.models.Product;
import com.magenic.jmaqs.webservices.jdk8.BaseWebServiceTest;
import com.magenic.jmaqs.webservices.jdk8.MediaType;
import com.magenic.jmaqs.webservices.jdk8.WebServiceConfig;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;

import org.springframework.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test web service gets.
 */
public class WebServiceDriverGetUnitTest extends BaseWebServiceTest {
  /**
   * String to hold the URL.
   */
  private static String baseUrl = WebServiceConfig.getWebServiceUri();

  /**
   * Test Json Get deserialize a single product.
   * @throws IOException if exception is thrown
   * @throws InterruptedException if exception is thrown
   */
  @Test(groups = TestCategories.WEB_SERVICE)
  public void getProductXmlDeserialize() throws IOException, InterruptedException {
    WebServiceDriver client = new WebServiceDriver(HttpClientFactory.getDefaultClient());
    HttpResponse<String> response = client.get(baseUrl + "/api/XML_JSON/GetProduct/2", MediaType.APP_XML, false);
    Product products = WebServiceUtilities.getResponseBody(response, MediaType.APP_XML, Product.class);
    Assert.assertEquals(products.getName(), "Yo-yo", "Expected 3 products to be returned");
  }

  /**
   * Test XML get all items.
   * @throws IOException if exception is thrown
   * @throws InterruptedException if exception is thrown
   */
  @Test(groups = TestCategories.WEB_SERVICE)
  public void getProductsXmlDeserialize() throws IOException, InterruptedException {
    WebServiceDriver client = new WebServiceDriver(HttpClientFactory.getDefaultClient());
    Product[] products = client.getContent(baseUrl + "/api/XML_JSON/GetAllProducts", MediaType.APP_XML, false, Product[].class);
    Assert.assertEquals(products.length, 3, "Expected 3 products to be returned");
  }

  /**
   * Test Json Get deserialize a single product.
   * @throws IOException if exception is thrown
   * @throws InterruptedException if exception is thrown
   */
  @Test(groups = TestCategories.WEB_SERVICE)
  public void getProductJsonDeserialize() throws IOException, InterruptedException {
    WebServiceDriver client = new WebServiceDriver(HttpClientFactory.getDefaultClient());
    HttpResponse<String> response = client.getContent(baseUrl + "/api/XML_JSON/GetProduct/2", MediaType.APP_JSON, false);
    Product products = WebServiceUtilities.getResponseBody(response, MediaType.APP_JSON, Product.class);
    Assert.assertEquals(products.getName(),"Yo-yo", "Expected 3 products to be returned");
  }

  /**
   * Test Json Get deserialize multiple products.
   * @throws IOException if exception is thrown
   * @throws InterruptedException if exception is thrown
   */
  @Test(groups = TestCategories.WEB_SERVICE)
  public void getProductsJsonDeserialize() throws IOException, InterruptedException {
    WebServiceDriver client = new WebServiceDriver(HttpClientFactory.getDefaultClient());
    Product[] products = client.get(baseUrl + "/api/XML_JSON/GetAllProducts", MediaType.APP_JSON, false, Product[].class);
    Assert.assertEquals(products.length, 3, "Expected 3 products to be returned");
  }

  /**
   * Test string Get all.
   * @throws IOException if exception is thrown
   * @throws InterruptedException if exception is thrown
   */
  @Test(groups = TestCategories.WEB_SERVICE)
  public void getProductsPlainText() throws IOException, InterruptedException {
    WebServiceDriver client = new WebServiceDriver(HttpClientFactory.getDefaultClient());
    HttpResponse<String> result = client.get(baseUrl + "/api/String/Get",  MediaType.PLAIN_TEXT, false);
    Assert.assertTrue(result.body().contains("Tomato Soup"),
        "Was expecting a result with Tomato Soup but instead got - " + result);
    Assert.assertTrue(result.body().contains("Yo-yo"),
        "Was expecting a result with Yo-yo but instead got - " + result);
    Assert.assertTrue(result.body().contains("Hammer"),
        "Was expecting a result with Hammer but instead got - " + result);
  }

  /**
   * Test string Get by id.
   * @throws IOException if exception is thrown
   * @throws InterruptedException if exception is thrown
   */
  @Test(groups = TestCategories.WEB_SERVICE)
  public void getStringById() throws IOException, InterruptedException {
    WebServiceDriver client = new WebServiceDriver(HttpClientFactory.getDefaultClient());
    HttpResponse<String> result = client.get(baseUrl + "/api/String/1",  MediaType.PLAIN_TEXT, false);
    Assert.assertTrue(result.body().contains("Tomato Soup"),
        "Was expecting a result with Tomato Soup but instead got - " + result);
  }

  /**
   * Test string Get by id.
   * @throws IOException if exception is thrown
   * @throws InterruptedException if exception is thrown
   */
  @Test(groups = TestCategories.WEB_SERVICE)
  public void getStringByName() throws IOException, InterruptedException {
    WebServiceDriver client = new WebServiceDriver(HttpClientFactory.getDefaultClient());
    HttpResponse<String> result = client.get(baseUrl + "/api/String/Yo-yo",  MediaType.PLAIN_TEXT, false);
    Assert.assertTrue(result.body().contains("Yo-yo"),
        "Was expecting a result with Yo-yo but instead got - " + result);
  }

  /**
   * Test that we can use the web service utility to deserialize JSON.
   * @throws IOException if exception is thrown
   * @throws InterruptedException if exception is thrown
   */
  @Test(groups = TestCategories.WEB_SERVICE)
  public void getResponseAndDeserializeJson()
      throws IOException, InterruptedException {
    WebServiceDriver client = new WebServiceDriver(HttpClientFactory.getDefaultClient());
    HttpResponse<String> message = client.get(baseUrl + "/api/XML_JSON/GetAllProducts",
       MediaType.APP_JSON, true  );
    Product[] products = WebServiceUtilities.deserializeJson(message, Product[].class);
    Assert.assertEquals(products.length, 3, "Expected 3 products to be returned");
  }

  /**
   * Test that we can use the web service utility to deserialize XML.
   * @throws IOException if exception is thrown
   * @throws InterruptedException if exception is thrown
   */
  @Test(groups = TestCategories.WEB_SERVICE)
  public void getResponseAndDeserializeXml()
      throws IOException, InterruptedException {
    WebServiceDriver client = new WebServiceDriver(HttpClientFactory.getDefaultClient());
    HttpResponse<String> message = client.get(baseUrl + "/api/XML_JSON/GetAllProducts",
        MediaType.APP_XML, true);
    Product[] products = WebServiceUtilities.deserializeXml(message, Product[].class);
    Assert.assertEquals(products.length,3,"Expected 3 products to be returned");
  }

  /**
   * Test that we can use the web service utility to deserialize JSON and have an expected Status.
   * @throws IOException if exception is thrown
   * @throws InterruptedException if exception is thrown
   */
  @Test(groups = TestCategories.WEB_SERVICE)
  public void getResponseAndDeserializeJsonExpectedStatus()
      throws IOException, InterruptedException {
    WebServiceDriver client = new WebServiceDriver(HttpClientFactory.getDefaultClient());
    HttpResponse<String> message = client.get(baseUrl + "/api/XML_JSON/GetAllProducts",
        MediaType.APP_JSON, HttpStatus.OK);
    Product[] products = WebServiceUtilities.deserializeJson(message, Product[].class);
    Assert.assertEquals(products.length, 3, "Expected 3 products to be returned");
  }

  /**
   * Test that we can use the web service utility to deserialize XML and have an expected Status.
   * @throws IOException if exception is thrown
   * @throws InterruptedException if exception is thrown
   */
  @Test(groups = TestCategories.WEB_SERVICE)
  public void getResponseAndDeserializeXmlExpectedStatus()
      throws IOException, InterruptedException {
    WebServiceDriver client = new WebServiceDriver(HttpClientFactory.getDefaultClient());
    HttpResponse<String> message = client.get(baseUrl + "/api/XML_JSON/GetAllProducts",
        MediaType.APP_XML, HttpStatus.OK);
    Product[] products = WebServiceUtilities.deserializeXml(message, Product[].class);
    Assert.assertEquals(products.length,3,"Expected 3 products to be returned");
  }
}