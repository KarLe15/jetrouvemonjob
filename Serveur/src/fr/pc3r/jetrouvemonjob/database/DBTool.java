package fr.pc3r.jetrouvemonjob.database;

import fr.pc3r.jetrouvemonjob.beans.*;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class DBTool {
    // this is where methods using requests will be

    public static User getUser(
        Connection c,
        String username,
        String passwordHash
    ) throws SQLException {
        try {
            PreparedStatement statement = c.prepareStatement(DBRequests.CHECK_USERNAME_PASSWORD_SQL);
            statement.setString(1,passwordHash);
            statement.setString(2,username);
            ResultSet result = statement.executeQuery();
            if (!result.next()){
                return User.EMPTY_USER;
            }
            return new User(
                result.getString("username"),
                result.getString("nom"),
                result.getString("prenom"),
                result.getString("mail"),
                result.getString("password"),
                result.getInt("id_cv")
            );
        } catch (SQLException e) {
            Logger.getLogger().SubmitError("connecting user",e);
            throw e;
        }
    }

    public static User getUserSession(
        Connection c,
        String username,
        String sessionkey
    ) throws SQLException {
        try {
            PreparedStatement statement = c.prepareStatement(DBRequests.CHECK_USERNAME_SESSIONKEY_SQL);
            statement.setString(1, username);
            statement.setString(2, sessionkey);
            ResultSet result = statement.executeQuery();

            Set<Competence> competences = new HashSet<>();
            Set<Experience> experiences = new HashSet<>();
            Set<Formation> formations = new HashSet<>();
            Set<Bookmark> bookmarks = new HashSet<>();
            Set<Like> likes = new HashSet<>();
            Set<Like> dislikes = new HashSet<>();
            String musername = "";
            String nom = "";
            String prenom = "";
            String mail = "";
            while (result.next()) {
                musername = result.getString("username");
                nom = result.getString("nom");
                prenom = result.getString("prenom");
                mail = result.getString("mail");
                Bookmark bookmarkTMP = new Bookmark(result.getString("ub.id_offre"));
                bookmarks.add(bookmarkTMP);
                Competence competenceTMP = new Competence(result.getString("comp.nom_comp"));
                competences.add(competenceTMP);
                Experience xpTMP = new Experience(
                    result.getInt("xp.debut_mois"),
                    result.getInt("xp.debut_annee"),
                    result.getInt("xp.fin_mois"),
                    result.getInt("xp.fin_annee"),
                    result.getString("xp.entreprise"),
                    result.getString("xp.post_description"),
                    result.getString("xp.intitule")
                );
                experiences.add(xpTMP);
                Formation formationTMP = new Formation(
                    result.getInt("f.debut_mois"),
                    result.getInt("f.debut_annee"),
                    result.getInt("f.fin_mois"),
                    result.getInt("f.fin_annee"),
                    result.getString("f.etablissement"),
                    result.getString("f.nom_diplome"),
                    result.getString("f.niveau_etude")
                );
                formations.add(formationTMP);
                Like likeTMP = new Like(result.getString("ul.id_commentaire"));
                Like dislikeTMP = new Like(result.getString("ud.id_commentaire"));
                likes.add(likeTMP);
                dislikes.add(dislikeTMP);
            }
            System.out.println("===========> nom " + nom + " prenom " +  prenom);
            if (nom.equals("")){
                return User.EMPTY_USER;
            }
            CV cv = new CV(competences, experiences, formations);
            return new User(musername, nom, prenom, mail, cv, bookmarks, likes, dislikes);
        } catch (SQLException e) {
            Logger.getLogger().SubmitError("connecting user",e);
            throw e;
        }
    }

    public static long getLastSessionKeyTimestamp(
        Connection c,
        String username,
        String passwordHash
    ) throws SQLException {
        try {
            PreparedStatement statement = c.prepareStatement(DBRequests.CHECK_CONNEXION_USER_SQL);
            statement.setString(1, username);
            statement.setString(2, passwordHash);
            ResultSet result = statement.executeQuery();
            if (! result.next()) {
                return 0;
            }
            return result.getLong("last_connexion");
        } catch (SQLException e) {
            Logger.getLogger().SubmitError("getting previous sessionKey", e);
            throw e;
        }
    }

    public static String getLastSessionKey(
        Connection c,
        String username,
        String passwordHash
    ) throws SQLException {
        try {
            PreparedStatement statement = c.prepareStatement(DBRequests.CHECK_CONNEXION_USER_SQL);
            statement.setString(1, username);
            statement.setString(2, passwordHash);
            ResultSet result = statement.executeQuery();
            if (!result.next()) {
                return "";
            }
            return result.getString("session_key");
        } catch (SQLException e) {
            Logger.getLogger().SubmitError("getting previous sessionKey", e);
            throw e;
        }
    }

    public static boolean updateSessionKey(
        Connection c,
        String username,
        String password,
        String sessionKey,
        long newTime
    ) throws SQLException {
        try {
            PreparedStatement statement = c.prepareStatement(DBRequests.UPDATE_SESSION_KEY_SQL);
            statement.setString(1,sessionKey);
            statement.setString(2, String.valueOf(newTime));
            statement.setString(3,username);
            statement.setString(4,password);
            int res = statement.executeUpdate();
            return res == 1;
        } catch (SQLException e) {
            Logger.getLogger().SubmitError("update session key", e);
            throw e;
        }
    }

    public static boolean insertConnexion(
        Connection c,
        String username,
        String password,
        String sessionKey,
        long newTime
    ) throws SQLException {
        try {
            PreparedStatement statement = c.prepareStatement(DBRequests.INSERT_CONNEXION_SQL);
            statement.setString(1,username);
            statement.setString(2,password);
            statement.setString(3,sessionKey);
            statement.setString(4, String.valueOf(newTime));
            int res = statement.executeUpdate();
            return res == 1;
        } catch (SQLException e) {
            Logger.getLogger().SubmitError("Insert session key", e);
            throw e;
        }
    }

    public static boolean removeConnexion(
        Connection c,
        String username,
        String session_key
    ) throws SQLException {
        try {
            PreparedStatement statement = c.prepareStatement(DBRequests.REMOVE_CONNEXION_SESSION_SQL);
            statement.setString(1, username);
            statement.setString(2, session_key);
            int res = statement.executeUpdate();
            return res == 1;
        } catch (SQLException e) {
            Logger.getLogger().SubmitError("Remove Connexion", e);
            throw e;
        }
    }

    public static boolean insertUser(
        Connection c,
        String username,
        String password,
        String nom,
        String prenom,
        String mail
    ) throws SQLException {
        try {
            PreparedStatement statement;
            int cv_id;
            int user_id;
            // create CV first
            statement = c.prepareStatement(DBRequests.INSERT_EMPTY_CV_SQL, Statement.RETURN_GENERATED_KEYS);
            int created = statement.executeUpdate();
            if (created == 0 ) {
                throw new SQLException("Creating user failed, Could not create CV EMPTY");
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    cv_id = generatedKeys.getInt(1);
                }
                else {
                    throw new SQLException("Creating EMPTY CV failed, no ID obtained.");
                }
            }
            for (String request : DBRequests.REQUESTS_INSERTION_EMPTY_CV_SQL) {
                statement = c.prepareStatement(request);
                statement.setInt(1,cv_id);
                created = statement.executeUpdate();
                if (created != 1) {
                    throw new SQLException("Creating EMPTY CV failed, no ID obtained.");
                }
            }
            statement = c.prepareStatement(DBRequests.INSERT_USER_SQL, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, username);
            statement.setString(2, nom);
            statement.setString(3, prenom);
            statement.setString(4, mail);
            statement.setString(5, password);
            statement.setInt(6, cv_id);

            created = statement.executeUpdate();
            if (created != 1) {
                throw new SQLException("Creating user failed.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user_id = generatedKeys.getInt(1);
                }
                else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
            // create empty bookmarks
            statement = c.prepareStatement(DBRequests.INSERT_EMPTY_BOOKMARK_SQL);
            statement.setInt(1, user_id);
            created = statement.executeUpdate();

            // create empty like
            // create empty dislike
            for (String request : DBRequests.REQUESTS_INSERTION_EMPTY_LIKES_SQL) {
                statement = c.prepareStatement(request);
                statement.setInt(1, user_id);
                created = statement.executeUpdate();
                if (created != 1) {
                    throw new SQLException("Creating EMPTY CV failed, no ID obtained.");
                }
            }

            return created == 1;
        } catch (SQLException e) {
            Logger.getLogger().SubmitError("Insert User", e);
            throw e;
        }
    }

    public static boolean checkUserExist(
        Connection c,
        String username,
        String mail
    ) throws SQLException {
        try {
            PreparedStatement statement = c.prepareStatement(DBRequests.CHECK_USER_EXISTS_SQL);
            statement.setString(1, username);
            statement.setString(2, mail);
            ResultSet res = statement.executeQuery();
            return res.next();
        } catch (SQLException e) {
            Logger.getLogger().SubmitError("Remove Connexion", e);
            throw e;
        }
    }

    public static boolean checkUserExistSession(
        Connection c,
        String username,
        String sessionkey
    ) throws SQLException {
        try {
            PreparedStatement statement = c.prepareStatement(DBRequests.CHECK_USER_EXISTS_SESSION_SQL);
            statement.setString(1, username);
            statement.setString(2, sessionkey);
            ResultSet res = statement.executeQuery();
            return res.next();
        } catch (SQLException e) {
            Logger.getLogger().SubmitError("Remove Connexion", e);
            throw e;
        }
    }

    public static boolean deleteUser(
        Connection c,
        String username,
        String sessionkey
    ) throws SQLException {
        try {
            PreparedStatement statement = c.prepareStatement(DBRequests.REMOVE_USER_SQL);
            statement.setString(1, username);
            statement.setString(2, sessionkey);
            int res = statement.executeUpdate();
            return res == 1;
        } catch (SQLException e) {
            Logger.getLogger().SubmitError("Remove User", e);
            throw e;
        }
    }

    public static boolean likeComment(
        Connection c,
        String username,
        String sessionkey,
        int idComm,
        boolean like
    ) throws SQLException {
        try {
            PreparedStatement statement;
            if (like) {
                statement = c.prepareStatement(DBRequests.ADD_LIKE_COMMENT_SQL);
            } else {
                statement = c.prepareStatement(DBRequests.ADD_DISLIKE_COMMENT_SQL);
            }
            statement.setInt(1, idComm);
            int res = statement.executeUpdate();
            if (res != 1) {
              return false;
            }
            if (like) {
                statement = c.prepareStatement(DBRequests.ADD_LIKE_USER_SQL);
            } else {
                statement = c.prepareStatement(DBRequests.ADD_DISLIKE_USER_SQL);
            }
            statement.setString(1, username);
            statement.setString(2, sessionkey);
            statement.setInt(3,idComm);
            return  statement.executeUpdate() == 1;
        } catch (SQLException e) {
            Logger.getLogger().SubmitError("Remove User", e);
            throw e;
        }
    }

    public static Set<Commentaire> getCommentaireOfOffre(
        Connection c,
        String idOffre
    ) throws SQLException {
        try {
            PreparedStatement statement = c.prepareStatement(DBRequests.GET_COMMENTAIRES_OFFERS_SQL);
            statement.setString(1, idOffre);
            ResultSet resultSet = statement.executeQuery();
            Set<Commentaire> res = new HashSet<>();
            while (resultSet.next()) {
                String content_comm = resultSet.getString("content_com");
                int nb_likes = resultSet.getInt("nb_likes");
                int nb_dislikes = resultSet.getInt("nb_dislikes");
                int id_com = resultSet.getInt("id_com");
                int id_user = resultSet.getInt("id_user");
                res.add(new Commentaire(content_comm,nb_likes, nb_dislikes, id_com, id_user));
            }
            return res;
        } catch (SQLException e) {
            Logger.getLogger().SubmitError("Get Commentaires Of Offers", e);
            throw e;
        }
    }

    public static boolean toogleOffreToBookmark(
        Connection c,
        String idOffre,
        String username
    ) throws SQLException {
        try {
            PreparedStatement statement = c.prepareStatement(DBRequests.GET_BOOKMARK_OFFER_SQL);
            statement.setString(1, idOffre);
            statement.setString(2, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                statement = c.prepareStatement(DBRequests.REMOVE_BOOKMARK_USER_SQL);
            } else {
                statement = c.prepareStatement(DBRequests.ADD_BOOKMARK_USER_SQL);
            }
            statement.setString(1, username);
            statement.setString(2, idOffre);
            int res = statement.executeUpdate();
            return res == 1;
        } catch (SQLException e) {
            Logger.getLogger().SubmitError("Get Commentaires Of Offers", e);
            throw e;
        }
    }

    public static int addCommentToOffer(
        Connection c,
        String username,
        String idOffre,
        String contentMessage
    ) throws SQLException {
        try {
            PreparedStatement statement = c.prepareStatement(DBRequests.ADD_COMMENT_OFFER_SQL, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, username);
            statement.setString(2, idOffre);
            statement.setString(3, contentMessage);
            int res = statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
                else {
                    throw new SQLException("Creating comment failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            Logger.getLogger().SubmitError("Get Commentaires Of Offers", e);
            throw e;
        }
    }
}
