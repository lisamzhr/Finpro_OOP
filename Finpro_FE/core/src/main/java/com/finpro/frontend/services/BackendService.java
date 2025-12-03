package com.finpro.frontend.services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpRequestBuilder;

public class BackendService {

    // COBA GANTI KE 127.0.0.1 jika localhost tidak work
    private static final String BASE_URL = "http://127.0.0.1:8081/api/player";

    public interface RequestCallback {
        void onSuccess(String response);
        void onError(String error);
    }

    public void register(String username, RequestCallback callback) {
        String json = "{\"username\":\"" + username + "\"}";

        System.out.println("\n=== REGISTER REQUEST ===");
        System.out.println("URL: " + BASE_URL + "/register");
        System.out.println("Body: " + json);

        HttpRequestBuilder builder = new HttpRequestBuilder();
        Net.HttpRequest request = builder.newRequest()
            .method(Net.HttpMethods.POST)
            .url(BASE_URL + "/register")
            .header("Content-Type", "application/json")
            .content(json)
            .timeout(30000) // 30 detik timeout
            .build();

        sendRequest(request, callback, "REGISTER");
    }

    public void login(String username, String playerId, RequestCallback callback) {
        String json = "{\"username\":\"" + username + "\",\"playerId\":\"" + playerId + "\"}";

        System.out.println("\n=== LOGIN REQUEST ===");
        System.out.println("URL: " + BASE_URL + "/login");
        System.out.println("Body: " + json);

        HttpRequestBuilder builder = new HttpRequestBuilder();
        Net.HttpRequest request = builder.newRequest()
            .method(Net.HttpMethods.POST)
            .url(BASE_URL + "/login")
            .header("Content-Type", "application/json")
            .content(json)
            .timeout(30000) // 30 detik timeout
            .build();

        sendRequest(request, callback, "LOGIN");
    }

    private void sendRequest(Net.HttpRequest request, RequestCallback callback, String requestType) {
        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                int status = httpResponse.getStatus().getStatusCode();
                String result = httpResponse.getResultAsString();

                System.out.println("\n=== " + requestType + " RESPONSE ===");
                System.out.println("Status Code: " + status);
                System.out.println("Response Body: " + result);
                System.out.println("Response Length: " + (result != null ? result.length() : 0));

                if (status >= 200 && status < 300) {
                    System.out.println("✓ SUCCESS - Calling onSuccess callback");
                    callback.onSuccess(result);
                } else {
                    System.err.println("✗ HTTP ERROR: " + status);
                    callback.onError("HTTP " + status + " [" + result + "]");
                }
            }

            @Override
            public void failed(Throwable t) {
                System.err.println("\n=== " + requestType + " FAILED ===");
                System.err.println("Exception Type: " + t.getClass().getName());
                System.err.println("Error Message: " + t.getMessage());
                System.err.println("Stack Trace:");
                t.printStackTrace();

                String errorMsg = "HTTP -1 [koneksi error]";
                if (t.getMessage() != null) {
                    errorMsg = "HTTP -1 [" + t.getMessage() + "]";
                }

                System.err.println("✗ Calling onError callback with: " + errorMsg);
                callback.onError(errorMsg);
            }

            @Override
            public void cancelled() {
                System.err.println("\n=== " + requestType + " CANCELLED ===");
                callback.onError("Request cancelled");
            }
        });
    }
}
