package repository.model;

public class Quote implements Comparable<Quote> {

    private Long id;

    private String symbol;

    private String side;

    private Integer size;

    private Float price;

    public Quote(Long id, String symbol, String side, Integer size, Float price) {
        this.id = id;
        this.symbol = symbol;
        this.side = side;
        this.size = size;
        this.price = price;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }


    public Float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void mergeWith(Quote quote) {
        synchronized (this) {
            if (quote.getSide() != null) {
                this.side = quote.getSide();
            }
            if (quote.getSize() != null) {
                this.size = quote.getSize();
            }
            if (quote.getPrice() != null) {
                this.price = quote.getPrice();
            }
        }
    }

    @Override
    public int compareTo(Quote o) {
        if (this.getPrice().equals(o.getPrice())) {
            return 0;
        }
        else if (this.getPrice()>o.getPrice()) {
            return -1;
        } else {
            return 1;
        }
    }
}
