package uz.techie.shifobaxshasaluz.models;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "cart")
public class Cart {

    @PrimaryKey
    private int id;
    private int product;
    private int price;
    private int bonus;
    private int aff_bonus;
    private int quantity;
    private String product_name;
    private String desc;
    private String photo;

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProduct() {
        return product;
    }

    public void setProduct(int product) {
        this.product = product;
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

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cart cart = (Cart) o;
        return id == cart.id && product == cart.product && price == cart.price && bonus == cart.bonus && aff_bonus == cart.aff_bonus && quantity == cart.quantity && Objects.equals(product_name, cart.product_name) && Objects.equals(desc, cart.desc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, product, price, bonus, aff_bonus, quantity, product_name, desc);
    }


    public static DiffUtil.ItemCallback<Cart> itemCallback = new DiffUtil.ItemCallback<Cart>() {
        @Override
        public boolean areItemsTheSame(@NonNull Cart oldItem, @NonNull Cart newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Cart oldItem, @NonNull Cart newItem) {
            return oldItem.equals(newItem);
        }
    };




}
