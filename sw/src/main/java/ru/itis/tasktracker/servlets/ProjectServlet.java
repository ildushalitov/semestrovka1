package ru.itis.tasktracker.servlets;

import ru.itis.tasktracker.dto.UserDto;
import ru.itis.tasktracker.services.AuthorizationService;
import ru.itis.tasktracker.services.MembershipService;
import ru.itis.tasktracker.services.ProjectService;
import ru.itis.tasktracker.services.TaskService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/project")
public class ProjectServlet extends HttpServlet {
    private ProjectService projectService;
    private TaskService taskService;
    private MembershipService membershipService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext servletContext = config.getServletContext();
        projectService = (ProjectService) servletContext.getAttribute("projectService");
        taskService = (TaskService) servletContext.getAttribute("taskService");
        membershipService = (MembershipService) servletContext.getAttribute("membershipService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String projectId = req.getParameter("id");
        req.setAttribute("tasks", taskService.getTasks(Integer.parseInt(projectId)));
        req.setAttribute("project", projectService.getProject(Integer.parseInt(projectId)));
        Object user = req.getSession().getAttribute("user");
        boolean isAdmin = membershipService.isAdmin(projectId, ((UserDto) user).getId().toString());
        req.setAttribute("isAdmin", isAdmin);
        req.getRequestDispatcher("project.ftl").forward(req, resp);
    }
}
