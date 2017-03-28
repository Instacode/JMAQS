// --------------------------------------------------
// <copyright file="HttpClientWrapper.java" company="Magenic">
// Copyright 2017 Magenic, All rights Reserved
// </copyright>
// <summary>Class contains the Http Client features. To be expanded later.</summary>
// --------------------------------------------------

package com.magenic.jmaqs.webservices.BaseWebServiceTest;

import com.beust.jcommander.internal.Lists;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;

import java.net.URI;
import java.net.URISyntaxException;

public class HttpClientWrapper {
	// The HttpClient to be used
	private CloseableHttpClient baseHttpClient;
	// The URI to be stored for the Web Service
	private URI baseAddress;

	/**
	 * Constructor
	 * 
	 * @param baseAddress
	 *            The base URI address to use
	 * @throws URISyntaxException
	 */
	public HttpClientWrapper(String baseAddress, ContentType mediaType)
			throws URISyntaxException {
		this(new URI(baseAddress), mediaType);
	}

	/**
	 * Constructor
	 * 
	 * @param baseAddress
	 *            The base URI address to use
	 */
	public HttpClientWrapper(URI baseAddress, ContentType mediaType) {
		Header header = new BasicHeader(HttpHeaders.CONTENT_TYPE,
				mediaType.toString());
		this.baseHttpClient = HttpClientBuilder.create()
				.setDefaultHeaders(Lists.newArrayList(header))
				.setDefaultRequestConfig(GetRequestConfig()).build();

		this.baseAddress = baseAddress;
	}

	/**
	 * @return a HttpClient to interact with
	 */
	public HttpClient getHttpClient() {
		return this.baseHttpClient;
	}

	/**
	 * @return A URI containing the Web Service Address
	 */
	public URI getBaseWebServiceAddress() {
		return this.baseAddress;
	}

	/**
	 * Sets the base Web Service address for the HttpClientWrapper
	 * 
	 * @param address
	 *            The string to set the URI to
	 * @throws URISyntaxException
	 *             thrown when the string can't be parsed into a URI object
	 */
	public void setBaseWebServiceAddress(String address)
			throws URISyntaxException {
		this.baseAddress = new URI(address);
	}

	public CloseableHttpResponse GetWithResponse(String requestUri,
			boolean expectSuccess) throws Exception {

		return GetContent(requestUri, expectSuccess);
	}

	protected CloseableHttpResponse GetContent(String requestUri,
			boolean expectSuccess) throws Exception {
		HttpGet newGet = new HttpGet(new URIBuilder(this.baseAddress).setPath(
				requestUri).build());

		CloseableHttpResponse response = this.baseHttpClient.execute(newGet);

		// Should we check for success
		if (expectSuccess) {
			ensureSuccessStatusCode(response);
		}

		return response;
	}

	protected HttpEntity CreateEntity(String contentMessage,
			ContentType contentType) {
		HttpEntity newEntity = new StringEntity(contentMessage, contentType);

		return newEntity;
	}

	protected CloseableHttpResponse PutContent(String requestUri,
			HttpEntity content, boolean expectSuccess) throws Exception {
		HttpPut newPut = new HttpPut(new URIBuilder(this.baseAddress).setPath(
				requestUri).build());
		newPut.setEntity(content);
		CloseableHttpResponse response = this.baseHttpClient.execute(newPut);

		// Should we check for success
		if (expectSuccess) {
			ensureSuccessStatusCode(response);
		}

		return response;
	}

	protected CloseableHttpResponse PostContent(String requestUri,
			HttpEntity content, boolean expectSuccess) throws Exception {
		HttpPost newPost = new HttpPost(new URIBuilder(this.baseAddress)
				.setPath(requestUri).build());
		newPost.setEntity(content);
		CloseableHttpResponse response = this.baseHttpClient.execute(newPost);

		// Should we check for success
		if (expectSuccess) {
			ensureSuccessStatusCode(response);
		}

		return response;
	}

	protected CloseableHttpResponse DeleteContent(String requestUri,
			String returnMediaType, boolean expectSuccess) throws Exception {
		HttpDelete newDelete = new HttpDelete(new URIBuilder(this.baseAddress)
				.setPath(requestUri).build());
		CloseableHttpResponse response = this.baseHttpClient.execute(newDelete);

		// Should we check for success
		if (expectSuccess) {
			ensureSuccessStatusCode(response);
		}

		return response;
	}

	private static void ensureSuccessStatusCode(HttpResponse response)
			throws Exception {
		// Make sure a response was returned
		if (response == null) {
			throw new NullPointerException("Response was null");
		}

		// Check if it was a success and if not create a user friendly error
		// message
		if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
			String body = response.getStatusLine().toString();

			throw new Exception(
					String.format(
							"Response did not indicate a success. %s Response code was: %s ",
							System.lineSeparator(), body));
		}

	}

	private RequestConfig GetRequestConfig() {

		return RequestConfig
				.copy(RequestConfig.DEFAULT)
				.setConnectionRequestTimeout(
						WebServiceConfig.GetWebServiceTimeOut() * 1000)
				.setConnectTimeout(
						WebServiceConfig.GetWebServiceTimeOut() * 1000).build();
	}
}
