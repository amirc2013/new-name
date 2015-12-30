package bgu.spl.app.Passive;

import bgu.spl.Exception.NegativeNumber;
import bgu.spl.Exception.NotEnoughItem;
import bgu.spl.Exception.NotEnoughShoesToBuy;
import bgu.spl.app.Json.StockInfo;

/**
 * Created by Amir on 20/12/2015.
 */
public class ShoeStorageInfo {

    private String shoeType ;
    private volatile int amountOnStorage;
    private volatile int discountedAmount;


    /**
     * amounts are defaulted to 0
     * @param shoesType
     */
    public ShoeStorageInfo(String shoesType){
        this.shoeType = shoesType;
        amountOnStorage = 0;
        discountedAmount = 0;
    }

    /**
     * you got the power man, use it wisely
     * newStorage must be greater than newDiscount or else - runtime exception
     * @param shoesType
     * @param newStorage
     * @param newDiscount
     */
    public ShoeStorageInfo(String shoesType, int newStorage,int newDiscount) throws NegativeNumber{
        if(newStorage<0)
            throw  new NegativeNumber();
        if(newStorage<newDiscount)
            throw new RuntimeException("Cant be more discounted shoes than shoes available on store");
        this.shoeType = shoesType;
        this.amountOnStorage = newStorage;
        this.discountedAmount = newDiscount;
    }

    public ShoeStorageInfo(StockInfo f) {
        shoeType = f.getShoeType();
        amountOnStorage = f.getAmount();
        discountedAmount = 0;
    }


    /**
     * Getter
     * @return the number of shoes of shoeType currently on the storage
     */
    public int getAmountOfShoes(){
        return amountOnStorage;
    }

    /**
     * Getter
     * @return amount of shoes in this storage that can be sale in a discounted price.
     */
    public int getDiscountedAmount(){
        return discountedAmount;
    }

    /**
     * Getter for shoes type
     * @return Shoes Name
     */
    public String getShoeType() {
        return shoeType;
    }

    /**
     * Setting a new amount of shoes !
     * Reseting the amount of the discounted shoes.
     * @param newAmount
     * @throws NegativeNumber
     */
    public synchronized void setNewAmountOnStorage(int newAmount) throws NegativeNumber {

        // checking correctness
        if (newAmount < 0) {throw new NegativeNumber();}

        this.amountOnStorage = newAmount;
        this.discountedAmount = 0;

    }

    /**
     * Setting a new amount of discounted shoes
     * Must be smaller than the amount of shoes at the storage
     * @param newAmount
     * @throws NotEnoughItem
     * @throws NegativeNumber
     */
    public synchronized void setNewDiscountAmount(int newAmount) throws NotEnoughItem,NegativeNumber {

        // checking correctness
        if (newAmount > amountOnStorage) {throw new NotEnoughItem("More discounted shoes than we have in our storage");}
        if (newAmount < 0) {throw new NegativeNumber();}

        this.discountedAmount = newAmount;

    }

    /**
     * Adding a new amount of shoes to our storage
     * @param newAmount
     * @throws NegativeNumber
     */
    public synchronized void addNewShoes(int newAmount) throws NegativeNumber{
        // checking correctness
        if (newAmount < 0) {throw new NegativeNumber();}

        this.amountOnStorage = this.amountOnStorage + newAmount;
    }

    /**
     * Adding a new shoes to our discounted Shoes  **ADDING FROM OUT EXISTING STORAGE**
     * @param newAmount
     * @throws NotEnoughItem
     * @throws NegativeNumber
     */
    public synchronized void addNewDiscountedShoes(int newAmount)throws  NegativeNumber{
        // checking correctness

       
        if (newAmount < 0) throw new NegativeNumber();
        
        if (newAmount+discountedAmount > amountOnStorage) discountedAmount=amountOnStorage;

        else
            this.discountedAmount = newAmount;
    }


    /**
     * return true if there is shoes with 50% discount
     * @return
     * @throws NotEnoughItem
     * @throws NegativeNumber
     */
    public boolean isDiscounted(){
        return discountedAmount>0;
    }


    /**
     * it will buy shoe as you want w/o discount.
     * @return true if you bought someting succefully
     * @param wantDiscount
     */
    public synchronized boolean  buyShoe(boolean wantDiscount){
        if(amountOnStorage<0) {
            System.out.println("ERROR ERROR ERROR ERROR - YOU SHOULD CHECK WHY THERE IS LESS THAN 0 AMOUNT ON STORAGE");
            throw new Error("Negative Amount On Store, something went wrong");
        }
        else if(discountedAmount>0) {
            this.discountedAmount--;
            this.amountOnStorage--;

            return true;
        }
        else if (!wantDiscount && discountedAmount==0 && amountOnStorage>0 ) {
            this.amountOnStorage--;
            return true;
        }
        else{ //!wantDiscount && discountedAmount==0 && amountOnStorage==0
            return false;
        }
    }

}
