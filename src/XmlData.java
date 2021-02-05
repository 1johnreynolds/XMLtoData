import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.*;
import javax.xml.parsers.*;
public class XmlData{
    public static void main(String[] args) {
        try (Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/test", "root", "root")){

            Statement st=con.createStatement();
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
                // Add the Article list to DB
                for (Publication e : publicationList) {
                    int ins=st.executeUpdate("insert into publication(mdate,title) values('"+e.mdate+"','"+e.title+"')");
                }

            }
            System.out.println("Data is successfully inserted!");
        }catch (Exception err) {
            System.out.println("" + err.getMessage ());
        }
    }
}