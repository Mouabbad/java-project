package controller;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class home extends JFrame {
    public home() {
        setTitle("Accueil - Gestion des Employés");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Couleur de fond propre avec image de fond
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon background = new ImageIcon("src/icons/back.png");
 // Remplace par ton image
                Image img = background.getImage(); // Récupère l'image
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this); // Ajuste l'image à la taille de la fenêtre
            }
        };
        mainPanel.setLayout(new BorderLayout());

        // Navbar en haut
        JPanel navbar = new JPanel();
        navbar.setBackground(new Color(33, 150, 243)); // Bleu foncé
        navbar.setPreferredSize(new Dimension(1000, 60));
        navbar.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        JLabel titleLabel = new JLabel("Gestion des Employés");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        navbar.add(titleLabel);

        // Centre - contenu
        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel welcomeLabel = new JLabel("Bienvenue !");
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        welcomeLabel.setForeground(new Color(33, 33, 33)); // Gris très foncé

        // Espace entre le titre et les boutons
        contentPanel.add(welcomeLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        // Ajouter le bouton Login
        JButton loginBtn = createStyledButton("Login");
        JButton logoutBtn = createStyledButton("Déconnexion");

        contentPanel.add(loginBtn);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(logoutBtn);

        // Action sur Login
        loginBtn.addActionListener(e -> {
            dispose(); // تسد الصفحة الحالية
            new Login(); // تفتح صفحة تسجيل الدخول
        });

        // Action sur Déconnexion
        logoutBtn.addActionListener(e -> {
            dispose();
            JOptionPane.showMessageDialog(this, "Déconnecté !");
        });

        mainPanel.add(navbar, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel);
        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(new Font("SansSerif", Font.PLAIN, 20));
        button.setBackground(new Color(30, 136, 229)); // Bleu clair
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(250, 50));
        button.setMaximumSize(new Dimension(250, 50));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Effet hover
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(21, 101, 192)); // Bleu foncé
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(30, 136, 229));
            }
        });

        return button;
    }

    public static void main(String[] args) {
        new home();
    }
}