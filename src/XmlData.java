import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.*;
import javax.xml.parsers.*;

/**
 * File Name: XmlData.java
 *
 * Parse XML file and upload info into MySQL DB
 *
 * <p>In this class, it mainly has two part, first part is parsing XML file which is publication information from dblp-spc-papers.xml
 * The XML file record the paper information including author, mdate, title, page and etc in recent year.
 * After we parse these file, then we extract all info from XML and save it into Publication class.
 * Then according to different attribute, we create a MySQL DB and create 3 tables: pub_info, auth_info, pub_auth_relInfo
 * Then we finish the SQL queries from Dr. Zhang requirments in MySQL.</p>
 *
 * @author Beichen Hu, John Reynolds
 * @date Feb 5th, 2021
 * @since 1.0
 */
public class XmlData{

    // MySQL 8.0 version or lower - JDBC Driver and Database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/test";

    // set up user name and password of DB
    static final String USER = "root";
    static final String PASS = "123321123";

    /**
     * Set up connection for MySQL DB
     *
     * <p>Create a connection class in order to connect with local MySQL Database.
     * After connection, we create three tables.
     * Then we parse the XML file to get information
     * Last, we insert the XML information into our MySQL DB
     * </p>
     *
     * @throws SQLException if there is an SQL error, fetch the error and print it out in terminal
     */
    public static void conDB() throws SQLException {
        // initialize a connection class to connect MySQL DB
        Connection conn = null;
        // initialize a sql statement to execute SQL query
        Statement stmt = null;
        try{
            // sign up for DB driver
            Class.forName(JDBC_DRIVER);

            // open links
            System.out.println("Connecting to Database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            // operate query
            System.out.println("Instantiate the Statement object...");
            stmt = conn.createStatement();

            // please operate createTable(stmt) first time, it create three tables, workflow_metadata, workflow_api and api_info
           // createTB(stmt);

            // parse the XML file
            parseXML(stmt);

            // select all information from pub_info
            String sql;
            sql = "SELECT * FROM pub_info";
            ResultSet rs = stmt.executeQuery(sql);

            // Expand the result set database
            while(rs.next()){
                // Search by field
                int id  = rs.getInt("pid");
                String mdate = rs.getString("mdate");
                String author = rs.getString("author");
                String title = rs.getString("title");

                // output data into terminal
                System.out.print("ID: " + id);
                System.out.print(", mdate: " + mdate);
                System.out.print(", author: " + author);
                System.out.print(", title: " + title);
                System.out.print("\n");
            }
            // finished and close
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            // deal with JDBC error
            se.printStackTrace();
        }catch(Exception e){
            // deal with Class.forName error
            e.printStackTrace();
        }finally{
            // close resources
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){
            }// do nothing
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
        // finish whole DB program and say goodbye.
        System.out.println("Goodbye!");
    }

    /**
     * Create tables for MySQL
     *
     * <p> At here, we can create our tables by using MySQL statement.
     * We would like to create 3 tables, pub_info, auth_info and pub_auth_relInfo
     * table 1: pub_info has primary key: pid which represent publication id
     * table 2: auth_info has primary key: aid which represent author id
     * table 3: Then we use table 3, pub_auth_relInfo, to connect table1 and table2 according to their relations
     * </p>
     *
     * @throws SQLException if there is an SQL error, fetch the error and print it out in terminal
     */

    public static void createTB(Statement stmt) throws SQLException{
        String createAPIInforTable = "CREATE TABLE `pub_info` (\n" +
                "  `pid` int(11) NOT NULL AUTO_INCREMENT,\n" + // api id is primary key
                "  `mdate` varchar(255) NOT NULL DEFAULT '',\n" +
                "  `author` varchar(255) NOT NULL DEFAULT '',\n" +
                "  `title` varchar(255) NOT NULL DEFAULT '',\n" +
                "  PRIMARY KEY (`pid`)\n" +
                ") ENGINE=InnoDB;";
        stmt.execute(createAPIInforTable);
    }

    /**
     * parse dblp-soc-paper.xml file.
     *
     * In order to save the paper info into MySQL DB, we need to parse the XML file at first.
     * We use DocumentBuilderFactory to create a parse instance.
     * Then use Document Stream to open the file and pass it into our parser.
     * According to Dr.ZHang's requirement, then we extract all information what she need.
     * Last, insert these information into our database.
     * @param stmt pass MySQL statement object in order to execute insert command.
     */
    public static void parseXML(Statement stmt){
        try {

            // Get the DOM Builder Factory
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            // Get the DOM Builder
            DocumentBuilder builder = factory.newDocumentBuilder();
            // Load and Parse the XML document
            // document contains the complete XML as a Tree
            Document document = builder.parse(ClassLoader.getSystemResourceAsStream("dblp-soc-papers.xml"));
            // Iterating through the nodes and extracting the data
            NodeList nodeList = document.getDocumentElement().getChildNodes();
            List<Publication> publicationList = new ArrayList<Publication>();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node instanceof Element) {
                    // We have encountered an <inproceedings> or <article> tag
                    Publication pub = new Publication();
                    //initialize authors list.
                    pub.authors = new ArrayList<String>();
                    //initialize editors list.
                    pub.mdate = node.getAttributes().getNamedItem("mdate").getNodeValue();
                    //initialize and define key value
                    NodeList childNodes = node.getChildNodes();
                    for (int j = 0; j < childNodes.getLength(); j++) {
                        Node cNode = childNodes.item(j);
                        // Identifying the child tag of inproceedings encountered or article
                        if (cNode instanceof Element) {
                            String content = cNode.getLastChild().getTextContent().trim();
                            switch (cNode.getNodeName()) {
                                case "author":
                                    pub.authors.add(content);
                                    break;
                                case "title":
                                    pub.title = content;
                                    break;
                            }
                        }
                    }
                    publicationList.add(pub);
                }
                // Add the publication info list to DB
                for (Publication e : publicationList) {
                    // create sql statement
                    String sql;
                    sql = "insert INTO `pub_info`(mdate,author,title) VALUES ( '"+e.mdate+"','"+e.authors+"' ,'"+e.title+"');";
                    // execute sql statement
                    stmt.execute(sql);

//                    System.out.println(e.mdate);
//                    System.out.println(e.authors.get(0));
//                    System.out.println(e.title);
//                    System.out.println("-----------------------");
                }

            }
            System.out.println("Data is successfully inserted!");
        }catch (Exception err) {
            System.out.println("" + err.getMessage ());
        }
    }
    public static void main(String[] args) throws SQLException {
        conDB();
    }
}