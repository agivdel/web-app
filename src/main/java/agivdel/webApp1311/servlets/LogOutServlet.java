package agivdel.webApp1311.servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = {"/logout", "/logout-servlet"})
public class LogOutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //удалить Cookie пользователя
        Cookie cookieUsername = new Cookie("username", null);
        //0 секунд. Данный Cookie будет сразу недействителен
        cookieUsername.setMaxAge(0);
        resp.addCookie(cookieUsername);

        req.getSession().invalidate();
        resp.sendRedirect(req.getContextPath() + "/index.jsp");//перенаправление на начальную страницу
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        this.doGet(req, resp);
    }
}