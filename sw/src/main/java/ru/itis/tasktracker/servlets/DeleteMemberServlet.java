package ru.itis.tasktracker.servlets;

import ru.itis.tasktracker.dto.UserDto;
import ru.itis.tasktracker.services.MembershipService;
import ru.itis.tasktracker.services.ProjectService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class DeleteMemberServlet extends HttpServlet {

    MembershipService membershipService;


    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext servletContext = config.getServletContext();
        membershipService = (MembershipService) servletContext.getAttribute("membershipService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String projectId = req.getParameter("project_id");
        String userId = req.getParameter("user_id");
        HttpSession session = req.getSession(true);
        UserDto user = ((UserDto) session.getAttribute("user"));
        if (user.getId().toString().equals(userId)) {
            req.setAttribute("errorMessage", "It's you :)");
        }
        membershipService.deleteMember(userId, projectId);
        resp.sendRedirect("members");
    }
}
