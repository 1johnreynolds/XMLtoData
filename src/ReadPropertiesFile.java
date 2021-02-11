import jdk.nashorn.internal.scripts.JD;

import java.io.FileInputStream;
import java.sql.Statement;
import java.util.Properties;

public class ReadPropertiesFile {

    public static void main(String[] args) throws Exception{
        //Handles the config.properties file from location in project
        FileInputStream fis = new FileInputStream("resources/config.properties");
        //Initializes Properties to read in properties from config.properties
        Properties prop = new Properties();
        //Loads properties from file.
        prop.load(fis);
        //Assigns value in config.properties to the values below.
        String JDBC_DRIVER = prop.getProperty("JDBC_DRIVER");
        String DB_URL = prop.getProperty("DB_URL");
        String USER = prop.getProperty("USER");
        String PASS = prop.getProperty("PASS");

    }


}

