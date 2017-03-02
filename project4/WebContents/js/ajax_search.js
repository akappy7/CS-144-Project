var xmlHttp = new XMLHttpRequest(); // works only for Firefox, Safari, ...
var counter = 0;
var input = "";

  //next button, increase counter
  function increment()
  {
      counter+=20;
      sendAjaxRequest();
  }
  //new search, reset counter and reset query input
  function newSearchFunction()
  {
    input = document.getElementById("Search").value;
    console.log(input);
    counter = 0;
    sendAjaxRequest();
  }
 function sendAjaxRequest()
 {
   var request = "http://localhost:1448/eBay/search";
   console.log(input);
   request = request + "?query=" + input + "&skip=" + counter;
   console.log(request);
   xmlHttp.open("GET", request);
   xmlHttp.onreadystatechange = showResult;
   xmlHttp.send(null);
 }
 function showResult() {
   if (xmlHttp.readyState == 4) {
     response = xmlHttp.responseText;
     document.getElementById("result").innerHTML = response;
  }
}
