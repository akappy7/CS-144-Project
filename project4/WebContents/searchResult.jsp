<%@ page import="edu.ucla.cs.cs144.SearchResult" %>
      <%
          SearchResult[] results = (SearchResult[]) request.getAttribute("itemList");
      %>
       <div class="list-group">
        <% for (SearchResult result : results) { %>
              <a href="/eBay/item?itemId=<%= result.getItemId() %>" class="list-group-item"><%= result.getName() %></a>
              <br><br>
            <% } %>
        <%
        if (results.length == 20){
          %>
              <div class= "bottom">
              <input type="button" id="next" value="next results >>" onclick="increment()">
              </div>
          <%
        }
        %>
