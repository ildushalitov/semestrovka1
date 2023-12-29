package ru.itis.tasktracker.servlets;

import ru.itis.tasktracker.dto.MemberForm;
import ru.itis.tasktracker.exceptions.TaskTrackerException;
import ru.itis.tasktracker.services.MembershipService;


import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/add-member")

public class AddMemberServlet extends HttpServlet {
    MembershipService membershipService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext servletContext = config.getServletContext();
        membershipService = (MembershipService) servletContext.getAttribute("membershipService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String projectId = req.getParameter("project_id");
        req.setAttribute("project_id", projectId);
        req.getRequestDispatcher("add-member.ftl").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        MemberForm form = MemberForm.builder()
                .email(req.getParameter("email"))
                .isAdmin(req.getParameter("is_admin"))
                .projectId(req.getParameter("project_id"))
                .build();
        try {
            membershipService.addMember(form);
        } catch (TaskTrackerException e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.setAttribute("project_id", req.getParameter("project_id"));
            req.getRequestDispatcher("add-member.ftl?project_id=" + req.getParameter("project_id")).forward(req, resp);
            return;
        }
        resp.sendRedirect("members?project_id=" + req.getParameter("project_id"));
    }
}
