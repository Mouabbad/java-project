package controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.nio.file.*;
import java.sql.*;
import java.util.Properties;
import java.util.Random;
import javax.mail.*;
import javax.mail.internet.*;

public class AddEmployee extends JFrame implements ActionListener {

    JTextField tfname, tffname, tfemail, tfphone, tfcin, tfaddress, tfdateNais, tfdepartement, tfdesignation, tfeducation, tfsalary, tfembauche, tfexperience, tfphoto;
    JComboBox<String> cbsexe, cbetatCivil, cbnationalite, cbtypeEmploye;
    JLabel lblempId;
    JButton add, back, browseBtn;
    File selectedImageFile;

    public AddEmployee() {
        setTitle("Ajouter un nouvel employé");
        setSize(1280, 720);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);

        JLabel heading = new JLabel("Ajouter un nouvel employé", JLabel.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 26));
        heading.setBounds(0, 20, 1280, 30);
        add(heading);

        Random rand = new Random();
        lblempId = new JLabel("" + rand.nextInt(999999));
        lblempId.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblempId.setForeground(Color.BLUE);

        // Panel Gauche
        JPanel left = new JPanel(new GridLayout(10, 2, 10, 10));
        left.setBounds(30, 70, 530, 500);
        left.setBackground(new Color(245, 245, 245));

        tfname = createTextField();
        tffname = createTextField();
        tfdateNais = createTextField();
        tfphone = createTextField();
        tfaddress = createTextField();
        cbsexe = new JComboBox<>(new String[]{"Homme", "Femme"});
        cbetatCivil = new JComboBox<>(new String[]{"Célibataire", "Marié(e)","devorcé(e)"});
        tfcin = createTextField();
        cbnationalite = new JComboBox<>(new String[]{"Marocain(e)", "Autre"});
        tfphoto = createTextField();
        tfphoto.setEditable(false);
        browseBtn = createStyledButton("Parcourir", new Color(96, 96, 96));

        browseBtn.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                selectedImageFile = fc.getSelectedFile();
                tfphoto.setText(selectedImageFile.getName());
            }
        });

        left.add(new JLabel("Nom:")); left.add(tfname);
        left.add(new JLabel("Prénom:")); left.add(tffname);
        left.add(new JLabel("Date de naissance:")); left.add(tfdateNais);
        left.add(new JLabel("Téléphone:")); left.add(tfphone);
        left.add(new JLabel("Adresse:")); left.add(tfaddress);
        left.add(new JLabel("Sexe:")); left.add(cbsexe);
        left.add(new JLabel("État Civil:")); left.add(cbetatCivil);
        left.add(new JLabel("CIN:")); left.add(tfcin);
        left.add(new JLabel("Nationalité:")); left.add(cbnationalite);
        left.add(new JLabel("Photo:"));
        JPanel photoPanel = new JPanel(new BorderLayout());
        photoPanel.setOpaque(false);
        photoPanel.add(tfphoto, BorderLayout.CENTER);
        photoPanel.add(browseBtn, BorderLayout.EAST);
        left.add(photoPanel);

        // Panel Droit
        JPanel right = new JPanel(new GridLayout(10, 2, 10, 10));
        right.setBounds(600, 70, 530, 500);
        right.setBackground(new Color(245, 245, 245));

        tfemail = createTextField();
        tfdepartement = createTextField();
        tfdesignation = createTextField();
        tfeducation = createTextField();
        tfsalary = createTextField();
        tfembauche = createTextField();
        cbtypeEmploye = new JComboBox<>(new String[]{"Stagiaire", "Permanent"});
        tfexperience = createTextField();

        right.add(new JLabel("Email:")); right.add(tfemail);
        right.add(new JLabel("Département:")); right.add(tfdepartement);
        right.add(new JLabel("ID Employé:")); right.add(lblempId);
        right.add(new JLabel("Poste:")); right.add(tfdesignation);
        right.add(new JLabel("Éducation:")); right.add(tfeducation);
        right.add(new JLabel("Salaire:")); right.add(tfsalary);
        right.add(new JLabel("Date d'embauche:")); right.add(tfembauche);
        right.add(new JLabel("Type d'employé:")); right.add(cbtypeEmploye);
        right.add(new JLabel("Années d'expérience:")); right.add(tfexperience);
        right.add(new JLabel("")); right.add(new JLabel(""));

        add(left); add(right);

        add = createStyledButton("Ajouter", new Color(76, 175, 80));
        back = createStyledButton("Retour", new Color(96, 96, 96));
        add.setBounds(450, 600, 150, 40);
        back.setBounds(630, 600, 150, 40);
        add(add); add(back);

        add.addActionListener(this);
        back.addActionListener(e -> {
            dispose();
            new dashbordAdmin().setVisible(true);
        });

        setVisible(true);
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bgColor.darker()),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        return button;
    }

    public void actionPerformed(ActionEvent e) {
        try {
            String nom = tfname.getText(), prenom = tffname.getText(), date_naissance = tfdateNais.getText();
            String telephone = tfphone.getText(), adresse = tfaddress.getText(), sexe = cbsexe.getSelectedItem().toString();
            String etat_civil = cbetatCivil.getSelectedItem().toString(), cin = tfcin.getText();
            String nationalite = cbnationalite.getSelectedItem().toString(), photo = tfphoto.getText();
            String email = tfemail.getText(), departement = tfdepartement.getText(), id = lblempId.getText();
            String poste = tfdesignation.getText(), education = tfeducation.getText();
            double salaire = Double.parseDouble(tfsalary.getText());
            String date_embauche = tfembauche.getText(), typeEmploye = cbtypeEmploye.getSelectedItem().toString();
            String experience = tfexperience.getText();

            if (selectedImageFile != null) {
                File destDir = new File("images");
                if (!destDir.exists()) destDir.mkdirs();
                Path destPath = Paths.get("images", selectedImageFile.getName());
                Files.copy(selectedImageFile.toPath(), destPath, StandardCopyOption.REPLACE_EXISTING);
            }

            controller.conn c = new controller.conn();
            PreparedStatement ps = c.getConnection().prepareStatement(
                "INSERT INTO employe (id, nom, prenom, date_naissance, telephone, adresse, sexe, etat_civil, cin, nationalite, photo_profil, email, departement, poste, education, salaire, date_embauche, type_employe, experience) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)" );
            ps.setString(1, id); ps.setString(2, nom); ps.setString(3, prenom); ps.setString(4, date_naissance);
            ps.setString(5, telephone); ps.setString(6, adresse); ps.setString(7, sexe); ps.setString(8, etat_civil);
            ps.setString(9, cin); ps.setString(10, nationalite); ps.setString(11, photo); ps.setString(12, email);
            ps.setString(13, departement); ps.setString(14, poste); ps.setString(15, education); ps.setDouble(16, salaire);
            ps.setString(17, date_embauche); ps.setString(18, typeEmploye); ps.setString(19, experience);
            ps.executeUpdate();

            sendEmail(email, nom);
            JOptionPane.showMessageDialog(this, "Employé ajouté et email envoyé avec succès !");
            dispose();
            new dashbordAdmin().setVisible(true);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
        }
    }

    public void sendEmail(String toEmail, String name) {
        final String fromEmail = "haneenmtk8@gmail.com";
        final String password = "xqcm udsg wdsr rnuk";

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
            String body = "Bonjour " + name + ",\n\n" +
                "Voici les détails enregistrés pour vous :\n\n" +
                "Nom : " + name + "\n" +
                "Prénom : " + tffname.getText() + "\n" +
                "Date de naissance : " + tfdateNais.getText() + "\n" +
                "Téléphone : " + tfphone.getText() + "\n" +
                "Adresse : " + tfaddress.getText() + "\n" +
                "Sexe : " + cbsexe.getSelectedItem().toString() + "\n" +
                "État Civil : " + cbetatCivil.getSelectedItem().toString() + "\n" +
                "CIN : " + tfcin.getText() + "\n" +
                "Nationalité : " + cbnationalite.getSelectedItem().toString() + "\n" +
                "Email : " + tfemail.getText() + "\n" +
                "Département : " + tfdepartement.getText() + "\n" +
                "Poste : " + tfdesignation.getText() + "\n" +
                "Éducation : " + tfeducation.getText() + "\n" +
                "Salaire : " + tfsalary.getText() + "\n" +
                "Date d'embauche : " + tfembauche.getText() + "\n" +
                "Type Employé : " + cbtypeEmploye.getSelectedItem().toString() + "\n" +
                "Expérience : " + tfexperience.getText() + "\n" +
                "\nMerci de vérifier si toutes les informations sont correctes.";

            message.setText(body);
            Transport.send(message);
        } catch (MessagingException ex) {
         
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AddEmployee());
    }
}
