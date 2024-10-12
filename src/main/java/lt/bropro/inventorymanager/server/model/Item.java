package lt.bropro.inventorymanager.server.model;

import jakarta.persistence.*;

@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;
    private String name;
    private String description;
    @ManyToOne(targetEntity = ItemCategory.class, cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private ItemCategory itemCategory;

    public Item(String name, String description, ItemCategory itemCategory) {
        this.name = name;
        this.description = description;
        this.itemCategory = itemCategory;
    }

    public Item() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ItemCategory getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(ItemCategory itemCategory) {
        this.itemCategory = itemCategory;
    }
}
