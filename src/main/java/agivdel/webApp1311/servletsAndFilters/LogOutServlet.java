package agivdel.webApp1311.servletsAndFilters;

import agivdel.webApp1311.utils.CookieUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = {"/logout", "/logout-servlet"})
public class LogOutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        CookieUtil.deleteUserCookie(resp);
        req.getSession().invalidate();
        resp.sendRedirect(req.getContextPath() + "/index.jsp");//перенаправление на начальную страницу
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        this.doGet(req, resp);
    }
}