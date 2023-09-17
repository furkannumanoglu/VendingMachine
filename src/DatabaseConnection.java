
import java.sql.*;

public class DatabaseConnection {
    private final String DB_URL = "jdbc:postgresql://localhost:5432/db_urun";
    private final String USER = "postgres";
    private final String PASSWORD = "password";
    private static Connection connection;
    private ResultSet resultSet = null;
    public String getDB_URL() {
        return DB_URL;
    }
    public String getUSER() {
        return USER;
    }
    public String getPASSWORD(){
        return PASSWORD;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    public static void setConnection(Connection connection) {
        DatabaseConnection.connection = connection;
    }

    public Connection getConnection() {
        try {
            connection = DriverManager.getConnection(this.getDB_URL(), this.getUSER(), this.getPASSWORD());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}

