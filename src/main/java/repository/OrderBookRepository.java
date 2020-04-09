package repository;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import repository.model.Quote;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class OrderBookRepository {

    private ConcurrentHashMap<Long, Quote> quoteStorage = new ConcurrentHashMap<>();

    private Log logger = LogFactory.getLog(this.getClass());

    public void fillTable(List<Quote> initialQuotes) {
        ConcurrentHashMap<Long, Quote> newQuoteStorage = new ConcurrentHashMap<>();
        for (Quote quote : initialQuotes) {
            newQuoteStorage.put(quote.getId(), quote);
        }
        quoteStorage = newQuoteStorage;
    }

    public void insertQuote(List<Quote> insertedQuotes) {
        for (Quote quote : insertedQuotes) {
            quoteStorage.put(quote.getId(), quote);
        }
    }

    public void deleteQuote(List<Quote> quotesToDelete) {
        for (Quote quote : quotesToDelete) {
            quoteStorage.remove(quote.getId());
        }

    }

    public void updateQuote(List<Quote> quotesToUpdate) {
        for (Quote newDataQuote : quotesToUpdate) {
            Quote quote = quoteStorage.get(newDataQuote.getId());
            if (quote != null) {
                quote.mergeWith(quote);
            }
        }

    }

    public List<Quote> getBestQuotes() {
        List<Quote> quoteList = quoteStorage.values().stream().filter(q -> q.getSide().equals("Sell")).sorted().limit(5).collect(Collectors.toList());
        quoteList.addAll(quoteStorage.values().stream().filter(q -> q.getSide().equals("Buy")).sorted().limit(5).collect(Collectors.toList()));
        return quoteList;
    }

}
