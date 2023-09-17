import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.Objects;

public class AdminPanel extends JFrame{
    private JTextField idField;
    private JTextField isimField;
    private JTextField priceField;
    private JTextField stockField;
    private JButton ürünSilButton;
    private JButton isimDeğiştirButton;
    private JButton fiyatGüncelleButton;
    private JButton stokGüncelleButton;
    private JButton logOutButton;
    private JButton yeniÜrünGirişiButton;
    private JComboBox menuChoiceButton;
    JPanel MainPanel;
    private JTextArea infoArea;
    private JTable table1;
    private JButton clearButton;
    private JButton superAdminButton;
    private JLabel nameLabel;

    public AdminPanel(Admin admin) throws SQLException {
        table_load("Drinks");

        menuChoiceButton.addItem("Drinks");
        menuChoiceButton.addItem("Snacks");
        superAdminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Reader reader = new Reader();
                if(reader.authorityControl(admin).equals("No")){
                    JOptionPane.showMessageDialog(null, "You have no authority!");
                }
                else{
                    AdminManagement adminManagement = null;
                    try {
                        adminManagement = new AdminManagement(admin);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    adminManagement.setContentPane(adminManagement.MainPanel);
                    adminManagement.setVisible(true);
                    adminManagement.setSize(600,300);
                }
            }
        });
        menuChoiceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    table_load(Objects.requireNonNull(menuChoiceButton.getSelectedItem()).toString());
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        stokGüncelleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int urunID = -1;
                int netStock = -1;
                try {
                    urunID = Integer.parseInt(idField.getText());
                    netStock = Integer.parseInt(stockField.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Enter valid number!");
                }
                if (urunID != -1 && netStock !=-1 ) {
                    infoArea.append("Stock change has been done");
                    Reader reader = new Reader();
                    String tableName = menuChoiceButton.getSelectedItem().equals("Drinks") ? "A" : "B";
                    reader.setStock(tableName, urunID, netStock);

                } else {
                    JOptionPane.showMessageDialog(null, "Please enter product and quantity informations.");
                }
                try {
                    table_load(Objects.requireNonNull(menuChoiceButton.getSelectedItem()).toString());
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        fiyatGüncelleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String priceText = priceField.getText();
                int urunID = -1;
                String urunIDText = idField.getText();
                Double updatedPrice = -1.0;
                try {
                    urunID = Integer.parseInt(urunIDText);
                    updatedPrice = Double.parseDouble(priceText);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Enter valid number.");
                }
                if (urunID != -1 && updatedPrice != -1.0) {
                    infoArea.append("Price change has been done.\n");
                    Reader reader = new Reader();
                    String tableName = menuChoiceButton.getSelectedItem().equals("Drinks") ? "A" : "B";
                    reader.setPrice(tableName, urunID, updatedPrice);

                } else {
                    JOptionPane.showMessageDialog(null, "Please enter product and price informations.");
                }
                try {
                    table_load(Objects.requireNonNull(menuChoiceButton.getSelectedItem()).toString());
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        ürünSilButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int urunID = -1;
                String urunIDText = idField.getText();
                try {
                    urunID = Integer.parseInt(urunIDText);

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Select item");
                }
                if (urunID != -1) {
                    infoArea.append("Delete process has been done!\n");
                } else {
                    JOptionPane.showMessageDialog(null, "Delete process has not done\n");
                }

                String tableName = (String) menuChoiceButton.getSelectedItem();
                Reader reader = new Reader();
                reader.deleteItem(tableName, urunID);

                try {
                    table_load(Objects.requireNonNull(menuChoiceButton.getSelectedItem()).toString());
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        isimDeğiştirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newName = isimField.getText();
                int urunID = -1;
                String urunIDText = idField.getText();
                try {
                    urunID = Integer.parseInt(urunIDText);
                    if (urunID != -1) {
                        infoArea.append("Name change process has done\n");
                    }
                    String tableName = (String) menuChoiceButton.getSelectedItem();
                    Reader reader = new Reader();
                    reader.changeName(tableName,urunID,newName);

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Enter valid number");
                }
                try {
                    table_load(Objects.requireNonNull(menuChoiceButton.getSelectedItem()).toString());
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        yeniÜrünGirişiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int urunID = -1;
                double updatedPrice = -1.0;
                int stock = -1;
                String urunIDText = idField.getText();
                String priceText = priceField.getText();
                String stockText = stockField.getText();
                String name = isimField.getText();
                try {
                    urunID = Integer.parseInt(urunIDText);
                    updatedPrice = Double.parseDouble(priceText);
                    stock = Integer.parseInt(stockText);
                    if (urunID != -1 && updatedPrice != -1 && stock != -1) {
                        infoArea.append("ID'si: " + urunID + "olan ürün eklendi."+ "\n");
                        String tableName = menuChoiceButton.getSelectedItem().toString();
                        DatabaseConnection databaseConnection = new DatabaseConnection();
                        try (Connection connection = databaseConnection.getConnection();
                             Statement statement = connection.createStatement()) {
                            if(tableName.equals("Drinks")){
                                String sqlQuery = "INSERT INTO \"Drinks\" (\"ID\",\"Description\",\"Price\",\"Quantity\") VALUES (" + urunID + ", '" + name + "', " + updatedPrice + ", " + stock + ");";
                                int rowsAffected = statement.executeUpdate(sqlQuery);
                            }
                            else if(tableName.equals("Snacks")){
                                String sqlQuery = "INSERT INTO \"Snacks\" (\"ID\",\"Description\",\"Price\",\"Quantity\") VALUES (" + urunID + ", '" + name + "', " + updatedPrice + ", " + stock + ");";
                                int rowsAffected = statement.executeUpdate(sqlQuery);
                            }
                        } catch (SQLException b) {
                            b.printStackTrace();
                        }
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Yeterli veri giriniz.");
                }
                try {
                    table_load(Objects.requireNonNull(menuChoiceButton.getSelectedItem()).toString());
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                infoArea.setText("");
            }
        });
        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                ProgramEnter programEnter = new ProgramEnter();
                programEnter.setContentPane(programEnter.MainPanel);
                programEnter.setVisible(true);
                programEnter.setSize(650, 450);
                programEnter.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        String sqlTable = "SELECT * FROM public.\"" + tableName + "\" ORDER BY \"ID\" ASC;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlTable);
            ResultSet res = preparedStatement.executeQuery();
            DefaultTableModel tblModel = new DefaultTableModel(new Object[]{"ID", "Name", "Price", "Quantity"}, 0);
            while (res.next()) {
                String id = String.valueOf(res.getInt("ID"));
                String name = String.valueOf(res.getString("Description"));
                String price = String.valueOf(res.getDouble("Price"));
                String quantity = String.valueOf(res.getInt("Quantity"));
                Object tbData[] = {id, name, price, quantity};
                tblModel.addRow(tbData);
                table1.setModel(tblModel);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    private void table1MouseClicked(java.awt.event.MouseEvent evt){
        DefaultTableModel tblModel = (DefaultTableModel)table1.getModel();

        String tId = tblModel.getValueAt(table1.getSelectedRow(),0).toString();
        String tName = tblModel.getValueAt(table1.getSelectedRow(),1).toString();
        String tPrice = tblModel.getValueAt(table1.getSelectedRow(), 2).toString();
        String tQuantity = tblModel.getValueAt(table1.getSelectedRow(), 3).toString();

        idField.setText(tId);
        isimField.setText(tName);
        priceField.setText(tPrice);
        stockField.setText(tQuantity);
    }
}
