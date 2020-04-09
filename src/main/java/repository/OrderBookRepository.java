package repository;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import repository.model.Quote;

import javax.persistence.Query;
import java.util.List;

@Repository
public class OrderBookRepository {

    private Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    public void fillTable(List<Quote> quotes) {}

    @Transactional
    public void insertQuote(Quote quote) {
        sessionFactory.getCurrentSession().save(quote);
    }

    @Transactional
    public void deleteQuote(Quote quote) {
        try {
            String queryString = "delete from quotes where bitmexId=?";
            Query queryObject = sessionFactory.getCurrentSession().createQuery(queryString);
            queryObject.setParameter(0, quote.getId());
            queryObject.executeUpdate();
        } catch (RuntimeException re) {
            logger.error("delete operation is failed", re);
        }
    }

    @Transactional
    public void updateQuote(Quote q) {

    }

    public Quote findByBitmexId(Long bitmexId) {
        String queryString = "from quotes as model where model.bitmexId= ?";
        Query queryObject = sessionFactory.getCurrentSession().createQuery(queryString);
        queryObject.setParameter(0, bitmexId);
        return (Quote) queryObject.getSingleResult();
    }

    public List<Quote> getBest5Quotes() {
        String queryString = "(SELECT TOP 5 * FROM quotes WHERE side='Buy' ORDER BY price DESC)\n" +
                " UNION\n" +
                " (SELECT TOP 5 * FROM quotes WHERE side='Sell' ORDER BY price)\n" +
                "ORDER BY price;";
        Query queryObject = sessionFactory.getCurrentSession().createNamedQuery(queryString);
        return queryObject.getResultList();
    }

    private List<Quote> findByProperty(String propertyName, Object value) {
        String queryString = "from quotes as model where model." + propertyName + "= ?";
        Query queryObject = sessionFactory.getCurrentSession().createQuery(queryString);
        queryObject.setParameter(0, value);
        return queryObject.getResultList();
    }

}
