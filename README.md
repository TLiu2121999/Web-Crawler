Web-Crawler
===========

Retrieve certain information from a given web site  
Documentation for Sears.com Results Text Scraper
Aug. 20, 2014

This package includes:
TongtongLIU_BrightEdge_fat.jar
Ducument_Sears_TongtongLIU.pdf
Parsing.java (/ src)
Result.java (/src)
	
Target
The package is mainly for two questions:

Query 1: Total number of a given product on Sears.com
Given a keyword, such as "digital camera", return the total number of results found.
Implementation:
java -jar Assignment.jar <keyword>
(e.g. java -jar Assignment.jar "baby strollers")

Query 2: Result Object
Given a keyword (e.g. "digital cameras") and page number (e.g. "1"), return the results in a result object and then print results on screen. For each result, return title, price, vendor, and vendor information.
Implementation:
java -jar Assignment.jar <keyword> <page number> 
(e.g. java -jar Assignment.jar "baby strollers" 2)  

Implementation: 
Command line
For Query 1:
java -jar Assignment.jar <keyword>
(e.g. java -jar Assignment.jar "baby strollers")
For Query 2:
java -jar Assignment.jar <keyword> <page number> 
(e.g. java -jar Assignment.jar "baby strollers" 2)  

Import Package “Sears_Scraper.*” to specified class files



External libraries for HTML parsing
jsoup-1.7.3.jar
jsoup-1.7.3-javadoc.jar
jsoup-1.7.3-sources.jar
Web.jar

Forms of classes and methods:


Result

name:String
price: String
vendor: String
Result ( String name,String price,String vendor)
String getName()
String getPrice()
String getVendor()

Parsing

results:  ArrayList<Result>

String  retrieveUrlFragment(String url)
retrievePage(String search)
int  getNumProducts(String  product_str)
ArrayList<Result> getProductDetails(String product_str, int paging)
main(String[] args)


*Note:
This scraper (Assignment.jar) is developed in Java with Eclipse in win8 system. 
Execution of this program requires JVM. 
To enable the function and make sure it can be implemented in cmd, another Eclipse plugin is also needed: Fat Jar.















