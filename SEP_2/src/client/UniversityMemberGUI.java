package client;

import model.UniversityMember;
import server.UniversityMemberRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UniversityMemberGUI extends JFrame {

  private final UniversityMemberRepository repository =
      new UniversityMemberRepository();

  private JTable table;
  private DefaultTableModel tableModel;

  private JTextField tfEmail;
  private JPasswordField pfPassword;

  private JLabel lblLoggedUser;
  private JLabel lblStatus;

  private boolean loggedIn = false;

  public UniversityMemberGUI() {

    super("University Member Portal");

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(850, 500);
    setLocationRelativeTo(null);

    initComponents();
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
        "Application ID",
        "Student ID",
        "Internship ID",
        "Status",
        "Date"
    };

    tableModel = new DefaultTableModel(columns, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };

    table = new JTable(tableModel);
    table.setRowHeight(24);

    JScrollPane scrollPane = new JScrollPane(table);
    add(scrollPane, BorderLayout.CENTER);

    JButton btnRefresh = new JButton("Refresh");

    lblStatus = new JLabel("Ready.");

    JPanel bottomPanel = new JPanel(new BorderLayout());
    bottomPanel.add(btnRefresh, BorderLayout.WEST);
    bottomPanel.add(lblStatus, BorderLayout.EAST);

    add(bottomPanel, BorderLayout.SOUTH);

    btnLogin.addActionListener(e -> login());
    btnLogout.addActionListener(e -> logout());
    btnRefresh.addActionListener(e -> loadStudentProgress());
  }

  private void login() {

    String email = tfEmail.getText().trim();
    String password = new String(pfPassword.getPassword());

    List<UniversityMember> members = repository.getAll();

    for (UniversityMember member : members) {

      if (member.getEmail().equals(email)
          && password.equals("1234")) {

        loggedIn = true;

        lblLoggedUser.setText(
            "Logged in as " + member.getName()
        );

        setStatus("Login successful.", false);

        loadStudentProgress();

        return;
      }
    }

    loggedIn = false;
    lblLoggedUser.setText("Not logged in");
    setStatus("Wrong email or password.", true);
  }

  private void logout() {

    loggedIn = false;

    tfEmail.setText("");
    pfPassword.setText("");

    lblLoggedUser.setText("Not logged in");

    tableModel.setRowCount(0);

    setStatus("Logged out.", false);
  }

  private void loadStudentProgress() {

    if (!loggedIn) {
      setStatus("Please login first.", true);
      return;
    }

    tableModel.setRowCount(0);

    tableModel.addRow(new Object[]{1, 1, 1, "Rejected", "2026-05-12"});
    tableModel.addRow(new Object[]{2, 1, 2, "Accepted", "2026-05-12"});
    tableModel.addRow(new Object[]{3, 1, 1, "Pending", "2026-05-12"});
    tableModel.addRow(new Object[]{4, 1, 7, "Pending", "2026-05-12"});
    tableModel.addRow(new Object[]{5, 1, 13, "Pending", "2026-05-12"});

    setStatus("Student progress loaded.", false);
  }

  private void setStatus(String message, boolean isError) {

    lblStatus.setText(message);

    lblStatus.setForeground(
        isError ? Color.RED : new Color(0, 128, 0)
    );
  }

  public static void main(String[] args) {

    SwingUtilities.invokeLater(() -> {

      UniversityMemberGUI gui =
          new UniversityMemberGUI();

      gui.setVisible(true);
    });
  }
}