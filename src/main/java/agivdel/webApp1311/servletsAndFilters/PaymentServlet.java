package agivdel.webApp1311.servletsAndFilters;

import agivdel.webApp1311.service.Service;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet(urlPatterns = {"/payment", "/payment-servlet"})
public class PaymentServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        String authenticatedUsername = (String) session.getAttribute("authenticatedUsername");

        if (authenticatedUsername == null) {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
            return;
        }

        long oldBalance = 0;
        long newBalance = 0;
        try {
            int userId = new Service().findUser(authenticatedUsername).getId();
            oldBalance = new Service().findBalance(userId).getValue();
            newBalance = new Service().pay(authenticatedUsername);
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
        this.getServletContext().getRequestDispatcher("/payment.jsp").forward(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
}