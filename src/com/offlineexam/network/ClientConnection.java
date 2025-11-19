package com.offlineexam.network;

import com.offlineexam.model.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientConnection {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public ClientConnection(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }

    public User login(String username, String password) throws IOException {
        out.println("LOGIN|" + username + "|" + password);
        String resp = in.readLine();
        if (resp == null) {
            return null;
        }
        String[] parts = resp.split("\\|", -1);
        if (!"OK".equals(parts[0])) {
            return null;
        }
        String roleStr = parts[1];
        String userId = parts[2];
        String uname = parts[3];

        User.Role role = User.Role.valueOf(roleStr.toUpperCase());
        return new User(userId, uname, password, uname, null, role);
    }

    public boolean createUser(User user) throws IOException {
        out.println("CREATE_USER|" + user.getUsername() + "|" + user.getPassword() + "|" + user.getRole());
        String resp = in.readLine();
        return resp != null && resp.startsWith("OK");
    }

    public boolean deleteUser(String userId) throws IOException {
        out.println("DELETE_USER|" + userId);
        String resp = in.readLine();
        return resp != null && resp.startsWith("OK");
    }

    public List<User> getAllUsers() throws IOException {
        out.println("GET_ALL_USERS");
        String resp = in.readLine();
        List<User> result = new ArrayList<>();
        if (resp == null) {
            return result;
        }
        String[] parts = resp.split("\\|", 2);
        if (!"OK".equals(parts[0])) {
            return result;
        }
        if (parts.length < 2 || parts[1].isEmpty()) {
            return result;
        }
        String payload = parts[1];
        String[] items = payload.split(";");
        for (String item : items) {
            String[] f = item.split(",");
            if (f.length < 3) {
                continue;
            }
            String id = f[0];
            String uname = f[1];
            String roleStr = f[2];
            User.Role role = User.Role.valueOf(roleStr.toUpperCase());
            result.add(new User(id, uname, "", uname, null, role));
        }
        return result;
    }

    public void close() {
        try {
            if (in != null) in.close();
        } catch (IOException ignored) {}
        if (out != null) out.close();
        try {
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException ignored) {}
    }
}
