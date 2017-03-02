package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SearchServlet extends HttpServlet implements Servlet {

    public SearchServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
      String item=null;
      if(request.getParameter("search") != null ){
        item = "User requested: " + request.getParameter("search");
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(item);
      }
      else{
          String pageTitle = "Search Main";
          request.setAttribute("title", pageTitle);
          request.getRequestDispatcher("/search.jsp").forward(request, response);
        }
    }
}
