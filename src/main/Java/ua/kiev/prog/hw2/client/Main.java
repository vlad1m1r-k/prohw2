package ua.kiev.prog.hw2.client;


import ua.kiev.prog.hw2.controller.Message;
import ua.kiev.prog.hw2.controller.User;

import java.io.Console;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String choose = "";
        try {
            do {
                System.out.println("1 - Enter to Chat.  2 - Register new user.  Enter - Exit.");
                if (scanner.hasNextLine()) {
                    choose = scanner.nextLine();
                }
                switch (choose) {
                    case "1":
                        login();
                        break;
                    case "2":
                        register();
                        break;
                }
            } while (!choose.equals(""));
        } finally {
            scanner.close();
        }
    }

    private static void register() {
        Console console = System.console();
        if (console != null) {
            String login = console.readLine("Login: ");
            if (login.equals("")) {
                return;
            }
            String pass1 = new String(console.readPassword("Password: "));
            String pass2 = new String(console.readPassword("Retype Password: "));
            if (pass1.equals(pass2) && !pass1.equals("")) {
                User user = new User(login, pass1);
                try {
                    int[] result = user.send(Utils.getURL() + "/register");
                    if (result[0] == 200 && result[1] == 0) {
                        System.out.println("Registration successful.");
                    } else {
                        System.out.println("Error registering user. Login may already taken.");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Passwords is not match or empty. Try again.");
            }
        } else {
            System.out.println("Use console for this operation.");
        }
    }

    private static void login() {
        Console console = System.console();
        if (console != null) {
            String login = console.readLine("Login: ");
            if (login.equals("")) {
                return;
            }
            String pass = new String(console.readPassword("Password: "));
            User user = new User(login, pass);
            try {
                int[] result = user.send(Utils.getURL() + "/auth");
                if (result[0] == 200 && result[1] != 0) {
                    mainChat(result[1], login);
                } else {
                    System.out.println("Wrong credentials or user is already online.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Use console for this operation.");
        }
    }

    private static void mainChat(int token, String login) {
        Scanner scanner = new Scanner(System.in);
        GetThread listener = new GetThread(token);
        Thread th = new Thread(listener);
        th.setDaemon(true);
        th.start();
        try {
            System.out.println("Welcome to chat! Type /? for command list.");
            while (true) {
                String text = scanner.nextLine();
                if (text.isEmpty()) break;

                Message m = new Message(login, text, token);
                int res = m.send(Utils.getURL() + "/add");

                if (res != 200) { // 200 OK
                    System.out.println("HTTP error occurred: " + res);
                    return;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            listener.exitRequest();
            logOff(token);
        }
    }

    private static void logOff(int token) {
        try {
            URL obj = new URL(Utils.getURL() + "/logoff");
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();
            try {
                os.write(String.valueOf(token).getBytes(StandardCharsets.UTF_8));
            } finally {
                conn.getResponseCode();
                conn.disconnect();
                os.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Log off.");
    }
}