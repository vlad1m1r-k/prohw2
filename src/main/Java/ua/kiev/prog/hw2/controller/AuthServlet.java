package ua.kiev.prog.hw2.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "AuthServlet", urlPatterns = "/auth")
public class AuthServlet extends HttpServlet {
    private Users users = Users.getInstance();
    private OnlineUsers onlineUsers = OnlineUsers.getInstance();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        InputStream inputStream = request.getInputStream();
        byte[] buf = new byte[10240];
        inputStream.read(buf);
        String bufStr = new String(buf, StandardCharsets.UTF_8).trim();
        User user = User.fromJSON(bufStr);
        if (user != null && users.isUserAuthenticated(user) && !onlineUsers.isUserOnline(user)) {
            int token = onlineUsers.add(user);
            response.getWriter().println(token);
        } else {
            response.getWriter().println("0");
        }
    }
}
