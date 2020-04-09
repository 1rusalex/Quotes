package handler;

import adaptor.BitmexAdaptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import operations.SubscribeOperation;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;

public abstract class AbstractCoreHandler implements CoreHandler {

    private BitmexAdaptor bitmexAdaptor;

    public BitmexAdaptor getBitmexAdaptor() {
        return bitmexAdaptor;
    }

    public void setBitmexAdaptor(BitmexAdaptor bitmexAdaptor) {
        this.bitmexAdaptor = bitmexAdaptor;
    }

    protected void subscribe(String subscriptionTopic) throws IOException {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        SubscribeOperation subscribeOperation = new SubscribeOperation(subscriptionTopic);
        String json = gson.toJson(subscribeOperation);
        bitmexAdaptor.getWebSocketSession().sendMessage(new TextMessage(json));
    }

    protected void unsubscribe(String subscriptionTopic) {}

}
