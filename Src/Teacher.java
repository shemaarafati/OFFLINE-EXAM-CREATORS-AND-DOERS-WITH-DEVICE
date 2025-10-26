package OFFLINE;


public class Teacher extends User {
    public Teacher(String userId, String username, String password) {
        super(userId, username, password);
    }

    @Override
    public void login() {
        System.out.println("Teacher logged in");
    }

    @Override
    public void logout() {
        System.out.println("Teacher logged out");
    }
}

