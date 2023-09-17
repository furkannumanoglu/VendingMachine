import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class ProgramEnter extends JFrame {
    private JTextField userNameField;
    private JPasswordField passwordField1;
    private JButton adminLogInButton;
    private JButton guestLogInButton;
    private JButton signUpButton;
    private JButton exitButton;
    JPanel MainPanel;
    public ProgramEnter(){
        adminLogInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = userNameField.getText();
                String sifre = new String(passwordField1.getPassword());
                Reader reader = new Reader();
                Admin admin = new Admin(name, sifre,"No");
                admin.authority = reader.authorityControl(admin);
                if(reader.isAdminExist(name) && reader.isPasswordOK(name, sifre)){
                    JOptionPane.showMessageDialog(null, "You logged in!", "Welcome", JOptionPane.WARNING_MESSAGE);
                    AdminPanel adminPanel = null;
                    try {
                        adminPanel = new AdminPanel(admin);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    adminPanel.setContentPane(adminPanel.MainPanel);
                    adminPanel.setVisible(true);
                    adminPanel.setSize(750, 400);
                    adminPanel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    dispose();
                }

                else if(reader.isAdminExist(name)){
                    JOptionPane.showMessageDialog(null, "Wrong password!", "Warning", JOptionPane.WARNING_MESSAGE);
                }
                else{
                    JOptionPane.showMessageDialog(null, "Try again!", "Warning", JOptionPane.WARNING_MESSAGE);
                }

            }
        });

        guestLogInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GuestPanel guestPanel = null;
                try {
                    guestPanel = new GuestPanel();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                guestPanel.setContentPane(guestPanel.MainPanel);
                guestPanel.setVisible(true);
                guestPanel.setSize(600, 400);
                guestPanel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                dispose();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SignUp signUp = new SignUp();
                signUp.setContentPane(signUp.MainPanel);
                signUp.setVisible(true);
                signUp.setSize(300,300);
            }
        });

    }
    public static void main(String[] args) {
        ProgramEnter programEnter = new ProgramEnter();
        programEnter.setContentPane(programEnter.MainPanel);
        programEnter.setVisible(true);
        programEnter.setSize(650, 400);
    }
}
