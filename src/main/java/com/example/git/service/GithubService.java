package com.example.git.service;


import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Service
public class GithubService {
    @Value(value ="{github.token}")
    private String token= "";

    @Value("${url.github}")
    private String url;

    public String getBranchedFromGithub(){
        try {
            URL myURL = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) myURL.openConnection();
            token = token + ":x-oauth-basic";
            String authString = "Basic " + Base64.encodeBase64String(token.getBytes());
            connection.setRequestProperty("Authorization", authString);
            connection.getResponseCode();
            InputStream input = connection.getInputStream();
            String text = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));
            return text;
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
    }
}