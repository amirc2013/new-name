package bgu.spl.app.Passive;


import bgu.spl.app.PrettyLogger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Logger;


/**
 * Created by Amir on 21/12/2015.
 */
public class Store {

    Logger LOGGER;

    //ENUM
    public enum BuyResult {
        NOT_IN_STOCK,NOT_ON_DISCOUNT,
        REGULAR_PRICE, DISCOUNTED_PRICE
    }


    // Fields
    private final ConcurrentMap<String ,ShoeStorageInfo> storage ;
    private final List<Receipt> receipts ;


    //lockers
    Object receipts_lock;

    private Store(){
        storage = new ConcurrentHashMap<String, ShoeStorageInfo>();
        receipts = Collections.synchronizedList(new LinkedList<Receipt>());
        receipts_lock = new Object();
        LOGGER = PrettyLogger.getLogger("Store");
    }


    private static class SingeltonHolder{
        private static Store instance = new Store();
    }

    public static Store getInstance(){
        return SingeltonHolder.instance;
    }



    public BuyResult take(String shoeType , boolean onlyDiscount) {
        ShoeStorageInfo shoe = storage.get(shoeType);
        
        	if(shoe != null){
        		synchronized(shoe){
                    boolean discounted = false;
                    if(shoe.getDiscountedAmount()>0)
                        discounted = true;

                    boolean ok = shoe.buyShoe(onlyDiscount);

                    if(!ok && onlyDiscount) return BuyResult.NOT_ON_DISCOUNT;
                    else if(ok && onlyDiscount) return  BuyResult.DISCOUNTED_PRICE;
                    else if(ok && !onlyDiscount && !discounted) return  BuyResult.REGULAR_PRICE;
                    else if(ok && !onlyDiscount && discounted) return  BuyResult.DISCOUNTED_PRICE;
                    else return BuyResult.NOT_IN_STOCK;

        		}
        	}
	        else{
	           // throw new RuntimeException("Something went wrong - no such a shoes in the storage");    // we should not get into thiss error.
	         return BuyResult.NOT_IN_STOCK;
	        }
        
    }


    /**
     * you cannot access to the data until load is finished .
     * Loading shoesinfo from array.
     * @param storage
     */
    public void load(ShoeStorageInfo[] storage){ // - I need to check if the synch is acceptable
        for(ShoeStorageInfo s : storage){
            this.storage.put(s.getShoeType(),s);
        }
    }

    /**
     * adding shoes to the storage !
     * a negative amount will throw runtime exception
     * @param shoeType
     * @param amount
     */
    public void add(String shoeType, int amount) {
        if(!storage.containsKey(shoeType)){
            storage.put(shoeType, new ShoeStorageInfo(shoeType,amount,0));
        }
        else{
            storage.get(shoeType).addNewShoes(amount);
        }
        LOGGER.info("Store : We added "+amount+" shoes of : "+shoeType);
    }


    /**
     * adding dicounted shoes to our storage.
     * adding more shoes than we can will cause runtime exception (shoes always bigger than discounted)
     * @param shoeType
     * @param amount
     */
    public void addDiscount(String shoeType, int amount){
        if(!storage.containsKey(shoeType)){
          LOGGER.info("Someone tried tried to add discount to something we don't own in our store");
        }
        else{
            storage.get(shoeType).addNewDiscountedShoes(amount);
        }
        LOGGER.info(storage.get(shoeType).getDiscountedAmount()+" DISCOUNTED shoes of : "+shoeType+" has been added");
    }

    /**
     * Save the given receipt in the store.
     * @param receipt
     */
    public void file(Receipt receipt){
            receipts.add(receipt);
    }


    public void print(){

        synchronized (storage){
            for (Map.Entry<String,ShoeStorageInfo> s : storage.entrySet()){
                synchronized (LOGGER) {
                    LOGGER.info("Shoe Type : " + s.getKey() + " : Amount of shoes - " + s.getValue().getAmountOfShoes()
                            + " , Amount of discounted shoes - " + s.getValue().getDiscountedAmount());
                }
            }
        }

        synchronized (receipts){
            for (Receipt r : receipts){
                synchronized (LOGGER) {
                    LOGGER.info("Seller : " + r.getSeller() + " , Customer : " + r.getCustomer() + " , Shoe Type : " + r.getShoeType()
                            + " , isDiscounted:" + r.isDiscount() + " , Issued Tick : " + r.getIssuedTick() + " , Request Tick : " +
                            r.getRequestTick() + " , Amount Sold : " + r.getAmountSold());
                }
            }
        }
    }



}
