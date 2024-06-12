package task.lab_project_oop;

public class Doctor {
    private int id;
    private String username;
    private String password;
    private String name;
    private String email;
    private String contact;

    public Doctor(int id,String username, String password, String name, String email, String contact) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.id = id;
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

    public String getContact() {
        return contact;
    }
    public int getId() {
        return id;
    }
}