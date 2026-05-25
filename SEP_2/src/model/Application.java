    package model;

    import java.io.Serializable;
    import java.time.LocalDate;

    public class Application implements Serializable {

      private int applicationId;
      private int studentId;
      private int internshipId;
      private String status;
      private LocalDate applicationDate;
      private String studentName;
      private int studentAge;
      private String university;
      private String workingExperience;
      private String personalityTraits;

      public Application(int applicationId, int studentId,
          int internshipId, String status,
          LocalDate applicationDate) {

        this(applicationId, studentId, internshipId, status,
            applicationDate, "", 0, "", "", "");
      }

      public Application(int applicationId, int studentId,
          int internshipId, String status,
          LocalDate applicationDate,
          String studentName, int studentAge,
          String university, String workingExperience,
          String personalityTraits) {

        this.applicationId = applicationId;
        this.studentId = studentId;
        this.internshipId = internshipId;
        this.status = status;
        this.applicationDate = applicationDate;
        this.studentName = studentName;
        this.studentAge = studentAge;
        this.university = university;
        this.workingExperience = workingExperience;
        this.personalityTraits = personalityTraits;
      }

      public int getApplicationId() {
        return applicationId;
      }

      public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
      }

      public int getStudentId() {
        return studentId;
      }

      public void setStudentId(int studentId) {
        this.studentId = studentId;
      }

      public int getInternshipId() {
        return internshipId;
      }

      public void setInternshipId(int internshipId) {
        this.internshipId = internshipId;
      }

      public String getStatus() {
        return status;
      }

      public void setStatus(String status) {
        this.status = status;
      }

      public LocalDate getApplicationDate() {
        return applicationDate;
      }

      public void setApplicationDate(LocalDate applicationDate) {
        this.applicationDate = applicationDate;
      }

      public String getStudentName() {
        return studentName;
      }

      public void setStudentName(String studentName) {
        this.studentName = studentName;
      }

      public int getStudentAge() {
        return studentAge;
      }

      public void setStudentAge(int studentAge) {
        this.studentAge = studentAge;
      }

      public String getUniversity() {
        return university;
      }

      public void setUniversity(String university) {
        this.university = university;
      }

      public String getWorkingExperience() {
        return workingExperience;
      }

      public void setWorkingExperience(String workingExperience) {
        this.workingExperience = workingExperience;
      }

      public String getPersonalityTraits() {
        return personalityTraits;
      }

      public void setPersonalityTraits(String personalityTraits) {
        this.personalityTraits = personalityTraits;
      }

      public void applyForInternship() {
        System.out.println("Application submitted.");
      }

      public void checkStatus() {
        System.out.println("Application status: " + status);
      }

      public void updateStatus(String newStatus) {
        if (newStatus == null) {
          throw new IllegalArgumentException("status must not be null");
        }
        this.status = newStatus;
        System.out.println("Status updated to: " + status);
      }
    }
