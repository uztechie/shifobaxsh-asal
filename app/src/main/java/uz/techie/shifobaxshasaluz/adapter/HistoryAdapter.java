package uz.techie.shifobaxshasaluz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import uz.nisd.asalsavdosi.R;
import uz.techie.shifobaxshasaluz.Utils;
import uz.techie.shifobaxshasaluz.models.History;
import uz.techie.shifobaxshasaluz.models.Order;


public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    List<History> histories = new ArrayList<>();
    List<Order> orders = new ArrayList<>();
    List<Order> filterredOrders = new ArrayList<>();

    HistoryInterface historyInterface;
    Context context;
    boolean isExpand = false;

    public HistoryAdapter(List<History> histories, List<Order> orders, HistoryInterface historyInterface) {
        this.histories = histories;
        this.orders = orders;
        this.historyInterface = historyInterface;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_history, parent, false);
        context = parent.getContext();
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        History history = histories.get(position);


        String quantity = context.getString(R.string.mahsulot_soni)+history.getItem_product_total();
        String date = Utils.formatDate(history.getUpdated_at());

        String name = history.getSeller_name();
        if (Utils.isUserSeller()){
            name = history.getCustomer_name();
        }
        else {
            name = history.getSeller_name();
        }

        if (name.equals("0")){
            name = context.getString(R.string.internet);
            holder.tvName.setTextColor(context.getResources().getColor(R.color.purple_500));
        }
        else {
            holder.tvName.setTextColor(context.getResources().getColor(R.color.black));
        }

        holder.tvName.setText(name);
        holder.tvPrice.setText(Utils.moneyToDecimal(history.getItem_product_price())+context.getString(R.string.som));
        holder.tvQuantity.setText(quantity);
        holder.tvDate.setText(date);
        holder.tvBonusUsed.setText(Utils.moneyToDecimal(history.getPay_bonus()));
        holder.tvBonusIncome.setText(Utils.moneyToDecimal(history.getTotal_bonus()));

        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.addItemDecoration(new DividerItemDecoration(context,DividerItemDecoration.VERTICAL));

        filterredOrders = new ArrayList<>();
        for (Order order:orders){
            if (order.getOrder() == history.getId()){
                filterredOrders.add(order);
            }
        }

        HistoryItemAdapter itemAdapter = new HistoryItemAdapter(filterredOrders);
        holder.recyclerView.setAdapter(itemAdapter);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExpand){
                    isExpand = false;
                    holder.recyclerView.setVisibility(View.GONE);
                }
                else {
                    isExpand = true;
                    holder.recyclerView.setVisibility(View.VISIBLE);
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return histories.size();
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDate, tvPrice, tvQuantity, tvBonusUsed, tvBonusIncome;
        RecyclerView recyclerView;
        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBonusUsed = itemView.findViewById(R.id.adapter_history_bonus);
            tvBonusIncome = itemView.findViewById(R.id.adapter_history_bonus_income);
            tvName = itemView.findViewById(R.id.adapter_history_seller_name);
            tvDate = itemView.findViewById(R.id.adapter_history_date);
            tvPrice = itemView.findViewById(R.id.adapter_history_price);
            tvQuantity = itemView.findViewById(R.id.adapter_history_quantity);
            recyclerView = itemView.findViewById(R.id.adapter_history_recyclerview);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    historyInterface.onItemClick(histories.get(getAdapterPosition()));
                }
            });
        }
    }

    public void setOrdersList(List<Order> orderList){
        orders = new ArrayList<>(orderList);
        notifyDataSetChanged();
    }

    public interface HistoryInterface {
        void onItemClick(History history);
    }


}
