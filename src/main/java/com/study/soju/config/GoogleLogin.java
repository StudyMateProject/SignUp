package com.study.soju.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GoogleLogin {
    public static JsonNode getAccessToken(String code) {
        final String requestUrl = "https://www.googleapis.com/oauth2/v4/token";

        final List<NameValuePair> postParams = new ArrayList<NameValuePair>();
        postParams.add(new BasicNameValuePair("grant_type", "authorization_code"));
        postParams.add(new BasicNameValuePair("client_id", "826440518994-6t8ghsdrabmgh8vfsnimpofjnmmgcocn.apps.googleusercontent.com"));
        postParams.add(new BasicNameValuePair("client_secret", "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"));
        postParams.add(new BasicNameValuePair("redirect_uri", "http://localhost:8888/loginform/googleauthentication"));
        postParams.add(new BasicNameValuePair("code", code));

        final HttpClient client = HttpClientBuilder.create().build();
        final HttpPost post = new HttpPost(requestUrl);
        JsonNode resultJson = null;

        try {
            post.setEntity(new UrlEncodedFormEntity(postParams));
            final HttpResponse response = client.execute(post);
            final int responseCode = response.getStatusLine().getStatusCode();

            System.out.println("\nSending 'POST' request to URL : " + requestUrl);
            System.out.println("Post parameters : " + postParams);
            System.out.println("Response Code : " + responseCode);

            ObjectMapper mapper = new ObjectMapper();
            resultJson = mapper.readTree(response.getEntity().getContent());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultJson;
    }

    public static JsonNode getUserInfo(String accessToken) {
        final String requestUrl = "https://www.googleapis.com/oauth2/v2/userinfo";

        final HttpClient client = HttpClientBuilder.create().build();
        final HttpGet get = new HttpGet(requestUrl);
        JsonNode resultJson = null;

        get.addHeader("Authorization", "Bearer " + accessToken);

        try {
            final HttpResponse response = client.execute(get);
            final int responseCode = response.getStatusLine().getStatusCode();
            ObjectMapper mapper = new ObjectMapper();
            resultJson = mapper.readTree(response.getEntity().getContent());

            System.out.println("\nSending 'GET' request to URL : " + requestUrl);
            System.out.println("Response Code : " + responseCode);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultJson;
    }

    /*
    public static JsonNode people (String accessToken) {
        final String requestUrl = "https://people.googleapis.com/v1/people/me?" +
                                  "personFields=" + "addresses" +
                                  "&personFields=" + "genders" +
                                  "&key=" + "AIzaSyAZ5qgPQ8PuIzpa5hQA4fdDgWpmRHFOJSI" +
                                  "&access_token=" + accessToken;

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet(requestUrl);
        JsonNode resultJson = null;

        try {
            final HttpResponse response = client.execute(get);
            final int responseCode = response.getStatusLine().getStatusCode();
            ObjectMapper mapper = new ObjectMapper();
            resultJson = mapper.readTree(response.getEntity().getContent());

            System.out.println("\nSending 'GET' request to URL : " + requestUrl);
            System.out.println("Response Code : " + responseCode);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultJson;
    }
     */
}
