package edu.ucla.cs.cs144;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Nero on 3/2/17.
 */
public class Item {
    private List<String> categories = new LinkedList<>();
    private List<Bid> bids = new LinkedList<>();
    private String itemID;
    private String name;
    private String currently;
    private String buyPrice;
    private String firstBid;
    private String numBids;
    private String location;
    private String longitude;
    private String latitude;
    private String country;
    private String started;
    private String ends;
    private String sellerID;
    private String sellerRating;
    private String description;


    public Item() {}

    public Item(String xml) {

        DocumentBuilder builder = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setIgnoringElementContentWhitespace(true);
            builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new XMLParser.MyErrorHandler());
        }
        catch (FactoryConfigurationError e) {
            System.out.println("unable to get a document builder factory");
        }
        catch (ParserConfigurationException e) {
            System.out.println("parser was unable to be configured");
        }

        Document doc = null;
        try {
            doc = builder.parse(new InputSource(new ByteArrayInputStream(xml.getBytes("utf-8"))));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (SAXException e) {
            System.out.println("Parsing error on file " + xml);
            System.out.println("  (not supposed to happen with supplied XML files)");
            e.printStackTrace();
        }

        try {
            doc.getDocumentElement().normalize();
            Element item = doc.getDocumentElement();

            itemID = item.getAttribute("ItemID");
            name = XMLParser.getElementTextByTagNameNR(item, "Name");

            Element[] categoryElements = XMLParser.getElementsByTagNameNR(item, "Category");
            for (Element category: categoryElements) {
                categories.add(XMLParser.getElementText(category));
            }

            currently = XMLParser.getElementTextByTagNameNR(item, "Currently");
            buyPrice = XMLParser.getElementTextByTagNameNR(item, "Buy_Price");
            firstBid = XMLParser.getElementTextByTagNameNR(item, "First_Bid");
            numBids = XMLParser.getElementTextByTagNameNR(item, "Number_of_Bids");

            Element[] bidElements = XMLParser.getElementsByTagNameNR(XMLParser.getElementByTagNameNR(item, "Bids"), "Bid");
            for (Element bidElement: bidElements) {
                Bid bid = new Bid();
                Element bidder = XMLParser.getElementByTagNameNR(bidElement, "Bidder");
                bid.setUserID(bidder.getAttribute("UserID"));
                bid.setRating(bidder.getAttribute("Rating"));

                bid.setLocation(XMLParser.getElementTextByTagNameNR(bidder, "Location"));
                bid.setCountry(XMLParser.getElementTextByTagNameNR(bidder, "Country"));

                bid.setTime(XMLParser.getElementTextByTagNameNR(bidElement, "Time"));
                bid.setAmount(XMLParser.getElementTextByTagNameNR(bidElement, "Amount"));
                bids.add(bid);
            }

            Element locationElement = XMLParser.getElementByTagNameNR(item, "Location");
            location = XMLParser.getElementText(locationElement);
            latitude = locationElement.getAttribute("Latitude");
            longitude = locationElement.getAttribute("Longitude");
            country = XMLParser.getElementTextByTagNameNR(item, "Country");

            started = XMLParser.getElementTextByTagNameNR(item, "Started");
            ends = XMLParser.getElementTextByTagNameNR(item, "Ends");

            Element seller = XMLParser.getElementByTagNameNR(item, "Seller");
            sellerID = seller.getAttribute("UserID");
            sellerRating = seller.getAttribute("Rating");

            description = XMLParser.getElementTextByTagNameNR(item, "Description");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public String getLocation() {
        return location;
    }

    public String getCountry() {
        return country;
    }

    public List<Bid> getBids() {
        return bids;
    }

    public List<String> getCategories() {
        return categories;
    }

    public String getBuyPrice() {
        return buyPrice;
    }

    public String getCurrently() {
        return currently;
    }

    public String getDescription() {
        return description;
    }

    public String getEnds() {
        return ends;
    }

    public String getFirstBid() {
        return firstBid;
    }

    public String getItemID() {
        return itemID;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }

    public String getNumBids() {
        return numBids;
    }

    public String getSellerID() {
        return sellerID;
    }

    public String getSellerRating() {
        return sellerRating;
    }

    public String getStarted() {
        return started;
    }
}
