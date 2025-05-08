package controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;


public class Login extends JFrame implements ActionListener {

    JTextField tfUsername;
    JPasswordField tfPassword;
    JButton loginBtn;
  

    public Login() {
        setTitle("Login admin");
        setSize(450, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Background Panel with gradient color
        JPanel mainPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color c1 = new Color(58, 123, 213);
                Color c2 = new Color(0, 210, 255);
                GradientPaint gp = new GradientPaint(0, 0, c1, 0, getHeight(), c2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setBounds(0, 0, 450, 550);
        mainPanel.setLayout(null);
        add(mainPanel);

        // Title
        JLabel title = new JLabel("HELLO & WELCOME");
        title.setBounds(90, 40, 300, 40);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        mainPanel.add(title);

        /*JLabel subtitle = new JLabel("<html><center>Lorem ipsum dolor sit amet, consectetur<br>adipiscing elit.</center></html>");
        subtitle.setBounds(90, 80, 300, 50);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subtitle.setForeground(Color.WHITE);
        mainPanel.add(subtitle);
        */
        // Login Panel (white box)
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(null);
        loginPanel.setBackground(Color.WHITE);
        loginPanel.setBounds(50, 150, 340, 300);
        loginPanel.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1));
        mainPanel.add(loginPanel);

        JLabel loginTitle = new JLabel("USER LOGIN");
        loginTitle.setBounds(110, 20, 150, 30);
        loginTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        loginPanel.add(loginTitle);

        tfUsername = new JTextField();
        tfUsername.setBounds(50, 70, 240, 35);
        tfUsername.setBorder(BorderFactory.createTitledBorder("Username"));
        loginPanel.add(tfUsername);

        tfPassword = new JPasswordField();
        tfPassword.setBounds(50, 120, 240, 35);
        tfPassword.setBorder(BorderFactory.createTitledBorder("Password"));
        loginPanel.add(tfPassword);

        JCheckBox remember = new JCheckBox("Remember");
        remember.setBounds(50, 170, 100, 20);
        remember.setBackground(Color.WHITE);
        loginPanel.add(remember);

        JLabel forgot = new JLabel("Forget Password?");
        forgot.setBounds(180, 170, 120, 20);
        forgot.setFont(new Font("SansSerif", Font.PLAIN, 12));
        forgot.setForeground(Color.GRAY);
        loginPanel.add(forgot);

        loginBtn = new JButton("LOGIN");
        loginBtn.setBounds(90, 210, 160, 40);
        loginBtn.setBackground(new Color(85, 0, 255));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        loginBtn.setFocusPainted(false);
        loginBtn.addActionListener(this);
        loginPanel.add(loginBtn);
        
       

        setVisible(true);
    }

    @Override
public void actionPerformed(ActionEvent e) {
    String username = tfUsername.getText();
    String password = String.valueOf(tfPassword.getPassword());

    try {
        conn c = new conn();
        String query = "SELECT * FROM admin WHERE username = '" + username + "' AND password = '" + password + "'";
        ResultSet rs = c.s.executeQuery(query);

        if (rs.next()) {
            JOptionPane.showMessageDialog(this, "Login réussi !");
           dispose(); // fermer login
           // new home(); // lancer page home
        } else {
            JOptionPane.showMessageDialog(this, "Nom d'utilisateur ou mot de passe incorrect !");
        }
    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Erreur de connexion à la base de données !");
    }
}


    public static void main(String[] args) {
        new Login();
    }
}
