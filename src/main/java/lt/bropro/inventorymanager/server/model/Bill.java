package lt.bropro.inventorymanager.server.model;

import jakarta.persistence.*;

@Entity
@Table(name = "bills")
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;
    private String billNumber;
    private double price;
    private double additionalCosts;
    @ManyToOne(targetEntity = Role.class, cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private Event event;

    public Bill(String billNumber, double price, double additionalCosts) {
        this.billNumber = billNumber;
        this.price = price;
        this.additionalCosts = additionalCosts;
    }

    public Bill() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getAdditionalCosts() {
        return additionalCosts;
    }

    public void setAdditionalCosts(double additionalCosts) {
        this.additionalCosts = additionalCosts;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
