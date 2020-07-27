package fr.pc3r.jetrouvemonjob.api;

import com.google.gson.JsonObject;
import fr.pc3r.jetrouvemonjob.beans.HttpParams;
import fr.pc3r.jetrouvemonjob.beans.MyHttpResponse;

import java.util.Collections;
import java.util.List;

public class APIRequest {

    public static final String APPELLATION  = "APPELLATIONS";
    public static final String CONTRAT      = "CONTRATS";
    public static final String DEPARTEMENT  = "DEPARTEMENTS";
    public static final String DOMAINE      = "DOMAINES";
    public static final String FORMATION    = "FORMATIONS";
    public static final String REGION       = "REGIONS";
    public static final String COMMUNE      = "COMMUNES";
    public static final String NATURE       = "NATURES";


    public static JsonObject getOffresFromAPI(List<HttpParams> body) {
        String urlOffres =
            "https://api.emploi-store.fr/partenaire/offresdemploi/v2/offres/search";
        List<HttpParams> headers = APIUtils.getBasicHeader();
        MyHttpResponse response = HttpClientMaker.makeGetAPIRequestSync(urlOffres, headers, body, null);
        return response.getBody();
    }

    public static JsonObject getDetailOffreFromAPI(String offreID) {
        String urlOffre =
            "https://api.emploi-store.fr/partenaire/offresdemploi/v2/offres/{id}";
        List<HttpParams> headers = APIUtils.getBasicHeader();
        List<HttpParams> routes = Collections.singletonList(
            new HttpParams("id", offreID)
        );
        MyHttpResponse response = HttpClientMaker.makeGetAPIRequestSync(urlOffre,headers,null,routes);
        return response.getBody();
    }

    public static void appendDomainsTo(JsonObject res) {
        appendTo(res, "domains");
    }
    public static void appendAppellationsTo(JsonObject res) {
        appendTo(res,"appellations");
    }
    public static void appendTypeContratTo(JsonObject res) {
        appendTo(res,"typecontrat");
    }
    public static void appendFormationsTo(JsonObject res) {
        appendTo(res,"formations");
    }
    public static void appendRegionsTo(JsonObject res) {
        appendTo(res,"regions");
    }
    public static void appendDepartementsTo(JsonObject res) {
        appendTo(res, "departements");
    }

    public static void appendCommunesTo(JsonObject res) {
        appendTo(res, "communes");
    }

    public static void appendNaturesTo(JsonObject res) {
        appendTo(res, "natures");
    }

    private static void appendTo(JsonObject res, String name) {
        res.add(name, APIUtils.readPropertiesFilterFromFile(name));
    }
}
