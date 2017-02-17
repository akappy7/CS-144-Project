package edu.ucla.cs.cs144;

import java.io.IOException;
import java.io.StringReader;
import java.io.File;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.util.HashMap;
import java.util.Map;

public class Indexer {

private IndexWriter indexWriter = null;


  public IndexWriter getIndexWriter(){
    if (indexWriter == null){
      try{
        initIndexWrite();
      } catch (IOException ex) {
        System.out.println(ex);
      }
    } //
    return indexWriter;
  }
  public void initIndexWrite() throws IOException{
      Directory indexDir = FSDirectory.open(new File("/var/lib/lucene/index1"));
      IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_2, new StandardAnalyzer());
      config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
      indexWriter = new IndexWriter(indexDir, config);
    }
  //close indexWriter
  public void finishIndexWriter() throws IOException {
    if (indexWriter != null) {
            indexWriter.close();
        }
  }
    /** Creates a new instance of Indexer */
    public Indexer() {
    }

    public void createIndex(String itemID, String itemName, String itemDescription, String itemCategories) throws IOException{
      //parameters: items values to create new index

      Document doc = new Document();
      //get indexWriter in append mode
      IndexWriter tempWriter = getIndexWriter();

      //add appropriate values to Document
      //Field store is to display info on search, only want to return ID and Name
      doc.add(new StringField("ItemID", itemID, Field.Store.YES));
      doc.add(new StringField("ItemName", itemName, Field.Store.YES));
      String searchable = itemName + "," + itemDescription + "," + itemCategories;
      doc.add(new TextField("search", searchable, Field.Store.NO));
      tempWriter.addDocument(doc);
    }

    public void rebuildIndexes() {

        Connection conn = null;

        // create a connection to the database to retrieve Items from MySQL
	try {
	    conn = DbManager.getConnection(true);

	} catch (SQLException ex) {
	    System.out.println(ex);
	}

   try {

     initIndexWrite();

     //init string ID, description, categories, name
     String itemID, itemName, itemDescription, itemCategories = "";
     Statement stmt = conn.createStatement();

     ResultSet rs_item = stmt.executeQuery("SELECT ID, Name, Description FROM Items");


     //create a hash map for categories to reduce query numbers
     PreparedStatement categoryQuery = conn.prepareStatement("Select ItemID, Category from AssociateCategory ");
     ResultSet rs_category = categoryQuery.executeQuery();

     Map<String, String> map = new HashMap<String, String>();

     while(rs_category.next()){
       String key = rs_category.getString("ItemID");
       String value = rs_category.getString("Category");
       if(map.get(key) != null){
         String category = map.get(key);
         category += "," + value;
         map.put(key, category);
       }
       else{
         map.put(key, value);
       }
     }



   while (rs_item.next()) {
       itemID = rs_item.getString("ID");
       itemName = rs_item.getString("Name");
       itemDescription = rs_item.getString("Description");

       itemCategories = map.get(itemID);
       //System.out.println(itemID);
       createIndex(itemID, itemName, itemDescription, itemCategories);

     }
     //close indexWriter
     finishIndexWriter();
   } catch (SQLException ex) {
       System.out.println(ex);
   }catch (IOException ex) {
    System.out.println(ex);
  }
        // close the database connection
	try {
	    conn.close();
	} catch (SQLException ex) {
	    System.out.println(ex);
	}
    }

    public static void main(String args[]) {
        Indexer idx = new Indexer();
        idx.rebuildIndexes();
    }
}
