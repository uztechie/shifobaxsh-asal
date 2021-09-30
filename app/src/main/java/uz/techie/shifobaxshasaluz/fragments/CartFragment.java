package uz.techie.shifobaxshasaluz.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import uz.nisd.asalsavdosi.R;
import uz.techie.shifobaxshasaluz.fragments.CartFragmentDirections;
import uz.techie.shifobaxshasaluz.Utils;
import uz.techie.shifobaxshasaluz.adapter.CartAdapter;
import uz.techie.shifobaxshasaluz.models.Cart;
import uz.techie.shifobaxshasaluz.room.HoneyViewModel;


public class CartFragment extends Fragment implements CartAdapter.CartInterface {
    KProgressHUD progressHUD;
    HoneyViewModel viewModel;
    RecyclerView recyclerView;
    CartAdapter adapter;

    ProgressBar progressBar;
    CompositeDisposable disposable;
    List<Cart> carts = new ArrayList<>();
    TextView tvTotalPrice;
    Button btnNext;
    RelativeLayout relativeLayout;
    int totalPrice = 0;
    private String token;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressHUD = new KProgressHUD(requireContext());
        viewModel = ViewModelProviders.of(this).get(HoneyViewModel.class);
        disposable = new CompositeDisposable();

        tvTotalPrice = view.findViewById(R.id.cart_total_price);
        btnNext = view.findViewById(R.id.cart_btn_place_order);
        relativeLayout = view.findViewById(R.id.cart_relative);

        carts = viewModel.getCarts();
        progressBar = view.findViewById(R.id.home_progressbar);
        recyclerView = view.findViewById(R.id.cart_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new CartAdapter(carts, this);
        recyclerView.setAdapter(adapter);

        if (viewModel.getUser()!=null){
            token = viewModel.getUser().getToken();
        }

        if (carts.isEmpty()){
            relativeLayout.setVisibility(View.GONE);
        }
        else {
            relativeLayout.setVisibility(View.VISIBLE);
        }

        updateTotalPrice();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalPrice > 0){
                    if (!Utils.hasInternetConnection(requireContext())){
                        Snackbar.make(requireView(), getString(R.string.internetga_ulanmagan), Snackbar.LENGTH_SHORT).show();
                        return;
                    }

                    if (Utils.isUserSeller()){
                        Navigation.findNavController(requireView())
                                .navigate(CartFragmentDirections.actionCartFragmentToCartSelectCustomerFragment(totalPrice));
                    }
                    else {
                        Navigation.findNavController(requireView())
                                .navigate(CartFragmentDirections.actionCartFragmentToCartSelectSellerFragment(totalPrice));
                    }

                }

            }
        });




    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
//        super.onPrepareOptionsMenu(menu);
        menu.clear();
    }


    private void insertCart(Cart cart, int position) {

        viewModel.insertCart(cart)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onComplete() {
                        adapter.changeCartCount(cart, position);
                        updateTotalPrice();
//                        Snackbar.make(requireView(), getString(R.string.mahsulot_savatchaga_qoshildi), Snackbar.LENGTH_SHORT)
//                                .show();
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Log.e("TAG", "onError: insertCart ", e);
                    }
                });
    }

    @Override
    public void onItemClick(Cart cart) {

    }

    @Override
    public void onItemCounterClick(Cart cart, int position) {
        insertCart(cart, position);
    }

    @Override
    public void deleteItem(Cart cart, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(R.string.savatchadan_malumot_ochirish);
        builder.setMessage(R.string.siz_rostdan_ushbu_malumotni_ochir);
        builder.setPositiveButton(getString(R.string.ha), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteSingleCartItem(cart, position);
            }
        }).setNegativeButton(getString(R.string.yoq), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).create().show();

        Log.d("CART", "onDelete: " + cart.getId());

    }

    private void deleteSingleCartItem(Cart cart, int position) {
        viewModel.deleteCart(cart)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onComplete() {
                        adapter.deleteItem(position);
                        updateTotalPrice();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });

    }

    private void updateTotalPrice(){
        viewModel.getTotalPriceInCart()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Integer>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull Integer integer) {
                       String price = getString(R.string.umumiy_narx)+"\n"+ Utils.moneyToDecimal(integer);
                        tvTotalPrice.setText(price+getString(R.string.som));
                        totalPrice = integer;
                        if (integer == 0){
                            relativeLayout.setVisibility(View.GONE);
                        }
                        else {
                            relativeLayout.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        tvTotalPrice.setText(Utils.moneyToDecimal(0)+getString(R.string.som));
                        totalPrice = 0;
                    }
                });
    }



}