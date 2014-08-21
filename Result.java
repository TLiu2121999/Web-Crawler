package Sears_Scraper;
/**
 * 
 * @author Flora
 * 
 * The Result class stores the parsing data retrieved from Sears.java.
 * It takes 3 properties of one item as title, price and vendor.
 *
 */
public class Result{
    String name;
    String price;
    String vendor;
    public Result(String name,String price,String vendor){
        this.name=name;
        this.price=price;
        this.vendor=vendor;
    }

    public String getName(){
        return name;
    }

    public String getPrice(){
        return price;
    }

    public String getVendor(){
        return vendor;
    }

}
