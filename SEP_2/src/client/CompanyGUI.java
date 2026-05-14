package client;

import model.Application;
import model.Company;
import model.Internship;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class CompanyGUI extends JFrame {

  private final Client client = new Client();

  private Company currentCompany;
  private boolean loggedIn = false;

  private JTable table;
  private DefaultTableModel tableModel;

  private JTextField tfEmail;
  private JPasswordField pfPassword;
  private JLabel lblLoggedCompany;

  private JTextField tfTitle;
  private JTextField tfDescription;
  private JTextField tfCompanyId;
  private JTextField tfLocation;
  private JTextField tfPosition;
  private JTextField tfStartDate;
  private JTextField tfEndDate;
  private JTextField tfStatus;

  private JLabel lblStatus;

  public CompanyGUI() {
    super("Company Internship Portal");

    currentCompany = new Company(
        1,
        "Trifork",
        "company@email.com",
        "1234",
        "Software company"
    );

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(950, 600);
    setLocationRelativeTo(null);

    initComponents();
    loadInternships();
  }

  private void initComponents() {
    setLayout(new BorderLayout(10, 10));

    JPanel loginPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

    tfEmail = new JTextField(12);
    pfPassword = new JPasswordField(8);

    JButton btnLogin = new JButton("Login");
    JButton btnLogout = new JButton("Logout");

    lblLoggedCompany = new JLabel("Not logged in");

    loginPanel.add(new JLabel("Email:"));
    loginPanel.add(tfEmail);
    loginPanel.add(new JLabel("Password:"));
    loginPanel.add(pfPassword);
    loginPanel.add(btnLogin);
    loginPanel.add(btnLogout);
    loginPanel.add(lblLoggedCompany);

    add(loginPanel, BorderLayout.NORTH);

    String[] columns = {
        "ID", "Title", "Company ID", "Location", "Position", "Status"
    };

    tableModel = new DefaultTableModel(columns, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };

    table = new JTable(tableModel);
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    table.setRowHeight(24);

    add(new JScrollPane(table), BorderLayout.CENTER);

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

    Dimension fieldSize = new Dimension(150, 26);
    JTextField[] fields = {
        tfTitle,
        tfDescription,
        tfCompanyId,
        tfLocation,
        tfPosition,
        tfStartDate,
        tfEndDate,
        tfStatus
    };

    for (JTextField field : fields) {
      field.setPreferredSize(fieldSize);
      field.setMinimumSize(fieldSize);
    }

    addField(formPanel, gbc, 0, "Title:", tfTitle);
    addField(formPanel, gbc, 1, "Description:", tfDescription);
    addField(formPanel, gbc, 2, "Company ID:", tfCompanyId);
    addField(formPanel, gbc, 3, "Location:", tfLocation);
    addField(formPanel, gbc, 4, "Position:", tfPosition);
    addField(formPanel, gbc, 5, "Start Date:", tfStartDate);
    addField(formPanel, gbc, 6, "End Date:", tfEndDate);
    addField(formPanel, gbc, 7, "Status:", tfStatus);

    JButton btnAdd = new JButton("Add Internship");

    gbc.gridx = 0;
    gbc.gridy = 8;
    gbc.gridwidth = 2;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    formPanel.add(btnAdd, gbc);

    JPanel rightPanel = new JPanel(new BorderLayout());
    rightPanel.setPreferredSize(new Dimension(300, 0));
    rightPanel.add(formPanel, BorderLayout.NORTH);

    add(rightPanel, BorderLayout.EAST);

    JButton btnRefresh = new JButton("Refresh");
    JButton btnDelete = new JButton("Delete Selected");
    JButton btnViewApplications = new JButton("View Applications");
    JButton btnUpdateStatus = new JButton("Update Application Status");

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    buttonPanel.add(btnRefresh);
    buttonPanel.add(btnDelete);
    buttonPanel.add(btnViewApplications);
    buttonPanel.add(btnUpdateStatus);

    lblStatus = new JLabel("Ready.");

    JPanel bottomPanel = new JPanel(new BorderLayout());
    bottomPanel.add(buttonPanel, BorderLayout.WEST);
    bottomPanel.add(lblStatus, BorderLayout.EAST);

    add(bottomPanel, BorderLayout.SOUTH);

    btnLogin.addActionListener(e -> loginCompany());
    btnLogout.addActionListener(e -> logoutCompany());
    btnRefresh.addActionListener(e -> loadInternships());
    btnAdd.addActionListener(e -> addInternship());
    btnDelete.addActionListener(e -> deleteSelected());
    btnViewApplications.addActionListener(e -> viewApplications());
    btnUpdateStatus.addActionListener(e -> updateApplicationStatus());
  }

  private void loginCompany() {
    String email = tfEmail.getText().trim();
    String password = new String(pfPassword.getPassword());

    boolean success = currentCompany.login(email, password);

    if (success) {
      loggedIn = true;
      lblLoggedCompany.setText("Logged in as " + currentCompany.getCompanyName());
      setStatus("Login successful.", false);
    } else {
      loggedIn = false;
      lblLoggedCompany.setText("Not logged in");
      setStatus("Wrong email or password.", true);
    }
  }

  private void logoutCompany() {
    currentCompany.logout();

    loggedIn = false;
    tfEmail.setText("");
    pfPassword.setText("");
    lblLoggedCompany.setText("Not logged in");

    setStatus("Logged out.", false);
  }

  private void addField(JPanel panel, GridBagConstraints gbc, int row,
      String labelText, JTextField textField) {

    gbc.gridx = 0;
    gbc.gridy = row;
    gbc.gridwidth = 1;
    gbc.fill = GridBagConstraints.NONE;
    panel.add(new JLabel(labelText), gbc);

    gbc.gridx = 1;
    gbc.weightx = 1.0;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    panel.add(textField, gbc);

    gbc.weightx = 0.0;
    gbc.fill = GridBagConstraints.NONE;
  }

  private void loadInternships() {
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

      setStatus(internships.size() + " internships loaded.", false);

    } catch (Exception e) {
      setStatus("Error loading internships.", true);
    }
  }

  private void addInternship() {
    if (!loggedIn) {
      setStatus("You must login first.", true);
      return;
    }

    try {
      Internship internship = new Internship(
          0,
          tfTitle.getText().trim(),
          tfDescription.getText().trim(),
          Integer.parseInt(tfCompanyId.getText().trim()),
          tfLocation.getText().trim(),
          tfPosition.getText().trim(),
          LocalDate.parse(tfStartDate.getText().trim()),
          LocalDate.parse(tfEndDate.getText().trim()),
          tfStatus.getText().trim()
      );

      boolean success = client.add(internship);

      if (success) {
        currentCompany.addInternship();
        setStatus("Internship added successfully.", false);
        clearFields();
        loadInternships();
      } else {
        setStatus("Could not add internship.", true);
      }

    } catch (Exception e) {
      setStatus("Error: check all fields.", true);
    }
  }

  private void deleteSelected() {
    if (!loggedIn) {
      setStatus("You must login first.", true);
      return;
    }

    int selectedRow = table.getSelectedRow();

    if (selectedRow == -1) {
      setStatus("Please select an internship.", true);
      return;
    }

    int id = (int) tableModel.getValueAt(selectedRow, 0);

    try {
      boolean success = client.delete(id);

      if (success) {
        currentCompany.deleteInternship();
        setStatus("Internship deleted.", false);
        loadInternships();
      } else {
        setStatus("Internship not found.", true);
      }

    } catch (Exception e) {
      setStatus("Error deleting internship.", true);
    }
  }

  private void viewApplications() {
    if (!loggedIn) {
      setStatus("You must login first.", true);
      return;
    }

    try {
      List<Application> applications = client.getAllApplications();

      if (applications.isEmpty()) {
        JOptionPane.showMessageDialog(
            this,
            "There are no applications yet.",
            "Applications",
            JOptionPane.INFORMATION_MESSAGE
        );
        return;
      }

      String[] columns = {
          "Application ID",
          "Student ID",
          "Internship ID",
          "Status",
          "Date"
      };

      DefaultTableModel applicationModel =
          new DefaultTableModel(columns, 0);

      for (Application application : applications) {
        applicationModel.addRow(new Object[]{
            application.getApplicationId(),
            application.getStudentId(),
            application.getInternshipId(),
            application.getStatus(),
            application.getApplicationDate()
        });
      }

      JTable applicationTable = new JTable(applicationModel);
      JScrollPane scrollPane = new JScrollPane(applicationTable);

      JOptionPane.showMessageDialog(
          this,
          scrollPane,
          "Applications",
          JOptionPane.INFORMATION_MESSAGE
      );

      setStatus(applications.size() + " applications loaded.", false);

    } catch (Exception e) {
      setStatus("Error loading applications: " + e.getMessage(), true);
    }
  }

  private void updateApplicationStatus() {
    if (!loggedIn) {
      setStatus("You must login first.", true);
      return;
    }

    String applicationIdText = JOptionPane.showInputDialog(
        this,
        "Enter Application ID:"
    );

    if (applicationIdText == null || applicationIdText.trim().isEmpty()) {
      return;
    }

    String[] options = {"Pending", "Accepted", "Rejected"};

    String newStatus = (String) JOptionPane.showInputDialog(
        this,
        "Select new status:",
        "Update Status",
        JOptionPane.QUESTION_MESSAGE,
        null,
        options,
        options[0]
    );

    if (newStatus == null) {
      return;
    }

    try {
      int applicationId = Integer.parseInt(applicationIdText.trim());

      boolean success =
          client.updateApplicationStatus(applicationId, newStatus);

      if (success) {
        setStatus("Application status updated to " + newStatus + ".", false);
      } else {
        setStatus("Application not found or internship already has an accepted student.", true);
      }

    } catch (NumberFormatException e) {
      setStatus("Application ID must be a number.", true);

    } catch (Exception e) {
      setStatus("Error updating application status: " + e.getMessage(), true);
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
    lblStatus.setText(message);
    lblStatus.setForeground(isError ? Color.RED : new Color(0, 128, 0));
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      CompanyGUI gui = new CompanyGUI();
      gui.setVisible(true);
    });
  }
}
