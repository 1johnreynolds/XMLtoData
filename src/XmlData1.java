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
public class XmlData1{

    // MySQL 8.0 version or lower - JDBC Driver and Database URL
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/CS7340TEAMONELABONE";

    // set up user name and password of DB
    static final String USER = "root";
    static final String PASS = "";

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
            //DropTB(stmt);
            createTB(stmt);
            parseXML(stmt);
            AnswerQuery aq = new AnswerQuery();
            if (aq.chosen().equals("a")) {
                String sql_1_3_1;
                sql_1_3_1 = "/* 1.3.1 Given author name A, list all of her co-authors.*/\n" +
                        "Select Distinct (author) from pub_auth_relInfo where title in" +
                        "(select title from pub_auth_relInfo where author='"+aq.returnQueryA_AuthorName()+"') \n" +
                        "and mdate in(select mdate from pub_auth_relInfo where author='" +
                        aq.returnQueryA_AuthorName() +
                        "') Order by author;\n";
                ResultSet rs = stmt.executeQuery(sql_1_3_1);

                List<String> coAuthors = new ArrayList<String>();
                while (rs.next()) {
                    String author = rs.getString("author");
                    // System.out.print(author);
                    // System.out.print("\n");
                    coAuthors.add(author);
                }

                if (!coAuthors.isEmpty()) {
                    System.out.println("co-authors are: ");
                    for (String s : coAuthors) {
                        System.out.println(s);
                    }
                    System.out.println();
//                    System.out.println(coAuthors);
                } else {
                    System.out.println("Cannot find co-authors for given author.");
                }

                rs.close();

            } else if (aq.chosen().equals("b")) {
                String sql_1_3_2;
                sql_1_3_2 = "/* 1.3.1 Given author name A, list all of her co-authors.*/\n" +
                        "Select * from pub_auth_relInfo where " +
                        "(journal in (select journal from pub_auth_relInfo where title='"+aq.returnQueryB_PaperName()+"') \n" +
                        "and book_title in (select book_title from pub_auth_relInfo where title='"+aq.returnQueryB_PaperName()+"') \n" +
                        "and  pub_year in (select pub_year from pub_auth_relInfo where title='"+aq.returnQueryB_PaperName()+"') \n" +
                        "and volume in (select volume from pub_auth_relInfo where title='"+aq.returnQueryB_PaperName()+"') \n" +
                        "and pub_number in (select pub_number  from pub_auth_relInfo where title='"+aq.returnQueryB_PaperName()+"')  ;";

                ResultSet rs = stmt.executeQuery(sql_1_3_2);

                List<String> coAuthors = new ArrayList<String>();
                while (rs.next()) {
                    String author = rs.getString("author");
                    // System.out.print(author);
                    // System.out.print("\n");
                    coAuthors.add(author);
                }

                if (!coAuthors.isEmpty()) {
                    System.out.println("co-authors are: ");
                    for (String s : coAuthors) {
                        System.out.println(s);
                    }
                    System.out.println();
//                    System.out.println(coAuthors);
                } else {
                    System.out.println("Cannot find co-authors for given author.");
                }

                rs.close();
            } else if (aq.chosen().equals("c")) {
                //
            } else if (aq.chosen().equals("d")) {
                //
            } else if (aq.chosen().equals("ex")) {
                //
            } else {
                System.out.println("Error exists");
            }
            // select all information from pub_info
           /* String sql;
            sql = "/* 1.3.1 Given author name A, list all of her co-authors.\n" +
                    "Select Distinct (author) from pub_auth_relInfo where title in" +
                    "(select title from pub_auth_relInfo where author='Jia zhang') \n" +
                    "and mdate in(select mdate from pub_auth_relInfo where author='" +
                    aq.returnQueryA_AuthorName() +
                    "') Order by author;\n";
            ResultSet rs = stmt.executeQuery(sql);**/
            int count=0;
            // Expand the result set database
            //while(rs.next()){
            // Search by field
               /* int id  = rs.getInt("pid");
                String mdate = rs.getString("mdate");
                String author = rs.getString("author");
                String title = rs.getString("title");**/

            // output data into terminal
                /*System.out.print("ID: " + id);
                System.out.print(", mdate: " + mdate);
                System.out.print(", author: " + author);
                System.out.print(", title: " + title);
                System.out.print("\n");**/

            //}
            // finished and close
            //rs.close();
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
    public static void DropTB(Statement stmt) throws SQLException{
        String Drop = "Drop table pub_auth_relInfo;\n" +
                "  Drop table auth_info;\n" + // api id is primary key
                "  Drop table pub_info;";

        stmt.executeUpdate(Drop);
    }

    public static void createTB(Statement stmt) throws SQLException{
        String createpub = "CREATE TABLE `pub_info` (\n" +
                "  PUB_YEAR INT DEFAULT 0000,\n" + // api id is primary key
                "  VOLUME INT NOT NULL DEFAULT 0,\n" +
                "  JOURNAL VARCHAR(100) NOT NULL DEFAULT '',\n" +
                "  PUB_NUMBER INT NOT NULL DEFAULT 0,\n" +
                "  PUBLISHER VARCHAR(100) NOT NULL DEFAULT '',\n" +
                "  ISBN VARCHAR(50) NOT NULL DEFAULT '',\n" + // api id is primary key
                "  SERIES VARCHAR(100) NOT NULL DEFAULT '',\n" +
                "  CROSS_REF VARCHAR(100) NOT NULL DEFAULT '',\n" +
                "  BOOK_TITLE VARCHAR(100) NOT NULL DEFAULT '',\n" +
                "  PRIMARY KEY(JOURNAL,BOOK_TITLE,PUB_YEAR,VOLUME,PUB_NUMBER)\n" +
                ") ENGINE=InnoDB;";

        stmt.execute(createpub);

        String createauth = "CREATE TABLE auth_info (\n" +
                "  AUTHOR VARCHAR(100),\n" + // api id is primary key
                "  PRIMARY KEY(AUTHOR)\n" +
                ") ENGINE=InnoDB;";
        stmt.execute(createauth);

        String createarticle = "CREATE TABLE pub_auth_relInfo(\n" +
                "TITLE VARCHAR (250)NOT NULL DEFAULT '',\n" + // api id is primary key
                "MDATE VARCHAR(100) NOT NULL DEFAULT '',\n" +
                "AUTHOR VARCHAR(100)NOT NULL DEFAULT '',\n" +
                "AUTHOR_LIST VARCHAR(250)NOT NULL DEFAULT '',\n" +
                "ARTICLE_KEY VARCHAR(100) NOT NULL DEFAULT '',\n" +
                "EDITORS VARCHAR(100)NOT NULL DEFAULT '',\n" +
                "PAGES VARCHAR(50) NOT NULL DEFAULT '',\n" + // api id is primary key
                "EE VARCHAR(200) NOT NULL DEFAULT '',\n" +
                "URL VARCHAR(100) NOT NULL DEFAULT '',\n" +
                "PUB_YEAR INT DEFAULT 0000,\n" +
                "JOURNAL VARCHAR(100) NOT NULL DEFAULT '',\n" +
                "BOOK_TITLE VARCHAR(100) NOT NULL DEFAULT '',\n" +
                "VOLUME INT NOT NULL DEFAULT 0,\n" +
                "PUB_NUMBER INT NOT NULL DEFAULT 0,\n" +
                "foreign key(AUTHOR) references auth_info(AUTHOR),\n" +
                "foreign key(JOURNAL,BOOK_TITLE,PUB_YEAR,VOLUME,PUB_NUMBER) references pub_info(JOURNAL,BOOK_TITLE,PUB_YEAR,VOLUME,PUB_NUMBER),\n" +
                "  Primary key(TITLE,URl,MDATE,AUTHOR)\n" +
                ") ENGINE=InnoDB;";

        stmt.execute(createarticle);
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
                    pub.editors = new ArrayList<String>();
                    //initialize ee list.
                    pub.ee = new ArrayList<String>();
                    //initialize and define mdate value
                    pub.mdate = node.getAttributes().getNamedItem("mdate").getNodeValue();
                    //initialize and define key value
                    pub.key = node.getAttributes().getNamedItem("key").getNodeValue();
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
                                case "editor":
                                    pub.editors.add(content);
                                    break;
                                case "publisher":
                                    pub.publisher = content;
                                    break;
                                case "title":
                                    pub.title = content;
                                    break;
                                case "series":
                                    pub.series =content;
                                    break;
                                case "journal":
                                    pub.journal = content;
                                    break;
                                case "number":
                                    pub.number = Integer.parseInt(content);
                                    break;
                                case "volume":
                                    pub.volume = Integer.parseInt(content);
                                    break;
                                case "pages":
                                    pub.pages = content;
                                    break;
                                case "year":
                                    pub.year = Integer.parseInt(content);
                                    break;
                                case "crossref":
                                    pub.cross_ref = content;
                                    break;
                                case "booktitle":
                                    pub.book_title = content;
                                    break;
                                case "ee":
                                    pub.ee.add(content);
                                    break;
                                case "isbn":
                                    pub.isbn = content;
                                    break;
                                case "url":
                                    pub.url = content;
                                    break;
                            }
                        }
                    }
                    publicationList.add(pub);
                }
            }
            // Add the publication info list to DB
            int count = 0;

            for (Publication e : publicationList) {
                // create sql statement
                count++;

                String pubsql;
                /*List<String> temp2 = new ArrayList<>();
                for(int i=0;i<e.authors.size();i++){
                    temp2.add(e.authors.get(i).replaceAll("'","''"));
                }**/
                //System.out.println(e.year);
                //String temp1=
                replacePunctuation(e.journal);
                replacePunctuation(e.book_title);
                replacePunctuation(e.publisher);
                replacePunctuation(e.series);
                replacePunctuation(e.isbn);
                replacePunctuation(e.cross_ref);
                //String temp1=e.journal.replaceAll("'","''");
                /*String temp2=e.series.replaceAll("'","''");
                String temp3=e.cross_ref.replaceAll("'","''");
                String temp4=e.title.replaceAll("'","''");**/

                // pubsql = "insert INTO pub_info(pub_year,volume,journal,ISBN,BOOK_TITLE)  VALUES ( " + e.year + ","+e.volume+",'"+e.journal+"','"+e.isbn+"','"+e.book_title+"') ON duplicate KEY UPDATE book_title = book_title;";
                pubsql = "Insert INTO pub_info VALUES ( " + e.year + "," + e.volume + ",'" + e.journal + "'," +
                        "" + e.number + ",'"+e.publisher+"','"+e.isbn+"'," +
                        "'"+e.series+"','"+e.cross_ref+"','"+e.book_title+"') " +
                        "ON duplicate KEY UPDATE book_title = book_title;";
                stmt.execute(pubsql);

                replacePunctuation(e.authors);
                for(String auth: e.authors){
                    //replacePunctuation(auth);
                    String authsql="Insert INTO auth_info VALUES ('"+auth+"') " +
                            "ON duplicate KEY UPDATE author = author;";
                    stmt.execute(authsql);

                }

                //System.out.println(e.editors.toString());
                for(String auth: e.authors){
                    //replacePunctuation(auth);
                    String article="Insert INTO pub_auth_relInfo VALUES ('" + replacePunctuation(e.title) + "'," +
                            "'"+ e.mdate+ "','" + auth + "','"+e.authors+"','" + e.key + "'," +
                            "'" + replacePunctuation(e.editors) + "','" + e.pages + "'," +
                            "'" + replacePunctuation(e.ee) + "','" + e.url + "',"+e.year+"," +
                            "'"+e.journal+"','"+e.book_title+"',"+e.volume+","+e.number+") ;";
                    stmt.execute(article);

                }


                //pubsql = "insert INTO `pub_info`(pub_year) VALUES (2005);"; (mdate,author,title)
                // execute sql statement

                /*PreparedStatement preparedStmt = conn.prepareStatement(sql);
                preparedStmt.setString(1, e.mdate);
                preparedStmt.setString(3, e.title);**/

                // execute the preparedstatement
                //preparedStmt.execute();
                    /*
                    System.out.println(e.mdate);
                    System.out.println(e.authors.get(0));
                    System.out.println(e.title);
                    System.out.println("-----------------------");**/
                //count++;
                //System.out.println("asfasdfasdf"+count);

                /***/


                System.out.printf("Data %d is successfully inserted!\n",count);
            }
        }catch(Exception err){
            System.out.println("" + err.getMessage());
        }

    }
    //To solve the problem that cannot insert string including single '.

    public static String replacePunctuation(String str) {
        if(str==null){
            return str;
        }
        String returnStr = "";
        if(str.indexOf("'") != -1) {
            // original code from the Internet
            // returnStr = str.replace("'", "''");
            returnStr = str.replaceAll("'", "''");
            str = returnStr;
        }
        return str;
    }



    public static List<String> replacePunctuation(List<String> str) {
        String returnStr = "";
        for(String s: str) {
            if(s.indexOf("'") != -1) {
                str.set(str.indexOf(s), s.replaceAll("'", "''"));
            }
        }
        return str;
    }

    public static void main(String[] args) throws SQLException {
        conDB();
    }
}