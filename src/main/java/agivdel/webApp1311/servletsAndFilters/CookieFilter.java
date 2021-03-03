package agivdel.webApp1311.servletsAndFilters;

import agivdel.webApp1311.utils.CookieUtil;
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

        String checked = (String) session.getAttribute("checked_cookie");
        if (checked == null) {
            String username = CookieUtil.getUsernameInCookie(req);
            session.setAttribute("authenticatedUsername", username);
            session.setAttribute("checked_cookie", "checked");
        }
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}