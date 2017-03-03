<%@ page import="edu.ucla.cs.cs144.Item" %>
<%@ page import="java.util.List" %>
<%@ page import="edu.ucla.cs.cs144.Bid" %><%--
  Created by IntelliJ IDEA.
  User: Nero
  Date: 3/2/17
  Time: 2:27 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<link rel="stylesheet" type="text/css" href="css/main.css">
<html>
<head>
    <title>Item</title>
    <%
        Item item = (Item) request.getAttribute("item");
        List<String> categories = item.getCategories();
        List<Bid> bids = item.getBids();
    %>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
    <script type="text/javascript"
            src="http://maps.google.com/maps/api/js?sensor=false">
    </script>
    <script type="text/javascript">
        function initialize() {
            <%
                if (!item.getLatitude().isEmpty()) {
            %>
            var latlng = new google.maps.LatLng(<%= Float.parseFloat(item.getLatitude())%>,<%= Float.parseFloat(item.getLongitude())%>);
            var myOptions = {
                zoom: 14, // default is 8
                center: latlng,
                mapTypeId: google.maps.MapTypeId.ROADMAP
            };
            var map = new google.maps.Map(document.getElementById("map_canvas"),
                myOptions);
            <%
                } else {
            %>
            var map = new google.maps.Map(document.getElementById("map_canvas"), {
                center: {lat: 0, lng: 0},
                zoom: 2
            });
            <%
                }
            %>
        }
    </script>
</head>
<body onload="initialize()">
    <div class="leftColumn">
        <h2> Item Info </h2>
        <table>
            <tr>
                <th>ID</th>
                <td><%= item.getItemID()%></td>
            </tr>
            <tr>
                <th>Name</th>
                <td><%= item.getName()%></td>
            </tr>
            <tr>
                <th>Categories</th>
                <td>
                    <ul>
                        <%
                            for (String category:categories) {
                        %>
                        <li><%=category%></li>
                        <%
                            }
                        %>
                    </ul>
                </td>
            </tr>
            <tr>
                <th>Currently</th>
                <td><%= item.getCurrently()%></td>
            </tr>
            <%
                if (!item.getBuyPrice().isEmpty()){
            %>
            <tr>
                <th>Buy Price</th>
                <td><%= item.getBuyPrice()%></td>
            </tr>
            <%
                }
            %>
            <tr>
                <th>First Bid</th>
                <td><%= item.getFirstBid()%></td>
            </tr>
            <tr>
                <th>Number of Bids</th>
                <td><%= item.getNumBids()%></td>
            </tr>
            <%
                if (bids.size() > 0) {
            %>
            <tr>
                <th>Bids</th>
                <td>
                    <table>
                        <tr>
                            <th>Bidder ID</th>
                            <th>Bidder Rating</th>
                            <th>Bidder Location</th>
                            <th>Bidder Country</th>
                            <th>Bidder Time</th>
                            <th>Bidder Amount</th>
                        </tr>
                        <%
                            for (Bid bid:bids) {
                        %>
                        <tr>
                            <td><%= bid.getUserID()%></td>
                            <td><%= bid.getRating()%></td>
                            <td><%= bid.getLocation()%></td>
                            <td><%= bid.getCountry()%></td>
                            <td><%= bid.getTime()%></td>
                            <td><%= bid.getAmount()%></td>
                        </tr>
                        <%
                            }
                        %>
                    </table>
                </td>
            </tr>
            <%
                }
            %>
            <tr>
                <th>Location</th>
                <td><%= item.getLocation()%></td>
            </tr>
            <tr>
                <th>Country</th>
                <td><%= item.getCountry()%></td>
            </tr>
            <tr>
                <th>Started</th>
                <td><%= item.getStarted()%></td>
            </tr>
            <tr>
                <th>Ends</th>
                <td><%= item.getEnds()%></td>
            </tr>
            <tr>
                <th>Seller ID</th>
                <td><%= item.getSellerID()%></td>
            </tr>
            <tr>
                <th>Seller Rating</th>
                <td><%= item.getSellerRating()%></td>
            </tr>
            <tr>
                <th>Description</th>
                <td><%= item.getDescription()%></td>
            </tr>
        </table>
    </div>
    <div id="map_canvas" class="rightColumn"></div>
</body>
</html>
