package fr.pc3r.jetrouvemonjob.services;

import com.google.gson.JsonObject;
import fr.pc3r.jetrouvemonjob.beans.BadRequestException;
import fr.pc3r.jetrouvemonjob.beans.Logger;
import fr.pc3r.jetrouvemonjob.beans.ResponseTool;
import fr.pc3r.jetrouvemonjob.beans.User;
import fr.pc3r.jetrouvemonjob.database.DBTool;
import fr.pc3r.jetrouvemonjob.database.DataBase;
import org.apache.commons.lang3.RandomStringUtils;

import java.sql.Connection;
import java.sql.SQLException;

public class UserService {

    public static JsonObject connectUser(User req_user) throws BadRequestException {
        Connection c = null;
        try {
            c = DataBase.getMySQLConnection();
            User user;
            user = DBTool.getUser(c, req_user.getUsername(), req_user.getPasswordHash());
            JsonObject res = ResponseTool.serviceAccepted();
            if (user.equals(User.EMPTY_USER)) {
                res = ResponseTool.serviceRejected("User not found (Wrong username/password)",ResponseTool.CLIENT_ERROR);
            } else {
                String sessionKey = getSessionKey(c, user);
                if (sessionKey.equals("")) {
                    Logger.getLogger().SubmitError("connecting user | updating session key", new BadRequestException());
                    return ResponseTool.serviceRejected(
                        "Internal connection error",
                        ResponseTool.SQL_ERROR
                    );
                }
                res.addProperty("results", sessionKey);
            }
            c.close();
            return res;
        } catch (SQLException e) {
        	System.out.println(e);
            Logger.getLogger().SubmitError("connecting user | SQL Error", e);
            return ResponseTool.serviceRejected(
                "Internal connection error",
                ResponseTool.SQL_ERROR
            );
        }
    }

    public static JsonObject disconnectUser(String username, String sessionKey) throws BadRequestException {
        Connection c = null;
        try {
            c = DataBase.getMySQLConnection();
            boolean correct = DBTool.removeConnexion(c, username, sessionKey);
            if (! correct) {
                return ResponseTool.serviceRejected(
                    "User not found (Wrong username/password)",
                    ResponseTool.CLIENT_ERROR
                );
            }
            JsonObject res = ResponseTool.serviceAccepted();
            res.addProperty("result", "User correctly disconnected");
            return res;
        } catch (SQLException e) {
            Logger.getLogger().SubmitError("disconnecting user | SQL Error", e);
            return ResponseTool.serviceRejected(
                "Internal connection error",
                ResponseTool.SQL_ERROR
            );
        }
    }

    public static JsonObject createUser(User user) throws BadRequestException {
        try {
            Connection c = DataBase.getMySQLConnection();
            boolean correct = DBTool.checkUserExist(c, user.getUsername(), user.getMail());
            if(correct) {
                return ResponseTool.serviceRejected(
                    "User already exists (username/password)",
                    ResponseTool.CLIENT_ERROR
                );
            }
            correct = DBTool.insertUser(
                c,
                user.getUsername(),
                user.getPasswordHash(),
                user.getNom(),
                user.getPrenom(),
                user.getMail()
            );
            if (! correct) {
                return ResponseTool.serviceRejected(
                    "Could not insert User",
                    ResponseTool.CLIENT_ERROR
                );
            }
            JsonObject res = ResponseTool.serviceAccepted();
            res.addProperty("result", "User correctly created");
            return res;
        } catch (SQLException e) {
            Logger.getLogger().SubmitError("creating user | SQL Error", e);
            return ResponseTool.serviceRejected(
                "Internal connection error",
                ResponseTool.SQL_ERROR
            );
        }
    }

    public static JsonObject removeUser(String username, String sessionkey) throws BadRequestException {
        try {
            Connection c = DataBase.getMySQLConnection();
            boolean correct = DBTool.deleteUser(c, username, sessionkey);
            if (! correct) {
                return ResponseTool.serviceRejected(
                    "User not found (Wrong username)",
                    ResponseTool.CLIENT_ERROR
                );
            }
            JsonObject res = ResponseTool.serviceAccepted();
            res.addProperty("result", "User correctly removed");
            return res;
        } catch (SQLException e) {
            Logger.getLogger().SubmitError("Deleting user | SQL Error", e);
            return ResponseTool.serviceRejected(
                "Internal connection error",
                ResponseTool.SQL_ERROR
            );
        }
    }

    public static JsonObject retrieveDataUser(String username, String sessionkey) throws BadRequestException {
        try {
            Connection c = DataBase.getMySQLConnection();
            User user = DBTool.getUserSession(c, username, sessionkey);
            if (user.equals(User.EMPTY_USER)) {
                return ResponseTool.serviceRejected(
                    "User not found (Wrong username)",
                    ResponseTool.CLIENT_ERROR
                );
            }
            JsonObject res = ResponseTool.serviceAccepted();
            JsonObject data = ResponseTool.getJsonObjectFromJavaObject(user);
            res.add("result", data);
            return res;
        } catch (SQLException e) {
            Logger.getLogger().SubmitError("Retrieve Data user | SQL Error", e);
            return ResponseTool.serviceRejected(
                "Internal connection error",
                ResponseTool.SQL_ERROR
            );
        }
    }

    public static JsonObject addOffreToBookmark(
        String idOffre,
        String username,
        String sessionkey
    ) throws BadRequestException {
        Connection c = null;
        try {
            c = DataBase.getMySQLConnection();
            boolean correct = DBTool.getUserSession(c, username, sessionkey).equals(User.EMPTY_USER);
            if (!correct) {
               throw new BadRequestException();
            }
            JsonObject res;
            correct = DBTool.toogleOffreToBookmark(c, idOffre, username);
            if (!correct) {
                res = ResponseTool.serviceRejected(
                    "Could not add to bookmark",
                    ResponseTool.SQL_ERROR
                );
            } else {
                res = ResponseTool.serviceAccepted();
                res.addProperty("result", "offer successfully added to bookmark");
            }
            return res;
        } catch (SQLException e) {
            Logger.getLogger().SubmitError("connecting user | SQL Error", e);
            return ResponseTool.serviceRejected(
                "Internal connection error",
                ResponseTool.SQL_ERROR
            );
        }
    }

    // ===============================================================================================
    //                                          UTILITIES
    // ===============================================================================================

    private static String getSessionKey(Connection c, User user) throws SQLException {
        if (mustUpdateSessionKey(c, user.getUsername(), user.getPasswordHash())) {
            String sessionKey = generateSessionKey();
            boolean correct = DBTool.updateSessionKey(
                c,
                user.getUsername(),
                user.getPasswordHash(),
                sessionKey,
                System.currentTimeMillis()
            );
            if (! correct) {
                correct = DBTool.insertConnexion(
                    c,
                    user.getUsername(),
                    user.getPasswordHash(),
                    sessionKey,
                    System.currentTimeMillis()
                );
                if (! correct) {
                    return "";
                }
                return sessionKey;
            }
            return sessionKey;
        } else {
            return DBTool.getLastSessionKey(c, user.getUsername(), user.getPasswordHash());
        }
    }
    private static boolean mustUpdateSessionKey(Connection c, String username, String password) throws SQLException {
        long lastConn = DBTool.getLastSessionKeyTimestamp(c, username, password);
        if (lastConn == 0) {
            return true;
        }
        long currentTime = System.currentTimeMillis();
        return currentTime - lastConn > 30 * 60 * 1000;
    }
    private static String generateSessionKey() {
        return RandomStringUtils.random(16,true,true);
    }
}
