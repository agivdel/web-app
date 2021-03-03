package agivdel.webApp1311.servletsAndFilters;

import agivdel.webApp1311.service.ServletUtil;
import agivdel.webApp1311.entities.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = {"/signup", "/signup-servlet"})
public class SignUpServlet extends HttpServlet {
    private final String addressIfError = "/index.jsp";
    private final String addressIfSuccess = "/payment.jsp";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.getServletContext().getRequestDispatcher(addressIfError).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        ServletUtil servletUtil = new ServletUtil(this, req, resp, addressIfError, addressIfSuccess);
        servletUtil.valid(username, password);
        servletUtil.isNotExists(username, password);
        User user = servletUtil.signUp(username, password);
        servletUtil.saveUserInSessionAndGo(username, user);
    }
}