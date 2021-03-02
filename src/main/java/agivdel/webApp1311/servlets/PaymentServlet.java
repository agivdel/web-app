package agivdel.webApp1311.servlets;

import agivdel.webApp1311.service.Service;
import agivdel.webApp1311.entities.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

//@WebServlet(urlPatterns = {"/payment", "/payment-servlet"})
public class PaymentServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User authenticatedUser = (User) session.getAttribute("authenticatedUser");

        if (authenticatedUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        long balance = 0L;
        try {
            balance = new Service().pay(authenticatedUser.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }

        req.setAttribute("balance", balance);
        req.setAttribute("user", authenticatedUser);
        this.getServletContext().getRequestDispatcher("/views/payment.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}