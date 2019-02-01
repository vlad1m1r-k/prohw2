package ua.kiev.prog.hw2.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class User implements Serializable {
    private String login;
    private String password;

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String toJSON() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }

    public static User fromJSON(String string) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(string, User.class);
    }

    public int[] send(String url) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        OutputStream os = conn.getOutputStream();
        try {
            String json = toJSON();
            os.write(json.getBytes(StandardCharsets.UTF_8));
        } finally {
            os.close();
        }
        InputStream inputStream = conn.getInputStream();
        try {
            byte[] response = new byte[inputStream.available()];
            inputStream.read(response);
            int[] result = new int[2];
            result[0] = conn.getResponseCode();
            result[1] = Integer.parseInt(new String(response).trim());
            return result;
        } finally {
            inputStream.close();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return login.toLowerCase().equals(user.login.toLowerCase()) &&
                password.equals(user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login.toLowerCase(), password);
    }
}
