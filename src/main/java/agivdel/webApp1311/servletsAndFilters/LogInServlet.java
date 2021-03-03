package agivdel.webApp1311.servletsAndFilters;

import agivdel.webApp1311.service.ServletService;
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
    //при успешной аутентификации имя сохраняется в куки и в сессии

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.getServletContext().getRequestDispatcher(addressIfError).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        ServletService servletService = new ServletService(this, req, resp, addressIfError, addressIfSuccess);
        servletService.valid(username, password);
        servletService.isExists(username, password);
        User user = servletService.authentication(username, password);
        servletService.saveUserInSessionAndGo(username, user.getId());
    }
}