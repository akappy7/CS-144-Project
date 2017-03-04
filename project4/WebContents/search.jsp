<%@ page import="edu.ucla.cs.cs144.SearchResult" %>
<html>
  <script src="http://code.jquery.com/jquery-latest.js">  </script>
    <link rel="stylesheet" type="text/css" href="css/suggest.css">
  <link rel="stylesheet" type="text/css" href="css/main.css">

  <head>
    <title><%= request.getAttribute("title") %></title>
  </head>
  <body>
    <br>
      <h1> Ebay Class Website </h1>
      <br>
      <div class = "align">
        <h2> Begin Search </h2>
      <br>

        <form class="form-inline" action="search" method="GET">
                <div class="form-group">
                    <input type="text" name="query" class="center" id="SearchBox" value="" autocomplete="off" placeholder="Enter search..">
                    <button class="btn" type="submit">Search</button>
                </div>
                <input type="hidden" id="resultSkip" name="resultSkip" value="0">
            </form>
      <div class="result" id=result>
        <%
            int flag =  (Integer) request.getAttribute("flag");
            int toSkip = (Integer) request.getAttribute("toSkip");
            String query = (String) request.getAttribute("query");
            SearchResult[] results = (SearchResult[]) request.getAttribute("itemList");
            if(results.length == 0 && flag == 1){
              %>
              <div> No Search Results. </div>
              <%
            }
        %>
         <div class="list-group">
          <% for (SearchResult result : results) { %>
                <a href="/eBay/item?itemId=<%= result.getItemId() %>" class="list-group-item"><%= result.getName() %></a>
                <br><br>
              <% } %>
            <br>
              <br>
                <br>
          <%
          if (results.length == 20){
            %>
                <div class= "bottom">
                  <a href="/eBay/search?query=<%= query %>&resultSkip=<%= toSkip + 20 %>"> Next</a>
                </div>
            <%
          }
          %>
      </div>
      </div>
      <script type="text/javascript" src="js/ajax_Suggest.js"></script>
      <script type="text/javascript">
        window.onload = function () {
            var oTextbox = new AutoSuggestControl(document.getElementById("SearchBox"), new SuggestionCreater());
        }
    </script>
    </body>
</html>
