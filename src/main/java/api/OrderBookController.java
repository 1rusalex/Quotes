package api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import repository.OrderBookRepository;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class OrderBookController {

    @Autowired
    private OrderBookRepository orderBookRepository;

    @RequestMapping(value = "/quotes", method = GET)
    public Map<String, Object> getQuotes () {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", orderBookRepository.getBestQuotes());
        return response;
    }

}
