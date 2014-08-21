package Sears_Scraper;

import java.io.IOException;
import web.Result;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Document;

import java.util.ArrayList;

/**
 * 
 * @author Tongtong Flora LIU
 * 
 * This class implements two queries based on different inputs.
 *
 * With one argument input as a keyword, it returns the total amount of
 * items found related to that keyword. 
 * java -jar Assignment.jar <keyword> (e.g. java -jar Assignment.jar "baby strollers")
 * 
 * With two arguments input as a keyword and a page index, 
 * it will print out the detailed info of results found based on that keyword on that specific page.
 * java -jar Assignment.jar <keyword> <page number> (e.g. java -jar Assignment.jar "baby strollers" 2)  
 *
 */
public class Parsing {

    private ArrayList<Result> results;
   /* Retrieve a page from the fragmented url
   */
    private String retrieveUrlFragment(String url){
        String baseAddress="http://www.sears.com";
        String append=url.replace(" ","%20");
        String dest=baseAddress+append;
        Document doc=null;
        try{
            doc=Jsoup.connect(dest).get();
        }catch(IOException e){
            System.out.println("Invalid URL");
            System.exit(0);
        }
        return doc.toString();
    }

    public String retrievePage(String search){
        //base address for sears.com
        String baseAddress="http://www.sears.com";
        //the appended URL for searching inside sears.com
        String searching="/search=";
        //replacing space with %20 in the search term
        String searchTerm=search.replace(" ","%20");
        //Constructing the URL
        String destination=baseAddress+searching+searchTerm;
        Document doc=null;
        
        try{
                String holder="";
                int count=0;
                //Sears.com uses javascript redirect to redirect user to the correct page so
                // we have to extract the new URL and go to the correct page
                while(holder.indexOf("productCount = ") == -1){
                    //counter to prevent too many redirect
                    count++;
        		    doc=Jsoup.connect(destination).get();
                    holder=doc.toString();
                    //finding location of url variable
                    int findUrl=holder.indexOf("url");
                    //finding the actual url
                    //sometimes instead of giving the url fragment, the javascript might give a full url address
                    //this is to handle that case
                    int http=holder.indexOf("www",findUrl);
                    int starting=holder.indexOf("/",findUrl);
                	int ending=holder.indexOf(";",starting);
                    if(http !=-1 && holder.indexOf("productCount = ")== -1 && ending>http){
                        String tempUrl=holder.substring(http,ending-1).replace("\\","");
                        destination="http://"+tempUrl;
                    } else {
                        if(holder.indexOf("productCount = ")== -1 && starting>=0 && ending>0 && ending>starting){
                            //The URL extracted from the javascript; the url is just then appended to the baseAddress
                            String newUrl=holder.substring(starting,ending-1);
                            //Constructing new destination URL
                            destination=baseAddress+newUrl.replace(" ","%20").replace("\\","");
                        }
                    }
                    //After more than 4 redirect we conclude that there's no product count in the page that we found
                    if(count>4){
                        System.out.println("Total product count not found. Too many redirect");
                        System.exit(0);
                    }
		        }
           
                
        } catch (IOException e){
                System.out.println("Error in opening URL no total product found");
                System.exit(0);
        }
        return doc.toString();
     }

    
    /*
     * This method is uesd to return the total number of a certain product which is given by user
     */
    public int getNumProducts(String product_str){
        //Array to store total product and sears product just in case sears product might be needed
        String html=retrievePage(product_str);
        String products="";
        Document doc=Jsoup.parse(html);
        Elements scripts=doc.select("script[type=text/javascript]");
        for(Element text :scripts){
            String value=text.toString();
            int start=value.indexOf("productCount = ");
            int ending=value.indexOf(",",start);
            if(value.indexOf("productCount = ")!=-1){
                products=value.substring(start+16,ending-1);
            }   
        }
        return Integer.parseInt(products);
        
	}
    /*This method is used to get all the product details from a certain page
    and storing the resulting Result object in an arrayList
    @search is the search term
    @page is the page number specified*/
    public ArrayList<Result> getProductDetails(String product_str, int paging){
        Elements products=null;
        results=new ArrayList<Result>();
        String pageHtml=retrievePage(product_str);
        Document doc=Jsoup.parse(pageHtml);
        /*int paging=0;
        try{
            paging=Integer.parseInt(page_str);
        } catch(NumberFormatException e){
            System.out.println("page has to be a valid number");
            System.exit(0);
        }*/
        //if page is negative terminate program and give notice to user
        if(paging<=0){
            System.out.println("page must be greater than 0");
            System.exit(0);
        } else if (paging>1){
            //In this case we are getting the URL of the destination page from the first page
            Element link=doc.select("p[id=nextPageResults]").first().child(0);
            String value=link.attr("href");
            String newPage="pageNum="+String.valueOf(paging);
            //manipulating the url to get the specified page by user
            value=value.replace("pageNum=2",newPage);
            String actualTarget=retrieveUrlFragment(value);
            Document actualDoc=Jsoup.parse(actualTarget);
            products=actualDoc.select("div[class=cardInner]");
        } else {
            //in case of the user want to get to the first page
            products=doc.select("div[class=cardInner]");
        }

        //checking if the page indeed contains the products
        if(products.isEmpty()){
            System.out.println("The specified page does not exist!");
            System.exit(0);
        }
        for(Element product :products){
            String name=product.select("h2").text();
            Element pricing=product.select("span[itemprop=price]").first();
            String price="";
            if(pricing !=null){
                price=pricing.text();
            }

            Elements vendors=product.select("#mrkplc");
            String vendor="Sears";
            //checking if there's a vendor name
            //if there is, then set it to the vendor name
            //else just set it to Sears
            if(!(vendors.isEmpty())){
                vendor=vendors.first().select("p").last().text();
                vendor=vendor.replace("Sold by","");
            }
            Result details=new Result(name,price,vendor);
            results.add(details);
            System.out.println("Product name: "+name);
            System.out.println("Price: "+price);
            System.out.println("Vendor: "+vendor);
            
        }

        return results;
    }

    public static void main(String[] args){
        Parsing machine=new Parsing();
        //if there's only one argument use the first query
        //if there's two then use the second query
        //else we terminate with notice to user
        if(args.length==1){
        	System.out.println("Totla num:"+machine.getNumProducts(args[0]));
        }else if(args.length==2){
            machine.getProductDetails(args[0],Integer.parseInt(args[1]));
        } else {
            System.out.println("Wrong number of arguments");
        }
        
        
        //Here are two test cases:
        //System.out.println("Totla num:"+machine.getNumProducts("camera"));
        //machine.getProductDetails("camera",2);
    }
}
