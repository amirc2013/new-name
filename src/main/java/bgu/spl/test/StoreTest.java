package bgu.spl.test;

import bgu.spl.app.Passive.Receipt;
import bgu.spl.app.Passive.ShoeStorageInfo;
import bgu.spl.app.Passive.Store;
import org.junit.Before;
import org.junit.Test;
import org.omg.CORBA.portable.OutputStream;

import java.io.PrintStream;

import static org.junit.Assert.*;

/**
 * Created by Amir on 27/12/2015.
 */
public class StoreTest {

    Store s ;

    @Before
    public void setUp() throws Exception {
        s = Store.getInstance();
    }

    @Test
    public void testGetInstance() throws Exception {
        assertNotNull("No Store", s);
        assertEquals("Should be singelton",Store.getInstance(),s);
    }

    @Test
    public void testLoad() throws Exception { //checking there is not RUNTIME Exception
        ShoeStorageInfo[] array = {new ShoeStorageInfo("test1", 8 , 2), new ShoeStorageInfo("test2", 10,0),new ShoeStorageInfo("k",1,1), new ShoeStorageInfo("k2",1,0)};
        s.load(array);
    }


    @Test
    public void testTake() throws Exception {
        assertEquals("Should not be on discount", Store.BuyResult.NOT_ON_DISCOUNT,s.take("k2",true));
        assertEquals("Should buy with regular price", Store.BuyResult.REGULAR_PRICE,s.take("k2",false));
        assertEquals("Should not be on stock", Store.BuyResult.NOT_ON_DISCOUNT,s.take("k2",true));
        assertEquals("Should not be on stock", Store.BuyResult.NOT_IN_STOCK ,s.take("sss",true));
        //checking both Load and take
    }



    @Test
    public void testAdd() throws Exception {
        s.add("k2",2);
        assertEquals("Should be on REGULAR_PRICE", Store.BuyResult.REGULAR_PRICE,s.take("k2",false));
        assertEquals("Should be on NOT_ON_DISCOUNT", Store.BuyResult.NOT_ON_DISCOUNT,s.take("k2",true));
        assertEquals("Should be on REGULAR_PRICE", Store.BuyResult.REGULAR_PRICE,s.take("k2",false));
        assertEquals("Should be on NOT_ON_DISCOUNT", Store.BuyResult.NOT_ON_DISCOUNT,s.take("k2",true));
        assertEquals("Should be on NOT_IN_STOCK", Store.BuyResult.NOT_IN_STOCK,s.take("k2",false));
    }

    @Test
    public void testAddDiscount() throws Exception {
        s.add("k2",2);
        s.addDiscount("k2",2);
        assertEquals("Should be on discount", Store.BuyResult.DISCOUNTED_PRICE,s.take("k2",false));
        assertEquals("Should be on discount", Store.BuyResult.DISCOUNTED_PRICE,s.take("k2",true));
        assertEquals("Should be NOT_IN_STOCK", Store.BuyResult.NOT_IN_STOCK,s.take("k2",false));
        assertEquals("Should not be on discount", Store.BuyResult.NOT_ON_DISCOUNT,s.take("k2",true));

        boolean thrown = false;
        try {
            s.addDiscount("k2",1);
        }
        catch (RuntimeException re){
            thrown = true;
        }
        assertTrue(thrown);


    }

    @Test
    public void testFile() throws Exception {
        s.add("k2",2);
        s.take("k2",false);
        boolean thrown = false;
        try {
            Receipt r = new Receipt("seller","customer","k2",false,1,1,1);
            s.file(r);
            s.print();
        }
        catch (RuntimeException re){
            thrown = true;
        }

        assertFalse("Should not throw Exception",thrown);
    }
}