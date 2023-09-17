import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.ArrayList;

public class GuestPanel extends JFrame{
    private JTextField textField2;
    private JButton addMoneyButton;
    private JButton orderButton;
    private JButton toMenuButton;
    JPanel MainPanel;
    private JComboBox<String> menuChoiceButton;
    private JButton alısverisiBitirButton;
    private JTable table1;
    private JScrollPane sc;
    private JLabel label1;
    private JLabel label2;
    private JLabel balanceLabel;
    private String menuChoice = "";
    public GuestPanel() throws SQLException {
        table_load("Drinks");
        VendingMachine vendingMachine = new VendingMachine();
        Consumer consumer = new Consumer(0);
        menuChoiceButton.addItem("Drinks");
        menuChoiceButton.addItem("Snacks");
        menuChoiceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tableName = "Drinks";
                if(menuChoiceButton.getSelectedItem().equals("Snacks")){
                    tableName = "Snacks";
                }
                try {
                    table_load(tableName);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        addMoneyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Double money = Double.parseDouble(textField2.getText());
                consumer.moneyAmount += money;
                balanceLabel.setText("Wallet: "+consumer.moneyAmount+ " $");
            }
        });
        orderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String selectedProduct = "";
                if (menuChoiceButton.getSelectedItem().equals("Drinks")) {
                    selectedProduct = label2.getText();
                    String sqlQueryPrice ="";
                    String sqlQueryID="";
                    String sqlQueryQuantity="";
                    String sqlQueryDescription="";

                    try {
                        DatabaseConnection databaseConnection = new DatabaseConnection();
                        Statement statement = databaseConnection.getConnection().createStatement();
                        sqlQueryPrice = "SELECT \"Price\" FROM \"Drinks\" WHERE \"Description\"='" + selectedProduct + "'";
                        sqlQueryID = "SELECT \"ID\" FROM \"Drinks\" WHERE \"Description\"='" + selectedProduct + "'";
                        sqlQueryQuantity = "SELECT \"Quantity\" FROM \"Drinks\" WHERE \"Description\"='" + selectedProduct + "'";

                        ResultSet resultSetPrice = statement.executeQuery(sqlQueryPrice);

                        int itemNo=-1;
                        double price = 0;
                        while (resultSetPrice.next()) {
                            price = resultSetPrice.getDouble("Price");
                        }
                        ResultSet resultSetID = statement.executeQuery(sqlQueryID);
                        while (resultSetID.next()) {
                            itemNo = resultSetID.getInt("ID");
                        }
                        int quantity=-1;
                        ResultSet resultSetQuantity = statement.executeQuery(sqlQueryQuantity);
                        while (resultSetQuantity.next()) {
                            quantity = resultSetQuantity.getInt("Quantity");
                        }
                        if(consumer.moneyAmount>=price){
                            Reader reader = new Reader();
                            String choice = "";

                            if(menuChoiceButton.getSelectedItem().equals("Drinks")){choice = "A";}
                            else if(menuChoiceButton.getSelectedItem().equals("Snacks")){choice = "B";}

                            if(quantity>0){
                                reader.changeLineStock(choice, itemNo, -1.0);
                                Urun urun = new Urun(itemNo,selectedProduct,price ,quantity);
                                consumer.moneyAmount -= price;
                                consumer.choicesList.add(urun);
                                balanceLabel.setText("Balance: "+consumer.moneyAmount+"\nEnjoy!");
                                table_load("Drinks");
                                }
                            else{
                                balanceLabel.setText("Balance: "+consumer.moneyAmount+"\nInsufficient stock! ");

                            }
                        }
                        if(consumer.moneyAmount<price){
                            balanceLabel.setText("Insufficient to order ("+consumer.moneyAmount+")");
                        }
                        resultSetPrice.close();
                        resultSetID.close();
                        resultSetQuantity.close();

                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }

                }
                else if (menuChoiceButton.getSelectedItem().equals("Snacks")) {
                    selectedProduct = label2.getText();
                    String sqlQueryPrice="";
                    String sqlQueryID="";
                    String sqlQueryQuantity="";

                    try {
                        DatabaseConnection databaseConnection = new DatabaseConnection();
                        Statement statement = databaseConnection.getConnection().createStatement();
                        sqlQueryPrice = "SELECT \"Price\" FROM \"Snacks\" WHERE \"Description\"='" + selectedProduct + "'";
                        sqlQueryID = "SELECT \"ID\" FROM \"Snacks\" WHERE \"Description\"='" + selectedProduct + "'";
                        sqlQueryQuantity = "SELECT \"Quantity\" FROM \"Snacks\" WHERE \"Description\"='" + selectedProduct + "'";

                        ResultSet resultSetPrice = statement.executeQuery(sqlQueryPrice);

                        int itemNo=-1;
                        double price = 0;

                        while (resultSetPrice.next()) {
                            price = resultSetPrice.getDouble("Price");
                        }

                        ResultSet resultSetID = statement.executeQuery(sqlQueryID);
                        while (resultSetID.next()) {
                            itemNo = resultSetID.getInt("ID");
                        }
                        int quantity=-1;
                        ResultSet resultSetQuantity = statement.executeQuery(sqlQueryQuantity);
                        while (resultSetQuantity.next()) {
                            quantity = resultSetQuantity.getInt("Quantity");
                        }
                        if(consumer.moneyAmount>=price){
                            consumer.moneyAmount -= price;
                            Reader reader = new Reader();
                            String choice = "";

                            if(quantity>0){
                                reader.changeLineStock(choice, itemNo, -1.0);

                                Urun urun = new Urun(itemNo,selectedProduct,price ,quantity);
                                consumer.choicesList.add(urun);
                                balanceLabel.setText("Balance: "+consumer.moneyAmount+"\nEnjoy!");
                                table_load("Snacks");

                            }
                            else{
                                balanceLabel.setText("Balance: "+consumer.moneyAmount+"\nInsufficient stock!");
                            }
                        }
                        else {
                            balanceLabel.setText("Insufficient ("+consumer.moneyAmount+"$) balance! ");
                        }

                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
    });
        toMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<Urun> list = consumer.getChoicesList();
                int size = list.size();
                String res = "You purchased: \n";
                for(int i = 0; i<size; i++){
                    res += i+1 + ". "+ list.get(i).getDescription() + "\n";
                }
                res += "Change: "+ consumer.moneyAmount;
                JOptionPane.showMessageDialog(null, res);
                dispose();
                ProgramEnter programEnter = new ProgramEnter();
                programEnter.setContentPane(programEnter.MainPanel);
                programEnter.setVisible(true);
                programEnter.setSize(650, 450);
                programEnter.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            }
        });
        alısverisiBitirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //
                ArrayList<Urun> list = consumer.getChoicesList();
                int size = list.size();
                String res = "You purchased: \n";
                for(int i = 0; i<size; i++){
                    res += i+1 + ". "+ list.get(i).getDescription() + "\n";
                }
                JOptionPane.showMessageDialog(null, res);
                System.exit(0);
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
        String tName = tblModel.getValueAt(table1.getSelectedRow(),1).toString();
        label2.setText(tName);
    }
}
