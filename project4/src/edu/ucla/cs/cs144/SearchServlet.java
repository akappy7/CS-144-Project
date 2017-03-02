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
      int toSkip = 0;
      if(request.getParameter("query") != null ){
        item = request.getParameter("query");
        toSkip =  Integer.parseInt(request.getParameter("skip"));
        SearchResult[] result = AuctionSearch.basicSearch(item, toSkip, 20);

        if (result.length == 0){
          String pageTitle = "Search Main";
          String notif = "No results found for query: " + item;
          request.setAttribute("itemList", notif);
          request.getRequestDispatcher("/noResult.jsp").forward(request, response);
        }
        else{
          String pageTitle = "Search Main";
          request.setAttribute("itemList", result);
          request.getRequestDispatcher("/searchResult.jsp").forward(request, response);
        }
      }
      else{
          String pageTitle = "Search Main";
          request.setAttribute("title", pageTitle);
          request.getRequestDispatcher("/search.jsp").forward(request, response);
        }
    }
}
