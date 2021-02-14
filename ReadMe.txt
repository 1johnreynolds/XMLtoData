CS 5-7340-Spring 2021 Service-Oriented Computing

Group 1: Beichen Hu, Jiachen Tang, Jianyu Shen, John Reynolds

Lab 1 ReadMe.txt file


1. Software Preference:
IntelliJ IDEA 2019.3.1 (Edu)
Build #IE-193.6015.46, built on January 21, 2020
Runtime version: 11.0.5+10-b520.30 x86_64
VM: OpenJDK 64-Bit Server VM by JetBrains s.r.o
macOS 10.16
GC: ParNew, ConcurrentMarkSweep
Memory: 989M
Cores: 8

MySQL Workbench 8.0 Version: 8.0.17
JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
DB_URL = "jdbc:mysql://localhost:3306/test";
//These values can be updated to match your database in config.properties noted later.
DB_USER = "root";
DB_PASSWORD = "123321123";

Java Version: 1.8.0_211


2. Set up Configuration (To avoid the error "cannot connect to database", please do the following)
2.1 Change MySQL DB configuration:
(1) First open the project by IntelliJ IDEA
File -> Open -> Select the Project File Name: XMLtoData then open it.
Check the left bar Project and get the project file structure.

(2) Open config.properties file under the path: ./XMLtoData/resources
Set up MySQL DB Schema name, it should have a schema at first
- JDBC_DRIVER = com.mysql.cj.jdbc.Driver
- DB_URL = jdbc:mysql://localhost:3306/CS7340TEAMONELABONE // your DB name

Set up user name and password of DB
- USER = root
- PASS = 123321123 // your DB password

We have eliminated the need for MySQL Dump File because we create the DB tables and load the information internally.
Our Config.properties file in resources allows you to apply your database information accordingly.
When the code is run, it will create tables and import the data from the XML file automatically.
Once this is completed you can execute the queries.

2.2 Set up project Structure
File -> Project Setting:
-> Project:
  Module SDK 1.8 (Java Version 1.8.0_211)
  Project Language Level: SDK Default 8
  Project Compiler Output: ./Service-Oriented Computing /Lab1/CS7340Lab1/XMLtoData/out //this can be unique to you
-> Modules:
  Dependencies: Please add external jar files: ./XMLtoData/lib, then apply it
  Paths: Use Module Compile Output Path


3. Execution
(1) Open ./src/XMLData.java file
(2) Run this file at main() function

Now it should create DB tables and upload data into it. Then it will display the options:

Which query do you want to choose?
a. 1.3.1 Given author name A, list all of her co-authors.
b. 1.3.2 Given a paper name, list its publication metadata.
c. 1.3.3 Given a journal name, a year (volume) and an issue (number), find out the metadata of all the papers published in the book.
d. 1.3.4 Given a conference name and a year, find out the metadata of all the papers published in the book.
e. 2.1 Display all the article titles published in the area of SOSE.
f. 2.2 Display the titles of the articles published by a researcher (Jia Zhang) in a specific year (2018).
g. 2.3 Display all the authors who have published more than 10 papers in the area of SOSE to date.
h. 2.4 Given a paper name, list its publication metadata, including paper title, all co-authors, publication channel.
q. Quit the queries.
Please choose:

You can choose the options according to Dr. Zhang's questions.
To choose which query to use, you only need to type in letter a to h or q to quit the program.

// For query 1.3.3, the value of year means "volume" attribute and value of issue means "number" attribute.
// For query 1.3.4, conference name means "booktitle" attribute.
// For query 2.3, this query needs more time to run than others. Please wait patiently.
// All of the inputs are case-, space- and punctuation-sensitive.
// If this program turns error, perhaps you have typed in wrong format input, please check and select the choice (a to h) again.
// If you want to run this program again, please drop the generated tables from MySQL firstly, the MySQL code is:
drop table pub_info;
drop table author;
