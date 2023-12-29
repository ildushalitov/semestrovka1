package ru.itis.tasktracker.servlets;

import ru.itis.tasktracker.services.ProjectService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/delete-project")
public class DeleteProjectServlet extends HttpServlet {
    ProjectService projectService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext servletContext = config.getServletContext();
        projectService = (ProjectService) servletContext.getAttribute("projectService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int projectId = Integer.parseInt(req.getParameter("projectId"));
        projectService.delete(projectId);
        resp.sendRedirect("my-projects");
    }
}
