package uz.techie.shifobaxshasaluz.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "order_item_history")
public class Order {
    @PrimaryKey
    private int id;
    private String product_name;
    private int price;
    private int bonus;
    private int aff_bonus;
    private int quantity;
    private int product;
    private int order;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public int getAff_bonus() {
        return aff_bonus;
    }

    public void setAff_bonus(int aff_bonus) {
        this.aff_bonus = aff_bonus;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getProduct() {
        return product;
    }

    public void setProduct(int product) {
        this.product = product;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
