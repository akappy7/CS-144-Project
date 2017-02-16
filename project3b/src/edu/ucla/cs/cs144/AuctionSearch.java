package edu.ucla.cs.cs144;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
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
		// TODO: Your code here!
		return new SearchResult[0];
	}

	public String getXMLDataForItemId(String itemId) {
		// TODO: Your code here!
		return "";
	}

	public String echo(String message) {
		return message;
	}

}
