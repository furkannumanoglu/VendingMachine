import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.Objects;

public class AdminManagement extends JFrame{
    JPanel MainPanel;
    private JTable table1;
    private JTextField userNameText;
    private JComboBox authorityBox;
    private JButton createButton;
    private JButton deleteButton;
    private JButton updateButton;
    private JScrollPane Table0;
    private JPasswordField passwordText;
    private JTextField nameField;
    private JButton backButton;

    public AdminManagement(Admin admin) throws SQLException {
        nameField.setText(admin.getUserName());
        table_load("Admins");
        authorityBox.addItem("Yes");
        authorityBox.addItem("No");
        createButton.addActionListener(new ActionListener() {
            //BUNU ADD ADMİNE BAĞLA!
            @Override
            public void actionPerformed(ActionEvent e) {
                String adminName = userNameText.getText();
                String password = new String(passwordText.getPassword());
                String aut = authorityBox.getSelectedItem().toString();
                int authority = aut.equals("Yes") ? 1 : 0;
                try {
                    if (adminName != "" && password != "") {
                        String tableName = "Admins";
                        DatabaseConnection databaseConnection = new DatabaseConnection();
                        try (Connection connection = databaseConnection.getConnection();
                             Statement statement = connection.createStatement()) {
                            String sqlQuery = "INSERT INTO \"Admins\" (\"adminName\", \"password\", \"Authority\") VALUES ('" + adminName + "', '" + password + "', '" + authority + "');";
                            int rowsAffected = statement.executeUpdate(sqlQuery);
                            table_load("Admins");

                        } catch (SQLException b) {
                            b.printStackTrace();
                        }
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Yeterli veri giriniz.");
                }
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DatabaseConnection databaseConnection = new DatabaseConnection();
                try (Connection connection = databaseConnection.getConnection();
                     Statement statement = connection.createStatement()) {
                    String tableName = "Admins";
                    String adminName = userNameText.getText();
                    String sqlQuery = "DELETE FROM \""+tableName+"\" WHERE \"adminName\" = '" + adminName+"'";
                    statement.executeUpdate(sqlQuery);
                    table_load("Admins");

                } catch (SQLException b) {
                    b.printStackTrace();
                }
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DatabaseConnection databaseConnection = new DatabaseConnection();
                try (Connection connection = databaseConnection.getConnection();
                     Statement statement = connection.createStatement()) {
                    String sqlQuery = "";
                    String authority = Objects.requireNonNull(authorityBox.getSelectedItem()).toString();
                    int aut = authority.equals("Yes") ? 1 : 0;
                    String name = userNameText.getText();
                    sqlQuery = "UPDATE \"Admins\"" +
                            " SET \"Authority\" = " + aut +
                            " WHERE \"adminName\" = '" + name+"'";

                    statement.executeUpdate(sqlQuery);
                    table_load("Admins");
                } catch (SQLException b) {
                    b.printStackTrace();
                }
            }
        });
        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                table1MouseClicked(e);
            }
        });
    }

    public void table_load(String tableName) throws SQLException {
        table1.setVisible(true);
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection connection = databaseConnection.getConnection();
        String sqlTable = "SELECT * FROM public.\"" + tableName + "\";";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlTable);
            ResultSet res = preparedStatement.executeQuery();
            DefaultTableModel tblModel = new DefaultTableModel(new Object[]{"adminName", "Authority"}, 0);
            while (res.next()) {
                String adminName = String.valueOf(res.getString("adminName"));
                Integer aut = Integer.valueOf(res.getInt("Authority"));

                Object tbData[] = {adminName, aut};
                tblModel.addRow(tbData);
                table1.setModel(tblModel);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void table1MouseClicked(java.awt.event.MouseEvent evt){
        DefaultTableModel tblModel = (DefaultTableModel)table1.getModel();
        String tName = tblModel.getValueAt(table1.getSelectedRow(),0).toString();
        String tAuthority = tblModel.getValueAt(table1.getSelectedRow(), 1).toString();
        tAuthority = tAuthority.equals("1") ? "Yes" : "No";
        userNameText.setText(tName);
        authorityBox.setSelectedItem(tAuthority);
    }
}
