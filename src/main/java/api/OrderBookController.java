package api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import repository.OrderBookRepository;
import repository.model.Quote;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class OrderBookController {

    @Autowired
    private OrderBookRepository orderBookRepository;

    @RequestMapping(value = "/quotes", method = GET)
    public List<Quote> getQuotes () {
        return orderBookRepository.getBest5Quotes();
    }

}
