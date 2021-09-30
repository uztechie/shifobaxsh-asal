package uz.techie.shifobaxshasaluz.models;

import java.util.List;

public class RequestOrder {
    private List<Order> operations_item;
    private int pay_bonus;
    private int status;
    private int op_type;
    private int customer;
    private int seller_id;
    private int Bonus_type;
    private int affilate_id;


    public List<Order> getOperations_item() {
        return operations_item;
    }

    public void setOperations_item(List<Order> operations_item) {
        this.operations_item = operations_item;
    }

    public int getPay_bonus() {
        return pay_bonus;
    }

    public void setPay_bonus(int pay_bonus) {
        this.pay_bonus = pay_bonus;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getOp_type() {
        return op_type;
    }

    public void setOp_type(int op_type) {
        this.op_type = op_type;
    }

    public int getCustomer() {
        return customer;
    }

    public void setCustomer(int customer) {
        this.customer = customer;
    }

    public int getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(int seller_id) {
        this.seller_id = seller_id;
    }

    public int getBonus_type() {
        return Bonus_type;
    }

    public void setBonus_type(int bonus_type) {
        Bonus_type = bonus_type;
    }

    public int getAffilate_id() {
        return affilate_id;
    }

    public void setAffilate_id(int affilate_id) {
        this.affilate_id = affilate_id;
    }
}
