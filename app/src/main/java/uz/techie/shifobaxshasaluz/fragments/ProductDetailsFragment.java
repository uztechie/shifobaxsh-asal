package uz.techie.shifobaxshasaluz.fragments;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.squareup.picasso.Picasso;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import uz.nisd.asalsavdosi.R;
import uz.techie.shifobaxshasaluz.fragments.ProductDetailsFragmentArgs;
import uz.techie.shifobaxshasaluz.Utils;
import uz.techie.shifobaxshasaluz.models.Cart;
import uz.techie.shifobaxshasaluz.models.Product;
import uz.techie.shifobaxshasaluz.room.HoneyViewModel;

public class ProductDetailsFragment extends Fragment {
    HoneyViewModel viewModel;
    NavController navController;
    TextView tvTitle, tvPrice, tvDesc, tvFullDescText, tvTotalPrice;
    MaterialTextView tvFullDesc;
    ElegantNumberButton numberButton;
    ImageView imageView;
    Button btnAddToCart;

    String title ="";
    String desc  ="";
    String totalDecsText  ="";
    int price = 0;
    int totalPrice = 0;
    int quantity = 1;
    int productId = -1;
    boolean isFullDescVisible = false;
    Product product = new Product();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_details, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(HoneyViewModel.class);
        navController = Navigation.findNavController(view);

        imageView = view.findViewById(R.id.product_details_image);
        tvTitle = view.findViewById(R.id.product_details_title);
        tvPrice = view.findViewById(R.id.product_details_price);
        tvDesc = view.findViewById(R.id.product_details_desc);
        tvTotalPrice = view.findViewById(R.id.product_details_total_price);
        tvFullDesc = view.findViewById(R.id.product_details_full_desc);
        tvFullDescText = view.findViewById(R.id.product_details_full_desc_text);

        numberButton = view.findViewById(R.id.product_details_counter);
        btnAddToCart = view.findViewById(R.id.product_details_add_cart);

        if (getArguments() != null){
            productId = ProductDetailsFragmentArgs.fromBundle(getArguments()).getProductId();
        }
        product = viewModel.getSingleProduct(productId);
        if (product != null) {
            tvTitle.setText(product.getName());
            tvDesc.setText(product.getDesc());
            tvFullDescText.setText(Html.fromHtml(product.getFull_desc()));
            tvPrice.setText(Utils.moneyToDecimal(product.getPrice())+getString(R.string.som));
            tvTotalPrice.setText(Utils.moneyToDecimal(product.getPrice())+getString(R.string.som));
            price = product.getPrice();

            Picasso.get()
                    .load(product.getPhoto())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(imageView);
        }


        tvFullDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFullDescVisible){
                    tvFullDescText.setVisibility(View.GONE);
                    isFullDescVisible = false;
                    tvFullDesc.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_keyboard_arrow_down_24, 0);
                }
                else {
                    tvFullDescText.setVisibility(View.VISIBLE);
                    isFullDescVisible = true;
                    tvFullDesc.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_keyboard_arrow_up_24, 0);
                }
            }
        });




        numberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                totalPrice = price *newValue;
                quantity = newValue;
                tvTotalPrice.setText(Utils.moneyToDecimal(totalPrice)+getString(R.string.som));

            }
        });


        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cart cart = new Cart();
                cart.setId(product.getId());
                cart.setProduct(product.getId());
                cart.setPrice(product.getPrice());
                cart.setQuantity(quantity);
                cart.setAff_bonus(product.getAfilate_bonus());
                cart.setBonus(product.getBonus_amount());
                cart.setPhoto(product.getPhoto());

                cart.setDesc(product.getDesc());
                cart.setProduct_name(product.getName());

                viewModel.insertCart(cart)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new CompletableObserver() {
                            @Override
                            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                            }

                            @Override
                            public void onComplete() {
                                Snackbar.make(requireView(), getString(R.string.mahsulot_savatchaga_qoshildi), Snackbar.LENGTH_SHORT)
                                        .show();

                                Navigation.findNavController(requireView())
                                        .popBackStack();
                            }

                            @Override
                            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                                Log.e("TAG", "onError: insertCart", e);
                            }
                        });
            }
        });


    }
}
