package OFFLINE;


public class Student extends User {
    public Student(String userId, String username, String password) {
        super(userId, username, password);
    }

    @Override
    public void login() {
        System.out.println("Student logged in");
    }

    @Override
    public void logout() {
        System.out.println("Student logged out");
    }
}
