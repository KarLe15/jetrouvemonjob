package fr.pc3r.jetrouvemonjob.servlets;

import com.google.gson.JsonObject;
import fr.pc3r.jetrouvemonjob.beans.BadRequestException;
import fr.pc3r.jetrouvemonjob.beans.Logger;
import fr.pc3r.jetrouvemonjob.beans.ResponseTool;
import fr.pc3r.jetrouvemonjob.beans.User;
import fr.pc3r.jetrouvemonjob.services.UserService;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *  <h1>URL</h1> : <code>/login</code>
 *  <h1>Description</h1> :
 *  <p>
 *      This Servlet is used to manage connexion
 *  </p>
 *  <h1>Methods allowed</h1> :
 *  <ul>
 *      <li>
 *          <h3>PUT</h3> :: Connect user
 *          parameter
 *          <ul>
 *              <li>username</li>
 *              <li>password</li>
 *          </ul>
 *      </li>
 *      <li>
 *          <h3>DELETE</h3> :: Disconnect user
 *          parameter
 *          <ul>
 *              <li>sessionkey</li>
 *              <li>username</li>
 *          </ul>
 *      </li>
 *      <li>
 *          <h3>POST</h3> :: UNHANDLED METHOD
 *      </li>
 *      <li>
 *          <h3>GET</h3> :: UNHANDLED METHOD
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
@WebServlet(name = "LoggingServlet", urlPatterns = "/login")
public class LoggingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    @Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ServletUtils.setupResponse(resp);
		System.out.println(req.getParameter("username"));
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		JsonObject res;
		if(username == null || password == null) {
			res = ResponseTool.serviceRejected("Method called with wrong parameters", ResponseTool.CLIENT_ERROR);
		} else {
			User user = new User(username, password);
			try {
				res = UserService.connectUser(user);
			} catch (BadRequestException e) {
				Logger.getLogger().SubmitError("LoggingServlet called with PUT", e);
				res = ResponseTool.serviceRejected("Internal error occurred", ResponseTool.CLIENT_ERROR);
			}
		}
		ServletUtils.writeResponse(resp, res);
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ServletUtils.setupResponse(resp);
		String sessionkey = req.getParameter("session_key");
		String username = req.getParameter("username");
		JsonObject res;
		if(sessionkey == null || username == null) {
			res = ResponseTool.serviceRejected("Method called with wrong parameters", ResponseTool.CLIENT_ERROR);
		} else {
			try {
				res = UserService.disconnectUser(username, sessionkey);
			} catch (BadRequestException e) {
				Logger.getLogger().SubmitError("LoggingServlet called with DELETE", e);
				res = ResponseTool.serviceRejected("Internal error occurred", ResponseTool.CLIENT_ERROR);
			}
		}
		ServletUtils.writeResponse(resp, res);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletUtils.unhandledRequest(response);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ServletUtils.unhandledRequest(resp);
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
