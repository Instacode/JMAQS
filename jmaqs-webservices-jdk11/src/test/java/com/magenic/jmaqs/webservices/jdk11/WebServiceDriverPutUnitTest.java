/*
 * Copyright 2020 (C) Magenic, All rights Reserved
 */

package com.magenic.jmaqs.webservices.jdk11;

import com.magenic.jmaqs.utilities.helper.TestCategories;
import com.magenic.jmaqs.webservices.jdk11.models.Product;
import com.magenic.jmaqs.webservices.jdk8.WebServiceConfig;
import com.magenic.jmaqs.webservices.jdk8.MediaType;
import java.io.IOException;
import java.net.URISyntaxException;
import org.springframework.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;
import javax.xml.stream.XMLStreamException;

/**
 * Test web service gets using the base test driver.
 */
public class WebServiceDriverPutUnitTest {
  /**
   * Verify the string status code.
   */
  @Test(groups = TestCategories.WEB_SERVICE)
  public void putJSONSerializedVerifyStatusCode()
      throws URISyntaxException, IOException, InterruptedException {
    Product p = new Product();
    p.setCategory("ff");
    p.setId(4);
    p.setName("ff");
    p.setPrice(3.25);

    var content = WebServiceUtilities.makeStringContent(p, MediaType.APP_JSON);
    WebServiceDriver webServiceDriver = new WebServiceDriver(WebServiceConfig.getWebServiceUri());
    var result = webServiceDriver.putWithResponse("/api/XML_JSON/Put/1", MediaType.APP_JSON, content, true);
    Assert.assertEquals(result.statusCode(), HttpStatus.OK.value());
  }

  /**
   * Put With JSON Type.
   * @throws URISyntaxException if the exception is thrown
   * @throws IOException if the exception is thrown
   * @throws InterruptedException if the exception is thrown
   */
  @Test(groups = TestCategories.WEB_SERVICE)
  public void putJSONWithType()
      throws URISyntaxException, IOException, InterruptedException {
    Product p = new Product();
    p.setCategory("ff");
    p.setId(4);
    p.setName("ff");
    p.setPrice(3.25);

    var content = WebServiceUtilities.makeStringContent(p, MediaType.APP_JSON);
    WebServiceDriver webServiceDriver = new WebServiceDriver(WebServiceConfig.getWebServiceUri());
    var result = webServiceDriver.put("/api/XML_JSON/Put/1", MediaType.APP_JSON, content, true);
    Assert.assertEquals(result, "");
  }

  /**
   * Verify the stream status code.
   * @throws URISyntaxException if the exception is thrown
   * @throws IOException if the exception is thrown
   * @throws InterruptedException if the exception is thrown
   */
  @Test(groups = TestCategories.WEB_SERVICE)
  public void putJSONStringSerializedVerifyStatusCode()
      throws URISyntaxException, IOException, InterruptedException {
    Product p = new Product();
    p.setCategory("ff");
    p.setId(4);
    p.setName("ff");
    p.setPrice(3.25);

    var content = WebServiceUtilities.makeStringContent(p, MediaType.APP_JSON);
    WebServiceDriver webServiceDriver = new WebServiceDriver(WebServiceConfig.getWebServiceUri());
    var result = webServiceDriver.putWithResponse("/api/XML_JSON/Put/1", MediaType.APP_JSON, content, true);
    Assert.assertEquals(result.statusCode(), HttpStatus.OK.value());
  }

  /**
   * XML string verify status code.
   * @throws URISyntaxException if the exception is thrown
   * @throws IOException if the exception is thrown
   * @throws InterruptedException if the exception is thrown
   */
  @Test(groups = TestCategories.WEB_SERVICE)
  public void putXMLSerializedVerifyStatusCode()
      throws URISyntaxException, IOException, InterruptedException {
    Product p = new Product();
    p.setCategory("ff");
    p.setId(4);
    p.setName("ff");
    p.setPrice(3.25);

    var content = WebServiceUtilities.makeStringContent(p, MediaType.APP_XML);
    WebServiceDriver webServiceDriver = new WebServiceDriver(WebServiceConfig.getWebServiceUri());
    var result = webServiceDriver.putWithResponse("/api/XML_JSON/Put/1", MediaType.APP_XML, content, true);
    Assert.assertEquals(result.statusCode(), HttpStatus.OK.value());
  }

  /**
   * XML stream verify status code.
   * @throws URISyntaxException if the exception is thrown
   * @throws IOException if the exception is thrown
   * @throws InterruptedException if the exception is thrown
   */
  @Test(groups = TestCategories.WEB_SERVICE)
  public void putXMLStringSerializedVerifyStatusCode()
      throws URISyntaxException, IOException, InterruptedException {
    Product p = new Product();
    p.setCategory("ff");
    p.setId(4);
    p.setName("ff");
    p.setPrice(3.25);

    var content = WebServiceUtilities.makeStringContent(p, MediaType.APP_XML);
    WebServiceDriver webServiceDriver = new WebServiceDriver(WebServiceConfig.getWebServiceUri());
    var result = webServiceDriver.putWithResponse("/api/XML_JSON/Put/1",
        MediaType.APP_XML, content, true);
    Assert.assertEquals(result.statusCode(), HttpStatus.OK.value());
  }

  /**
   * Verify put returns an empty string.
   * @throws URISyntaxException if the exception is thrown
   * @throws IOException if the exception is thrown
   * @throws InterruptedException if the exception is thrown
   */
  @Test(groups = TestCategories.WEB_SERVICE)
  public void putXMLSerializedVerifyEmptyString()
      throws URISyntaxException, IOException, InterruptedException {
    Product p = new Product();
    p.setCategory("ff");
    p.setId(4);
    p.setName("ff");
    p.setPrice(3.25);

    var content = WebServiceUtilities.makeStringContent(p, MediaType.APP_XML);
    WebServiceDriver webServiceDriver = new WebServiceDriver(WebServiceConfig.getWebServiceUri());
    var result = webServiceDriver.put("/api/XML_JSON/Put/1", MediaType.APP_XML, content,true);
    Assert.assertEquals(result, "");
  }

  /**
   * String without using the utility.
   * @throws URISyntaxException if the exception is thrown
   * @throws IOException if the exception is thrown
   * @throws InterruptedException if the exception is thrown
   */
  @Test(groups = TestCategories.WEB_SERVICE)
  public void putStringWithoutMakeContent()
      throws URISyntaxException, IOException, InterruptedException {
    WebServiceDriver webServiceDriver = new WebServiceDriver(WebServiceConfig.getWebServiceUri());
    var result = webServiceDriver.put("/api/String/Put/1", MediaType.PLAIN_TEXT, "Test",
        MediaType.PLAIN_TEXT, HttpStatus.OK, true);
    Assert.assertEquals(result, "");
  }

  /**
   * String using the utility.
   * @throws URISyntaxException if the exception is thrown
   * @throws IOException if the exception is thrown
   * @throws InterruptedException if the exception is thrown
   */
  @Test(groups = TestCategories.WEB_SERVICE)
  public void putStringWithMakeStringContent()
      throws URISyntaxException, IOException, InterruptedException {
    var content = WebServiceUtilities.makeStringContent("Test", MediaType.PLAIN_TEXT);
    WebServiceDriver webServiceDriver = new WebServiceDriver(WebServiceConfig.getWebServiceUri());
    var result = webServiceDriver.put("/api/String/Put/1", MediaType.PLAIN_TEXT, content, true);
    Assert.assertEquals(result, "");
  }

  /**
   * Put string without utility.
   * @throws URISyntaxException if the exception is thrown
   * @throws IOException if the exception is thrown
   * @throws InterruptedException if the exception is thrown
   */
  @Test(groups = TestCategories.WEB_SERVICE)
  public void putStringWithoutContentStatusCode()
      throws URISyntaxException, IOException, InterruptedException {
    WebServiceDriver webServiceDriver = new WebServiceDriver(WebServiceConfig.getWebServiceUri());
    var result = webServiceDriver.putWithResponse("/api/String/Put/1", MediaType.PLAIN_TEXT,
        "Test", MediaType.PLAIN_TEXT, true, true);
    Assert.assertEquals(result.statusCode(), HttpStatus.OK.value());
  }

  /**
   * Put string with utility.
   * @throws URISyntaxException if the exception is thrown
   * @throws IOException if the exception is thrown
   * @throws InterruptedException if the exception is thrown
   */
  @Test(groups = TestCategories.WEB_SERVICE)
  public void putStringMakeContentStatusCode()
      throws URISyntaxException, IOException, InterruptedException {
    var content = WebServiceUtilities.makeStringContent("Test", MediaType.PLAIN_TEXT);
    WebServiceDriver webServiceDriver = new WebServiceDriver(WebServiceConfig.getWebServiceUri());
    var result = webServiceDriver.putWithResponse("/api/String/Put/1", MediaType.PLAIN_TEXT, content, true);
    Assert.assertEquals(result.statusCode(), HttpStatus.OK.value());
  }

  /**
   * Test other status codes.
   * @throws URISyntaxException if the exception is thrown
   * @throws IOException if the exception is thrown
   * @throws InterruptedException if the exception is thrown
   */
  @Test(groups = TestCategories.WEB_SERVICE)
  public void putExpectContentError() throws URISyntaxException, IOException, InterruptedException {
    WebServiceDriver webServiceDriver = new WebServiceDriver(WebServiceConfig.getWebServiceUri());
    var result = webServiceDriver.putWithResponse("/api/String/Put/1", MediaType.APP_JSON,
        "", false);
    Assert.assertEquals(result.statusCode(), HttpStatus.CONFLICT.value());
  }

  /**
   * Test string response.
   * @throws URISyntaxException if the exception is thrown
   * @throws IOException if the exception is thrown
   * @throws InterruptedException if the exception is thrown
   */
  @Test(groups = TestCategories.WEB_SERVICE)
  public void putExpectStringError() throws URISyntaxException, IOException, InterruptedException {
    WebServiceDriver webServiceDriver = new WebServiceDriver(WebServiceConfig.getWebServiceUri());
    var result = webServiceDriver.put("/api/String/Put/1", MediaType.APP_JSON, "", false);
    Assert.assertEquals(result, "{\"Message\":\"No Product found for name = 1 \"}");
  }
}
