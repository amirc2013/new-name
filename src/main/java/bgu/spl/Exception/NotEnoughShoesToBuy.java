package bgu.spl.Exception;

/**
 * Created by Amir on 20/12/2015.
 */
public class NotEnoughShoesToBuy extends RuntimeException{

    public NotEnoughShoesToBuy(String message){
        super(message);
    }

}
