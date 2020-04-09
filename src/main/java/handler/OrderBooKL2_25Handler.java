package handler;

import org.springframework.web.socket.TextMessage;

import java.io.IOException;

public class OrderBooKL2_25Handler extends AbstractCoreHandler {

    public final String INSTRUMENT = "XBTUSD";
    public final String INFO_TYPE = "orderBookL2_25";



    public void init () throws IOException {
        subscribe(INFO_TYPE + ":" + INSTRUMENT);
    }

    @Override
    public void handleMessage(TextMessage message) {
        System.out.println(message);
        /*
        * if insert -> repository.insert()
        * else if delete ......
        * */
    }

}
