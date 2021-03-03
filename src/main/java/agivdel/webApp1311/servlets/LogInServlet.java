package agivdel.webApp1311.servlets;

import agivdel.webApp1311.service.ServletUtil;
import agivdel.webApp1311.entities.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = {"/login", "/login-servlet"})
public class LogInServlet extends HttpServlet {
    private final String addressIfError = "/index.jsp";
    private final String addressIfSuccess = "/payment.jsp";
    //при успешной аутентификации выдает токен

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.getServletContext().getRequestDispatcher(addressIfError).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        ServletUtil servletUtil = new ServletUtil(this, req, resp, addressIfError, addressIfSuccess);
        servletUtil.valid(username, password);
        servletUtil.isExists(username, password);
        User user = servletUtil.authentication(username, password);
        servletUtil.makeSessionAndGo(username, user);
    }
}