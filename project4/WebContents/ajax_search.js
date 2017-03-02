var xmlHttp = new XMLHttpRequest(); // works only for Firefox, Safari, ...

 function sendAjaxRequest()
 {
   var request = "http://localhost:1448/eBay/search";
   var param = document.getElementById("Search").value;
   console.log(param);
   request = request + "?param=" + param;
   xmlHttp.open("GET", request);
   xmlHttp.onreadystatechange = showResult;
   xmlHttp.send(null);
   console.log(xmlHttp);
 }
 function showResult() {
   if (xmlHttp.readyState == 4) {
     response = xmlHttp.responseText;
     document.getElementById("response").innerHTML = response;
  }
}
