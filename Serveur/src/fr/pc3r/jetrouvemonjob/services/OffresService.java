package fr.pc3r.jetrouvemonjob.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import fr.pc3r.jetrouvemonjob.api.APIRequest;
import fr.pc3r.jetrouvemonjob.beans.*;
import fr.pc3r.jetrouvemonjob.database.DBTool;
import fr.pc3r.jetrouvemonjob.database.DataBase;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class OffresService {
    public static JsonObject getStaticDatas(List<String> datasToGet) throws BadRequestException {
        JsonObject datas = new JsonObject();
        for (String data : datasToGet) {
            switch (data) {
                case APIRequest.APPELLATION:
                    APIRequest.appendAppellationsTo(datas);
                    break;
                case APIRequest.CONTRAT:
                    APIRequest.appendTypeContratTo(datas);
                    break;
                case APIRequest.REGION:
                    APIRequest.appendRegionsTo(datas);
                    break;
                case APIRequest.DEPARTEMENT:
                    APIRequest.appendDepartementsTo(datas);
                    break;
                case APIRequest.DOMAINE:
                    APIRequest.appendDomainsTo(datas);
                    break;
                case APIRequest.FORMATION:
                    APIRequest.appendFormationsTo(datas);
                    break;
                case APIRequest.COMMUNE:
                    APIRequest.appendCommunesTo(datas);
                    break;
                case APIRequest.NATURE:
                    APIRequest.appendNaturesTo(datas);
                    break;
                default:
                    throw new BadRequestException("Datas called with wrong arguments");
            }
        }
        JsonObject res = ResponseTool.serviceAccepted();
        res.add("results", datas);
        return res;
    }

    public static JsonObject getRechercheResults(
        String appellations,
        String contrat,
        String departements,
        String domains,
        String formations,
        String regions,
        String motCle
    ) throws BadRequestException {
        List<HttpParams> params = new ArrayList<>();
        if (appellations != null) {
            params.add(new HttpParams("appelation", appellations));
        }
        if (contrat != null) {
            params.add(new HttpParams("typeContrat", contrat));
        }
        if (departements != null) {
            params.add(new HttpParams("departement", departements));
        }
        if (domains != null) {
            params.add(new HttpParams("domaine", domains));
        }
        if (formations != null) {
            params.add(new HttpParams("niveauFormation", formations));
        }
        if (regions != null) {
            params.add(new HttpParams("region", regions));
        }
        if (motCle != null) {
            params.add(new HttpParams("motsCles", motCle));
        }
        JsonObject result = APIRequest.getOffresFromAPI(params);
        JsonObject res = ResponseTool.serviceAccepted();
        res.add("results", result);
        return res;
    }

    public static JsonObject getDetailsAnnonce(String idOffre) throws BadRequestException {
        try {
            Connection c = DataBase.getMySQLConnection();
            JsonObject offre = APIRequest.getDetailOffreFromAPI(idOffre);
            JsonObject res;
            if (offre.get("codeHttp") != null) {
                return ResponseTool.serviceRejected(
                    "Id does not existe",
                    ResponseTool.CLIENT_ERROR
                );
            }
            res = ResponseTool.serviceAccepted();
            res.add("results", offre);
            Set<Commentaire> commentaires = DBTool.getCommentaireOfOffre(c, idOffre);
            JsonArray coms = ResponseTool.getJsonArrayFromJavaCollection(commentaires);
            res.add("commentaires", coms);
            return res;
        } catch (SQLException e) {
            Logger.getLogger().SubmitError("get offer details | SQL Error", e);
            return ResponseTool.serviceRejected(
                "Internal connection error",
                ResponseTool.SQL_ERROR
            );
        }
    }

    public static JsonObject addCommentToOffer(
        String idOffre,
        String username,
        String sessionkey,
        String contentMessage
    ) throws BadRequestException {
        try {
            Connection c = DataBase.getMySQLConnection();
            boolean correct = DBTool.checkUserExistSession(c,username, sessionkey);
            if (! correct) {
                throw new BadRequestException("User not connected");
            }
            int id_com = DBTool.addCommentToOffer(c, username, idOffre, contentMessage);

            JsonObject res = ResponseTool.serviceAccepted();
            JsonObject result = new JsonObject();
            result.addProperty("message", "comment successfully added to offer");
            result.addProperty("id_com",id_com);
            res.add("result", result);
            return res;
        } catch (SQLException e) {
            Logger.getLogger().SubmitError("commenting offer | SQL Error", e);
            return ResponseTool.serviceRejected(
                "Internal connection error",
                ResponseTool.SQL_ERROR
            );
        }
    }

    public static JsonObject likeComment(
        String username,
        String sessionkey,
        int idComm,
        boolean like
    ) throws BadRequestException {
        try {
            Connection c = DataBase.getMySQLConnection();
            boolean correct =  DBTool.checkUserExistSession(c,username, sessionkey);
            if (! correct) {
                return ResponseTool.serviceRejected(
                    "User not connected",
                    ResponseTool.CLIENT_ERROR
                );
            }
            correct = DBTool.likeComment(c, username, sessionkey, idComm, like);
            if(!correct) {
                return ResponseTool.serviceRejected(
                    "Error cannot like",
                    ResponseTool.CLIENT_ERROR
                );
            }
            JsonObject res = ResponseTool.serviceAccepted();
            res.addProperty("result", "like/dislike successfully added to comment");
            return res;
        } catch (SQLException e) {
            Logger.getLogger().SubmitError("Liking comment | SQL Error", e);
            return ResponseTool.serviceRejected(
                "Internal connection error",
                ResponseTool.SQL_ERROR
            );
        }
    }
}
