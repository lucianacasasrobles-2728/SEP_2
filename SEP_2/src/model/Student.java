package model;

public class Student
{
    private int studentId;
    private String name;
    private String email;
    private String cv;

    public Student(int studentId, String name, String email, String cv){
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.cv = cv;
    }

    public int getStudentId()
    {
        return studentId;
    }

    public String getName()
    {
        return name;
    }
    public String getEmail(){
        return email;
    }

    public String getCv(){
        return cv;

    }

    public void setStudentId(int studentId)
    {
        this.studentId = studentId;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public void setCv(String cv)
    {
        this.cv = cv;
    }
    public void browseInternships() {
        //  Connect with UI_Client and System
    }

    public void clickApply() {
        //  Connect with UI_Client
    }

    public void confirmApplication() {
        //  Connect with System and Database
    }

    public void receiveNotification() {
        // Connect with  UI_Client
    }
}
