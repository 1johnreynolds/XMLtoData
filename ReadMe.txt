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
DB_USER = "root";
DB_PASSWORD = "123321123";

Java Version: 1.8.0_211


2. Set up Configuration
2.1 Change MySQL DB configuration:
(1) First open the project by IntelliJ IDEA
File -> Open -> Select the Project File Name: XMLtoData then open it.
Check the left bar Project and get the project file structure.

(2) Open config.properties file under the path: ./XMLtoData/resources
Set up MySQL DB Schema name, it should have a schema at first
- JDBC_DRIVER = com.mysql.cj.jdbc.Driver
- DB_URL = jdbc:mysql://localhost:3306/CS7340TEAMONELABONE

Set up user name and password of DB
- USER = root
- PASS = 123321123 // your DB password

2.2 Set up project Structure
File -> Project Setting:
-> Project:
  Module SDK 1.8 (Java Version 1.8.0_211)
  Project Language Level: SDK Default 8
  Project Compiler Output: ./Service-Oriented Computing /Lab1/CS7340Lab1/XMLtoData/out
-> Modules:
  Dependencies: Please add external jar file: ./XMLtoData/lib, then apply it
  Paths: Use Module Compile Output Path


3. Execution
(1) Open ./src/XMLData.java file
(2) Run this file at main() function


Now it should create DB tables and upload data into it. Then it will display the options:

Which query do you want to choose?
1.3.1 Given author name A, list all of her co-authors
1.3.2 Given a paper name, list its publication metadata
1.3.3 Given a journal name, a year (volume) and an issue (number), find out the metadata of all the papers published in the book
1.4.3 Given a conference name and a year, find out the metadata of all the papers published in the book
Please choose:
a. 1.3.1
b. 1.3.2
c. 1.3.3
d. 1.3.4
q. Quit the queries:

You can choose the options according to Dr. Zhang's questions.




