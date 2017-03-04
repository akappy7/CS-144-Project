var xmlHttp = new XMLHttpRequest();

//base Control Object
function AutoSuggestControl(oTextbox, oProvider) {
    this.current = -1;
    this.layer = null;
    this.provider = oProvider;
    this.textbox = oTextbox;
    this.init();
}

//--------------------Request for proxy suggest----------------
function SuggestionCreater(){
}
SuggestionCreater.prototype.sendAjaxRequest = function (oAutoSuggestControl) {
    var textboxInput = oAutoSuggestControl.textbox.value;
    if (textboxInput.length > 0){
        var request = "http://localhost:1448/eBay/suggest?query=" +encodeURI(textboxInput);
        xmlHttp.open("GET", request);
        xmlHttp.onreadystatechange = function(){if(xmlHttp.readyState == 4){createSuggestions(oAutoSuggestControl)}};
        xmlHttp.send(null);
    }//show suggestion
    else{
        var emptyList = [];
        this.cur = -1;
        oAutoSuggestControl.autosuggest(emptyList);
    }
};

function createSuggestions(oAutoSuggestControl) {
      var elements = xmlHttp.responseXML.getElementsByTagName('CompleteSuggestion');

      var suggestionList = [];
      for (var i=0; i < elements.length; i++) {
          var suggestion = elements[i].childNodes[0].getAttribute("data");
          suggestionList.push(suggestion);
      }
      oAutoSuggestControl.autosuggest(suggestionList);
};
//-------------------------------------------------------------

//to show or not
AutoSuggestControl.prototype.autosuggest = function (suggestionList) {
    if (suggestionList.length > 0) {
        this.showSuggestions(suggestionList);
    } else {
        this.hideSuggestions();
    }
};

AutoSuggestControl.prototype.hideSuggestions = function () {
    this.layer.style.visibility = "hidden";
};

AutoSuggestControl.prototype.showSuggestions = function (suggestionList ) {
    //clear everything
    this.layer.innerHTML = "";

    //fill up suggestions
    for (var i=0; i < suggestionList.length; i++) {
        var newDiv = document.createElement("div");
        newDiv.appendChild(document.createTextNode(suggestionList[i]));
        this.layer.appendChild(newDiv);
    }
    //position
    var positions = this.getPositions();
    this.layer.style.top = (positions[0] +this.textbox.offsetHeight) + "px";
    this.layer.style.left = positions[1] + "px";
    this.layer.style.visibility = "visible";
};

AutoSuggestControl.prototype.getPositions = function(){
   var box = this.textbox;
   var leftP = 0;
   var topP =0;

   while(box.tagName != "BODY") {
      leftP += box.offsetLeft;
      topP += box.offsetTop;
      box = box.offsetParent;
   }
   return [topP, leftP];
}

AutoSuggestControl.prototype.highlightSuggestion = function (oSuggestionNode) {

    for (var i=0; i < this.layer.childNodes.length; i++) {
        var oNode = this.layer.childNodes[i];
        if (oNode == oSuggestionNode) {
            oNode.className = "current"
        } else if (oNode.className == "current") {
            oNode.className = "";
        }
    }
};
AutoSuggestControl.prototype.createDropDown = function () {

    this.layer = document.createElement("div");
    this.layer.className = "suggestions";
    this.layer.style.visibility = "hidden";
    this.layer.style.width = this.textbox.offsetWidth;
    document.body.appendChild(this.layer);

    var newThis = this;

    //assign event handlers
    this.layer.onmousedown = this.layer.onmouseup = this.layer.onmouseover = function (oEvent) {
        oEvent = oEvent || window.event;
        var target = oEvent.target || oEvent.srcElement;

        if (oEvent.type == "mousedown") {
            newThis.textbox.value = target.firstChild.nodeValue;
            newThis.hideSuggestions();
        } else if (oEvent.type == "mouseover") {
            newThis.highlightSuggestion(target);
        } else {
            newThis.textbox.focus();
        }
    };
};
//------------------keyboard input----------------
AutoSuggestControl.prototype.handleKeyDown = function (oEvent) {
    switch(oEvent.keyCode) {
      //enter
      case 13:
          this.hideSuggestions();
          break;
        //up
        case 38:
            this.previousSuggestion();
            break;
        //down
        case 40:
            this.nextSuggestion();
            break;
    }
};

AutoSuggestControl.prototype.handleKeyUp = function (oEvent) {
     var keyCode = oEvent.keyCode;
     //need to refresh suggestions
    if (keyCode == 8 || keyCode == 46) {
          this.provider.sendAjaxRequest(this);
    }
    //space, numbers and alpha, and special chars
    else if (keyCode == 32 || (keyCode > 45 && keyCode < 112) || (keyCode > 123)) {
      this.provider.sendAjaxRequest(this);
    }
};
//next suggestions
AutoSuggestControl.prototype.nextSuggestion = function () {
    var elements = this.layer.childNodes;
    //cannot go down on last
    if (this.current < elements.length-1 && elements.length > 0 ) {
        this.current += 1;
        var oNode = elements[this.current];
        this.highlightSuggestion(oNode);
        this.textbox.value = oNode.firstChild.nodeValue;
    }
};
AutoSuggestControl.prototype.previousSuggestion = function () {
    var elements = this.layer.childNodes;
    //cannot go up on first
    if (this.current > 0 && elements.length > 0 ) {
        this.current -= 1;
        var oNode = elements[this.current];
        this.highlightSuggestion(oNode);
        this.textbox.value = oNode.firstChild.nodeValue;
    }
};
//create event handlers
AutoSuggestControl.prototype.init = function () {
    //event up
    var newThis = this;
    this.textbox.onkeyup = function (oEvent) {
        newThis.handleKeyUp(oEvent);
    }
    //event down
    this.textbox.onkeydown = function (oEvent) {
        newThis.handleKeyDown(oEvent);
    }
    //event lose focus of the search box->clear suggestions
    this.textbox.onblur = function () {
        newThis.hideSuggestions();
    }
    this.createDropDown();
};
