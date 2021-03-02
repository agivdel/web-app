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
        //содержимое метода перенести в отдельный сервис,
        //передавать туда лоигн-пароль, принимать юзера или исключение
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
        if (isNotValid(newUser)) {
            //если данные введены некорректно
            //пробрасываем вновь на страницу аутентификации
            //показываем пользователю ошибку ввода
            repeatLogIn(req, resp, newUser, "enter not empty username and password");
        }
        //если данные введены корректно
        //ищем пользователя в БД
        User user = null;
        try {
            //если пользователя с таким именем нет в БД
            //пробрасываем вновь на страницу аутентификации
            //показываем ошибку ввода
            if (!new Service().isUserExists(username)) {
                repeatLogIn(req, resp, newUser, "this username was not found");
            }
        } catch (Exception e) {
            //при ошибке доступа к БД показываем сообщение об этом
            repeatLogIn(req, resp, newUser, e.getMessage());
        }
        //если до сих пор ошибки не возникло,
        //данные введны корректно и пользователь есть в БД
        //проверяем пароль - при совпадении создаем новый объект User
        //если пароль не совпал,
        //пробрасываем вновь на страницу аутентификации
        try {
            if (!new Service().authentication(username, password)) {
                repeatLogIn(req, resp, newUser, "invalid username-password pair");
            }
            user = new Service().findUser(username);
        } catch (Exception e) {
            repeatLogIn(req, resp, newUser, e.getMessage());
        }
        //если дошли сюда,
        //аутентификация прошла успешно,
        return user;
    }

    private boolean isNotValid(User user) {
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