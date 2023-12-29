package ru.itis.tasktracker.servlets;

import ru.itis.tasktracker.dto.ProjectDto;
import ru.itis.tasktracker.dto.ProjectForm;
import ru.itis.tasktracker.dto.SignUpForm;
import ru.itis.tasktracker.dto.UserDto;
import ru.itis.tasktracker.exceptions.TaskTrackerException;
import ru.itis.tasktracker.model.User;
import ru.itis.tasktracker.services.AuthorizationService;
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

@WebServlet("/create-project")

public class CreateProjectServlet extends HttpServlet {
    private ProjectService projectService;

    public void init(ServletConfig config) throws ServletException {
        ServletContext servletContext = config.getServletContext();
        projectService = (ProjectService) servletContext.getAttribute("projectService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("create-project.ftl").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProjectForm form = ProjectForm.builder()
                .name(req.getParameter("name"))
                .description(req.getParameter("description"))
                .build();
        ProjectDto project;
        HttpSession session = req.getSession(true);
        try {
            project = projectService.save(form, (UserDto) session.getAttribute("user"));
        } catch (TaskTrackerException e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.getRequestDispatcher("create-project.ftl").forward(req, resp);
            return;
        }
        resp.sendRedirect("project?id=" + project.getId());
    }
}
