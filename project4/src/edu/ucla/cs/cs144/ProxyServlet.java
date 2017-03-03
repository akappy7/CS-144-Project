package edu.ucla.cs.cs144;

import java.io.IOException;
import java.net.HttpURLConnection;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProxyServlet extends HttpServlet implements Servlet {

    public ProxyServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String query = request.getParameter("query");
        String url = "http://google.com/complete/search?output=toolbar&q=" + query;

        URL url = new URL(url);
        HttpURLConnection c1 = (HttpURLConnection) url.openConnection();

        // optional default is GET
		    c1.setRequestMethod("GET");
		    //add request header
		    c1.setRequestProperty("User-Agent", USER_AGENT);
        if (c1.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader br1 = new BufferedReader(new InputStreamReader(c1.getInputStream()));
                String line;
                String total;

                while ((line = br1.readLine()) != null) {
                    total += line;
                }

                response.setContentType("text/xml");
                response.getWriter().write(total);
                //close BufferedReader and c1
                br1.close();
                c1.disconnect();
        }
    }
}
