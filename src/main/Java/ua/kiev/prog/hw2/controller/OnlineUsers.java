package ua.kiev.prog.hw2.controller;

import java.util.HashMap;
import java.util.Random;

public class OnlineUsers {
    private static final OnlineUsers online = new OnlineUsers();
    private HashMap<Integer, OnlineUser> onlineUsers = new HashMap<>();

    private OnlineUsers() {
    }

    public static OnlineUsers getInstance() {
        return online;
    }

    public synchronized int add(User user) {
        int token;
        do {
            token = new Random().nextInt();
        } while (token == 0 || isTokenPresent(token));
        onlineUsers.put(token, new OnlineUser(user.getLogin()));
        return  token;
    }

    public synchronized boolean isTokenPresent(int token) {
        if (onlineUsers.containsKey(token)) {
            return true;
        }
        return false;
    }

    public String getLogin(int token) {
        if (isTokenPresent(token)) {
            return onlineUsers.get(token).getLogin();
        }
        return null;
    }

    public String getRoom(int token) {
        return onlineUsers.get(token).getRoom();
    }

    public String getRoom(String login) {
        for (OnlineUser user : onlineUsers.values()) {
            if (login.toLowerCase().equals(user.getLogin().toLowerCase())) {
                return user.getRoom();
            }
        }
        return null;
    }

    public synchronized void setRoom(int token, String room) {
        onlineUsers.get(token).setRoom(room);
    }

    public void setRoom(String login, String room) {
        for (OnlineUser user : onlineUsers.values()) {
            if (login.toLowerCase().equals(user.getLogin().toLowerCase())) {
                user.setRoom(room);
            }
        }
    }

    public boolean isUserOnline(User user) {
        if (user != null && onlineUsers.containsValue(new OnlineUser(user.getLogin()))) {
            return true;
        }
        return false;
    }

    public boolean isUserOnline(String login) {
        if (login != null && onlineUsers.containsValue(new OnlineUser(login))) {
            return true;
        }
        return false;
    }

    public void logOff(int token) {
        onlineUsers.remove(token);
    }
}
