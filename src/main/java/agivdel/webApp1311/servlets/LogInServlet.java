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

        User user = userCheck(req, resp, username, password);

        //сохраняем информацию пользователя в сессию
        HttpSession session = req.getSession();
        session.setAttribute("authenticatedUser", user);
        resp.sendRedirect(req.getContextPath() + "payment.jsp");
    }

    private User userCheck(HttpServletRequest req, HttpServletResponse resp, String username, String password) throws ServletException, IOException {
        User newUser = new User(username, password);
        Service service = new Service();
        if (isNotValidEnteredData(newUser)) {
            repeatLogIn(req, resp, newUser, "enter not empty username and password");
        }
        try {
            if (!service.isUserExists(username)) {
                repeatLogIn(req, resp, newUser, "this username was not found");
            }
        } catch (Exception e) {
            repeatLogIn(req, resp, newUser, e.getMessage());
        }
        try {
            if (!service.authentication(username, password)) {
                repeatLogIn(req, resp, newUser, "invalid username-password pair");
            }
            newUser = service.findUser(username);
        } catch (Exception e) {
            repeatLogIn(req, resp, newUser, e.getMessage());
        }
        return newUser;
    }

    private boolean isNotValidEnteredData(User user) {
        return user.getUsername() == null
                || user.getPassword() == null
                || user.getUsername().length() == 0
                || user.getPassword().length() == 0;
    }

    private void repeatLogIn(HttpServletRequest req, HttpServletResponse resp, User newUser, String errorMessage) throws ServletException, IOException {
        req.setAttribute("errorMassage", errorMessage);
        req.setAttribute("user", newUser);
        this.getServletContext().getRequestDispatcher("views/login.jsp").forward(req, resp);
    }
}