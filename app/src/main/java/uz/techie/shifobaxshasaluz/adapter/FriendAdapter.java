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
import uz.techie.shifobaxshasaluz.models.Order;


public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {
    List<Friend> friends = new ArrayList<>();
    Context context;

    public FriendAdapter(List<Friend> friends) {
        this.friends = friends;
    }

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_friends, parent, false);
        context = parent.getContext();
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        Friend friend = friends.get(position);
        holder.tvName.setText(friend.getFirst_name());
        holder.tvPhone.setText("+"+friend.getUsername());
        holder.tvDate.setText(Utils.reFormatOnlyDateInverse(friend.getJoined_date()));

    }

    public void submitList(List<Friend> friends){
        this.friends = new ArrayList<>(friends);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public class FriendViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPhone, tvDate;

        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.adapter_friend_name);
            tvPhone = itemView.findViewById(R.id.adapter_friend_phone);
            tvDate = itemView.findViewById(R.id.adapter_friend_date);

        }
    }





}
