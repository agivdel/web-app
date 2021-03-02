package agivdel.webApp1311.servlets;

import agivdel.webApp1311.service.Check;
import agivdel.webApp1311.service.Service;
import agivdel.webApp1311.entities.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(urlPatterns = {"/login", "/login-servlet"})
public class LogInServlet extends HttpServlet {
    //проверяет login и password
    //при успешной аутентификации выдает токен
    //перенаправляет на сервлет payment


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.getServletContext().getRequestDispatcher("views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        Check check = new Check(this, req, resp);
        check.userValid(username, password);
        check.userExist(username, password);
        User user = check.password(username, password);

        HttpSession session = req.getSession();
        session.setAttribute("authenticatedUser", user);
        resp.sendRedirect(req.getContextPath() + "payment.jsp");
    }
}