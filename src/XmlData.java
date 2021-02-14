import java.io.*;
import java.sql.*;
import java.util.*;

import org.w3c.dom.*;
import javax.xml.parsers.*;

import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQPreparedExpression;
import javax.xml.xquery.XQResultSequence;

import com.saxonica.xqj.SaxonXQDataSource;

/**
 * File Name: XmlData.java
 *
 * Parse XML file and upload info into MySQL DB
 *
 * <p>In this class, it mainly has two part, first part is parsing XML file which is publication information from dblp-spc-papers.xml
 * The XML file record the paper information including author, mdate, title, page and etc in recent year.
 * After we parse these file, then we extract all info from XML and save it into Publication class.
 * Then according to different attribute, we create a MySQL DB and create 3 tables: pub_info, auth_info, pub_info
 * Then we finish the SQL queries from Dr. Zhang requirments in MySQL.</p>
 *
 * @author Beichen Hu, Jiachen Tang, Jianyu Shen, John Reynolds
 * @date Feb 5th, 2021
 * @since 1.0
 */
public class XmlData{

    // MySQL 8.0 version or lower - JDBC Driver and Database URL
    static String JDBC_DRIVER;
    static String DB_URL;

    // set up user name and password of DB
    static String USER;
    static String PASS;

    public static void configureProp() throws IOException {
        //Handles the config.properties file from location in project
        FileInputStream fis = new FileInputStream("resources/config.properties");
        //Initializes Properties to read in properties from config.properties
        Properties prop = new Properties();
        //Loads properties from file.
        prop.load(fis);
        //Assigns value in config.properties to the values below.
        JDBC_DRIVER = prop.getProperty("JDBC_DRIVER");
        DB_URL = prop.getProperty("DB_URL");
        USER = prop.getProperty("USER");
        PASS = prop.getProperty("PASS");

    }

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
            createTB(stmt);

            // parse the XML file
            parseXML(stmt);
            AnswerQuery aq = new AnswerQuery();
            while(!aq.chosen().equals("q")) {
                if (aq.chosen().equals("a")) {
                    String sql_1_3_1;
                    sql_1_3_1 ="Select Distinct (author) from pub_Info where title in" +
                            "(select title from pub_Info where author='" + aq.returnQueryA_AuthorName() + "') \n" +
                            "and mdate in(select mdate from pub_Info where author='" +
                            aq.returnQueryA_AuthorName() +
                            "') Order by author;\n";
                    ResultSet rs = stmt.executeQuery(sql_1_3_1);

                    List<String> coAuthors = new ArrayList<String>();

                    while (rs.next()) {
                        String author = rs.getString("author");
                        coAuthors.add(author);
                    }

                    if (!coAuthors.isEmpty()) {
                        System.out.println("---------------------------------------");
                        System.out.println("co-authors are: ");
                        for (String s : coAuthors) {
                            System.out.println(s);
                        }
                        System.out.println();
                        System.out.println("---------------------------------------");
                        System.out.println();
                    } else {
                        System.out.println("Cannot find co-authors for given author.");
                    }

                    rs.close();

                } else if (aq.chosen().equals("b")) {
                    String sql_1_3_2;
                    sql_1_3_2 = "Select distinct(title), author_list, mdate,article_key,editors,pages,author_list,EE,url,pub_year,journal,book_title,volume,pub_number," +
                            "publisher,ISBN,Series,CROSS_REF" +
                            " from pub_info where " +
                            "(journal in (select journal from pub_info where title='" + replacePunctuation(aq.returnQueryB_PaperName()) + "') \n" +
                            "and book_title in (select book_title from pub_info where title='" + replacePunctuation(aq.returnQueryB_PaperName()) + "') \n" +
                            "and  pub_year in (select pub_year from pub_info where title='" + replacePunctuation(aq.returnQueryB_PaperName()) + "') \n" +
                            "and volume in (select volume from pub_info where title='" + replacePunctuation(aq.returnQueryB_PaperName()) + "') \n" +
                    "and pub_number in (select pub_number  from pub_info where title='" + replacePunctuation(aq.returnQueryB_PaperName()) + "'));";
                    sql_1_3_2 = "select * from pub_info where title = '"+replacePunctuation(aq.returnQueryB_PaperName())+ "' \n" +
                    "limit 1;";
                    ResultSet rs = stmt.executeQuery(sql_1_3_2);
                    HashMap<String, List<String>> coAuthorsMap = new HashMap<>();
                    List<String> coAuthors = new ArrayList<String>();
                    while (rs.next()) {
                        String author_list = rs.getString("author_list");
                        String title = rs.getString("title");
                        String mdate = rs.getString("mdate");
                        String article_key = rs.getString("article_key");
                        String editors = rs.getString("editors");
                        String pages = rs.getString("pages");
                        String ee = rs.getString("EE");
                        String url = rs.getString("url");
                        int pub_year = rs.getInt("pub_year");
                        String journal = rs.getString("journal");
                        String book_title = rs.getString("book_title");
                        int volume = rs.getInt("volume");
                        int pub_number = rs.getInt("pub_number");
                        String publisher=rs.getString("publisher");
                        String ISBN=rs.getString("ISBN");
                        String Series=rs.getString("Series");
                        String CROSS_REF=rs.getString("CROSS_REF");

                        // output data into terminal
                        System.out.println("Title: " + title);
                        System.out.println("Mdate: " + mdate);
                        System.out.println("Author: " + author_list);
                        System.out.println("Key: " + article_key);
                        System.out.println("Editors: " + editors);
                        System.out.println("Pages: " + pages);
                        System.out.println("EE: " + ee);
                        System.out.println("URL: " + url);
                        System.out.println("Pub_year: " + pub_year);
                        System.out.println("Journal: " + journal);
                        System.out.println("Book_title: " + book_title);
                        System.out.println("Volume: " + volume);
                        System.out.println("Pub_number: " + pub_number);
                        System.out.println("Publisher: " + publisher);
                        System.out.println("ISBN: " + ISBN);
                        System.out.println("Series: " + Series);
                        System.out.println("CROSS_REF: " + CROSS_REF);
                        System.out.println("---------------------------------------");
                        System.out.println();
                    }
                    rs.close();
                } else if (aq.chosen().equals("c")) {
                    //1.3.3 Given Journal name and year(volume) and an issue (number), return metadata of all papers published in book
                    String sql_1_3_3;
                    sql_1_3_3 = "Select distinct(title), author_list, mdate,article_key,editors,pages, author_list,EE,url,pub_year,journal,volume,pub_number" +
                            " from pub_Info where " +
                            "journal='" + replacePunctuation(aq.returnQueryC_JournalName()) + "' \n" +
                            "and volume=" + aq.returnQueryC_Year() + " \n" +
                            "and  pub_number=" + aq.returnQueryC_Issue() + ";";
                    ResultSet rs = stmt.executeQuery(sql_1_3_3);
                    HashMap<String, List<String>> coAuthorsMap = new HashMap<>();
                    List<String> coAuthors = new ArrayList<String>();
                    while (rs.next()) {
                        String author_list = rs.getString("author_list");
                        String title = rs.getString("title");
                        String mdate = rs.getString("mdate");
                        String article_key = rs.getString("article_key");
                        String editors = rs.getString("editors");
                        String pages = rs.getString("pages");
                        String ee = rs.getString("EE");
                        String url = rs.getString("url");
                        int pub_year = rs.getInt("pub_year");
                        String journal = rs.getString("journal");
                        int volume = rs.getInt("volume");
                        int pub_number = rs.getInt("pub_number");

                        // output data into terminal
                        System.out.println("Title: " + title);
                        System.out.println(", Mdate: " + mdate);
                        System.out.println(", Author: " + author_list);
                        System.out.println(", Article_key: " + article_key);
                        System.out.println(", Editors: " + editors);
                        System.out.println(", Pages: " + pages);
                        System.out.println(", EE: " + ee);
                        System.out.println(", URL: " + url);
                        System.out.println(", Pub_year: " + pub_year);
                        System.out.println(", Journal: " + journal);
                        System.out.println(", Volume: " + volume);
                        System.out.println(", Pub_number: " + pub_number);
                        System.out.println("---------------------------------------");
                        System.out.println();
                    }
                    rs.close();
                } else if (aq.chosen().equals("d")) {
                    String sql_1_3_4;
                    /* 1.3.4 Given a Conference Name(Book Title) and Year, list its publication metadata, including paper title, all co-authors, publication channel (e.g., conference, journal, etc), time, page etc.*/
                    sql_1_3_4 = "Select distinct(title), author_list, mdate,article_key,editors,pages, author_list, pub_year,EE,url,book_title,cross_ref" +
                            " from pub_info where " +
                            "book_title='" + replacePunctuation(aq.returnQueryD_ConferenceName()) + "' \n" +
                            "and pub_year=" + aq.returnQueryD_Year() + ";";
                    ResultSet rs = stmt.executeQuery(sql_1_3_4);
                    while (rs.next()) {
                        String author_list = rs.getString("author_list");
                        String title = rs.getString("title");
                        String mdate = rs.getString("mdate");
                        String article_key = rs.getString("article_key");
                        String editors = rs.getString("editors");
                        String pages = rs.getString("pages");
                        String ee = rs.getString("EE");
                        String url = rs.getString("url");
                        int pub_year = rs.getInt("pub_year");
                        String book_title = rs.getString("book_title");
                        String Cross_ref = rs.getString("cross_ref");

                        // output data into terminal
                        System.out.println("Title: " + title);
                        System.out.println(", Mdate: " + mdate);
                        System.out.println(", Author: " + author_list);
                        System.out.println(", Article_key: " + article_key);
                        System.out.println(", Editors: " + editors);
                        System.out.println(", Pages: " + pages);
                        System.out.println(", EE: " + ee);
                        System.out.println(", URL: " + url);
                        System.out.println(", Pub_year: " + pub_year);
                        System.out.println(", Book_title: " + book_title);
                        System.out.println(", Crossref: " + Cross_ref);
                        System.out.println("---------------------------------------");
                        System.out.println();
                    }
                    rs.close();
                } else if (aq.chosen().equals("e")) {
                    System.out.println("---------------------------------------");
                    part2(1);
                } else if (aq.chosen().equals("f")) {
                    System.out.println("---------------------------------------");
                    part2(2);
                } else if (aq.chosen().equals("g")) {
                    System.out.println("---------------------------------------");
                    part2(3);
                } else if (aq.chosen().equals("h")) {
                    System.out.println("---------------------------------------");
                    part2(aq.returnQueryH_PaperName());
                } else {
                    System.out.println("Please check your Input!It is case and space sensitive.");
                }
                AnswerQuery.nextQuery();
            }


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
     * We would like to create 3 tables, pub_info, auth_info and pub_info
     * table 1: pub_info has primary key: pid which represent publication id
     * table 2: auth_info has primary key: aid which represent author id
     * table 3: Then we use table 3, pub_info, to connect table1 and table2 according to their relations
     * </p>
     *
     * @throws SQLException if there is an SQL error, fetch the error and print it out in terminal
     */
    public static void createTB(Statement stmt) throws SQLException{

        String createauth = "CREATE TABLE auth_info (\n" +
                "  AUTHOR VARCHAR(100),\n" + // api id is primary key
                "  PRIMARY KEY(AUTHOR)\n" +
                ") ENGINE=InnoDB;";
        stmt.execute(createauth);

        String createarticle = "CREATE TABLE pub_Info(\n" +
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
                "PUBLISHER VARCHAR(100) NOT NULL DEFAULT '',\n" +
                "ISBN VARCHAR(50) NOT NULL DEFAULT '',\n" + // api id is primary key
                "SERIES VARCHAR(100) NOT NULL DEFAULT '',\n" +
                "CROSS_REF VARCHAR(100) NOT NULL DEFAULT '',\n" +
                "foreign key(AUTHOR) references auth_info(AUTHOR),\n" +
                "Primary key(TITLE,URl,MDATE,AUTHOR)" +
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
                replacePunctuation(e.journal);
                replacePunctuation(e.book_title);
                replacePunctuation(e.publisher);
                replacePunctuation(e.series);
                replacePunctuation(e.isbn);
                replacePunctuation(e.cross_ref);
                replacePunctuation(e.authors);

                //add authors from list into DB table individually
                for(String auth: e.authors){
                    String authsql="Insert INTO  auth_info VALUES ('"+auth+"') " +
                            "ON duplicate KEY UPDATE author = author;";
                    stmt.execute(authsql);

                }

                //Insert all publication into DB table
                for(String auth: e.authors){
                    String article="Insert INTO pub_Info VALUES ('" + replacePunctuation(e.title) + "'," +
                            "'"+ e.mdate+ "','" + auth + "','"+e.authors+"','" + e.key + "'," +
                            "'" + replacePunctuation(e.editors) + "','" + e.pages + "'," +
                            "'" + replacePunctuation(e.ee) + "','" + e.url + "',"+e.year+"," +
                            "'"+e.journal+"','"+e.book_title+"',"+e.volume+","+e.number+"," +
                            "'"+e.publisher+"','"+e.isbn+"'," +
                            "'"+e.series+"','"+e.cross_ref+"') ;";
                    stmt.execute(article);

                }

                System.out.printf("Data %d is successfully inserted!\n",count);
            }
        }catch(Exception err){
            System.out.println("" + err.getMessage());
        }

    }

    /**
     * To solve the problem that cannot insert string including single quote'
     *
     * <p>Change the element from invalid which contains single quote to two single quote which my solve sql error</p>
     *
     * @param str a statement element which may contains single quote '
     * @return str a valid statement element which replace single quote into two single quote
     * */
    public static String replacePunctuation(String str) {
        if(str==null){
            return str;
        }
        String returnStr = "";
        if(str.indexOf("'") != -1) {
            returnStr = str.replaceAll("'", "''");
            str = returnStr;
        }
        return str;
    }

    /**
     * To solve the problem that cannot insert string including single quote'
     *
     * <p>Change the elements from invalid which contains single quote to two single quote which my solve sql error</p>
     *
     * @param str a list of statement elements which may contains single quote '
     * @return str a list of valid statement element which replace single quote into two single quote
     * */
    public static List<String> replacePunctuation(List<String> str) {
        String returnStr = "";
        for(String s: str) {
            if(s.indexOf("'") != -1) {
                str.set(str.indexOf(s), s.replaceAll("'", "''"));
            }
        }
        return str;
    }

    /**
     * XQuery statement selection
     *
     * <p> Pass an integer to help user select which XQuery he wants</p>
     *
     * @param questionNum a XQuery statement index selector
     * @return str a valid statement element which replace single quote into two single quote
     * */
    public static String XQueryStatement(int questionNum){
        String res;
        if (questionNum == 1) {
            res = "let $inproceedings :=doc(\"dblp-soc-papers.xml\")/dblp/inproceedings\n" +
                            "let $article := doc(\"dblp-soc-papers.xml\")/dblp/article\n" +
                            "return\n" +
                            "<articles>\n" +
                            "{for $x in $inproceedings\n" +
                            "return $x/title}\n" +
                            "{for $y in $article\n" +
                            "return $y/title}\n" +
                            "</articles>";
        } else if (questionNum == 2) {
            res = "let $inproceedings :=doc(\"dblp-soc-papers.xml\")/dblp/inproceedings\n" +
                    "let $article := doc(\"dblp-soc-papers.xml\")/dblp/article\n" +
                    "return\n" +
                    "<articles>\n" +
                    "{for $x in $inproceedings\n" +
                    "where $x/author='Jia Zhang' and $x/year=2018\n" +
                    "return $x/title}\n" +
                    "{for $y in $article\n" +
                    "where $y/author='Jia Zhang' and $y/year=2018\n" +
                    "return $y/title}\n" +
                    "</articles>";
        }else if(questionNum == 3) {
            res = "Query 3";
        }else{
            res = "error";
        }
        return res;
    }

    public static String Q3(String xqyname){
        String res;
        if(xqyname=="inproceedings"){
            res="let $inproceedings :=doc(\"dblp-soc-papers.xml\")/dblp/inproceedings\n" +
                    "let $article := doc(\"dblp-soc-papers.xml\")/dblp/article\n" +
            "let $authors1 := fn:distinct-values($inproceedings/author)\n" +
            "let $authors2 := fn:distinct-values($article/author)\n" +
            "for $a in $authors1\n" +
            "let $count := fn:count($inproceedings[author = $a])\n" +
            "order by $count\n" +
            "where $count>10\n" +
            "return <result>{$a}</result>";
         return res;
        }
        else if(xqyname=="articles"){
            res="let $inproceedings :=doc(\"dblp-soc-papers.xml\")/dblp/inproceedings\n" +
                    "let $article := doc(\"dblp-soc-papers.xml\")/dblp/article\n" +
                    "let $authors1 := fn:distinct-values($inproceedings/author)\n" +
                    "let $authors2 := fn:distinct-values($article/author)\n" +
                    "for $b in $authors2\n" +
                    "let $count := fn:count($article[author = $b])\n" +
                    "order by $count\n" +
                    "where $count>10\n" +
                    "return <result>{$b}</result>";
            return res;
        }
        else{
            res = "let $inproceedings :=doc(\"dblp-soc-papers.xml\")/dblp/inproceedings\n" +
                    "let $article := doc(\"dblp-soc-papers.xml\")/dblp/article\n" +
                    "let $authors1 := fn:distinct-values($inproceedings/author)\n" +
                    "let $authors2 := fn:distinct-values($article/author)\n" +

                    "for $a0 in $authors1\n" +
                    "let $count01 := fn:count($inproceedings[author = $a0])\n" +
                    "let $position01 := index-of($authors1, $a0)\n" +
                    "order by $count01\n" +
                    "return\n" +
                    "<result>\n" +
                    "{\n" +
                    "for $b0 in $authors2\n" +
                    "let $count02 := fn:count($article[author = $b0])\n" +
                    "let $position02 := index-of($authors2, $b0)\n" +
                    "where $a0 = $b0 and $count01 + $count02 > 10\n" +
                    "(:let $count0 := $count01 + $count02\n" +
                    "let $authors1 := remove($authors1, $position01)\n" +
                    "let $authors2 := remove($authors2, $position02):)\n" +
                    "return string($a0)\n" +
                    "}</result>";
            return res;
        }
    }

    public static String Q4(String papername){
        String res;
        res = "let $inproceedings :=doc(\"dblp-soc-papers.xml\")/dblp/inproceedings\n" +
                "let $article := doc(\"dblp-soc-papers.xml\")/dblp/article\n" +
                "return\n" +

                "<metadata>\n" +
                "{for $x in $inproceedings\n" +
                "where $x/title='"+replacePunctuation(papername)+"'\n" +
                "return\n" +
                "<InproceedingsMetadata>\n" +
                "{($x/title, $x/author,$x/pages,$x/year,$x/booktitle,$x/ee,$x/crossref,$x/url)}\n" +
                "</InproceedingsMetadata>\n" +
                "}\n" +

                " {for $y in $article\n" +
                "where $y/title='"+replacePunctuation(papername)+"'\n" +
                "return\n" +
                "<ArticlesMetadata>\n" +
                "{($y/title, $y/author,$y/pages,$y/year,$y/volume,$y/number,$y/journal,$y/ee,$y/url)}\n" +
                "</ArticlesMetadata>\n" +
                "}\n" +
                "</metadata>";

        return res;
    }
    /**
     * Execute XQuery engine and do the XQuery statement.
     *
     * <p>Set up a XQuery object engine then use @XQueryStatement get the XQuery statement
     * and show the result.</p>
     *
     * @param questionNum a XQuery statement index selector for lab 2 part questions
     * @throws XQException deal with the error when the XQuery is executed
     */
    private static void execute(int questionNum) throws XQException{

        XQDataSource ds = new SaxonXQDataSource();
        XQConnection conn = ds.getConnection();

        XQPreparedExpression exp = conn.prepareExpression(XQueryStatement(questionNum));
        XQResultSequence result = exp.executeQuery();

        while (result.next()) {
            System.out.println(result.getItemAsString(null));
        }
    }

    private static void execute(String state) throws XQException{

        XQDataSource ds = new SaxonXQDataSource();
        XQConnection conn = ds.getConnection();

        XQPreparedExpression exp = conn.prepareExpression(state);
        XQResultSequence result = exp.executeQuery();

        while (result.next()) {
            System.out.println(result.getItemAsString(null));
        }
    }

    private static XQResultSequence executeQ3WithReturn(String state) throws FileNotFoundException, XQException {
        XQDataSource ds = new SaxonXQDataSource();
        XQConnection conn = ds.getConnection();
        XQPreparedExpression exp = conn.prepareExpression(state);
        XQResultSequence result = exp.executeQuery();

        return result;
    }

    private static void executeQuery3() throws IOException, XQException {
        HashSet<String> set = new HashSet<>();
        XQResultSequence result1 = executeQ3WithReturn(Q3("inproceedings"));

        while (result1.next()) {
            set.add(result1.getItemAsString(null).trim());
        }

        XQResultSequence result2 = executeQ3WithReturn(Q3("articles"));

        while (result2.next()) {
            set.add(result2.getItemAsString(null).trim());
        }

        XQResultSequence result3 = executeQ3WithReturn(Q3("both"));

        while (result3.next()) {
            String cur = result3.getItemAsString(null).trim();
            if (cur.equals("<result/>"))
                continue;
            else
                set.add(cur);
        }

        for (String t : set) {
            System.out.println("author name: " + t);
        }
    }

    public static void part2(int num){
        try{
            if(num==3){
                executeQuery3();
                return;
            }
             // Select the lab1 question number for part 2. Ex: 2.1: 1,  2.2: 2, 2.3: 3 , 2.4: 4.
            execute(num);
        }
        catch (XQException | IOException e) {
            e.printStackTrace();
        }

    }



    public static void part2(String name){
        try{// Select the lab1 question number for part 2. Ex: 2.1: 1,  2.2: 2, 2.3: 3 , 2.4: 4.
            execute(Q4(name));
        }
        catch (XQException e) {
            e.printStackTrace();
        }

    }
    public static void main(String[] args) throws SQLException, IOException {
        //Pulls information from configure.properties file
        configureProp();
        //Establishes connection with DB
        conDB();

    }
}
