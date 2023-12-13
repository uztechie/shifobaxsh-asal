package uz.techie.shifobaxshasaluz.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "seller")
public class Seller {

    @PrimaryKey
    private int id;
    @SerializedName("first_name")
    private String name;
    @SerializedName("username")
    private String phone;

    @SerializedName("kim")
    private boolean isSeller;

    private int bonus;

    public Seller() {
    }

    @Ignore
    public Seller(int id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }


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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isSeller() {
        return isSeller;
    }

    public void setSeller(boolean seller) {
        isSeller = seller;
    }

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }
}
