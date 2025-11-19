package com.offlineexam.network;

import com.offlineexam.model.User;
import com.offlineexam.service.AuthService;
import com.offlineexam.service.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class ServerApp {

    public static void main(String[] args) {
        int port = 5000;
        System.out.println("ServerApp starting on port " + port + "...");

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server listening on 0.0.0.0:" + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected from " + clientSocket.getInetAddress());
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket socket;

        ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                String line;
                while ((line = in.readLine()) != null) {
                    String response = handleCommand(line);
                    out.println(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private String handleCommand(String line) {
            if (line == null || line.trim().isEmpty()) {
                return "ERROR|Empty command";
            }

            String[] parts = line.split("\\|", -1);
            String cmd = parts[0].trim().toUpperCase();

            try {
                switch (cmd) {
                    case "LOGIN":
                        return handleLogin(parts);
                    case "CREATE_USER":
                        return handleCreateUser(parts);
                    case "DELETE_USER":
                        return handleDeleteUser(parts);
                    case "GET_ALL_USERS":
                        return handleGetAllUsers();
                    default:
                        return "ERROR|Unknown command";
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                return "ERROR|" + ex.getMessage();
            }
        }

        private String handleLogin(String[] parts) {
            if (parts.length < 3) {
                return "ERROR|LOGIN requires username and password";
            }
            String username = parts[1];
            String password = parts[2];

            AuthService authService = new AuthService();
            User user = authService.login(username, password);
            if (user == null) {
                return "ERROR|Invalid credentials";
            }

            return "OK|" + user.getRole() + "|" + user.getUserId() + "|" + user.getUsername();
        }

        private String handleCreateUser(String[] parts) {
            if (parts.length < 4) {
                return "ERROR|CREATE_USER requires username, password, role";
            }
            String username = parts[1];
            String password = parts[2];
            String roleStr = parts[3];

            User.Role role = User.Role.valueOf(roleStr.toUpperCase());
            User newUser = new User(username, password, username, null, role);

            boolean ok = UserService.createUser(newUser);
            return ok ? "OK" : "ERROR|Failed to create user";
        }

        private String handleDeleteUser(String[] parts) {
            if (parts.length < 2) {
                return "ERROR|DELETE_USER requires userId";
            }
            String userId = parts[1];
            boolean ok = UserService.deleteUser(userId);
            return ok ? "OK" : "ERROR|Failed to delete user";
        }

        private String handleGetAllUsers() {
            List<User> users = UserService.getAllUsers();
            StringBuilder sb = new StringBuilder("OK|");
            for (int i = 0; i < users.size(); i++) {
                User u = users.get(i);
                if (i > 0) {
                    sb.append(";");
                }
                sb.append(u.getUserId()).append(",")
                  .append(u.getUsername()).append(",")
                  .append(u.getRole());
            }
            return sb.toString();
        }
    }
}
