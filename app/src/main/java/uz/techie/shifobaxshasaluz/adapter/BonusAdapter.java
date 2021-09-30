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
import uz.techie.shifobaxshasaluz.models.Friend;
import uz.techie.shifobaxshasaluz.models.History;


public class BonusAdapter extends RecyclerView.Adapter<BonusAdapter.BonusViewHolder> {
    List<History> bonuses = new ArrayList<>();
    Context context;
    int userId = -1;

    public BonusAdapter(List<History> bonuses) {
        this.bonuses = bonuses;
    }

    @NonNull
    @Override
    public BonusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_friends, parent, false);
        context = parent.getContext();
        return new BonusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BonusViewHolder holder, int position) {
        History bonus = bonuses.get(position);
        holder.tvAmount.setText(Utils.moneyToDecimal(bonus.getTotal_bonus()));
        holder.tvDate.setText(Utils.reFormatOnlyDateInverse(bonus.getCreated_at()));

        holder.tvTitle.setText(bonus.getBonus_name());
        if (bonus.getAffilate() == userId){
            holder.tvTitle.setText(context.getString(R.string.dostingiz_harididan));
        }

    }

    public void submitList(List<History> bonuses, int userId){
        this.bonuses = new ArrayList<>(bonuses);
        this.userId = userId;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return bonuses.size();
    }

    public class BonusViewHolder extends RecyclerView.ViewHolder {
        TextView tvAmount, tvTitle, tvDate;

        public BonusViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAmount = itemView.findViewById(R.id.adapter_friend_name);
            tvTitle = itemView.findViewById(R.id.adapter_friend_phone);
            tvDate = itemView.findViewById(R.id.adapter_friend_date);

        }
    }





}
