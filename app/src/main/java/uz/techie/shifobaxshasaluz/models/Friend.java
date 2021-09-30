package uz.techie.shifobaxshasaluz.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

public class Friend {

    private int id;
    private String username;
    private String first_name;
    private String kim;
    private int bonus;
    private String joined_date;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getKim() {
        return kim;
    }

    public void setKim(String kim) {
        this.kim = kim;
    }

    public int getBonus() {
        return bonus;
    }

    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public String getJoined_date() {
        return joined_date;
    }

    public void setJoined_date(String joined_date) {
        this.joined_date = joined_date;
    }
}
