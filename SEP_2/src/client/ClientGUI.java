package client;

import model.Internship;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class ClientGUI extends JFrame {

  private final Client client = new Client();

  private DefaultTableModel tableModel;
  private JTable table;

  private JTextField tfTitle;
  private JTextField tfDescription;
  private JTextField tfCompanyId;
  private JTextField tfLocation;
  private JTextField tfPosition;
  private JTextField tfStartDate;
  private JTextField tfEndDate;
  private JTextField tfStatus;

  private JLabel lblStatus;

  public ClientGUI() {
    super("Internship Application");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(1050, 600);
    setLocationRelativeTo(null);

    initComponents();
    loadData();
  }

  private void initComponents() {
    setLayout(new BorderLayout(10, 10));

    String[] columns = {"ID", "Title", "Company ID", "Location", "Position", "Status"};
    tableModel = new DefaultTableModel(columns, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };

    table = new JTable(tableModel);
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    table.getColumnModel().getColumn(0).setMaxWidth(50);

    JScrollPane scrollPane = new JScrollPane(table);
    add(scrollPane, BorderLayout.CENTER);

    JPanel formPanel = new JPanel(new GridBagLayout());
    formPanel.setBorder(BorderFactory.createTitledBorder("Add Internship"));

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(6, 6, 6, 6);
    gbc.anchor = GridBagConstraints.WEST;

    tfTitle = new JTextField(15);
    tfDescription = new JTextField(15);
    tfCompanyId = new JTextField(15);
    tfLocation = new JTextField(15);
    tfPosition = new JTextField(15);
    tfStartDate = new JTextField(15);
    tfEndDate = new JTextField(15);
    tfStatus = new JTextField(15);

    addField(formPanel, gbc, 0, "Title:", tfTitle);
    addField(formPanel, gbc, 1, "Description:", tfDescription);
    addField(formPanel, gbc, 2, "Company ID:", tfCompanyId);
    addField(formPanel, gbc, 3, "Location:", tfLocation);
    addField(formPanel, gbc, 4, "Position:", tfPosition);
    addField(formPanel, gbc, 5, "Start Date (yyyy-mm-dd):", tfStartDate);
    addField(formPanel, gbc, 6, "End Date (yyyy-mm-dd):", tfEndDate);
    addField(formPanel, gbc, 7, "Status:", tfStatus);

    JButton btnAdd = new JButton("Add");
    gbc.gridx = 0;
    gbc.gridy = 8;
    gbc.gridwidth = 2;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    formPanel.add(btnAdd, gbc);

    add(formPanel, BorderLayout.EAST);

    JButton btnRefresh = new JButton("Refresh");
    JButton btnDelete = new JButton("Delete Selected");

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    buttonPanel.add(btnRefresh);
    buttonPanel.add(btnDelete);

    lblStatus = new JLabel("Ready.");

    JPanel bottomPanel = new JPanel(new BorderLayout());
    bottomPanel.add(buttonPanel, BorderLayout.WEST);
    bottomPanel.add(lblStatus, BorderLayout.EAST);

    add(bottomPanel, BorderLayout.SOUTH);

    btnRefresh.addActionListener(e -> loadData());
    btnAdd.addActionListener(e -> addInternship());
    btnDelete.addActionListener(e -> deleteSelected());
  }

  private void addField(JPanel panel, GridBagConstraints gbc, int row, String labelText, JTextField textField) {
    gbc.gridx = 0;
    gbc.gridy = row;
    gbc.gridwidth = 1;
    gbc.fill = GridBagConstraints.NONE;
    panel.add(new JLabel(labelText), gbc);

    gbc.gridx = 1;
    panel.add(textField, gbc);
  }

  private void loadData() {
    try {
      List<Internship> internships = client.getAll();
      tableModel.setRowCount(0);

      for (Internship internship : internships) {
        tableModel.addRow(new Object[]{
            internship.getId(),
            internship.getTitle(),
            internship.getCompanyId(),
            internship.getLocation(),
            internship.getPosition(),
            internship.getStatus()
        });
      }

      setStatus("Loaded " + internships.size() + " internships.", false);

    } catch (Exception e) {
      setStatus("Error loading data: " + e.getMessage(), true);
    }
  }

  private void addInternship() {
    try {
      String title = tfTitle.getText().trim();
      String description = tfDescription.getText().trim();
      String companyIdText = tfCompanyId.getText().trim();
      String location = tfLocation.getText().trim();
      String position = tfPosition.getText().trim();
      String startDateText = tfStartDate.getText().trim();
      String endDateText = tfEndDate.getText().trim();
      String status = tfStatus.getText().trim();

      if (title.isEmpty() || companyIdText.isEmpty() || position.isEmpty() || status.isEmpty()) {
        setStatus("Title, company ID, position, and status are required.", true);
        return;
      }

      int companyId = Integer.parseInt(companyIdText);
      LocalDate startDate = startDateText.isEmpty() ? null : LocalDate.parse(startDateText);
      LocalDate endDate = endDateText.isEmpty() ? null : LocalDate.parse(endDateText);

      Internship internship = new Internship(
          0,
          title,
          description,
          companyId,
          location,
          position,
          startDate,
          endDate,
          status
      );

      boolean success = client.add(internship);

      if (success) {
        setStatus("Internship added successfully.", false);
        clearFields();
        loadData();
      } else {
        setStatus("Server rejected the internship.", true);
      }

    } catch (NumberFormatException e) {
      setStatus("Company ID must be a number.", true);
    } catch (Exception e) {
      setStatus("Error adding internship: " + e.getMessage(), true);
    }
  }

  private void deleteSelected() {
    int selectedRow = table.getSelectedRow();

    if (selectedRow == -1) {
      setStatus("Please select a row to delete.", true);
      return;
    }

    int id = (int) tableModel.getValueAt(selectedRow, 0);

    int confirm = JOptionPane.showConfirmDialog(
        this,
        "Are you sure you want to delete internship ID " + id + "?",
        "Confirm Delete",
        JOptionPane.YES_NO_OPTION
    );

    if (confirm != JOptionPane.YES_OPTION) {
      return;
    }

    try {
      boolean success = client.delete(id);

      if (success) {
        setStatus("Internship ID " + id + " deleted.", false);
        loadData();
      } else {
        setStatus("Internship ID " + id + " not found.", true);
      }

    } catch (Exception e) {
      setStatus("Error deleting internship: " + e.getMessage(), true);
    }
  }

  private void clearFields() {
    tfTitle.setText("");
    tfDescription.setText("");
    tfCompanyId.setText("");
    tfLocation.setText("");
    tfPosition.setText("");
    tfStartDate.setText("");
    tfEndDate.setText("");
    tfStatus.setText("");
  }

  private void setStatus(String message, boolean isError) {
    lblStatus.setText(message + "   ");
    lblStatus.setForeground(isError ? Color.RED : new Color(0, 128, 0));
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      ClientGUI gui = new ClientGUI();
      gui.setVisible(true);
    });
  }
}