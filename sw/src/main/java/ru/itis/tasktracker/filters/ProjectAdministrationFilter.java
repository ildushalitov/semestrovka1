package ru.itis.tasktracker.filters;

import ru.itis.tasktracker.services.MembershipService;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter()

public class ProjectAdministrationFilter extends ProjectFilter {
    protected String ID;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ID = "id";
        ServletContext servletContext = filterConfig.getServletContext();
        membershipService = (MembershipService) servletContext.getAttribute("membershipService");
    }
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String projectId = request.getParameter(ID);

        String userId = getCurrentUserId(httpRequest);

        boolean isAdmin = membershipService.isAdmin(projectId, userId);

        if (!isAdmin) {
            httpResponse.sendRedirect("access-denied");
        } else {
            chain.doFilter(request, response);
        }
    }
}
