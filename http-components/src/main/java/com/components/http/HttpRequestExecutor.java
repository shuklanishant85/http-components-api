package com.components.http;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.components.constants.Constants;
import com.components.model.Employee;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

public class HttpRequestExecutor {

	private static final Log LOGGER = LogFactory.getLog(HttpRequestExecutor.class);

	/**
	 * This method creates the simplest form of a request-response channel.
	 * 
	 * @param requestURI
	 */
	public void createSimpleRequest(URI requestURI) {
		HttpGet get = new HttpGet(requestURI);
		try (CloseableHttpClient client = HttpClients.createDefault();) {
			CloseableHttpResponse response = client.execute(get);
			String jsonResponse = EntityUtils.toString(response.getEntity());
			String json = printResponseAsJson(jsonResponse);
			LOGGER.info("json response : \n " + json);
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
		}
	}

	private String printResponseAsJson(String jsonResponse) {
		JsonParser parser = new JsonParser();
		JsonArray jsonArray = parser.parse(jsonResponse).getAsJsonArray();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(jsonArray);
		return json;
	}

	/**
	 * @return
	 */
	public static URI createSimpleRequestURI() {
		URI requestURI = null;
		try {
			requestURI = new URIBuilder().setScheme(Constants.Request.HTTP).setHost(Constants.Request.HOST)
					.setPort(Constants.Request.PORT).setPath(Constants.Request.PATH).build();
		} catch (URISyntaxException e) {
			LOGGER.error(e.getMessage());
		}
		return requestURI;
	}

	/**
	 * @return
	 */
	public HttpResponse createSimpleResponse() {
		return new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");
	}

	/**
	 * @return
	 */
	public HttpResponse addHeaderToResponse() {
		LOGGER.info("into method : addHeaderToResponse");
		HttpResponse response = createSimpleResponse();
		response.addHeader(Constants.Headers.SET_COOKIE, "c1=a; path=/; domain=localhost");
		response.addHeader(Constants.Headers.SET_COOKIE, "c2=b; path=\"/\", c3=c; domain=\"localhost\"");
		HeaderIterator iterator = response.headerIterator();
		while (iterator.hasNext()) {
			LOGGER.info("header: " + iterator.next());
		}
		return response;
	}

	/**
	 * 
	 */
	public void displayHeaders() {
		LOGGER.info("into method : displayHeaders");
		HttpResponse response = addHeaderToResponse();
		HeaderElementIterator headerElementIterator = new BasicHeaderElementIterator(
				response.headerIterator(Constants.Headers.SET_COOKIE));
		while (headerElementIterator.hasNext()) {
			HeaderElement element = headerElementIterator.nextElement();
			NameValuePair[] params = element.getParameters();
			LOGGER.info("header elements with key : Set-Cookie");
			for (NameValuePair param : params) {
				LOGGER.info(" " + param);
			}
		}

	}

	public void createSimpleEntity() {
		try {
			StringEntity entity = new StringEntity("Message", ContentType.create("Text/plain", "UTF-8"));
			entity.setContentEncoding("UTF-8");
			LOGGER.info("content-type : " + entity.getContentType());
			LOGGER.info("content-length : " + entity.getContentLength());
			LOGGER.info("content-encoding : " + entity.getContentEncoding());
			LOGGER.info("content-message : " + EntityUtils.toString(entity));
			LOGGER.info("content-length : " + EntityUtils.toByteArray(entity).length);
		} catch (ParseException | IOException e) {
			LOGGER.error(e.getMessage());
		}
	}

	/**
	 * 
	 */
	public void releaseResourcesByClosingResponse() {
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet get = new HttpGet(createSimpleRequestURI());
		CloseableHttpResponse response = null;
		try (OutputStream out = new FileOutputStream(new File("output.txt"));) {
			response = client.execute(get);
			response.getEntity().writeTo(out);
			out.flush();
			EntityUtils.consume(response.getEntity());
			LOGGER.info("response written into file");
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
		} finally {
			try {
				if (null != response) {
					response.close();
				}
			} catch (IOException e) {
				LOGGER.error(e.getMessage());
			}
		}
	}

	/**
	 * 
	 */
	public void useEntityUtislToConsume() {
		try {
			CloseableHttpClient client = HttpClients.createDefault();
			HttpGet get = new HttpGet(createSimpleRequestURI());
			CloseableHttpResponse response = client.execute(get);
			HttpEntity entity = response.getEntity();
			if (entity.getContentLength() != -1 && entity.getContentLength() < 2048) {
				LOGGER.info(EntityUtils.toString(entity));
				LOGGER.info("file logged sucessfully using EntityUtils");
			} else {
				try (OutputStream out = new FileOutputStream("output2.txt")) {
					entity.writeTo(out);
					LOGGER.info("file written sucessfully using output stream");
				}
			}
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
		}
	}

	/**
	 * 
	 */
	public void bufferEntityData() {

		try (CloseableHttpClient client = HttpClients.createDefault();) {
			HttpGet request = new HttpGet(createSimpleRequestURI());
			CloseableHttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();
			if (null != entity) {
				entity = new BufferedHttpEntity(entity);
			}
			LOGGER.info("first read : " + EntityUtils.toString(entity));
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				LOGGER.error(e.getMessage());
				Thread.currentThread().interrupt();
			}
			LOGGER.info("second read : " + EntityUtils.toString(entity));
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
		}
	}

	/**
	 * @return
	 */
	public HttpRequest sendRequestEntity() {
		File file = new File("output.txt");
		FileEntity entity = new FileEntity(file, ContentType.APPLICATION_JSON);
		HttpPost post = new HttpPost(createSimpleRequestURI());
		post.setEntity(entity);
		return post;
	}

	/**
	 * @return
	 */
	public HttpRequest simulateHTMLForms() {
		List<NameValuePair> formParams = new ArrayList<>();
		formParams.add(new BasicNameValuePair("username", "nishant"));
		formParams.add(new BasicNameValuePair("password", "nishant-shukla"));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formParams, StandardCharsets.UTF_8);
		entity.setChunked(true); // can change stream to chunk coding
		HttpPost post = new HttpPost(createSimpleRequestURI());
		post.setEntity(entity);
		return post;
	}

	/**
	 * 
	 */
	public void handleResponseViaResponseHandler() {
		HttpGet request = new HttpGet(createSimpleRequestURI());
		try (CloseableHttpClient client = HttpClients.createDefault()){
		ResponseHandler<Employee[]> responseHandler = new ResponseHandler<Employee[]>() {

			@Override
			public Employee[] handleResponse(HttpResponse response) throws IOException {
				StatusLine statusLine = response.getStatusLine();
				HttpEntity entity = response.getEntity();
				if (statusLine.getStatusCode() >= 300) {
					throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
				}
				if (null == entity) {
					throw new ClientProtocolException("Response contains no content!");
				}

				Gson gson = new GsonBuilder().create();
				ContentType contentType = ContentType.getOrDefault(entity);
				Reader reader = new InputStreamReader(entity.getContent(), contentType.getCharset());
				return gson.fromJson(reader, Employee[].class);
			}
		};

			Employee[] employees = responseHandler.handleResponse(client.execute(request));
			LOGGER.info("list of employess: ");
			for (Employee employee : employees) {
				LOGGER.info("employee : "+ employee.getEmployeeName() + " | Age : " +employee.getEmployeeAge() );
			}
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
		}
	}

	
	
	public static void main(String[] args) {
		HttpRequestExecutor executor = new HttpRequestExecutor();
		executor.createSimpleRequest(createSimpleRequestURI());
		executor.createSimpleEntity();
		executor.releaseResourcesByClosingResponse();
		executor.useEntityUtislToConsume();
		executor.bufferEntityData();
		executor.handleResponseViaResponseHandler();
	}
}
