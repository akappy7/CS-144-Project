package edu.ucla.cs.cs144;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.util.*;
import java.text.SimpleDateFormat;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.document.Document;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import edu.ucla.cs.cs144.DbManager;
import edu.ucla.cs.cs144.SearchRegion;
import edu.ucla.cs.cs144.SearchResult;

public class AuctionSearch implements IAuctionSearch {

	/*
         * You will probably have to use JDBC to access MySQL data
         * Lucene IndexSearcher class to lookup Lucene index.
         * Read the corresponding tutorial to learn about how to use these.
         *
	 * You may create helper functions or classes to simplify writing these
	 * methods. Make sure that your helper functions are not public,
         * so that they are not exposed to outside of this class.
         *
         * Any new classes that you create should be part of
         * edu.ucla.cs.cs144 package and their source files should be
         * placed at src/edu/ucla/cs/cs144.
         *
         */
		private IndexSearcher searcher = null;
    		private QueryParser parser = null;

		public void initialize() throws IOException{
		//as found on lucene guide given in instructions
			searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File("/var/lib/lucene/index1"))));
        		parser = new QueryParser("search", new StandardAnalyzer());
				 }

	public SearchResult[] basicSearch(String query, int numResultsToSkip,
			int numResultsToReturn) {
		// check if no results need to be returned
		 if(numResultsToReturn <= 0){
			return new SearchResult[0];
		}
		//not a valid entry for query
		if(numResultsToSkip < 0){
			return new SearchResult[0];
		}
		try{
			if (parser == null | searcher == null)
				initialize();
			int total_results = numResultsToSkip + numResultsToReturn;
			Query new_query = parser.parse(query);
			TopDocs docs = searcher.search(new_query, total_results);
			ScoreDoc[] hits = docs.scoreDocs;
			int result_size = 0;
			//if theres not enough results
			if((hits.length - numResultsToSkip) < numResultsToReturn){
				result_size = hits.length - numResultsToSkip;
			}
			else{
				result_size = numResultsToReturn;
			}
			//allocate enough space
			SearchResult[] results = new SearchResult[result_size];
			for (int i = numResultsToSkip; i < hits.length; i++) {
				int index = i - numResultsToSkip;
				Document doc = searcher.doc(hits[i].doc);
				results[index] = new SearchResult(doc.get("ItemID"), doc.get("ItemName"));
			}
			return results;

		} catch (IOException e) {
					 System.out.println(e);
		} catch (ParseException e) {
            System.out.println(e);
        }
		return new SearchResult[0];
	}

	public SearchResult[] spatialSearch(String query, SearchRegion region,
			int numResultsToSkip, int numResultsToReturn) {
		// check if no results need to be returned
		if(numResultsToReturn <= 0){
			return new SearchResult[0];
		}
		//not a valid entry for query
		if(numResultsToSkip < 0){
			return new SearchResult[0];
		}
		HashSet<String> spatialResults = new HashSet<>();

		try {
			Connection conn = DbManager.getConnection(true);
			Statement stmt = conn.createStatement();
			String sqlQuery = String.format("" +
					"SELECT * FROM ItemLocation " +
					"WHERE MBRContains(GeomFromText('Polygon((%f %f, %f %f, %f %f, %f %f, %f %f))'), Coord)",
					region.getLx(), region.getLy(),
					region.getLx(), region.getRy(),
					region.getRx(), region.getRy(),
					region.getRx(), region.getLy(),
					region.getLx(), region.getLy());
			System.out.println(sqlQuery);
			ResultSet rs = stmt.executeQuery(sqlQuery);
			while (rs.next())
				spatialResults.add(rs.getString("ItemID"));
			conn.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		try {
			if (parser == null | searcher == null)
				initialize();
			Query new_query = parser.parse(query);
			TopDocs docs = searcher.search(new_query, Integer.MAX_VALUE);
			ScoreDoc[] hits = docs.scoreDocs;
//			System.out.println("spatial: " + spatialResults.size());
//			System.out.println("hits: " + hits.length);

			List<SearchResult> results = new LinkedList<>();
			int skipped = 0;
			int added = 0;
			for (int i = 0; i < hits.length; i++) {
				Document doc = searcher.doc(hits[i].doc);
				if (spatialResults.contains(doc.get("ItemID"))) {
					if (skipped < numResultsToSkip) {
						skipped++;
					}
					else if (added < numResultsToReturn) {
						results.add(new SearchResult(doc.get("ItemID"), doc.get("ItemName")));
						added++;
					}
					else {
						break;
					}
				}
			}
			return results.toArray(new SearchResult[results.size()]);

		} catch (IOException | ParseException e) {
			System.out.println(e.getMessage());
		}

		return new SearchResult[0];
	}

	public String getXMLDataForItemId(String itemId) {

		//initialize string to return and connection to DB
		Connection conn = null;
		String xmlString = "";
		try{
			try {
	    	conn = DbManager.getConnection(true);
			} catch (SQLException ex) {
	    	System.out.println(ex);
			}
			Statement stmt = conn.createStatement();

			ResultSet result = stmt.executeQuery("Select * from Items where ID = " + itemId);

			while (result.next()){
				System.out.println("ID: " + result.getString("ID") + " | name: " + result.getString("Name"));

				//Create xml structure

				xmlString += "<Item>\n";
				xmlString += "<Item ItemID=\"" + result.getString("ID")+ "\">\n";
				String nameCheck = result.getString("Name");
				nameCheck = checkEscapeChars(nameCheck);
				xmlString += "<Name>" +  nameCheck + "</Name>\n";

				//Category for item
				Statement categoryStmt = conn.createStatement();
				ResultSet categoryResult = categoryStmt.executeQuery("Select Category from AssociateCategory where ItemID = " + itemId);

				while(categoryResult.next()){
					String categoryCheck = categoryResult.getString("Category");
					categoryCheck = checkEscapeChars(categoryCheck);
					xmlString +="<Category>" + categoryCheck + "</Category>\n";
				}
				xmlString +="<Currently>$" + result.getString("Currently") + "</Currently>\n";
				xmlString += "<First_Bid>$" + result.getString("FirstBid") + "</First_Bid>\n";
				xmlString += "<Number_of_Bids>" + result.getString("NumberBids") + "</Number_of_Bids>\n";
				if(result.getString("NumberBids").equals("0")){
					xmlString +="<Bids />\n";
				}//no Bids
				else{
					xmlString +="<Bids>\n";
					//Bids for item
					Statement bidStmt = conn.createStatement();
					ResultSet bidResult = bidStmt.executeQuery("Select UserID, Time, Amount from Bids where ItemID = " + itemId);
					while(bidResult.next()){
						//user for bidder
						Statement bidder = conn.createStatement();
						ResultSet bidderResult = bidder.executeQuery("Select ID, RatingBidder from User where ID = \"" + bidResult.getString("UserID") + "\"");

						xmlString +="<Bid>\n";
						bidderResult.first();

						xmlString += "<Bidder Rating=\"" + bidderResult.getString("RatingBidder") + "\" UserID=\"" +  bidderResult.getString("ID") + "\">\n";
						Statement locationStmt1 = conn.createStatement();
						ResultSet locationResult = locationStmt1.executeQuery("Select LocationID from AssociateBidder where UserID = \"" + bidderResult.getString("ID") + "\"");
						locationResult.first();

						Statement locationStmt2 = conn.createStatement();
						ResultSet locationResult2 = locationStmt2.executeQuery("Select Country, Location from Location where ID = \"" + locationResult.getString("LocationID") + "\"");
						locationResult2.first();

						String country = locationResult2.getString("Country");
						String location = locationResult2.getString("Location");

						if(!location.equals("")){
							xmlString+="<Location>" + location + "</Location>\n";
						}
						if(!country.equals("")){
							xmlString+="<Country>" + country + "</Country>\n";
						}
						xmlString+= "</Bidder>\n";

						xmlString+= "<Time>" + bidResult.getString("Time") + "</Time>\n";
						xmlString+="<Amount>$" + bidResult.getString("Amount") + "</Amount>\n";

						xmlString+="</Bid>\n";

					}//bidResult while()
					xmlString+="</Bids>\n";
				}//else there are Bids

				Statement locationStmt3 = conn.createStatement();
				ResultSet locationResult3 = locationStmt3.executeQuery("Select Longitude, Latitude, Country, Location from Location where ID = \"" + result.getString("LocationID") + "\"");
				locationResult3.first();

				String longitude = locationResult3.getString("Longitude");
				String latitude = locationResult3.getString("Latitude");

				xmlString+="<Location";
				if(!latitude.equals("")){
					xmlString+=" Latitude=\"" + latitude + "\"";
				}
				if(!longitude.equals("")){
					xmlString+=" Longitude=\"" + longitude + "\"";
				}
				xmlString+=">";
				xmlString+= locationResult3.getString("Location");
				xmlString+="</Location>\n";

				xmlString+= "<Country>" + locationResult3.getString("Country") + "</Country>\n";

				xmlString+= "<Started>"+ result.getString("Started") + "</Started>\n";
				xmlString+= "<Ends>"+ result.getString("Ends") + "</Ends>\n";

				Statement seller = conn.createStatement();
				ResultSet sellerResult = seller.executeQuery("Select ID, RatingSeller from User where ID = \"" + result.getString("Seller") + "\"");
				sellerResult.first();

				xmlString+= "<Seller Rating=\"" + sellerResult.getString("RatingSeller")  + "\" UserID=\""+ sellerResult.getString("ID") + "\" />\n";

				String description = result.getString("Description");
				description = checkEscapeChars(description);
				xmlString+= "<Description>" + description + "</Description>\n";
			}
			return xmlString;
		}catch(Exception e){
			System.out.println(e.getMessage());
			return "";
		}

	}
	public String checkEscapeChars(String string1){
		string1 = string1.replaceAll("<", "&lt;");
		string1 = string1.replaceAll(">", "&gt;");
		string1 = string1.replaceAll("\"", "&quot;");
		string1 = string1.replaceAll("\'", "&apos;");
		string1 = string1.replaceAll("&", "&amp;");
		return string1;
	}
	public String echo(String message) {
		return message;
	}

}
