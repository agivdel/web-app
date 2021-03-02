package agivdel.webApp1311.servlets;

import agivdel.webApp1311.service.Check;
import agivdel.webApp1311.entities.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

//@WebServlet(urlPatterns = {"/signup", "/signup-servlet"})
public class SignUpServlet extends HttpServlet {
    private final String forwardAddress = "/views/sign-up.jsp";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.getServletContext().getRequestDispatcher(forwardAddress).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        Check check = new Check(this, req, resp, forwardAddress);
        check.userValid(username, password);
        check.userExist(username, password);
        User user = check.userSignUp(username, password);

        HttpSession session = req.getSession();
        session.setAttribute("authenticatedUser", user);
        resp.sendRedirect(req.getContextPath() + "/payment.jsp");
    }
}