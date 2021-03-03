package agivdel.webApp1311.servletsAndFilters;

import agivdel.webApp1311.entities.User;
import agivdel.webApp1311.service.Service;
import agivdel.webApp1311.service.ServletUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(filterName = "cookieFilter", urlPatterns = {"/*"})
public class CookieFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession();

        String authenticatedUsername = (String) session.getAttribute("authenticatedUsername");

        if (authenticatedUsername != null) {
            session.setAttribute("checked_cookie", "checked");
            filterChain.doFilter(request, response);
            return;
        }
        System.out.println("authenticatedUsername == null");
        String checked = (String) session.getAttribute("checked_cookie");
        System.out.println("checked: " + checked);
        if (checked == null) {
            String username = ServletUtil.getUsernameInCookie(req);
            try {
                User user = new Service().findUser(username);
                session.setAttribute("authenticatedUsername", username);
            } catch (Exception e) {
                e.printStackTrace();
            }
            session.setAttribute("checked_cookie", "checked");
        }
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}