package fr.pc3r.jetrouvemonjob.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import fr.pc3r.jetrouvemonjob.beans.HttpParams;
import fr.pc3r.jetrouvemonjob.beans.MyHttpResponse;
import kong.unirest.*;

import java.util.Arrays;
import java.util.List;

class HttpClientMaker {
    private static long lastTimeGenerated = 0;
    private static long validityTime = 0;
    private static String accessToken = "";
    private static Gson gson = new GsonBuilder().create();

    //private static Object mutex = new Object(); // this is to synchronise


    static MyHttpResponse makePostAPIRequestSync(
        String url,
        List<HttpParams> headers,
        List<HttpParams> queryString,
        List<HttpParams> body,
        List<HttpParams> fields,
        List<HttpParams> routeParams
    ) {
        addAccessToken(headers);
        return makeRequestSync("POST", url, headers, queryString, body, fields, routeParams);
    }
    static MyHttpResponse makeGetAPIRequestSync(
        String url,
        List<HttpParams> headers,
        List<HttpParams> queryString,
        List<HttpParams> routesParams
    ) {
        addAccessToken(headers);
        return makeRequestSync("GET", url, headers, queryString, null, null, routesParams);
    }

    private static MyHttpResponse makeRequestSync(
        String method,
        String url,
        List<HttpParams> headers,
        List<HttpParams> queryString,
        List<HttpParams> body,
        List<HttpParams> fields,
        List<HttpParams> routesParams
    ) {

        HttpRequest httpRequest;
        switch (method) {
            case "GET" :
                httpRequest = Unirest.get(url);
                break;
            case "POST":
                httpRequest = Unirest.post(url);
                break;
            default:
                httpRequest = Unirest.post(url);
        }
        if (routesParams != null) {
            for (HttpParams route : routesParams) {
                httpRequest.routeParam(route.getName(), route.getValue());
            }
        }
        for (HttpParams h : headers) {
            httpRequest = httpRequest.header(h.getName(),h.getValue());
        }
        if (queryString != null) {
            for (HttpParams qs : queryString) {
                httpRequest = httpRequest.queryString(qs.getName(), qs.getValue());
            }
        }
        if (body != null) {
            JsonObject bodyJSON = new JsonObject();
            for (HttpParams f : body) {
                bodyJSON.addProperty(f.getName(),f.getValue());
            }
            HttpRequestWithBody httpRequestWithBody = (HttpRequestWithBody) httpRequest;
            httpRequest = httpRequestWithBody.body(bodyJSON.toString());
        }
        if (fields != null) {
            HttpRequestWithBody httpRequestWithBody = (HttpRequestWithBody) httpRequest;
            HttpParams f = fields.get(0);
            MultipartBody multipartBody = httpRequestWithBody.field(f.getName(), f.getValue());
            for (int i = 1; i < fields.size(); i++) {
                f = fields.get(i);
                multipartBody = multipartBody.field(f.getName(),f.getValue());
            }
            httpRequest = multipartBody;
        }
        HttpResponse response = httpRequest.asJson();
        JsonObject bodyResponse = gson.fromJson(response.getBody().toString(), JsonObject.class);
        int statusResponse = response.getStatus();
        return new MyHttpResponse(statusResponse,bodyResponse);
    }


    private static void addAccessToken(List<HttpParams> headers) {
        String accessToken = getAccessToken();
        headers.add(new HttpParams("Authorization","Bearer " + accessToken));
    }

    private static String getAccessToken() {
        if (isAccessTokenValide()) {
            return accessToken;
        }
        return generateAccessToken();
    }

    private static String generateAccessToken() {
        String url = "https://entreprise.pole-emploi.fr/connexion/oauth2/access_token";
        List<HttpParams> headers = Arrays.asList(
          new HttpParams("Content-Type", "application/x-www-form-urlencoded")
        );
        List<HttpParams> qs = Arrays.asList(
            new HttpParams("realm", "/partenaire")
        );
        List<HttpParams> fields = Arrays.asList(
            new HttpParams("grant_type", APIStatic.grant_type),
            new HttpParams("client_id", APIStatic.client_id),
            new HttpParams("client_secret", APIStatic.client_secret),
            new HttpParams("scope", APIStatic.scope)
        );
        MyHttpResponse response = makeRequestSync(
            "POST",
            url,
            headers,
            qs,
            null,
            fields,
            null
        );
        JsonObject res = response.getBody();
        lastTimeGenerated = System.currentTimeMillis();
        accessToken = res.get("access_token").getAsString();
        validityTime = res.get("expires_in").getAsLong() * 1000;

        return accessToken;
    }

    private static boolean isAccessTokenValide() {
        if (accessToken.isEmpty()) {
            return false;
        }
        long currentTime = System.currentTimeMillis();
        return currentTime > lastTimeGenerated + validityTime - 1000;
    }

}
