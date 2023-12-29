package ru.itis.tasktracker.filters;

import ru.itis.tasktracker.services.MembershipService;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;

@WebFilter("/create-task")
public class CreateTaskFilter extends ProjectAdministrationFilter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ID = "project_id";
        ServletContext servletContext = filterConfig.getServletContext();
        membershipService = (MembershipService) servletContext.getAttribute("membershipService");
    }

}
