package com.sealed.stream;

import android.content.Context;
import okhttp3.OkHttpClient;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import android.content.res.Configuration;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class StarStreamView {
    private final Context context;
    private final String LOG_TAG = "StarStreamView";

    public StarStreamView(Context context) {
        this.context = context;
    }

    public void start(OnReady onReady) {
        String appNameBox = "CNN";
        String getwayURL = "https://a6eab025ed75582ea.awsglobalaccelerator.com";
        String apiTokenBox = "AgEUYW5ib3gtc3RyZWFtLWdhdGV3YXkCBmRhdmlkMQACFDIwMjItMDQtMDRUMTI6NDY6MTJaAAAGIK90GpF9y64DHwz9vk5TXq1q88-gpDRs1lwquK9bUAuv";

        int width = 720, height = 1280;

        JSONObject sessionInfo = new JSONObject();
        JSONObject screenInfo = new JSONObject();
        try {
            screenInfo.put("width", width);
            screenInfo.put("height", height);
            screenInfo.put("fps", 30);
            sessionInfo.put("app", appNameBox);
            sessionInfo.put("screen", screenInfo);
        } catch (JSONException e) {
            Toast.makeText(context, "Failed to create session specification", Toast.LENGTH_SHORT).show();
            return;
        }

        Request createSessionReq = new Request.Builder()
                .url(getwayURL + "/1.0/sessions")
                .post(RequestBody.create(MEDIA_TYPE_JSON, sessionInfo.toString()))
                .addHeader("Authorization", "Macaroon root=" + apiTokenBox)
                .build();

        mClient.newCall(createSessionReq).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(LOG_TAG, "Failed to create session: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) {
                        Log.w(LOG_TAG, responseBody.string());
                        throw new IOException("Unexpected code " + response);
                    }

                    String json = responseBody.string();
                    Log.w(LOG_TAG, json);

                    JSONObject sessionResp = new JSONObject(json);
                    JSONObject metaDataObj = sessionResp.getJSONObject("metadata");

                    String signalingURL = metaDataObj.getString("url");

                    ArrayList<StunServer> stunServers = new ArrayList<>();
                    JSONArray stunServersArray = metaDataObj.getJSONArray("stun_servers");
                    for (int n = 0; n < stunServersArray.length(); n++) {
                        JSONObject stunServerObj = stunServersArray.getJSONObject(n);
                        StunServer server = new StunServer();

                        JSONArray urlsArray = stunServerObj.getJSONArray("urls");
                        List<String> urls = new ArrayList<String>();
                        for (int m = 0; m < urlsArray.length(); m++) {
                            urls.add(urlsArray.getString(m));
                        }
                        server.urls = urls.toArray(new String[0]);

                        if (stunServerObj.has("username"))
                            server.username = stunServerObj.getString("username");
                        if (stunServerObj.has("password"))
                            server.password = stunServerObj.getString("password");

                        stunServers.add(server);
                    }

                    onReady.run(signalingURL, stunServers);
                } catch (JSONException | IOException e) {
                    throw new IOException("Received invalid response");
                }
            }
        });
    }

    public static final MediaType MEDIA_TYPE_JSON
            = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient mClient = createHTTPClient().build();

    public static OkHttpClient.Builder createHTTPClient() {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            final X509Certificate[] acceptedIssuers = {};
                            return acceptedIssuers;
                        }
                    }
            };

            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0]);
            builder.hostnameVerifier((hostname, session) -> true);

            return builder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
