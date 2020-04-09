package repository.model;

import javax.persistence.*;

@Entity
@Table(name = "quotes")
public class Quote {

    @Id
    @GeneratedValue
    @Column(name = "Id", nullable = false)
    transient private Long dbId;

    @Column(name = "bitmexId", nullable = false)
    private Long id;

    @Column(name = "symbol", length = 64, nullable = false)
    private String symbol;

    @Column(name = "side", length = 64, nullable = false)
    private String side;

    @Column(name = "size", nullable = false)
    private Integer size;

    @Column(name = "price", nullable = false)
    private Integer price;

    public Quote() {}

    public Quote(Long id, String symbol, String side, Integer size) {
        this.id = id;
        this.symbol = symbol;
        this.side = side;
        this.size = size;

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

    public Long getDbId() {
        return dbId;
    }

    public void setDbId(Long dbId) {
        this.dbId = dbId;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
