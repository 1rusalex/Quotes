package handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONObject;
import repository.OrderBookRepository;
import repository.model.Quote;

import java.io.IOException;
import java.util.List;

public class OrderBooKL2_25Handler extends AbstractCoreHandler {

    public final String INSTRUMENT = "XBTUSD";
    public final String INFO_TYPE = "orderBookL2_25";

    private OrderBookRepository orderBookRepository;

    public OrderBookRepository getOrderBookRepository() {
        return orderBookRepository;
    }

    public void setOrderBookRepository(OrderBookRepository orderBookRepository) {
        this.orderBookRepository = orderBookRepository;
    }

    public void init() throws IOException {
        subscribe(INFO_TYPE + ":" + INSTRUMENT);
    }

    @Override
    public void handleMessage(JSONObject message) {
        if (message.keySet().contains("action")) {
            String action = message.getString("action");
            JSONArray jsonArray = message.getJSONArray("data");
            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting();
            Gson gson = builder.create();
            List<Quote> quotes = gson.fromJson(jsonArray.toString(), new TypeToken<List<Quote>>() {
            }.getType());
            if (action.equals("partial")) orderBookRepository.fillTable(quotes);
            if (action.equals("insert")) orderBookRepository.insertQuote(quotes);
            if (action.equals("update")) orderBookRepository.updateQuote(quotes);
            if (action.equals("delete")) orderBookRepository.deleteQuote(quotes);
        }
    }
}
