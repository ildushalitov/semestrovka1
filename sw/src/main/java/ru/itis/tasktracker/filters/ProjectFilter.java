package ru.itis.tasktracker.filters;

import ru.itis.tasktracker.dto.UserDto;
import ru.itis.tasktracker.services.MembershipService;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/project")
public class ProjectFilter implements Filter {
    protected MembershipService membershipService;

    protected Boolean condition;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ServletContext servletContext = filterConfig.getServletContext();
        membershipService = (MembershipService) servletContext.getAttribute("membershipService");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String projectId = request.getParameter("id");

        String userId = getCurrentUserId(httpRequest);

        boolean isMember = membershipService.isMember(projectId, userId);
        boolean isAdmin = membershipService.isAdmin(projectId, userId);

        if (!isMember && !isAdmin) {
            httpResponse.sendRedirect("access-denied");
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
    }


    protected String getCurrentUserId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        UserDto user = (UserDto) session.getAttribute("user");
        return user.getId().toString();
    }
}
