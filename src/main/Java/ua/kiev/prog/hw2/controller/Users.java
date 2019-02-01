package ua.kiev.prog.hw2.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Users {
    private static final Users userList = new Users();
    private List<User> users;

    private Users() {
        loadUsers();
    }

    public static Users getInstance() {
        return userList;
    }

    public synchronized boolean addUser(User user) {
        if (user != null && !isLoginTaken(user.getLogin())) {
            users.add(user);
            saveUsers();
            return true;
        }
        return false;
    }

    public synchronized boolean isUserAuthenticated(User user) {
        for (User storedUser : users) {
            if (storedUser.equals(user)) {
                return true;
            }
        }
        return false;
    }

    public synchronized boolean isLoginTaken(String login) {
        for (User user : users) {
            if (login.toLowerCase().equals(user.getLogin().toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public String getRealLogin(String login) {
        for (User user : users) {
            if (login.toLowerCase().equals(user.getLogin().toLowerCase())) {
                return user.getLogin();
            }
        }
        return null;
    }

    public String getUserList() {
        StringBuilder list = new StringBuilder("Registered users:\n");
        for (User user : users) {
            list.append(user.getLogin());
            list.append("\n");
        }
        return list.toString();
    }

    private void loadUsers() {
        File file = new File("Users.sav");
        if (!file.exists()) {
            users = new ArrayList<>();
        } else {
            try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(file))) {
                users = (ArrayList<User>)input.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void saveUsers() {
        File file = new File("Users.sav");
        if (file.exists() && !file.isDirectory()) file.delete();
        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(file))) {
            output.writeObject(users);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
