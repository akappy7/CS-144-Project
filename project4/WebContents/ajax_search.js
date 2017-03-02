var xmlHttp = new XMLHttpRequest(); // works only for Firefox, Safari, ...
var counter = 0;

 function sendAjaxRequest()
 {
   var request = "http://localhost:1448/eBay/search";
   var input = document.getElementById("Search").value;
   request = request + "?query=" + input + "&skip=" + counter;
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
