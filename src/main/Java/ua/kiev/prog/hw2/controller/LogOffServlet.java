package ua.kiev.prog.hw2.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "LogOffServlet", urlPatterns = "/logoff")
public class LogOffServlet extends HttpServlet {
    private OnlineUsers onlineUsers = OnlineUsers.getInstance();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        InputStream inputStream = request.getInputStream();
        byte[] buf = new byte[10];
        inputStream.read(buf);
        String bufStr = new String(buf, StandardCharsets.UTF_8).trim();
        int token = Integer.valueOf(bufStr);
        onlineUsers.logOff(token);
    }
}
