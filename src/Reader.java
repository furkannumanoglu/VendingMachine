import java.sql.*;
//DBDEKİ STRİNG AUTHORİTY Yİ BOOL OLARAK TUT VERİMLİLİK AÇISINDAN!
//ADD ADMİN FONKSYİONUNU DOLDUR
//İS ADMİN EXİSTTEKİ GİBİ İF ELSELERİ DÜZELT VE PASSWORDDAKİ
public class Reader {
    private ResultSet resultSet;
    private final DatabaseConnection databaseConnection = new DatabaseConnection();
    public ResultSet getResultSet() {
        return resultSet;
    }
    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }
    public void changeLineStock(String choice, int itemNo, Double changedPrice) {
        try (Connection connection = databaseConnection.getConnection();
             Statement statement = connection.createStatement()) {
            String tableName = choice.equals("A") ? "Drinks" : "Snacks";
            String sqlQuery = "SELECT * FROM public.\"" + tableName + "\" ORDER BY \"ID\" ASC;";
            resultSet = statement.executeQuery(sqlQuery);

            boolean isContinue = true;
            while (isContinue && resultSet.next()) {
                int id = resultSet.getInt("ID");
                int quantity = resultSet.getInt("Quantity");

                if (id == itemNo) {
                    String sqlUpdate = "UPDATE public.\"" + tableName + "\" SET \"Quantity\" = ? WHERE \"ID\" = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdate);

                    int newQuantity = quantity - 1;
                    preparedStatement.setInt(1, newQuantity);
                    preparedStatement.setInt(2, id);

                    preparedStatement.executeUpdate();
                    isContinue = false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void setPrice(String choice, int itemNo, Double updatedPrice){
        try (Connection connection = databaseConnection.getConnection();
             Statement statement = connection.createStatement()) {

            String tableName = choice.equals("A") ? "Drinks" : "Snacks";
            String sqlQuery = "SELECT * FROM public.\"" + tableName + "\" ORDER BY \"ID\" ASC;";
            resultSet = statement.executeQuery(sqlQuery);

            boolean isContinue = true;
            while (isContinue && resultSet.next()) {
                int id = resultSet.getInt("ID");
                double price = resultSet.getInt("Price");

                if (id == itemNo) {
                    String sqlUpdate = "UPDATE public.\"" + tableName + "\" SET \"Price\" = ? WHERE \"ID\" = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdate);

                    double newPrice = updatedPrice;
                    preparedStatement.setDouble(1, newPrice);
                    preparedStatement.setInt(2, id);

                    preparedStatement.executeUpdate();
                    isContinue = false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void setStock(String choice, int itemNo, int changeAmount) {

        try (Connection connection = databaseConnection.getConnection();
             Statement statement = connection.createStatement()) {

            String tableName = choice.equals("A") ? "Drinks" : "Snacks";
            String sqlQuery = "SELECT * FROM public.\"" + tableName + "\" ORDER BY \"ID\" ASC;";
            resultSet = statement.executeQuery(sqlQuery);

            boolean isContinue = true;
            while (isContinue && resultSet.next()) {
                int id = resultSet.getInt("ID");
                int quantity = resultSet.getInt("Quantity");

                if (id == itemNo) {
                    String sqlUpdate = "UPDATE public.\"" + tableName + "\" SET \"Quantity\" = ? WHERE \"ID\" = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdate);

                    int newQuantity = changeAmount;
                    preparedStatement.setInt(1, newQuantity);
                    preparedStatement.setInt(2, id);

                    preparedStatement.executeUpdate();
                    isContinue = false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deleteItem(String tableName, int urunID){
        DatabaseConnection databaseConnection = new DatabaseConnection();
        try (Connection connection = databaseConnection.getConnection();
             Statement statement = connection.createStatement()) {

            String sqlQuery = "DELETE FROM \""+tableName+"\" WHERE \"ID\" = " + urunID;
            statement.executeUpdate(sqlQuery);

        } catch (SQLException b) {
            b.printStackTrace();
        }
    }
    public String authorityControl(Admin admin){
        try (Connection connection = databaseConnection.getConnection();
             Statement statement = connection.createStatement()) {
            String sqlQuery = "SELECT * FROM public.\"Admins\" WHERE \"adminName\" = '" + admin.getUserName() + "';";
            resultSet = statement.executeQuery(sqlQuery);
            resultSet.next();
            Integer authority = resultSet.getInt(3);
            if(authority == 1){
                return "Yes";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "No";

    }
    public void changeName(String tableName, int urunID, String newName){
        DatabaseConnection databaseConnection = new DatabaseConnection();
        try (Connection connection = databaseConnection.getConnection();
             Statement statement = connection.createStatement()) {
            String sqlQuery = "";
            if(tableName.equals("Drinks")){
                sqlQuery = "UPDATE \"Drinks\"" +
                        " SET \"Description\" = '" + newName + "'" +
                        " WHERE \"ID\" = " + urunID;
            }
            else if(tableName.equals("Snacks")){
                sqlQuery = "UPDATE \"Snacks\"" +
                        " SET \"Description\" = '" + newName + "'" +
                        " WHERE \"ID\" = " + urunID;
            }

            statement.executeUpdate(sqlQuery);

        } catch (SQLException b) {
            b.printStackTrace();
        }

    }
    public void addProduct(){

    }
    public void addAdmin(){

    }

    public boolean isAdminExist(String userName){
        try (Connection connection = databaseConnection.getConnection();
             Statement statement = connection.createStatement()) {

            String sqlQuery = "SELECT * FROM public.\"Admins\" WHERE \"adminName\" = '" + userName + "';";

            resultSet = statement.executeQuery(sqlQuery);
            resultSet.next();

            String adminName = resultSet.getString("adminName");
            if(userName.equals(adminName)){
                return true;
            }
            else{
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean isPasswordOK(String userName, String password){
        try (Connection connection = databaseConnection.getConnection();
             Statement statement = connection.createStatement()) {

            String sqlQuery = "SELECT * FROM public.\"Admins\" WHERE \"adminName\" = '" + userName + "';";
            resultSet = statement.executeQuery(sqlQuery);
            resultSet.next();

            String adminName = resultSet.getString("adminName");
            String t_password = resultSet.getString("password");

            if(userName.equals(adminName) && password.equals(t_password)){
                return true;
            }
            else{
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
