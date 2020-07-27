package fr.pc3r.jetrouvemonjob.beans;

import com.google.gson.JsonObject;

public class MyHttpResponse {
    private int status;
    private JsonObject body;

    public MyHttpResponse(int status, JsonObject body) {
        this.status = status;
        this.body = body;
    }

    public int getStatus() {
        return status;
    }

    public JsonObject getBody() {
        return body;
    }
}
