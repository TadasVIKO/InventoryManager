package lt.bropro.inventorymanager.server.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "storedItems")
public class StoredItems {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;
    @ManyToOne(targetEntity = Item.class, cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private Item item;
    private double rentPrice;
    private boolean availability;

    public StoredItems(Item item, double rentPrice, boolean availability) {
        this.item = item;
        this.rentPrice = rentPrice;
        this.availability = availability;
    }

    public StoredItems() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public double getRentPrice() {
        return rentPrice;
    }

    public void setRentPrice(double rentPrice) {
        this.rentPrice = rentPrice;
    }

    public boolean getAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }
}
