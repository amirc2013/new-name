package bgu.spl.Exception;

/**
 * Created by Amir on 20/12/2015.
 */
public class NegativeNumber extends RuntimeException {
    public NegativeNumber(){
        super("Number can't be negative");
    }
}
