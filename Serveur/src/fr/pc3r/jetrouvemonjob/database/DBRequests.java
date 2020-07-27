package fr.pc3r.jetrouvemonjob.database;

import java.util.Arrays;
import java.util.List;

class DBRequests {

    // ===============================================================================================
    //                                          RETRIEVE DATA
    // ===============================================================================================

    static final String GET_BOOKMARK_OFFER_SQL =
        "SELECT * " +
            "FROM users_bookmark AS ub " +
                "INNER JOIN users AS u ON u.id = ub.id_user " +
            "WHERE u.username = ? " +
            "AND ub.id_offre = ?";


    static String CHECK_USERNAME_PASSWORD_SQL = "SELECT * FROM users WHERE password=? AND username=?";

    static String CHECK_USERNAME_SESSIONKEY_SQL =
        "SELECT * " +
            "FROM users AS u , " +
            "user_connexion AS c , " +
            "users_bookmark AS ub, " +
            "cv, " +
            "experiences AS xp , " +
            "formations AS f , " +
            "competences AS comp ," +
            "user_like AS ul , " +
            "user_dislike AS ud " +
            "WHERE u.username=? " +
            "AND c.session_key = ? " +
            "AND c.id_user = u.id " +
            "AND u.id = ub.id_user " +
            "AND u.id_cv = cv.id_cv " +
            "AND xp.id_cv = cv.id_cv " +
            "AND f.id_cv = cv.id_cv " +
            "AND comp.id_cv = cv.id_cv " +
            "AND ul.id_user = u.id " +
            "AND ud.id_user = u.id";

    static String CHECK_CONNEXION_USER_SQL =
        "SELECT * FROM user_connexion AS c, users AS u " +
        "WHERE u.id = c.id_user " +
        "AND u.username = ? " +
        "AND u.password = ? ";

    static String CHECK_USER_EXISTS_SQL =
        "SELECT * FROM users WHERE username = ? OR mail = ?";
    static String CHECK_USER_EXISTS_SESSION_SQL =
        "SELECT * FROM user_connexion AS c, users AS u " +
            "WHERE u.id = c.id_user " +
            "AND u.username = ? " +
            "AND c.session_key = ? ";

    static final String GET_COMMENTAIRES_OFFERS_SQL =
        "SELECT * FROM commentaires WHERE id_offre = ?";
    // ===============================================================================================
    //                                          UPDATE DATA
    // ===============================================================================================
    static String UPDATE_SESSION_KEY_SQL =
        "UPDATE user_connexion AS c \n" +
            "    INNER JOIN users AS u ON c.id_user = u.id\n" +
            "SET c.session_key=?, c.last_connexion=?\n" +
            "WHERE u.username=?\n" +
            "AND u.password=?";

    static final String ADD_LIKE_COMMENT_SQL =
        "UPDATE commentaires AS c " +
            "SET c.nb_likes = c.nb_likes + 1 " +
            "WHERE c.id_comm = ?";

    static final String ADD_DISLIKE_COMMENT_SQL =
        "UPDATE commentaires AS c " +
            "SET c.nb_likes = c.nb_dislikes + 1 " +
            "WHERE c.id_comm = ?";

    // ===============================================================================================
    //                                          INSERT DATA
    // ===============================================================================================

    static final String INSERT_EMPTY_CV_SQL =
        "INSERT INTO cv (titre) VALUE ('Empty CV')";

    static final List<String> REQUESTS_INSERTION_EMPTY_CV_SQL = Arrays.asList(
        "INSERT INTO experiences (id_cv) VALUE (?)",
        "INSERT INTO competences (id_cv) VALUE (?)",
        "INSERT INTO formations (id_cv) VALUE (?)"
    );

    static final List<String> REQUESTS_INSERTION_EMPTY_LIKES_SQL = Arrays.asList(
        "INSERT INTO user_like (id_user) VALUE (?)",
        "INSERT INTO user_dislike (id_user) VALUE (?);"
    );
    static final String INSERT_EMPTY_BOOKMARK_SQL =
        "INSERT INTO users_bookmark (id_user, id_offre) VALUE (?, '')";

    static final String ADD_COMMENT_OFFER_SQL =
        "INSERT INTO commentaires " +
            "(id_user, id_offre, content_com, nb_likes, nb_dislikes) " +
            "VALUE ((SELECT id FROM users WHERE username = ?), ?, ?, 0, 0)";

    static final String ADD_BOOKMARK_USER_SQL =
        "INSERT INTO users_bookmark (id_user, id_offre) " +
            "VALUES ((SELECT id FROM users WHERE username=?), ?)";

    static String INSERT_CONNEXION_SQL =
        "INSERT INTO user_connexion (id_user, session_key, last_connexion) " +
            "VALUES ((SELECT id FROM users WHERE username=? AND password = ?), ?, ?)";

    static String INSERT_USER_SQL =
        "INSERT INTO users (username, nom, prenom, mail, password, id_cv) " +
            "VALUES (?, ?, ?, ?, ?, ?)";

    static final String ADD_LIKE_USER_SQL =
        "INSERT INTO user_like (id_user, id_commentaire) " +
            "VALUES (" +
            "(SELECT id " +
                "FROM users, user_connexion " +
                "WHERE username = ? " +
                "AND session_key = ? " +
                "AND users.id = user_connexion.id_user" +
            "), ?)";

    static final String ADD_DISLIKE_USER_SQL =
        "INSERT INTO user_dislike (id_user, id_commentaire) " +
            "VALUES (" +
            "(SELECT id " +
            "FROM users, user_connexion " +
            "WHERE username = ? " +
            "AND session_key = ? " +
            "AND users.id = user_connexion.id_user" +
            "), ?)";

    // ===============================================================================================
    //                                          DELETE DATA
    // ===============================================================================================

    static final String REMOVE_BOOKMARK_USER_SQL =
        "DELETE FROM users_bookmark AS ub " +
                "INNER JOIN users AS u ON ub.id_user = u.id " +
            "WHERE u.username = ? " +
            "AND ub.id_offre = ?";

    static String REMOVE_CONNEXION_SESSION_SQL =
        "DELETE FROM user_connexion AS c " +
            "INNER JOIN users AS u ON c.id_user = u.id " +
            "WHERE u.username = ? " +
            "AND c.session_key = ?";

    static String REMOVE_USER_SQL =
        "DELETE FROM users AS u " +
            "INNER JOIN user_connexion AS c ON c.id_user = u.id " +
            "WHERE username = ? " +
            "AND c.session_key = ?";
}
