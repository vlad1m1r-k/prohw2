package ua.kiev.prog.hw2.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "RegisterServlet", urlPatterns = "/register")
public class RegisterServlet extends HttpServlet {
    private Users users = Users.getInstance();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        InputStream inputStream = request.getInputStream();
        byte[] buf = new byte[10240];
        inputStream.read(buf);
        String bufStr = new String(buf, StandardCharsets.UTF_8).trim();
        User user = User.fromJSON(bufStr);
        if (user != null && user.getLogin() != null && user.getPassword() != null && !user.getLogin().equals("") &&
                !users.isLoginTaken(user.getLogin()) && !user.getPassword().equals("") && users.addUser(user)) {
            response.getWriter().println("0");
        } else {
            response.getWriter().println("-1");
        }
    }
}
