package task.lab_project_oop;

public class Login {
    private String username;
    private String password;
    private String user;

    public Login(String username, String password, String user) {
        this.username = username;
        this.password = password;
        this.user = user;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getUser() {
        return user;
    }
}
