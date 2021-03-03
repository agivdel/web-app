package agivdel.webApp1311.servlets;

import agivdel.webApp1311.service.Service;
import agivdel.webApp1311.entities.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;

@WebServlet(urlPatterns = {"/payment", "/payment-servlet"})
public class PaymentServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User authenticatedUser = (User) session.getAttribute("authenticatedUser");//TODO можно убрать, хватит и аттрибута username
        String authenticatedUsername = (String) session.getAttribute("authenticatedUsername");

        if (authenticatedUser == null) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        long oldBalance = 0;
        long newBalance = 0;
        try {
            oldBalance = new Service().findBalance(authenticatedUser.getId()).getValue();
            newBalance = new Service().pay(authenticatedUser.getUsername());
            if (oldBalance == newBalance) {
                throw new Exception("there are not enough funds on your account");
            }
        } catch (Exception e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.setAttribute("balance", oldBalance);
            req.setAttribute("username", authenticatedUsername);
            this.getServletContext().getRequestDispatcher("/payment.jsp").forward(req, resp);
        }
        req.setAttribute("balance", newBalance);
        req.setAttribute("username", authenticatedUsername);
        req.setAttribute("user", authenticatedUser);
        this.getServletContext().getRequestDispatcher("/payment.jsp").forward(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
}