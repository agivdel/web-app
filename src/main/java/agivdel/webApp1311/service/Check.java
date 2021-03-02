package agivdel.webApp1311.service;

import agivdel.webApp1311.entities.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * The class contains methods for validating user data (name, password).
 */

public class Check {
    private final HttpServlet servlet;
    private final HttpServletRequest req;
    private final HttpServletResponse resp;
    private final String forwardAddress;

    public Check(HttpServlet servlet, HttpServletRequest req, HttpServletResponse resp, String forwardAddress) {
        this.servlet = servlet;
        this.req = req;
        this.resp = resp;
        this.forwardAddress = forwardAddress;
    }

    public void userValid(String username, String password) throws ServletException, IOException {
        if (isNotValidEnteredData(username, password)) {
            repeatLogIn(username, password, "enter not empty username and password");
        }
    }

    public void userExist(String username, String password) throws ServletException, IOException {
        try {
            if (new Service().isUserExists(username)) {
                repeatLogIn(username, password, "this name is already registered");
            }
        } catch (Exception e) {
            repeatLogIn(username, password, e.getMessage());
        }
    }

    public User userAuthentication(String username, String password) throws ServletException, IOException {
        try {
            if (!new Service().authentication(username, password)) {
                repeatLogIn(username, password, "invalid username-password pair");
            }
        } catch (Exception e) {
            repeatLogIn(username, password, e.getMessage());
        }
        return new User(username, password);
    }

    public User userSignUp(String username, String password) throws ServletException, IOException {
        try {
            if (!new Service().signUp(username, password)) {
                repeatLogIn(username, password, "registration error, please try again");
            }
        } catch (Exception e) {
            repeatLogIn(username, password, e.getMessage());
        }
        return new User(username, password);
    }

    private static boolean isNotValidEnteredData(String username, String password) {
        return username == null
                || password == null
                || username.length() == 0
                || password.length() == 0;
    }

    private void repeatLogIn(String username, String password, String errorMessage) throws ServletException, IOException {
        req.setAttribute("errorMassage", errorMessage);
        req.setAttribute("user", new User(username, password));
        servlet.getServletContext().getRequestDispatcher(forwardAddress).forward(req, resp);
    }
}