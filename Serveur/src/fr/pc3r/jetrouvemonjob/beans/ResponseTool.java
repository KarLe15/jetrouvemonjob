package fr.pc3r.jetrouvemonjob.beans;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Set;

public class ResponseTool {

    public static final int SQL_ERROR = 501;
    public static final int CLIENT_ERROR = 404;

    private static Gson gson = new GsonBuilder().create();

    public static JsonObject serviceAccepted() {
        JsonObject res = new JsonObject();
        res.addProperty("status", "accepted");
        return res;
    }

    public static JsonObject serviceRejected(String message, int errcode) {
        JsonObject res = new JsonObject();
        res.addProperty("status", "rejected");
        JsonObject error = new JsonObject();
        error.addProperty("code",errcode);
        error.addProperty("message",message);
        res.add("error",error);
        return res;
    }
    public static JsonObject getJsonObjectFromJavaObject(Object data) {
        return gson.fromJson(gson.toJson(data), JsonObject.class);
    }

    public static JsonArray getJsonArrayFromJavaCollection(Object data) {
        return gson.fromJson(gson.toJson(data), JsonArray.class);
    }
}
