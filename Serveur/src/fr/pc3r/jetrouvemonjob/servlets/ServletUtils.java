package fr.pc3r.jetrouvemonjob.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import fr.pc3r.jetrouvemonjob.beans.NoBodyAvailableException;
import fr.pc3r.jetrouvemonjob.beans.ResponseTool;
import org.apache.http.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

public class ServletUtils {
    private static Gson gson = new GsonBuilder().create();

    public static void unhandledRequest(HttpServletResponse resp) throws IOException {
        JsonObject res = ResponseTool.serviceRejected(
            "Your request is not handle with this parameters",
            ResponseTool.CLIENT_ERROR
        );

        resp.setStatus(404);
        PrintWriter out = resp.getWriter();
        out.println(getStringFromResponse(res));
    }



    public static void writeResponse(HttpServletResponse resp, JsonObject res) throws IOException {
        PrintWriter out = resp.getWriter();
        out.println(getStringFromResponse(res));
    }

    static String getStringFromResponse(JsonObject res) {
        return gson.toJson(res);
    }

    static void setupResponse(HttpServletResponse resp) {
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json");
    }

    static JsonObject getBodyOfPostRequest(HttpServletRequest req) throws IOException, NoBodyAvailableException {
        String body = req.getReader().lines().collect(Collectors.joining());
        if (body.isBlank()) {
            throw new NoBodyAvailableException();
        }
        return gson.fromJson(body,JsonObject.class);
    }
}
