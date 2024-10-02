package cn.edu.engine.qvog.engine.helper;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.IOException;

public class RestApi {
    private final String baseUrl;
    OkHttpClient client = new OkHttpClient();

    public RestApi(String baseUrl) {
        if (!baseUrl.endsWith("/")) {
            baseUrl += "/";
        }
        this.baseUrl = baseUrl;
    }

    public RestApi(String host, int port) {
        this("http://" + host + ":" + port);
    }

    public String post(String path, Object body) {
        var payload = RequestBody.create(JsonHelper.dumps(body), MediaType.get("application/json"));
        Request request = new Request.Builder()
                .url(baseUrl + path)
                .post(payload)
                .build();

        try (var response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("Failed to post to " + path + ": " + response);
            }
            if (response.body() == null) {
                throw new RuntimeException("Empty response body from " + path);
            }
            return response.body().string();
        } catch (IOException ex) {
            throw new RuntimeException("Failed to send request", ex);
        }
    }

    public <T> T post(String path, Object body, Class<T> clazz) {
        String response = post(path, body);
        return JsonHelper.loads(response, clazz);
    }
}
