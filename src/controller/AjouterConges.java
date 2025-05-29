// Code complet de AjouterConges avec ajout de fichier PDF
package controller;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.io.*;

public class AjouterConges extends JFrame {

    private JTextField tfStartDate, tfEndDate, tfReason, tfFilePath;
    private JComboBox<String> cbStatus;
    private JButton btnSubmit, btnBack, btnBrowse;
    private String empId;
    private File selectedFile;

    private final Color PRIMARY_COLOR = new Color(0, 123, 255);
    private final Color SUCCESS_COLOR = new Color(40, 167, 69);
    private final Color DANGER_COLOR = new Color(220, 53, 69);
    private final Color BACKGROUND_COLOR = new Color(248, 249, 250);
    private final Color CARD_COLOR = Color.WHITE;
    private final Color BORDER_COLOR = new Color(206, 212, 218);

    public AjouterConges(String empId) {
        this.empId = empId;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Ajouter Congés");
        setSize(1280, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(240, 240, 240));

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        mainPanel.setBackground(BACKGROUND_COLOR);

        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(1, 1, 1, 1, BORDER_COLOR),
                new EmptyBorder(25, 25, 25, 25)));
        cardPanel.setBackground(CARD_COLOR);
        cardPanel.setAlignmentX(CENTER_ALIGNMENT);

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(CARD_COLOR);
        JLabel titleLabel = new JLabel("AJOUTER UN CONGÉ");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(PRIMARY_COLOR);
        titlePanel.add(titleLabel);
        cardPanel.add(titlePanel);
        cardPanel.add(Box.createVerticalStrut(20));

        JPanel formPanel = new JPanel(new GridLayout(0, 1, 10, 15));
        formPanel.setBackground(CARD_COLOR);
        formPanel.setBorder(new EmptyBorder(10, 30, 10, 30));

        formPanel.add(createFormField("Date de début", tfStartDate = createStyledTextField()));
        formPanel.add(createFormField("Date de fin", tfEndDate = createStyledTextField()));
        formPanel.add(createFormField("Raison", tfReason = createStyledTextField()));

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        statusPanel.setBackground(CARD_COLOR);
        JLabel lblStatus = new JLabel("Statut:");
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblStatus.setPreferredSize(new Dimension(120, 30));
        String[] statuses = {"En attente", "Approuvé", "Refusé"};
        cbStatus = new JComboBox<>(statuses);
        styleComboBox(cbStatus);
        statusPanel.add(lblStatus);
        statusPanel.add(cbStatus);
        formPanel.add(statusPanel);

        JPanel filePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        filePanel.setBackground(CARD_COLOR);
        JLabel lblFile = new JLabel("Fichier PDF:");
        lblFile.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblFile.setPreferredSize(new Dimension(120, 30));
        tfFilePath = createStyledTextField();
        tfFilePath.setEditable(false);
        btnBrowse = new JButton("Parcourir");
        btnBrowse.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnBrowse.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBrowse.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedFile = fileChooser.getSelectedFile();
                tfFilePath.setText(selectedFile.getAbsolutePath());
            }
        });
        filePanel.add(lblFile);
        filePanel.add(tfFilePath);
        filePanel.add(btnBrowse);
        formPanel.add(filePanel);

        cardPanel.add(formPanel);
        cardPanel.add(Box.createVerticalStrut(25));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(CARD_COLOR);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        btnBack = createStyledButton("RETOUR", DANGER_COLOR);
        btnBack.addActionListener(e -> {
            dispose();
            new GererConges(empId).setVisible(true);
        });
        btnSubmit = createStyledButton("SOUMETTRE", SUCCESS_COLOR);
        btnSubmit.addActionListener(e -> addConge());
       buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(btnSubmit);
        buttonPanel.add(btnBack);
        
        cardPanel.add(buttonPanel);

        mainPanel.add(cardPanel);
        add(mainPanel);
    }

    private JPanel createFormField(String labelText, JTextField textField) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panel.setBackground(CARD_COLOR);
        JLabel label = new JLabel(labelText + ":");
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setPreferredSize(new Dimension(120, 30));
        textField.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(1, 1, 1, 1, BORDER_COLOR),
                new EmptyBorder(8, 10, 8, 10)));
        panel.add(label);
        panel.add(textField);
        return panel;
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setPreferredSize(new Dimension(250, 35));
        return textField;
    }

    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboBox.setPreferredSize(new Dimension(250, 35));
        comboBox.setBackground(Color.WHITE);
        comboBox.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(1, 1, 1, 1, BORDER_COLOR),
                new EmptyBorder(5, 10, 5, 10)));
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(bgColor.darker()),
                new EmptyBorder(10, 25, 10, 25)));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        return button;
    }

    private void addConge() {
        String startDate = tfStartDate.getText();
        String endDate = tfEndDate.getText();
        String reason = tfReason.getText();
        String status = (String) cbStatus.getSelectedItem();

        if (startDate.isEmpty() || endDate.isEmpty() || reason.isEmpty() || selectedFile == null) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez remplir tous les champs et sélectionner un fichier PDF",
                    "Champs manquants",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        File directory = new File("demandes_conges");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fileName = empId + "_" + System.currentTimeMillis() + ".pdf";
        File destFile = new File(directory, fileName);
        try {
            java.nio.file.Files.copy(selectedFile.toPath(), destFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors de la copie du fichier: " + ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String filePath = "demandes_conges/" + fileName;

        try {
            conn c = new conn();
            Connection conn = c.getConnection();
            String query = "INSERT INTO conges (emp_id, start_date, end_date, reason, status, file_path) VALUES (?, ?, ?, ?, ?, ?)";

            try (PreparedStatement pst = conn.prepareStatement(query)) {
                pst.setString(1, empId);
                pst.setString(2, startDate);
                pst.setString(3, endDate);
                pst.setString(4, reason);
                pst.setString(5, status);
                pst.setString(6, filePath);
                pst.executeUpdate();

                JOptionPane.showMessageDialog(this,
                        "Congé ajouté avec succès!",
                        "Succès",
                        JOptionPane.INFORMATION_MESSAGE);

                tfStartDate.setText("");
                tfEndDate.setText("");
                tfReason.setText("");
                tfFilePath.setText("");
                cbStatus.setSelectedIndex(0);
                selectedFile = null;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors de l'ajout du congé: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new AjouterConges("EMP001").setVisible(true);
        });
    }
}