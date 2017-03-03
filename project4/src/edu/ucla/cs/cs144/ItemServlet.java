package edu.ucla.cs.cs144;


import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class ItemServlet extends HttpServlet implements Servlet {

    public ItemServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String itemID = request.getParameter("itemId");
        String itemXML = AuctionSearch.getXMLDataForItemId(itemID);

        Item item;
        if (itemXML == null)
            item = new Item();
        else
            item = new Item(itemXML);

        request.setAttribute("item", item);
        request.getRequestDispatcher("/item.jsp").forward(request, response);
    }
}
