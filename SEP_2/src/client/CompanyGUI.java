package client;

import model.Application;
import model.Company;
import model.Internship;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
    JButton btnDeleteApplication = new JButton("Delete Application");

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    buttonPanel.add(btnRefresh);
    buttonPanel.add(btnDelete);
    buttonPanel.add(btnViewApplications);
    buttonPanel.add(btnUpdateStatus);
    buttonPanel.add(btnDeleteApplication);

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
    btnDeleteApplication.addActionListener(e -> deleteApplication());
  }

  private void loginCompany() {
    String email = tfEmail.getText().trim();
    String password = new String(pfPassword.getPassword());

    try {
      Company matchingCompany = client.loginCompany(email, password);

      if (matchingCompany != null) {
        currentCompany = matchingCompany;
        loggedIn = true;
        tfCompanyId.setText(String.valueOf(currentCompany.getCompanyId()));
        tfCompanyId.setEditable(false);
        lblLoggedCompany.setText("Logged in as " + currentCompany.getCompanyName());
        setStatus("Login successful.", false);
      } else {
        loggedIn = false;
        lblLoggedCompany.setText("Not logged in");
        setStatus("Wrong email or password.", true);
      }

    } catch (Exception e) {
      loggedIn = false;
      lblLoggedCompany.setText("Not logged in");
      setStatus("Login error: " + e.getMessage(), true);
    }
  }

  private void logoutCompany() {
    if (currentCompany != null) {
      currentCompany.logout();
    }

    currentCompany = null;
    loggedIn = false;
    tfEmail.setText("");
    pfPassword.setText("");
    tfCompanyId.setText("");
    tfCompanyId.setEditable(true);
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
          currentCompany.getCompanyId(),
          tfLocation.getText().trim(),
          tfPosition.getText().trim(),
          parseDate(tfStartDate.getText().trim()),
          parseDate(tfEndDate.getText().trim()),
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

    } catch (IllegalArgumentException e) {
      setStatus(e.getMessage(), true);

    } catch (Exception e) {
      setStatus("Error: check all fields.", true);
    }
  }

  private LocalDate parseDate(String dateText) {
    DateTimeFormatter danishFormatter =
        DateTimeFormatter.ofPattern("d.M.yyyy");

    try {
      return LocalDate.parse(dateText);
    } catch (DateTimeParseException e) {
      try {
        return LocalDate.parse(dateText, danishFormatter);
      } catch (DateTimeParseException ex) {
        throw new IllegalArgumentException(
            "Wrong date format. Use yyyy-MM-dd or d.M.yyyy."
        );
      }
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
      List<Application> applications =
          client.getApplicationsByCompany(currentCompany.getCompanyId());

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
          "Student Name",
          "Age",
          "University",
          "Working Experience",
          "Personality Traits",
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
            application.getStudentName(),
            application.getStudentAge() == 0 ? "" : application.getStudentAge(),
            application.getUniversity(),
            application.getWorkingExperience(),
            application.getPersonalityTraits(),
            application.getInternshipId(),
            application.getStatus(),
            application.getApplicationDate()
        });
      }

      showApplicationsDialog(applicationModel);

      setStatus(applications.size() + " applications loaded.", false);

    } catch (Exception e) {
      setStatus("Error loading applications: " + e.getMessage(), true);
    }
  }

  private void showApplicationsDialog(DefaultTableModel applicationModel) {
    JDialog dialog = new JDialog(this, "Applications", true);
    dialog.setLayout(new BorderLayout(10, 10));

    JTable applicationTable = new JTable(applicationModel);
    applicationTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    applicationTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    applicationTable.setRowHeight(24);
    applicationTable.getColumnModel().getColumn(0).setPreferredWidth(100);
    applicationTable.getColumnModel().getColumn(1).setPreferredWidth(80);
    applicationTable.getColumnModel().getColumn(2).setPreferredWidth(130);
    applicationTable.getColumnModel().getColumn(3).setPreferredWidth(50);
    applicationTable.getColumnModel().getColumn(4).setPreferredWidth(160);
    applicationTable.getColumnModel().getColumn(5).setPreferredWidth(240);
    applicationTable.getColumnModel().getColumn(6).setPreferredWidth(220);
    applicationTable.getColumnModel().getColumn(7).setPreferredWidth(90);
    applicationTable.getColumnModel().getColumn(8).setPreferredWidth(90);
    applicationTable.getColumnModel().getColumn(9).setPreferredWidth(100);

    JScrollPane scrollPane = new JScrollPane(applicationTable);
    scrollPane.setPreferredSize(new Dimension(900, 300));

    JButton btnDeleteSelected = new JButton("Delete Selected");
    JButton btnClose = new JButton("Close");

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.add(btnDeleteSelected);
    buttonPanel.add(btnClose);

    btnDeleteSelected.addActionListener(e ->
        deleteSelectedApplicationFromTable(applicationTable, applicationModel)
    );
    btnClose.addActionListener(e -> dialog.dispose());

    dialog.add(scrollPane, BorderLayout.CENTER);
    dialog.add(buttonPanel, BorderLayout.SOUTH);
    dialog.pack();
    dialog.setLocationRelativeTo(this);
    dialog.setVisible(true);
  }

  private void deleteSelectedApplicationFromTable(
      JTable applicationTable,
      DefaultTableModel applicationModel
  ) {
    int selectedRow = applicationTable.getSelectedRow();

    if (selectedRow == -1) {
      setStatus("Please select an application.", true);
      return;
    }

    int modelRow = applicationTable.convertRowIndexToModel(selectedRow);
    int applicationId = (int) applicationModel.getValueAt(modelRow, 0);

    int choice = JOptionPane.showConfirmDialog(
        this,
        "Delete application " + applicationId + "?",
        "Confirm Delete",
        JOptionPane.YES_NO_OPTION
    );

    if (choice != JOptionPane.YES_OPTION) {
      return;
    }

    try {
      boolean success = client.deleteApplication(applicationId);

      if (success) {
        applicationModel.removeRow(modelRow);
        setStatus("Application deleted.", false);
      } else {
        setStatus("Application not found.", true);
      }

    } catch (Exception e) {
      setStatus("Error deleting application: " + e.getMessage(), true);
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
          client.updateApplicationStatusForCompany(
              applicationId,
              newStatus,
              currentCompany.getCompanyId()
          );

      if (success) {
        setStatus("Application status updated to " + newStatus + ".", false);
      } else {
        setStatus("Application not found for your company or internship already has an accepted student.", true);
      }

    } catch (NumberFormatException e) {
      setStatus("Application ID must be a number.", true);

    } catch (Exception e) {
      setStatus("Error updating application status: " + e.getMessage(), true);
    }
  }

  private void deleteApplication() {
    if (!loggedIn) {
      setStatus("You must login first.", true);
      return;
    }

    String applicationIdText = JOptionPane.showInputDialog(
        this,
        "Enter Application ID to delete:"
    );

    if (applicationIdText == null || applicationIdText.trim().isEmpty()) {
      return;
    }

    try {
      int applicationId = Integer.parseInt(applicationIdText.trim());

      int choice = JOptionPane.showConfirmDialog(
          this,
          "Delete application " + applicationId + "?",
          "Confirm Delete",
          JOptionPane.YES_NO_OPTION
      );

      if (choice != JOptionPane.YES_OPTION) {
        return;
      }

      boolean success = client.deleteApplication(applicationId);

      if (success) {
        setStatus("Application deleted.", false);
      } else {
        setStatus("Application not found.", true);
      }

    } catch (NumberFormatException e) {
      setStatus("Application ID must be a number.", true);

    } catch (Exception e) {
      setStatus("Error deleting application: " + e.getMessage(), true);
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
