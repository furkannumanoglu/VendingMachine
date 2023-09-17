import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class VendingMachine {
    private ArrayList<Urun> urunList = new ArrayList<>();
    private ResultSet resultSet = null;
    private final DatabaseConnection databaseConnection = new DatabaseConnection();
    public ArrayList<Urun> getUrunList() {
        return urunList;
    }
    public DatabaseConnection getDatabaseConnection() {
        return databaseConnection;
    }
    public ResultSet getResultSet() {
        return resultSet;
    }
    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }
    public void setUrunList(ArrayList<Urun> urunList) {
        this.urunList = urunList;
    }

}


