package controller;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.nio.file.*;
import java.sql.*;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;


public class UpdateEmployee extends JFrame implements ActionListener {

    private JTextField tfeducation, tfprenom, tfaddress, tfphone, tfemail, tfsalary,
            tfdesignation, tfdateEmbouche, tfdob, tfNom, tfexperience, tfphoto;
    private JComboBox<String> cbtypeEmploye;
    private JButton updateBtn, backBtn, browseBtn;
    private File selectedImageFile;
    private String empId;

    public UpdateEmployee(String empId) {
        this.empId = empId;
        initializeUI();
        loadEmployeeData();
    }

    private void initializeUI() {
        setTitle("Mise à jour des informations employé");
        setSize(1280, 720);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(null);
        mainPanel.setBounds(170, 25, 940, 670);
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        add(mainPanel);

        JLabel heading = new JLabel("Mise à jour des informations employé", SwingConstants.CENTER);
        heading.setBounds(0, 10, 900, 40);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 24));
        heading.setForeground(new Color(0, 102, 204));
        mainPanel.add(heading);

        JPanel infoPanel = createSectionPanel("Informations Personnelles", 50, 60, 400, 250);
        mainPanel.add(infoPanel);

        tfNom = new JTextField(); tfdob = new JTextField(); tfaddress = new JTextField();
        addLabelAndField(infoPanel, "Nom", 20, 40, tfNom);
        addLabelAndField(infoPanel, "Date de naissance", 20, 90, tfdob);
        addLabelAndField(infoPanel, "Adresse", 20, 140, tfaddress);

        JPanel contactPanel = createSectionPanel("Coordonnées", 50, 330, 400, 200);
        mainPanel.add(contactPanel);

        tfprenom = new JTextField(); tfphone = new JTextField(); tfemail = new JTextField();
        addLabelAndField(contactPanel, "Prénom", 20, 40, tfprenom);
        addLabelAndField(contactPanel, "Téléphone", 20, 90, tfphone);
        addLabelAndField(contactPanel, "Email", 20, 140, tfemail);

        JPanel proPanel = createSectionPanel("Professionnelles", 480, 60, 400, 200);
        mainPanel.add(proPanel);

        tfdesignation = new JTextField(); tfsalary = new JTextField(); tfeducation = new JTextField();
        addLabelAndField(proPanel, "Poste", 20, 40, tfdesignation);
        addLabelAndField(proPanel, "Salaire", 20, 90, tfsalary);
        addLabelAndField(proPanel, "Éducation", 20, 140, tfeducation);

        JPanel suppPanel = createSectionPanel("Supplémentaires", 480, 280, 400, 240);
        mainPanel.add(suppPanel);

        tfdateEmbouche = new JTextField(); tfexperience = new JTextField();
        cbtypeEmploye = new JComboBox<>(new String[]{"Permanent", "Stagiaire"});
        tfphoto = new JTextField(); tfphoto.setEditable(false);
        browseBtn = new JButton("Parcourir");

        addLabelAndField(suppPanel, "Date embauche", 20, 40, tfdateEmbouche);
        addLabelAndField(suppPanel, "Type Employé", 20, 90, cbtypeEmploye);
        addLabelAndField(suppPanel, "Expérience", 20, 140, tfexperience);

        // Photo section
        JLabel lblPhoto = new JLabel("Photo:");
        lblPhoto.setBounds(20, 190, 100, 25);
        suppPanel.add(lblPhoto);

        tfphoto.setBounds(150, 190, 150, 25);
        suppPanel.add(tfphoto);

        browseBtn.setBounds(310, 190, 100, 25);
        browseBtn.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                selectedImageFile = fc.getSelectedFile();
                tfphoto.setText(selectedImageFile.getName());
            }
        });
        suppPanel.add(browseBtn);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        buttonPanel.setBounds(300, 560, 380, 60);
        buttonPanel.setOpaque(false);
        mainPanel.add(buttonPanel);

        updateBtn = createButton("Mettre à jour", new Color(76, 175, 80));
        backBtn = createButton("Retour", new Color(244, 67, 54));
        buttonPanel.add(updateBtn);
        buttonPanel.add(backBtn);
    }

    private JPanel createSectionPanel(String title, int x, int y, int width, int height) {
        JPanel panel = new JPanel(null);
        panel.setBounds(x, y, width, height);
        panel.setBackground(new Color(249, 249, 249));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                title, TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14), new Color(0, 102, 204)));
        return panel;
    }

    private void addLabelAndField(JPanel panel, String labelText, int x, int y, JComponent field) {
        JLabel label = new JLabel(labelText);
        label.setBounds(x, y, 120, 25);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(label);
        field.setBounds(x + 130, y, 200, 25);
        panel.add(field);
    }

    private JButton createButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.addActionListener(this);
        return btn;
    }

    private void loadEmployeeData() {
        try {
            conn c = new conn();
            ResultSet rs = c.s.executeQuery("SELECT * FROM employe WHERE id = '" + empId + "'");
            if (rs.next()) {
                tfNom.setText(rs.getString("nom"));
                tfprenom.setText(rs.getString("prenom"));
                tfdob.setText(rs.getString("date_naissance"));
                tfaddress.setText(rs.getString("adresse"));
                tfsalary.setText(rs.getString("salaire"));
                tfphone.setText(rs.getString("telephone"));
                tfemail.setText(rs.getString("email"));
                tfeducation.setText(rs.getString("education"));
                tfdesignation.setText(rs.getString("poste"));
                tfdateEmbouche.setText(rs.getString("date_embauche"));
                tfexperience.setText(rs.getString("experience"));
                cbtypeEmploye.setSelectedItem(rs.getString("type_employe"));
                tfphoto.setText(rs.getString("photo_profil"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == updateBtn) {
            updateEmployee();
        } else {
            dispose();
            new ViewEmployee().setVisible(true);
        }
    }

    private void updateEmployee() {
        try {
            String query = "UPDATE employe SET nom=?, prenom=?, date_naissance=?, adresse=?, salaire=?, telephone=?, email=?, education=?, poste=?, date_embauche=?, type_employe=?, experience=?, photo_profil=? WHERE id=?";
            conn c = new conn();
            PreparedStatement ps = c.getConnection().prepareStatement(query);
            ps.setString(1, tfNom.getText());
            ps.setString(2, tfprenom.getText());
            ps.setString(3, tfdob.getText());
            ps.setString(4, tfaddress.getText());
            ps.setString(5, tfsalary.getText());
            ps.setString(6, tfphone.getText());
            ps.setString(7, tfemail.getText());
            ps.setString(8, tfeducation.getText());
            ps.setString(9, tfdesignation.getText());
            ps.setString(10, tfdateEmbouche.getText());
            ps.setString(11, cbtypeEmploye.getSelectedItem().toString());
            ps.setString(12, tfexperience.getText());
            ps.setString(13, tfphoto.getText());
            ps.setString(14, empId);

            if (selectedImageFile != null) {
                File destDir = new File("images");
                if (!destDir.exists()) destDir.mkdirs();
                Path destPath = Paths.get("images", selectedImageFile.getName());
                Files.copy(selectedImageFile.toPath(), destPath, StandardCopyOption.REPLACE_EXISTING);
            }

              sendEmail(tfemail.getText(), tfNom.getText());

        JOptionPane.showMessageDialog(this, "les information sont modifiées avec succes et email envoyées!");
        dispose();
        new ViewEmployee().setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour : " + e.getMessage());
        }
        
        
    }
    public void sendEmail(String toEmail, String name) {
    final String fromEmail = "haneenmtk8@gmail.com"; // بريد المرسل
    final String password = "xqcm udsg wdsr rnuk"; // كلمة السر للبريد

    Properties props = new Properties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.port", "587");

    Session session = Session.getInstance(props, new Authenticator() {
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(fromEmail, password);
        }
    });

    try {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject("Bienvenue");

        // نص الرسالة مع استعمال نفس المتغيرات في الكلاس
        String body = "Bonjour " + name + ",\n\n" +
            "Voici les détails après la modification  :\n\n" +
            "Nom : " + tfNom.getText() + "\n" +
            "Prénom : " + tfprenom.getText() + "\n" +
            "Date de naissance : " + tfdob.getText() + "\n" +
            "Téléphone : " + tfphone.getText() + "\n" +
            "Adresse : " + tfaddress.getText() + "\n" +
            "Email : " + tfemail.getText() + "\n" +
            "Poste : " + tfdesignation.getText() + "\n" +
            "Éducation : " + tfeducation.getText() + "\n" +
            "Salaire : " + tfsalary.getText() + "\n" +
            "Date d'embauche : " + tfdateEmbouche.getText() + "\n" +
            "Type Employé : " + cbtypeEmploye.getSelectedItem().toString() + "\n" +
            "Expérience : " + tfexperience.getText() + "\n" +
            "\nMerci de vérifier si toutes les informations sont correctes.";

        message.setText(body);
        Transport.send(message);
       
    } catch (MessagingException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Erreur lors de l'envoi de l'email : " + ex.getMessage());
    }
}


    public static void main(String[] args) {
        new UpdateEmployee("EMP001").setVisible(true);
    }
}