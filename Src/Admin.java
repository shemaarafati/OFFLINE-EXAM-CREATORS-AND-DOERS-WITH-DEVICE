package OFFLINE;


public class Admin extends User {
    public Admin(String userId, String username, String password) {
        super(userId, username, password);
    }

    @Override
    public void login() {
        System.out.println("Admin logged in");
    }

    @Override
    public void logout() {
        System.out.println("Admin logged out");
    }
}
