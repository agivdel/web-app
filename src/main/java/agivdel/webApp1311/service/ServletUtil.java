package agivdel.webApp1311.service;

import agivdel.webApp1311.entities.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * The class contains methods for validating the user data (name, password).
 */

public class ServletUtil {
    private final HttpServlet servlet;
    private final HttpServletRequest req;
    private final HttpServletResponse resp;
    private final String addressIfError;
    private final String addressIfSuccess;

    public ServletUtil(HttpServlet servlet,
                       HttpServletRequest req,
                       HttpServletResponse resp,
                       String addressIfError,
                       String addressIfSuccess) {
        this.servlet = servlet;
        this.req = req;
        this.resp = resp;
        this.addressIfError = addressIfError;
        this.addressIfSuccess = addressIfSuccess;
    }

    public void valid(String username, String password) throws ServletException, IOException {
        if (isNotValidEnteredData(username, password)) {
            repeat(username, password, "enter not empty username and password");
        }
    }

    public void isNotExists(String username, String password) throws ServletException, IOException {
        try {
            if (new Service().isUserExists(username)) {
                repeat(username, password, "this name is already registered");
            }
        } catch (Exception e) {
            repeat(username, password, e.getMessage());
        }
    }

    public void isExists(String username, String password) throws ServletException, IOException {
        try {
            if (!new Service().isUserExists(username)) {
                repeat(username, password, "this username was not found");
            }
        } catch (Exception e) {
            repeat(username, password, e.getMessage());
        }
    }

    public User authentication(String username, String password) throws ServletException, IOException {
        Service service = new Service();
        User user = null;
        try {
            if (!service.authentication(username, password)) {
                repeat(username, password, "invalid username-password pair");
            }
            user = service.findUser(username);
        } catch (Exception e) {
            repeat(username, password, e.getMessage());
        }
        return user;
    }

    public User signUp(String username, String password) throws ServletException, IOException {
        Service service = new Service();
        User user = null;
        try {
            if (!new Service().signUp(username, password)) {
                repeat(username, password, "registration error, please try again");
            }
            user = service.findUser(username);
        } catch (Exception e) {
            repeat(username, password, e.getMessage());
        }
        return user;
    }

    private static boolean isNotValidEnteredData(String username, String password) {
        return username == null
                || password == null
                || username.length() == 0
                || password.length() == 0;
    }

    private void repeat(String username, String password, String errorMessage) throws ServletException, IOException {
        req.setAttribute("errorMessage", errorMessage);
        req.setAttribute("user", new User(username, password));
        servlet.getServletContext().getRequestDispatcher(addressIfError).forward(req, resp);
    }

    public void makeSessionAndGo(String username, User user) throws ServletException, IOException {
        HttpSession session = req.getSession();
        session.setAttribute("authenticatedUsername", username);
        session.setAttribute("authenticatedUser", user);
        long balance = 0;
        try {
            balance = new Service().findBalance(user.getId()).getValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        req.setAttribute("balance", balance);
        req.setAttribute("username", username);
        servlet.getServletContext().getRequestDispatcher(addressIfSuccess).forward(req, resp);
    }

}