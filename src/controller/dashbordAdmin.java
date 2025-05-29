package controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class dashbordAdmin extends JFrame {

    private static final Color PRIMARY_COLOR = new Color(45, 120, 229);
    private static final Color BACKGROUND_COLOR = new Color(245, 247, 250);
    private static final Color BUTTON_COLOR = new Color(255, 255, 255);
    private static final Color BUTTON_HOVER_COLOR = new Color(35, 94, 169);

    private JPanel contentPane;
    private JButton addButton, viewButton, removeButton, archiveButton;

    public dashbordAdmin() {
        configureWindow();
        initComponents();
        setupBackground();
        addListeners();
    }

    private void configureWindow() {
        setTitle("Admin Dashboard - Employee Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setResizable(true);
    }

    private void initComponents() {
        contentPane = new JPanel(new BorderLayout());
        setContentPane(contentPane);

        JPanel topBar = createTopBar();
        contentPane.add(topBar, BorderLayout.NORTH);
    }

    private JPanel createTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Color.decode("#87CEFA"));
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Icon
        ImageIcon rawIcon = new ImageIcon(getClass().getResource("/icons/team.png"));
        Image scaledImage = rawIcon.getImage().getScaledInstance(-1, 50, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JLabel iconLabel = new JLabel(scaledIcon);
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        topBar.add(iconLabel, BorderLayout.WEST);

        // Title
        JLabel title = new JLabel("Employee Management System", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(Color.WHITE);
        topBar.add(title, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = createButtonPanel();
        topBar.add(buttonPanel, BorderLayout.EAST);

        return topBar;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        buttonPanel.setOpaque(false);

        addButton = createModernButton("ajouter", "/icons/add.png");
        viewButton = createModernButton("afficher", "/icons/view.png");
        removeButton = createModernButton("supprimer", "/icons/remove.png");
        archiveButton = createModernButton("archives", "/icons/archive.png");

        Dimension buttonSize = new Dimension(120, 40);
        addButton.setPreferredSize(buttonSize);
        viewButton.setPreferredSize(buttonSize);
        removeButton.setPreferredSize(buttonSize);
        archiveButton.setPreferredSize(buttonSize);

        buttonPanel.add(addButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(archiveButton);

        return buttonPanel;
    }

    private void setupBackground() {
        try {
            ImageIcon originalIcon = new ImageIcon(getClass().getResource("/icons/back.png"));
            Image scaledImage = originalIcon.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
            JLabel background = new JLabel(new ImageIcon(scaledImage));
            background.setLayout(new BorderLayout());

            JLabel welcomeLabel = new JLabel("Welcome! Administrator", SwingConstants.CENTER);
            welcomeLabel.setBorder(BorderFactory.createEmptyBorder(100, 0, 0, 0));
            welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
            welcomeLabel.setForeground(new Color(30, 136, 229));

            background.add(welcomeLabel, BorderLayout.NORTH);
            contentPane.add(background, BorderLayout.CENTER);
        } catch (Exception e) {
            contentPane.setBackground(BACKGROUND_COLOR);
        }
    }

    private JButton createModernButton(String text, String iconPath) {
        JButton button = new JButton(text);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBackground(BUTTON_HOVER_COLOR);
        button.setForeground(PRIMARY_COLOR);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 1),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        button.setFocusable(false);

        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
            button.setIcon(new ImageIcon(icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
            button.setHorizontalTextPosition(SwingConstants.RIGHT);
            button.setIconTextGap(10);
        } catch (Exception e) {
            System.out.println("Icon not found: " + iconPath);
        }

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(BUTTON_HOVER_COLOR);
                button.setForeground(Color.WHITE);
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(BUTTON_COLOR);
                button.setForeground(PRIMARY_COLOR);
            }
        });

        return button;
    }

    private void addListeners() {
        addButton.addActionListener(e -> openAddEmployee());
        viewButton.addActionListener(e -> openViewEmployee());
        removeButton.addActionListener(e -> openRemoveEmployee());
        archiveButton.addActionListener(e -> openArchive());
    }

    private void openAddEmployee() {
        dispose();
        new AddEmployee().setVisible(true);
    }

    private void openViewEmployee() {
        dispose();
        new ViewEmployee().setVisible(true);
    }

    private void openUpdateEmployee() {
        dispose();
        new ViewEmployee().setVisible(true);
    }

    private void openRemoveEmployee() {
        String empId = selectEmployeeId();
        if (empId != null) {
            dispose();
            new RemoveEmployee(empId).setVisible(true);
        }
    }

    private void openArchive() {
        dispose();
        new Archive().setVisible(true);
    }

    // 📌 Sélection d’un ID employé depuis la base
    private String selectEmployeeId() {
        try {
            conn c = new conn();
            ResultSet rs = c.s.executeQuery("SELECT id, nom FROM employe WHERE statut = 'actif'");
            java.util.List<String> choices = new java.util.ArrayList<>();
            while (rs.next()) {
                choices.add(rs.getString("id") + " - " + rs.getString("nom"));
            }
            if (choices.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Aucun employé actif trouvé.");
                return null;
            }
            Object selected = JOptionPane.showInputDialog(
                    this, "Sélectionnez un employé à supprimer:", "Choisir employé",
                    JOptionPane.PLAIN_MESSAGE, null, choices.toArray(), choices.get(0)
            );
            if (selected != null) {
                return selected.toString().split(" - ")[0]; // retourne seulement l'ID
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des employés.");
        }
        return null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                new dashbordAdmin().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
