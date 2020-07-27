package fr.pc3r.jetrouvemonjob.servlets;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fr.pc3r.jetrouvemonjob.beans.BadRequestException;
import fr.pc3r.jetrouvemonjob.beans.Logger;
import fr.pc3r.jetrouvemonjob.beans.NoBodyAvailableException;
import fr.pc3r.jetrouvemonjob.beans.ResponseTool;
import fr.pc3r.jetrouvemonjob.services.OffresService;
import kong.unirest.json.JSONElement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *  <h1>URL</h1> : <code>/offres</code>
 *  <h1>Description</h1> :
 *  <p>
 *      This Servlet is used to manage data relative to offers.
 *      It could be used to get details, search for or bookmark any offer.
 *  </p>
 *  <h1>Methods allowed</h1> :
 *  <ul>
 *      <li>
 *          <h3>GET</h3> :: retrieve data of announce or search for offers
 *          parameter
 *          <h4>Searching offers with criterion</h4>
 *          <ul>
 *              <li>APPELLATIONS</li>
 *              <li>CONTRATS</li>
 *              <li>DEPARTEMENTS</li>
 *              <li>DOMAINES</li>
 *              <li>FORMATIONS</li>
 *              <li>REGIONS</li>
 *          </ul>
 *          <h4>Get detail of an offer</h4>
 *          <ul>
 *              <li>id</li>
 *          </ul>
 *      </li>
 *      <li>
 *          <h3>POST</h3> :: To add comment/advice to an offer
 *          parameter (could be in the body or as queryString)
 *          <ul>
 *              <li>username</li>
 *              <li>sessionkey</li>
 *              <li>idOffre</li>
 *              <li>contentMessage</li>
 *          </ul>
 *      </li>
 *      <li>
 *          <h3>DELETE</h3> :: Dislike a comment of an offer
 *           parameter
 *          <ul>
 *              <li>username</li>
 *              <li>sessionkey</li>
 *              <li>idcomm</li>
 *          </ul>
 *      </li>
 *      <li>
 *          <h3>PUT</h3> :: Like a comment of an offer
 *          parameter
 *          <ul>
 *              <li>username</li>
 *              <li>sessionkey</li>
 *              <li>idcomm</li>
 *          </ul>
 *      </li>
 *      <li>
 *          <h3>HEAD</h3> :: UNHANDLED METHOD
 *      </li>
 *      <li>
 *          <h3>OPTIONS</h3> :: UNHANDLED METHOD
 *      </li>
 *  </ul>
 *
 * @author Karim LEFFAD, Rabah Hadjebar
 */
@WebServlet(
    name = "OffresServlet",
    urlPatterns = {
        "/offres"
    }
)
public class OffresServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletUtils.setupResponse(resp);
        JsonObject response;
        String id = req.getParameter("id");
        try {
            if (id != null) {
                response = OffresService.getDetailsAnnonce(id);
            } else {
                String appellations = req.getParameter("APPELLATIONS");
                String contrats     = req.getParameter("CONTRATS");
                String departements = req.getParameter("DEPARTEMENTS");
                String domaines     = req.getParameter("DOMAINES");
                String formations   = req.getParameter("FORMATIONS");
                String regions      = req.getParameter("REGIONS");
                String motCle       = req.getParameter("KEYWORD");
                response = OffresService.getRechercheResults(
                    appellations,
                    contrats,
                    departements,
                    domaines,
                    formations,
                    regions,
                    motCle
                );
                
                try {
                	JsonArray filtre  = response.get("results").getAsJsonObject().get("filtresPossibles").getAsJsonArray();
                    for(JsonElement e: filtre ) {
                    	String s = e.getAsJsonObject().get("filtre").getAsString();
                    	if(s.equals("qualification") || e.equals("experience")) {
                    		filtre.remove(e);
                    	}
                    }
                } catch(NullPointerException e) {
                	
                }
                
            }
        } catch (BadRequestException e) {
            Logger.getLogger().SubmitError("OffresServlet called with GET", e);
            response = ResponseTool.serviceRejected("Internal error occurred", ResponseTool.CLIENT_ERROR);
        }
        ServletUtils.writeResponse(resp, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletUtils.setupResponse(resp);
        JsonObject response;
        String username;
        String sessionkey;
        String idOffre;
        String contentMessage;
        try {
            JsonObject body = ServletUtils.getBodyOfPostRequest(req);
            username        = body.get("username").getAsString();
            sessionkey      = body.get("sessionkey").getAsString();
            idOffre         = body.get("idOffre").getAsString();
            contentMessage  = body.get("contentMessage").getAsString();
        } catch (NoBodyAvailableException | NullPointerException e) {
            username        = req.getParameter("username");
            sessionkey      = req.getParameter("sessionkey");
            idOffre         = req.getParameter("idOffre");
            contentMessage  = req.getParameter("contentMessage");
        }

        if (username == null || sessionkey == null || idOffre == null || contentMessage == null) {
            response = ResponseTool.serviceRejected("Missing some argument or body", ResponseTool.CLIENT_ERROR);
        } else {
            try {
                response = OffresService.addCommentToOffer(idOffre, username, sessionkey, contentMessage);
            } catch (BadRequestException e) {
                Logger.getLogger().SubmitError("OffresServlet called with POST", e);
                response = ResponseTool.serviceRejected("Internal error occurred", ResponseTool.CLIENT_ERROR);
            }
        }
        ServletUtils.writeResponse(resp, response);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletUtils.setupResponse(resp);
        String username = req.getParameter("username");
        String sessionkey = req.getParameter("sessionkey");
        String idComm = req.getParameter("idcomm");
        JsonObject res;
        if(username == null || sessionkey == null || idComm == null) {
            res = ResponseTool.serviceRejected("Method called with wrong parameters", ResponseTool.CLIENT_ERROR);
        } else {
            try {
                res = OffresService.likeComment(username, sessionkey, Integer.parseInt(idComm), true);
            } catch (BadRequestException e) {
                Logger.getLogger().SubmitError("UserServlet called with GET", e);
                res = ResponseTool.serviceRejected("Internal error occurred", ResponseTool.CLIENT_ERROR);
            }
        }
        ServletUtils.writeResponse(resp, res);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletUtils.setupResponse(resp);
        String username = req.getParameter("username");
        String sessionkey = req.getParameter("sessionkey");
        String idComm = req.getParameter("idcomm");
        JsonObject res;
        if(username == null || sessionkey == null || idComm == null) {
            res = ResponseTool.serviceRejected("Method called with wrong parameters", ResponseTool.CLIENT_ERROR);
        } else {
            try {
                res = OffresService.likeComment(username, sessionkey, Integer.parseInt(idComm), false);
            } catch (BadRequestException e) {
                Logger.getLogger().SubmitError("UserServlet called with GET", e);
                res = ResponseTool.serviceRejected("Internal error occurred", ResponseTool.CLIENT_ERROR);
            }
        }
        ServletUtils.writeResponse(resp, res);
    }

    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletUtils.unhandledRequest(resp);
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletUtils.unhandledRequest(resp);
    }
}
