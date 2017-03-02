<html>
  <script src="http://code.jquery.com/jquery-latest.js">  </script>
  <script src="ajax_search.js" type="text/javascript"></script>
  <head>
    <title><%= request.getAttribute("title") %></title>
  </head>
  <body>
      Ready for searching!
      <br>
        <br>
      <form>
        Search: <input type="text" id="Search" value="Suggestion"><br>
                    <input type="button" id="Submit" value="Submit">
      </form>
      <div id="response" >
      </div>
    </body>
</html>
