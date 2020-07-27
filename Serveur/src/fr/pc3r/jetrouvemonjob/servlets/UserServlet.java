package fr.pc3r.jetrouvemonjob.servlets;

import com.google.gson.JsonObject;
import fr.pc3r.jetrouvemonjob.beans.*;
import fr.pc3r.jetrouvemonjob.services.OffresService;
import fr.pc3r.jetrouvemonjob.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *  <h1>URL</h1> : <code>/user</code>
 *  <h1>Description</h1> :
 *  <p>
 *      This Servlet is used to manage user personal content (name, email, CV, bookmarks ...)
 *  </p>
 *  <h1>Methods allowed</h1> :
 *  <ul>
 *      <li>
 *          <h3>PUT</h3> :: To register a new user
 *          parameter
 *          <ul>
 *              <li>username</li>
 *              <li>password</li>
 *              <li>mail</li>
 *              <li>nom</li>
 *              <li>prenom</li>
 *          </ul>
 *      </li>
 *      <li>
 *          <h3>DELETE</h3> :: To delete an account
 *          parameter
 *          <ul>
 *              <li>username</li>
 *              <li>sessionkey</li>
 *          </ul>
 *      </li>
 *      <li>
 *          <h3>POST</h3> :: Add and remove bookmark of user
 *          parameter
 *          <ul>
 *              <li>username</li>
 *              <li>sessionkey</li>
 *              <li>idOffre</li>
 *          </ul>
 *      </li>
 *      <li>
 *          <h3>GET</h3> :: Get All data of user
 *          parameter
 *          <ul>
 *              <li>username</li>
 *              <li>sessionkey</li>
 *          </ul>
 *      </li>
 *      <li>
 *          <h3>HEAD</h3> :: UNHANDLED METHOD
 *      </li>
 *      <li>
 *          <h3>OPTIONS</h3> :: Change/set personnal data (CV, formation, name, etc...)
 *          // TODO :: make parameters
 *          // TODO :: check if it works with chrome and angular
 *      </li>
 *  </ul>
 *
 * @author Karim LEFFAD, Rabah Hadjebar
 */
@WebServlet(name = "UserServlet", urlPatterns = "/user")
public class UserServlet extends HttpServlet {


    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletUtils.setupResponse(resp);
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String mail = req.getParameter("mail");
        String nom = req.getParameter("nom");
        String prenom = req.getParameter("prenom");
        System.out.println(username + password + mail + nom + prenom);
        JsonObject res;
        if(username == null ||
            password == null ||
            mail == null ||
            nom == null ||
            prenom == null
        ) {
            res = ResponseTool.serviceRejected("Method called with wrong parameters", ResponseTool.CLIENT_ERROR);
        } else {
            User user = new User(username, nom, prenom, mail, password);
            try {
                res = UserService.createUser(user);
            } catch (BadRequestException e) {
                Logger.getLogger().SubmitError("UserServlet called with PUT", e);
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
        JsonObject res;
        if(username == null || sessionkey == null) {
            res = ResponseTool.serviceRejected("Method called with wrong parameters", ResponseTool.CLIENT_ERROR);
        } else {
            try {
                res = UserService.removeUser(username, sessionkey);
            } catch (BadRequestException e) {
                Logger.getLogger().SubmitError("UserServlet called with DELETE", e);
                res = ResponseTool.serviceRejected("Internal error occurred", ResponseTool.CLIENT_ERROR);
            }
        }
        ServletUtils.writeResponse(resp, res);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletUtils.setupResponse(resp);
        String username = req.getParameter("username");
        String sessionkey = req.getParameter("sessionkey");
        JsonObject res;
        if(username == null || sessionkey == null) {
            res = ResponseTool.serviceRejected("Method called with wrong parameters", ResponseTool.CLIENT_ERROR);
        } else {
            try {
                res = UserService.retrieveDataUser(username, sessionkey);
            } catch (BadRequestException e) {
                Logger.getLogger().SubmitError("UserServlet called with GET", e);
                res = ResponseTool.serviceRejected("Internal error occurred", ResponseTool.CLIENT_ERROR);
            }
        }
        ServletUtils.writeResponse(resp, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletUtils.setupResponse(resp);
        JsonObject response;
        String username;
        String sessionkey;
        String idOffre;
        try {
            JsonObject body = ServletUtils.getBodyOfPostRequest(req);
            username        = body.get("username").getAsString();
            sessionkey      = body.get("sessionkey").getAsString();
            idOffre         = body.get("idOffre").getAsString();
        } catch (NoBodyAvailableException e) {
            username        = req.getParameter("username");
            sessionkey      = req.getParameter("sessionkey");
            idOffre         = req.getParameter("idOffre");
        }
        if (username == null || sessionkey == null || idOffre == null) {
            response = ResponseTool.serviceRejected("Missing some argument or body", ResponseTool.CLIENT_ERROR);
        } else {
            try {
                response = UserService.addOffreToBookmark(idOffre, username, sessionkey);
            } catch (BadRequestException e) {
                Logger.getLogger().SubmitError("UserServlet called with POST", e);
                response = ResponseTool.serviceRejected("Internal error occurred", ResponseTool.CLIENT_ERROR);
            }
        }
        ServletUtils.writeResponse(resp, response);
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletUtils.unhandledRequest(resp);
    }

    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletUtils.unhandledRequest(resp);
    }


}
