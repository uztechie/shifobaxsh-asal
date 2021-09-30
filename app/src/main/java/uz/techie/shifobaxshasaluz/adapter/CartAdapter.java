package uz.techie.shifobaxshasaluz.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import uz.nisd.asalsavdosi.R;
import uz.techie.shifobaxshasaluz.Utils;
import uz.techie.shifobaxshasaluz.models.Cart;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder>{
    List<Cart> carts = new ArrayList<>();
    CartInterface cartInterface;
    Context context;


    public CartAdapter(List<Cart> carts, CartInterface cartInterface) {
        this.cartInterface = cartInterface;
        this.carts = carts;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_cart, parent, false);
        context = parent.getContext();
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Cart cart = carts.get(position);

        holder.tvTitle.setText(cart.getProduct_name());
        holder.tvDesc.setText(cart.getDesc());
        holder.tvPrice.setText(Utils.moneyToDecimal(cart.getPrice())+context.getString(R.string.som));
        holder.tvTotalPrice.setText(Utils.moneyToDecimal(cart.getPrice()*cart.getQuantity())+context.getString(R.string.som));

        holder.numberButton.setNumber(String.valueOf(cart.getQuantity()));
        holder.numberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                cart.setQuantity(newValue);
                cartInterface.onItemCounterClick(cart, position);
            }
        });

        Log.d("TAG", "onBindViewHolder: "+cart.getPhoto());

        Picasso.get()
                .load(cart.getPhoto())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(holder.imageView);

    }



    @Override
    public int getItemCount() {
        return carts.size();
    }

    public void changeCartCount(Cart cart, int position){
        if (position != -1){
            carts.get(position).setPrice(cart.getPrice());
            notifyItemChanged(position);
        }
    }
    public void deleteItem(int position) {
        if (position != -1){
            carts.remove(position);
            notifyItemRemoved(position);
        }
    }


    public class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, ivDelete;
        TextView tvTitle, tvPrice, tvTotalPrice, tvDesc;
        ElegantNumberButton numberButton;


        public CartViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.adapter_cart_image);
            ivDelete = view.findViewById(R.id.adapter_cart_delete);
            tvPrice = view.findViewById(R.id.adapter_cart_price);
            tvTotalPrice = view.findViewById(R.id.adapter_cart_totalPrice);
            tvTitle = view.findViewById(R.id.adapter_cart_title);
            tvDesc = view.findViewById(R.id.adapter_cart_desc);
            numberButton = view.findViewById(R.id.adapter_cart_number);

            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cartInterface.deleteItem(carts.get(getAdapterPosition()), getAdapterPosition());
                }
            });

        }
    }



    public interface CartInterface{
        void onItemClick(Cart cart);
        void onItemCounterClick(Cart cart, int position);
        void deleteItem(Cart cart, int position);
    }



}
