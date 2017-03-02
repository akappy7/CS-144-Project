<html>
  <script src="http://code.jquery.com/jquery-latest.js">  </script>
  <script src="js/ajax_search.js" type="text/javascript"></script>
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
      <form>
        Search: <input class="center" type="text" id="Search" value="">
                    <input type="button" id="Submit" value="Submit" onclick="newSearchFunction()">
      </form>
      <div id=result>
      </div>
      </div>
    </body>
</html>
