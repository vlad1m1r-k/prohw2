package ua.kiev.prog.hw2.controller;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@WebServlet(urlPatterns = "/get")
public class GetListServlet extends HttpServlet {

    private MessageList msgList = MessageList.getInstance();
    private OnlineUsers onlineUsers = OnlineUsers.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String fromStr = req.getParameter("from");
        int token;
        int from = 0;
        try {
            token = Integer.parseInt(req.getParameter("token"));
            if (!onlineUsers.isTokenPresent(token)) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            from = Integer.parseInt(fromStr);
            if (from < 0) {
                from = 0;
            }
        } catch (Exception ex) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        resp.setContentType("application/json");

        String json = msgList.toJSON(from, onlineUsers.getLogin(token), onlineUsers.getRoom(token));
        if (json != null) {
            OutputStream os = resp.getOutputStream();
            byte[] buf = json.getBytes(StandardCharsets.UTF_8);
            os.write(buf);

            //PrintWriter pw = resp.getWriter();
            //pw.print(json);
        }
    }
}