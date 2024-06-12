package task.lab_project_oop;

public class Patient {
    private int id;
    private String username;
    private String password;
    private String name;
    private String email;
    private String contactNo;

    public Patient(int id, String username, String password, String name, String email, String contactNo) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.contactNo = contactNo;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getContactNo() {
        return contactNo;
    }
}