package uz.techie.shifobaxshasaluz.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "order_history")
public class History {

    @PrimaryKey
    private int id;
    private String updated_at;
    private String created_at;
    private String seller_phone;
    private String seller_name;
    private String customer_phone;
    private String customer_name;
    private int item_product_total;
    private int item_product_price;
    private int pay_bonus;
    private int total_bonus;
    private String bonus_name;
    private int Bonus_type;
    private int affilate;

    @Ignore
    private List<Order> operations_item;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getSeller_phone() {
        return seller_phone;
    }

    public void setSeller_phone(String seller_phone) {
        this.seller_phone = seller_phone;
    }

    public String getSeller_name() {
        return seller_name;
    }

    public void setSeller_name(String seller_name) {
        this.seller_name = seller_name;
    }

    public String getCustomer_phone() {
        return customer_phone;
    }

    public void setCustomer_phone(String customer_phone) {
        this.customer_phone = customer_phone;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public int getItem_product_total() {
        return item_product_total;
    }

    public void setItem_product_total(int item_product_total) {
        this.item_product_total = item_product_total;
    }

    public int getItem_product_price() {
        return item_product_price;
    }

    public void setItem_product_price(int item_product_price) {
        this.item_product_price = item_product_price;
    }

    public int getPay_bonus() {
        return pay_bonus;
    }

    public void setPay_bonus(int pay_bonus) {
        this.pay_bonus = pay_bonus;
    }

    public List<Order> getOperations_item() {
        return operations_item;
    }

    public void setOperations_item(List<Order> operations_item) {
        this.operations_item = operations_item;
    }

    public int getTotal_bonus() {
        return total_bonus;
    }

    public void setTotal_bonus(int total_bonus) {
        this.total_bonus = total_bonus;
    }

    public String getBonus_name() {
        return bonus_name;
    }

    public void setBonus_name(String bonus_name) {
        this.bonus_name = bonus_name;
    }

    public int getBonus_type() {
        return Bonus_type;
    }

    public void setBonus_type(int bonus_type) {
        Bonus_type = bonus_type;
    }

    public int getAffilate() {
        return affilate;
    }

    public void setAffilate(int affilate) {
        this.affilate = affilate;
    }
}
