package uz.techie.shifobaxshasaluz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import uz.nisd.asalsavdosi.R;
import uz.techie.shifobaxshasaluz.Utils;
import uz.techie.shifobaxshasaluz.models.Order;


public class HistoryItemAdapter extends RecyclerView.Adapter<HistoryItemAdapter.HistoryItemViewHolder> {
    List<Order> orders = new ArrayList<>();
    Context context;

    public HistoryItemAdapter( List<Order> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public HistoryItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_history_item, parent, false);
        context = parent.getContext();
        return new HistoryItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryItemViewHolder holder, int position) {
        Order order = orders.get(position);

        String totalPrice = Utils.moneyToDecimal(order.getPrice()*order.getQuantity());

        String total = order.getQuantity()+"x/"+totalPrice;


        holder.tvName.setText(order.getProduct_name());
        holder.tvPrice.setText(Utils.moneyToDecimal(order.getPrice())+context.getString(R.string.som));
        holder.tvTotal.setText(total+context.getString(R.string.som));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class HistoryItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice, tvTotal;

        public HistoryItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.adapter_history_item_name);
            tvPrice = itemView.findViewById(R.id.adapter_history_item_price);
            tvTotal = itemView.findViewById(R.id.adapter_history_item_total);

        }
    }




}
