$(document).ready(function() {                       
           $('#Submit').click(function(event) { 
                var search=$('#Search').val();
            $.get('SearchServlet',{search:search},function(responseText) {
                 $('#response').text(responseText);        
                });
           });
});
