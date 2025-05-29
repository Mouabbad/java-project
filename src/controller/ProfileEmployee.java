package controller;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import javax.swing.border.*;
import java.io.File;

public class ProfileEmployee extends JFrame {

    private JLabel tfNom, tfprenom, tfaddress, tfphone, tfemail, tfeducation, tfdateNais,
            tfsalary, tfdesignation, tfdateEmbouche, tfcin, tfetatCivil, tfnationalite,
            tftypeEmploye, tfexperience, tfdepartement, photoLabel;

    private String empId;
    private Color primaryColor = new Color(0, 123, 255);
    private Color backgroundColor = new Color(248, 249, 250);
    private Color cardColor = Color.white;

    public ProfileEmployee(String empId) {
        this.empId = empId;
        initializeUI();
        loadEmployeeData();
    }

    private void initializeUI() {
        setTitle("Profil de l'Employé");
        setLayout(new BorderLayout());
        setSize(1280, 1000);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(backgroundColor);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        mainPanel.setBackground(backgroundColor);

        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(1, 1, 1, 1, new Color(222, 226, 230)),
            new EmptyBorder(20, 20, 20, 20)
        ));
        cardPanel.setBackground(cardColor);
        cardPanel.setAlignmentX(CENTER_ALIGNMENT);

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(cardColor);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel title = new JLabel("PROFIL EMPLOYÉ");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(primaryColor);
        titlePanel.add(title);
        cardPanel.add(titlePanel);

        // Photo de profil
        photoLabel = new JLabel();
        photoLabel.setPreferredSize(new Dimension(120, 120));
        photoLabel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        photoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardPanel.add(photoLabel);
        cardPanel.add(Box.createVerticalStrut(30));

        JPanel infoGrid = new JPanel(new GridLayout(0, 2, 20, 15));
        infoGrid.setBackground(cardColor);
        infoGrid.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));

        addStyledLabelAndField(infoGrid, "Nom", tfNom = createInfoLabel());
        addStyledLabelAndField(infoGrid, "Prénom", tfprenom = createInfoLabel());
        addStyledLabelAndField(infoGrid, "Adresse", tfaddress = createInfoLabel());
        addStyledLabelAndField(infoGrid, "Téléphone", tfphone = createInfoLabel());
        addStyledLabelAndField(infoGrid, "Email", tfemail = createInfoLabel());
        addStyledLabelAndField(infoGrid, "CIN", tfcin = createInfoLabel());
        addStyledLabelAndField(infoGrid, "État Civil", tfetatCivil = createInfoLabel());
        addStyledLabelAndField(infoGrid, "Nationalité", tfnationalite = createInfoLabel());
        addStyledLabelAndField(infoGrid, "Éducation", tfeducation = createInfoLabel());
        addStyledLabelAndField(infoGrid, "Date de naissance", tfdateNais = createInfoLabel());
        addStyledLabelAndField(infoGrid, "Salaire", tfsalary = createInfoLabel());
        addStyledLabelAndField(infoGrid, "Poste", tfdesignation = createInfoLabel());
        addStyledLabelAndField(infoGrid, "Date d'embauche", tfdateEmbouche = createInfoLabel());
        addStyledLabelAndField(infoGrid, "Département", tfdepartement = createInfoLabel());
        addStyledLabelAndField(infoGrid, "Type Employé", tftypeEmploye = createInfoLabel());
        addStyledLabelAndField(infoGrid, "Expérience (années)", tfexperience = createInfoLabel());

        cardPanel.add(infoGrid);
        cardPanel.add(Box.createVerticalStrut(30));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(cardColor);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JButton backBtn = createStyledButton("Retour", new Color(108, 117, 125));
        backBtn.addActionListener(e -> {
            this.dispose();
            new ViewEmployee().setVisible(true);
        });

        

        buttonPanel.add(backBtn);
        buttonPanel.add(Box.createHorizontalStrut(20));
       

        cardPanel.add(buttonPanel);
        mainPanel.add(cardPanel);
        add(mainPanel, BorderLayout.CENTER);
    }

    private JLabel createInfoLabel() {
        JLabel label = new JLabel();
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(0, 0, 1, 0, new Color(222, 226, 230)),
            new EmptyBorder(0, 0, 5, 0)
        ));
        return label;
    }

    private void addStyledLabelAndField(JPanel panel, String labelText, JLabel field) {
        JPanel container = new JPanel(new BorderLayout(10, 0));
        container.setBackground(cardColor);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(73, 80, 87));

        container.add(label, BorderLayout.WEST);
        container.add(field, BorderLayout.CENTER);
        panel.add(container);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(bgColor.darker()),
            new EmptyBorder(8, 25, 8, 25)
        ));
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

    private void loadEmployeeData() {
        try {
            conn c = new conn();
            String query = "SELECT * FROM employe WHERE id = '" + empId + "'";
            ResultSet rs = c.s.executeQuery(query);

            if (rs.next()) {
                tfNom.setText(rs.getString("nom"));
                tfprenom.setText(rs.getString("prenom"));
                tfaddress.setText(rs.getString("adresse"));
                tfphone.setText(rs.getString("telephone"));
                tfemail.setText(rs.getString("email"));
                tfeducation.setText(rs.getString("education"));
                tfdateNais.setText(rs.getString("date_naissance"));
                tfsalary.setText(rs.getString("salaire") + " MAD");
                tfdesignation.setText(rs.getString("poste"));
                tfdateEmbouche.setText(rs.getString("date_embauche"));
                tfcin.setText(rs.getString("cin"));
                tfetatCivil.setText(rs.getString("etat_civil"));
                tfnationalite.setText(rs.getString("nationalite"));
                tftypeEmploye.setText(rs.getString("type_employe"));
                tfexperience.setText(rs.getString("experience"));
                tfdepartement.setText(rs.getString("departement"));

                // Affichage de la photo
                String photoPath = rs.getString("photo_profil");
                if (photoPath != null && !photoPath.isEmpty()) {
                    File imageFile = new File("images/" + photoPath);
                    if (imageFile.exists()) {
                        ImageIcon icon = new ImageIcon(imageFile.getAbsolutePath());
                        Image img = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                        photoLabel.setIcon(new ImageIcon(img));
                    } else {
                        photoLabel.setText("Image non trouvée");
                    }
                } else {
                    photoLabel.setText("Aucune image");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des données de l'employé", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            new ProfileEmployee("EMP001").setVisible(true);
        });
    }
}
