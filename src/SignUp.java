import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SignUp extends JFrame{
    private JTextField userNameField;
    JPanel MainPanel;
    private JPasswordField passwordField;
    private JButton buton;
    public SignUp(){
        buton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userNameFieldText = "";
                userNameFieldText = userNameField.getText();
                String passwordText = new String(passwordField.getPassword());

                Reader reader = new Reader();
                if(reader.isAdminExist(userNameFieldText)){
                    JOptionPane.showMessageDialog(null,"Try with another name");
                }
                else{
                    try {
                        if (!userNameFieldText.equals("") && !passwordText.equals("")) {
                            DatabaseConnection databaseConnection = new DatabaseConnection();
                            try (Connection connection = databaseConnection.getConnection();
                                 Statement statement = connection.createStatement()) {
                                String sqlQuery = "INSERT INTO \"Admins\" (\"adminName\", \"password\") VALUES ('" + userNameFieldText + "', '" + passwordText + "');";
                                int rowsAffected = statement.executeUpdate(sqlQuery);
                                JOptionPane.showMessageDialog(null, "Signed up successfully!. Returning main page...");
                                dispose();
                            } catch (SQLException b) {
                                b.printStackTrace();
                            }
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Try again!");
                    }
                }
            }
        });
    }
}


