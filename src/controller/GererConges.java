package controller;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.sql.*;

public class GererConges extends JFrame {

    private String empId;
    private JTable congesTable;
    private final Color PRIMARY_COLOR = new Color(0, 123, 255);
    private Color headerColor1 = new Color(70, 130, 180);
    private Color headerTextColor = Color.WHITE;

    public GererConges(String empId) {
        this.empId = empId;
        initializeUI();
        loadCongesData();
        setupTableEditListener();
    }

    private void initializeUI() {
        setTitle("Gestion des Congés");
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(240, 240, 240));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel("Gestion des Congés", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(PRIMARY_COLOR);
        mainPanel.add(title, BorderLayout.NORTH);

        String[] columnNames = {"ID Congé", "Date Début", "Date Fin", "Motif", "Statut", "Fichier"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // seule la colonne Statut est modifiable
            }
        };

        congesTable = new JTable(model) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 248, 248));
                }

                if (column == 4) {
                    String status = getValueAt(row, column).toString();
                    if (status.equalsIgnoreCase("Approuvé")) {
                        c.setBackground(new Color(220, 255, 220));
                    } else if (status.equalsIgnoreCase("Refusé")) {
                        c.setBackground(new Color(255, 220, 220));
                    } else {
                        c.setBackground(Color.WHITE);
                    }
                }

                return c;
            }
        };

        congesTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        congesTable.setRowHeight(35);
        congesTable.setSelectionBackground(new Color(200, 230, 255));
        congesTable.setGridColor(new Color(220, 220, 220));
        congesTable.setShowGrid(true);
        congesTable.setIntercellSpacing(new Dimension(0, 0));

        // ✅ Masquer la colonne ID Congé (index 0)
        TableColumnModel columnModel = congesTable.getColumnModel();
        columnModel.getColumn(0).setMinWidth(0);
        columnModel.getColumn(0).setMaxWidth(0);
        columnModel.getColumn(0).setWidth(0);

        String[] statuts = {"En attente", "Approuvé", "Rejeté"};
        JComboBox<String> statutCombo = new JComboBox<>(statuts);
        congesTable.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(statutCombo));

        congesTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int column = congesTable.getColumnModel().getColumnIndex("Fichier");
                int row = congesTable.rowAtPoint(e.getPoint());
                if (row >= 0 && column >= 0 && congesTable.columnAtPoint(e.getPoint()) == column) {
                    String filePath = (String) congesTable.getValueAt(row, column);
                    if (filePath != null && !filePath.isEmpty()) {
                        try {
                            Desktop.getDesktop().open(new File(filePath));
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(null, "Erreur lors de l'ouverture du fichier:\n" + ex.getMessage());
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Aucun fichier attaché à cette demande.");
                    }
                }
            }
        });

        customizeHeaderModern();

        JScrollPane scrollPane = new JScrollPane(congesTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        buttonPanel.setBackground(new Color(240, 240, 240));

        JButton addButton = createButton("Ajouter Congé", new Color(76, 175, 80));
        JButton backButton = createButton("Retour", new Color(244, 67, 54));

        addButton.addActionListener(e -> {
            new AjouterConges(empId).setVisible(true);
            dispose();
        });

        backButton.addActionListener(e -> {
            new ViewEmployee().setVisible(true);
            dispose();
        });

       
        buttonPanel.add(addButton);
         buttonPanel.add(backButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void customizeHeaderModern() {
        congesTable.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                label.setOpaque(true);
                label.setFont(new Font("Segoe UI", Font.BOLD, 14));
                label.setForeground(headerTextColor);
                label.setBackground(headerColor1);
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 2, 1, new Color(255, 255, 255, 100)),
                        BorderFactory.createEmptyBorder(8, 10, 8, 10)
                ));

                return label;
            }
        });
    }

    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker(), 1),
                BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));

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

    private void loadCongesData() {
        try {
            conn c = new conn();
            String query = "SELECT * FROM conges WHERE emp_id = '" + empId + "' ORDER BY start_date DESC";
            ResultSet rs = c.s.executeQuery(query);
            DefaultTableModel model = (DefaultTableModel) congesTable.getModel();

            model.setRowCount(0);

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("conge_id"),
                        rs.getString("start_date"),
                        rs.getString("end_date"),
                        rs.getString("reason"),
                        rs.getString("status"),
                        rs.getString("file_path")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors du chargement des congés:\n" + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setupTableEditListener() {
        ((DefaultTableModel) congesTable.getModel()).addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 4) {
                    int row = e.getFirstRow();
                    String newStatus = congesTable.getValueAt(row, 4).toString();
                    String congeId = congesTable.getValueAt(row, 0).toString();

                    try {
                        conn c = new conn();
                        String updateQuery = "UPDATE conges SET status = ? WHERE conge_id = ?";
                        PreparedStatement ps = c.c.prepareStatement(updateQuery);
                        ps.setString(1, newStatus);
                        ps.setString(2, congeId);
                        ps.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Statut mis à jour avec succès.");
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Erreur lors de la mise à jour:\n" + ex.getMessage());
                    }
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new GererConges("EMP001").setVisible(true);
        });
    }
}