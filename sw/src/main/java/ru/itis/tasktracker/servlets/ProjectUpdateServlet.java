package ru.itis.tasktracker.servlets;

import ru.itis.tasktracker.dto.ProjectDto;
import ru.itis.tasktracker.dto.ProjectForm;
import ru.itis.tasktracker.dto.UserDto;
import ru.itis.tasktracker.exceptions.TaskTrackerException;
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

@WebServlet("/update-project")

public class ProjectUpdateServlet extends HttpServlet {
    private ProjectService projectService;

    public void init(ServletConfig config) throws ServletException {
        ServletContext servletContext = config.getServletContext();
        projectService = (ProjectService) servletContext.getAttribute("projectService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String projectId = req.getParameter("id");
        req.setAttribute("project", projectService.getProject(Integer.parseInt(projectId)));
        req.getRequestDispatcher("update-project.ftl").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProjectDto form = projectService.getProject(Integer.parseInt(req.getParameter("id")));
        ProjectDto project = form;
        form.setName(req.getParameter("name"));
        form.setDescription(req.getParameter("description"));
        try {
            form = projectService.update(form);
        } catch (TaskTrackerException e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.setAttribute("project", project);
            req.getRequestDispatcher("update-project.ftl?id=" + form.getId()).forward(req, resp);
            return;
        }
        resp.sendRedirect("project?id=" + form.getId());
    }
}
