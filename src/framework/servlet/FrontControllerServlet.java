package framework.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FrontControllerServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String url = request.getRequestURI();
        String contextPath = request.getContextPath();

        String mapping = url.substring(contextPath.length());

        try (PrintWriter out = response.getWriter()) {

            out.println("<html>");
            out.println("<body>");
            out.println("<h2>Front Controller</h2>");
            out.println("<p>URL complète : " + url + "</p>");
            out.println("<p>Mapping : " + mapping + "</p>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}