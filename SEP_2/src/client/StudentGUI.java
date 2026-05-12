package client;

import model.Application;
import model.Internship;
import model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class StudentGUI extends JFrame {

  private final Client client = new Client();

  private Student currentStudent;
  private boolean loggedIn = false;

  private JTable table;
  private DefaultTableModel tableModel;

  private JTextField tfEmail;
  private JPasswordField pfPassword;

  private JLabel lblStatus;
  private JLabel lblLoggedUser;

  public StudentGUI() {

    super("Student Internship Portal");

    currentStudent = new Student(
        1,
        "Luciana",
        "luciana@email.com",
        "1234",
        "My CV"
    );

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(850, 550);
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

    lblLoggedUser = new JLabel("Not logged in");

    loginPanel.add(new JLabel("Email:"));
    loginPanel.add(tfEmail);
    loginPanel.add(new JLabel("Password:"));
    loginPanel.add(pfPassword);
    loginPanel.add(btnLogin);
    loginPanel.add(btnLogout);
    loginPanel.add(lblLoggedUser);

    add(loginPanel, BorderLayout.NORTH);

    String[] columns = {
        "ID",
        "Title",
        "Company ID",
        "Location",
        "Position",
        "Status"
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

    JButton btnRefresh = new JButton("Refresh");
    JButton btnApply = new JButton("Apply Selected");
    JButton btnViewApplications = new JButton("View My Applications");

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    buttonPanel.add(btnRefresh);
    buttonPanel.add(btnApply);
    buttonPanel.add(btnViewApplications);

    lblStatus = new JLabel("Ready.");

    JPanel bottomPanel = new JPanel(new BorderLayout());
    bottomPanel.add(buttonPanel, BorderLayout.WEST);
    bottomPanel.add(lblStatus, BorderLayout.EAST);

    add(bottomPanel, BorderLayout.SOUTH);

    btnLogin.addActionListener(e -> loginStudent());
    btnLogout.addActionListener(e -> logoutStudent());
    btnRefresh.addActionListener(e -> loadInternships());
    btnApply.addActionListener(e -> applySelectedInternship());
    btnViewApplications.addActionListener(e -> viewMyApplications());
  }

  private void loginStudent() {

    String email = tfEmail.getText().trim();
    String password = new String(pfPassword.getPassword());

    boolean success = currentStudent.login(email, password);

    if (success) {
      loggedIn = true;
      lblLoggedUser.setText("Logged in as " + currentStudent.getName());
      setStatus("Login successful.", false);
    } else {
      loggedIn = false;
      lblLoggedUser.setText("Not logged in");
      setStatus("Wrong email or password.", true);
    }
  }

  private void logoutStudent() {

    currentStudent.logout();
    loggedIn = false;

    tfEmail.setText("");
    pfPassword.setText("");
    lblLoggedUser.setText("Not logged in");

    setStatus("Logged out.", false);
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
      setStatus("Error loading internships: " + e.getMessage(), true);
    }
  }

  private void applySelectedInternship() {

    if (!loggedIn) {
      setStatus("You must login first.", true);
      return;
    }

    int selectedRow = table.getSelectedRow();

    if (selectedRow == -1) {
      setStatus("Please select an internship.", true);
      return;
    }

    int internshipId = (int) tableModel.getValueAt(selectedRow, 0);

    Application application = new Application(
        0,
        currentStudent.getStudentId(),
        internshipId,
        "Pending",
        LocalDate.now()
    );

    try {
      boolean success = client.apply(application);

      if (success) {
        currentStudent.applyForInternship();
        setStatus("Application sent successfully.", false);
      } else {
        setStatus("Could not send application.", true);
      }

    } catch (Exception e) {
      setStatus("Error applying: " + e.getMessage(), true);
    }
  }

  private void viewMyApplications() {

    if (!loggedIn) {
      setStatus("You must login first.", true);
      return;
    }

    try {
      List<Application> applications =
          client.getApplicationsByStudent(currentStudent.getStudentId());

      if (applications.isEmpty()) {
        JOptionPane.showMessageDialog(
            this,
            "You have not applied to any internships yet.",
            "My Applications",
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
          "My Applications",
          JOptionPane.INFORMATION_MESSAGE
      );

      setStatus(applications.size() + " applications loaded.", false);

    } catch (Exception e) {
      setStatus("Error loading applications: " + e.getMessage(), true);
    }
  }

  private void setStatus(String message, boolean isError) {
    lblStatus.setText(message + "   ");
    lblStatus.setForeground(isError ? Color.RED : new Color(0, 128, 0));
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      StudentGUI gui = new StudentGUI();
      gui.setVisible(true);
    });
  }
}