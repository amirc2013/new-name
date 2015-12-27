package bgu.spl.test;
import bgu.spl.mics.Broadcast;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.Request;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class MessageBusImplTest {

    class TestResult{

    }

    class TestRequest implements Request<TestResult> {

    }

    class TestBroadcast implements Broadcast{}

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testSubscribeRequest() throws Exception {

    }

    @Test
    public void testSubscribeBroadcast() throws Exception {

    }

    @Test
    public void testComplete() throws Exception {

    }

    @Test
    public void testSendBroadcast() throws Exception {
        Set<Integer> set = new CopyOnWriteArraySet<>();
        CountDownLatch cdl = new CountDownLatch(5);
        CountDownLatch cdl2 = new CountDownLatch(5);
        for(int i = 0 ; i < 5 ; i++){
            final int finalI = i;
            MicroService m = new MicroService(""+ finalI) {
                @Override
                protected void initialize() {
                    subscribeBroadcast(TestBroadcast.class,this::callback);
                    cdl2.countDown();
                }

                private void callback(TestBroadcast testBroadcast) {
                    set.add(finalI);
                    cdl.countDown();
                    terminate();
                }
            };

            new Thread(m).start();
        }

        MicroService m = new MicroService("broadcaster") {
            @Override
            protected void initialize() {
                sendBroadcast(new TestBroadcast());
            }
        };

        cdl2.await();
        new Thread(m).start();

        try {
            cdl.await();
            assertEquals(5 , set.size());
        } catch (InterruptedException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testSendRequest() throws Exception {

    }

    @Test
    public void testRegister() throws Exception {

    }

    @Test
    public void testUnregister() throws Exception {

    }

    @Test
    public void testAwaitMessage() throws Exception {

    }
}