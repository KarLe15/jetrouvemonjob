package fr.pc3r.jetrouvemonjob.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import fr.pc3r.jetrouvemonjob.beans.HttpParams;
import fr.pc3r.jetrouvemonjob.beans.Logger;
import fr.pc3r.jetrouvemonjob.beans.ResponseTool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

class APIUtils {

    private static Gson gson = new GsonBuilder().create();
    private static final String PREFIX_RESOURCES = "static_data/";

    static JsonArray readPropertiesFilterFromFile(String property) {
        switch (property) {
            case "domains" :
                return readFromFile(PREFIX_RESOURCES +"domains.json");
            case "appellations" :
                return readFromFile(PREFIX_RESOURCES + "appellations.json");
            case "typecontrat" :
                return readFromFile(PREFIX_RESOURCES + "contrat.json");
            case "formations" :
                return readFromFile(PREFIX_RESOURCES + "formations.json");
            case "regions" :
                return readFromFile(PREFIX_RESOURCES + "regions.json");
            case "departements" :
                return readFromFile(PREFIX_RESOURCES + "departements.json");
            case "communes" :
                return readFromFile(PREFIX_RESOURCES + "communes.json");
            case "natures" :
                return readFromFile(PREFIX_RESOURCES + "natures.json");
            default:
                return null;
        }
    }

    private static JsonArray readFromFile(String filePath) {
        StringBuilder data = new StringBuilder();
        try {
            // here I must read file
            InputStream ips = APIUtils.class.getClassLoader().getResourceAsStream(filePath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(ips));
            String line = "";
            while ((line = reader.readLine()) != null) {
                data.append(line).append("\n");
            }
        } catch (Exception e) {
            Logger.getLogger().SubmitError("could not read file " + filePath, e);
            JsonObject error = ResponseTool.serviceRejected(
                "could not read the file",
                ResponseTool.SQL_ERROR
            );
            JsonArray rerr = new JsonArray();
            rerr.add(error);
            return rerr;
        }
        return gson.fromJson(data.toString(), JsonArray.class);
    }

    static List<HttpParams> getBasicHeader() {
        List<HttpParams> res = new ArrayList<>();
        res.add(new HttpParams("Content-Type", "application/json"));
        res.add(new HttpParams("Accept", "application/json"));
        return res;
    }
}
