package ru.itis.tasktracker.servlets;

import ru.itis.tasktracker.dto.UserDto;
import ru.itis.tasktracker.model.User;
import ru.itis.tasktracker.services.MembershipService;
import ru.itis.tasktracker.services.ProjectService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/members")

public class MembersServlet extends HttpServlet {
    MembershipService membershipService;


    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext servletContext = config.getServletContext();
        membershipService = (MembershipService) servletContext.getAttribute("membershipService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String projectId = req.getParameter("project_id");
        req.setAttribute( "members" ,membershipService.getMembersByProject(projectId));
        Object user = req.getSession().getAttribute("user");
        req.setAttribute("is_admin", membershipService.isAdmin(projectId, ((UserDto)user).getId().toString()));
        req.setAttribute("project_id", projectId);
        req.getRequestDispatcher("members.ftl").forward(req, resp);
    }
}
