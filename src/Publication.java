import java.util.List;

public class Publication {
    String mdate;
    String key;
    List<String> authors;
    List<String> editors;
    String title;
    String pages;
    int year;
    int volume;
    String journal;
    int number;
    String publisher;
    String isbn;
    String series;
    String cross_ref;
    String book_title;
    List<String> ee;
    String url;


    @Override public String toString() {return "mdate: " + mdate + " Key: " + key + "\n" +
            "Title: " + title + "\n" +
            "Authors: " + authors + "\n" +
            "Editors: " + editors + "\n" +
            "Publisher: " + publisher + "\n" +
            "Series: " + series + "\n" +
            "Journal: " + journal + "\n" +
            "Number: " + number + "\n" +
            "Volume: " + volume + "\n" +
            "Pages: " + pages + "\n" +
            "Year: " + year + "\n" +
            "Cross Reference: " + cross_ref + "\n" +
            "Book Title: " + book_title + "\n" +
            "EE: " + ee + "\n" +
            "ISBN: " + isbn + "\n" +
            "URL: " + url + "\n";}
}