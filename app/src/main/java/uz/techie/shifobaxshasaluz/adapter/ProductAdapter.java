package uz.techie.shifobaxshasaluz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import uz.nisd.asalsavdosi.R;
import uz.techie.shifobaxshasaluz.Utils;
import uz.techie.shifobaxshasaluz.models.Product;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    ProductInterface productInterface;
    Context context;
    List<Product> productList;
    public ProductAdapter(Context context, ProductInterface productInterface, List<Product> productList) {
        this.productInterface = productInterface;
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.tvTitle.setText(product.getName());
        holder.tvDesc.setText(product.getDesc());
        holder.tvPrice.setText(Utils.moneyToDecimal(product.getPrice())+context.getString(R.string.som));
        Picasso.get()
                .load(product.getPhoto())
                .into(holder.imageView);

//        if (repository.hasProductInCart(product.getItemId())){
//            holder.ivCart.setImageResource(R.drawable.ic_cart_filled);
//        }
//        else {
//            holder.ivCart.setImageResource(R.drawable.ic_cart_stroke);
//        }

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void submitList(List<Product> products){
        productList.clear();
        productList.addAll(products);
        notifyDataSetChanged();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, ivCart;
        TextView tvTitle, tvPrice, tvDesc;


        public ProductViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.main_adapter_image);
            ivCart = view.findViewById(R.id.main_adapter_cart);
            tvTitle = view.findViewById(R.id.main_adapter_title);
            tvPrice = view.findViewById(R.id.main_adapter_price);
            tvDesc = view.findViewById(R.id.main_adapter_desc);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productInterface.onItemClick(productList.get(getAdapterPosition()));
                }
            });

            ivCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Product product = productList.get(getAdapterPosition());
                    productInterface.addItem(product);
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }


    public interface ProductInterface{
        void onItemClick(Product product);
        void addItem(Product product);
    }



}
