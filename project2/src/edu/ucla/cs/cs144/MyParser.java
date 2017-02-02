/* CS144
 *
 * Parser skeleton for processing item-???.xml files. Must be compiled in
 * JDK 1.5 or above.
 *
 * Instructions:
 *
 * This program processes all files passed on the command line (to parse
 * an entire diectory, type "java MyParser myFiles/*.xml" at the shell).
 *
 * At the point noted below, an individual XML file has been parsed into a
 * DOM Document node. You should fill in code to process the node. Java's
 * interface for the Document Object Model (DOM) is in package
 * org.w3c.dom. The documentation is available online at
 *
 * http://java.sun.com/j2se/1.5.0/docs/api/index.html
 *
 * A tutorial of Java's XML Parsing can be found at:
 *
 * http://java.sun.com/webservices/jaxp/
 *
 * Some auxiliary methods have been written for you. You may find them
 * useful.
 */

package edu.ucla.cs.cs144;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;


class MyParser {
    
    static final String columnSeparator = "|*|";
    static DocumentBuilder builder;

    private static final String itemFile = "item.dat";
    private static final String locationFile = "location.dat";
    private static final String bidsFile = "bids.dat";
    private static final String bidderFile = "bidder.dat";
    private static final String categoryFile = "category.dat";
    private static final String userFile = "user.dat";

    private static BufferedWriter itemWriter;
    private static BufferedWriter locationWriter;
    private static BufferedWriter bidsWriter;
    private static BufferedWriter bidderWriter;
    private static BufferedWriter categoryWriter;
    private static BufferedWriter userWriter;

    private static HashSet<String> bidderSet = new HashSet<>();
    private static HashMap<String, String[]> userMap = new HashMap<>();
    private static HashMap<String, Integer> locationMap = new HashMap<>();
    private static Integer locationID = 0;
    private static Integer num = 0;
    
    static final String[] typeName = {
	"none",
	"Element",
	"Attr",
	"Text",
	"CDATA",
	"EntityRef",
	"Entity",
	"ProcInstr",
	"Comment",
	"Document",
	"DocType",
	"DocFragment",
	"Notation",
    };
    
    static class MyErrorHandler implements ErrorHandler {
        
        public void warning(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void error(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void fatalError(SAXParseException exception)
        throws SAXException {
            exception.printStackTrace();
            System.out.println("There should be no errors " +
                               "in the supplied XML files.");
            System.exit(3);
        }
        
    }
    
    /* Non-recursive (NR) version of Node.getElementsByTagName(...)
     */
    static Element[] getElementsByTagNameNR(Element e, String tagName) {
        Vector< Element > elements = new Vector< Element >();
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
            {
                elements.add( (Element)child );
            }
            child = child.getNextSibling();
        }
        Element[] result = new Element[elements.size()];
        elements.copyInto(result);
        return result;
    }
    
    /* Returns the first subelement of e matching the given tagName, or
     * null if one does not exist. NR means Non-Recursive.
     */
    static Element getElementByTagNameNR(Element e, String tagName) {
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
                return (Element) child;
            child = child.getNextSibling();
        }
        return null;
    }
    
    /* Returns the text associated with the given element (which must have
     * type #PCDATA) as child, or "" if it contains no text.
     */
    static String getElementText(Element e) {
        if (e.getChildNodes().getLength() == 1) {
            Text elementText = (Text) e.getFirstChild();
            return elementText.getNodeValue();
        }
        else
            return "";
    }
    
    /* Returns the text (#PCDATA) associated with the first subelement X
     * of e with the given tagName. If no such X exists or X contains no
     * text, "" is returned. NR means Non-Recursive.
     */
    static String getElementTextByTagNameNR(Element e, String tagName) {
        Element elem = getElementByTagNameNR(e, tagName);
        if (elem != null)
            return getElementText(elem);
        else
            return "";
    }
    
    /* Returns the amount (in XXXXX.xx format) denoted by a money-string
     * like $3,453.23. Returns the input if the input is an empty string.
     */
    static String strip(String money) {
        if (money.equals(""))
            return money;
        else {
            double am = 0.0;
            NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
            try { am = nf.parse(money).doubleValue(); }
            catch (ParseException e) {
                System.out.println("This method should work for all " +
                                   "money values you find in our data.");
                System.exit(20);
            }
            nf.setGroupingUsed(false);
            return nf.format(am).substring(1);
        }
    }
    
    /* Process one items-???.xml file.
     */
    static void processFile(File xmlFile) {
        Document doc = null;
        try {
            doc = builder.parse(xmlFile);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(3);
        }
        catch (SAXException e) {
            System.out.println("Parsing error on file " + xmlFile);
            System.out.println("  (not supposed to happen with supplied XML files)");
            e.printStackTrace();
            System.exit(3);
        }
        
        /* At this point 'doc' contains a DOM representation of an 'Items' XML
         * file. Use doc.getDocumentElement() to get the root Element. */
        System.out.println("Successfully parsed - " + xmlFile);
        
        /* Fill in code here (you will probably need to write auxiliary
            methods). */
        Element[] items = getElementsByTagNameNR(doc.getDocumentElement(), "Item");

        try {
            for (Element item : items) {
                parseItem(item);
                parseCategory(item);
                parseBids(item);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
        /**************************************************************/
        
    }

    private static void parseItem (Element item) throws IOException {
        String itemID = item.getAttribute("ItemID");
        String currently = strip(getElementTextByTagNameNR(item, "Currently"));
        String buyPrice = strip(getElementTextByTagNameNR(item, "Buy_Price"));
        String firstBid = strip(getElementTextByTagNameNR(item, "First_Bid"));
        String numBids = getElementTextByTagNameNR(item, "Number_of_Bids");

        Element location = getElementByTagNameNR(item, "Location");
        String locationText = getElementText(location);
        String latitude = location.getAttribute("Latitude");
        String longitude = location.getAttribute("Longitude");
        String country = getElementTextByTagNameNR(item, "Country");
        String locationID = getLocationID(longitude, latitude, country, locationText).toString();

        String started = formatDate(getElementTextByTagNameNR(item, "Started"));
        String ends = formatDate(getElementTextByTagNameNR(item, "Ends"));

        Element seller = getElementByTagNameNR(item, "Seller");
        String userID = seller.getAttribute("UserID");
        String rating = seller.getAttribute("Rating");
        String[] newRatings;
        if (userMap.containsKey(userID)) {
            String[] oldRatings = userMap.get(userID);
            newRatings = new String[] {oldRatings[0], rating};
        } else {
            newRatings = new String[] {"", rating};
        }
        userMap.put(userID, newRatings);

        String description = getElementTextByTagNameNR(item, "Description");
        if (description.length() > 4000)
            description = description.substring(0, 4000);
        writeRow(itemWriter, itemID, currently, buyPrice, firstBid, numBids, locationID, started, ends, userID, description);
    }

    private static void parseCategory (Element item) throws IOException {
        Element[] categories = getElementsByTagNameNR(item, "Category");
        String itemID = item.getAttribute("ItemID");
        for (Element category: categories) {
            String categoryName = getElementText(category);
            writeRow(categoryWriter, itemID, categoryName);
        }
        if (categories.length == 4)
            num++;
    }

    private static void parseBids (Element item) throws IOException {
        Element[] bids = getElementsByTagNameNR(getElementByTagNameNR(item, "Bids"), "Bid");
        String itemID = item.getAttribute("ItemID");
        for (Element bid: bids) {
            Element bidder = getElementByTagNameNR(bid, "Bidder");
            String userID = bidder.getAttribute("UserID");
            String rating = bidder.getAttribute("Rating");
            String[] newRatings;
            if (userMap.containsKey(userID)) {
                String[] oldRatings = userMap.get(userID);
                newRatings = new String[] {rating, oldRatings[1]};
            } else {
                newRatings = new String[] {rating, ""};
            }
            userMap.put(userID, newRatings);

            String location = getElementTextByTagNameNR(bidder, "Location");
            String country = getElementTextByTagNameNR(bidder, "Country");
            String locationID = getLocationID("", "", country, location).toString();

            String bidderKey = userID + locationID;
            if (!bidderSet.contains(bidderKey)) {
                writeRow(bidderWriter, userID, locationID);
                bidderSet.add(bidderKey);
            }

            String time = formatDate(getElementTextByTagNameNR(bid, "Time"));
            String amount = strip(getElementTextByTagNameNR(bid, "Amount"));
            writeRow(bidsWriter, userID, time, itemID, amount);
        }
    }

    private static Integer getLocationID (String longitude, String latitude, String country, String location) throws IOException {
        String locationKey = longitude + latitude + country + location;
        if (locationMap.containsKey(locationKey))
            return locationMap.get(locationKey);
        else {
            locationID++;
            locationMap.put(locationKey, locationID);
            writeRow(locationWriter, locationID.toString(), longitude, latitude, country, location);
            return locationID;
        }
    }

    private static String formatDate (String dateString) {
        SimpleDateFormat mdy = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
        SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = mdy.parse(dateString);
            return ymd.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "Ill Formatted Timestamp.";
        }
    }

    private static void writeUserTable() throws IOException {
        for (Map.Entry<String, String[]> entry : userMap.entrySet())
        {
            String userID = entry.getKey();
            String ratingBidder = entry.getValue()[0];
            String ratingSeller = entry.getValue()[1];
            writeRow(userWriter, userID, ratingBidder, ratingSeller);
        }
    }

    private static void writeRow (BufferedWriter writer, String... strings) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < strings.length - 1; i++) {
            stringBuilder.append(strings[i]);
            stringBuilder.append(columnSeparator);
        }
        stringBuilder.append(strings[strings.length - 1]);
        writer.write(stringBuilder.toString());
        writer.newLine();
    }
    
    public static void main (String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java MyParser [file] [file] ...");
            System.exit(1);
        }
        
        /* Initialize parser. */
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setIgnoringElementContentWhitespace(true);      
            builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new MyErrorHandler());
        }
        catch (FactoryConfigurationError e) {
            System.out.println("unable to get a document builder factory");
            System.exit(2);
        } 
        catch (ParserConfigurationException e) {
            System.out.println("parser was unable to be configured");
            System.exit(2);
        }

        try {
            /* Initialize writers. */
            itemWriter = new BufferedWriter(new FileWriter(itemFile, true));
            locationWriter = new BufferedWriter(new FileWriter(locationFile, true));
            bidsWriter = new BufferedWriter(new FileWriter(bidsFile, true));
            bidderWriter = new BufferedWriter(new FileWriter(bidderFile, true));
            categoryWriter = new BufferedWriter(new FileWriter(categoryFile, true));
            userWriter = new BufferedWriter(new FileWriter(userFile, true));
        
            /* Process all files listed on command line. */
            for (int i = 0; i < args.length; i++) {
                File currentFile = new File(args[i]);
                processFile(currentFile);
            }
            /* Write to user table now to avoid duplicates. */
            writeUserTable();
            System.out.println(num);

            /* Close the writers. */
            itemWriter.close();
            locationWriter.close();
            bidsWriter.close();
            bidderWriter.close();
            categoryWriter.close();
            userWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
