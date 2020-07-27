package fr.pc3r.jetrouvemonjob.servlets;

import com.google.gson.JsonObject;
import fr.pc3r.jetrouvemonjob.beans.BadRequestException;
import fr.pc3r.jetrouvemonjob.beans.Logger;
import fr.pc3r.jetrouvemonjob.beans.ResponseTool;
import fr.pc3r.jetrouvemonjob.services.OffresService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


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
 *          <h3>GET</h3> :: retrieve filters that is been used with the API
 *          parameter
 *          <ul>
 *              <h4>datas</h4>:
 *              <p>
 *                  array parameter
 *                  see in API examples
 *              </p>
 *              <ul>
 *                  <li>APPELLATIONS</li>
 *                  <li>CONTRATS</li>
 *                  <li>DEPARTEMENTS</li>
 *                  <li>DOMAINES</li>
 *                  <li>FORMATIONS</li>
 *                  <li>REGIONS</li>
 *                  <li>COMMUNES</li>
 *                  <li>NATURES</li>
 *              </ul>
 *          </ul>
 *      </li>
 *      <li>
 *          <h3>DELETE</h3> :: UNHANDLED METHOD
 *      </li>
 *      <li>
 *          <h3>POST</h3> :: UNHANDLED METHOD
 *      </li>
 *      <li>
 *          <h3>PUT</h3> :: UNHANDLED METHOD
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
    name = "FilterServlet",
    urlPatterns = {
        "/filters"
    }
)
public class FilterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletUtils.setupResponse(resp);
        JsonObject response;
        List<String> staticDatasRequest = Arrays.asList(req.getParameterValues("datas"));
        try {
            if (! staticDatasRequest.isEmpty()) {
                response = OffresService.getStaticDatas(staticDatasRequest);
                ServletUtils.writeResponse(resp, response);
            } else {
                ServletUtils.unhandledRequest(resp);
            }
        } catch (BadRequestException e) {
            Logger.getLogger().SubmitError("FilterServlet called with GET", e);
            ServletUtils.writeResponse(
                resp,
                ResponseTool.serviceRejected("Method called with wrong parameters", ResponseTool.CLIENT_ERROR)
            );
        }
    }

    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletUtils.unhandledRequest(resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletUtils.unhandledRequest(resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletUtils.unhandledRequest(resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletUtils.unhandledRequest(resp);
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletUtils.unhandledRequest(resp);
    }
}
