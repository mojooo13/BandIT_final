package com.example.mo.bandit_v1;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class CommunicationUtilImpl implements CommunicationUtil {

    private static final String URI = "http://serveradresse.com";

    private HttpClient httpClient;

    public CommunicationUtilImpl() {
        httpClient = new DefaultHttpClient();
    }

    @Override
    public boolean signUp(SignUpData data) {
        Gson gson = new Gson();
        HttpPost httpPost = new HttpPost(URI);
        try {
            httpPost.setEntity(new StringEntity(gson.toJson(data), "utf-8"));
            httpPost.addHeader("command", "signUp");
            HttpResponse response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                // alles ok
                return true;
            } else {
                // fehler -> 404: site not found, 500: internal server error
                return false;
            }
        } catch (UnsupportedEncodingException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public ProfilData login(LoginData data) {
        Gson gson = new Gson();
        HttpPost httpPost = new HttpPost(URI);
        try {
            httpPost.setEntity(new StringEntity(gson.toJson(data), "utf-8"));
            httpPost.addHeader("command", "login");
            HttpResponse response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                // alles ok
                // convert response to string
                String result = getResult(response);
                ProfilData profilData = gson.fromJson(result, ProfilData.class);
                return profilData;
            } else {
                // fehler -> 404: site not found, 500: internal server error
                return null;
            }
        } catch (UnsupportedEncodingException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    private String getResult(HttpResponse response) {
        InputStream is = null;
        StringBuilder sb = new StringBuilder();
        try {
            is = response.getEntity().getContent();
            final BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"), 8);
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
