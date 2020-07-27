package fr.pc3r.jetrouvemonjob.database;



import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBase {
    private static DataSource dataSource;

    private DataBase(String jndiname) throws SQLException {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/" + jndiname);
        } catch(NamingException e) {
            throw new SQLException(jndiname + "is missing in JNDI! : " + e.getMessage());
        }
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static Connection getMySQLConnection() throws SQLException {
        if(!DBStatic.mysql_pooling) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
            }
            return (DriverManager.getConnection(
                "jdbc:mysql://" +
                    DBStatic.mysql_host + "/" +
                    DBStatic.mysql_db +
                    "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",
                DBStatic.mysql_username,
                DBStatic.mysql_password)
            );
        } else {
            return new DataBase("jdbc/db").getConnection();
        }
    }

}
