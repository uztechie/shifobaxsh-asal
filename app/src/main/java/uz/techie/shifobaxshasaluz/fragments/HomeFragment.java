package uz.techie.shifobaxshasaluz.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import uz.nisd.asalsavdosi.R;
import uz.techie.shifobaxshasaluz.fragments.HomeFragmentDirections;
import uz.techie.shifobaxshasaluz.Constants;
import uz.techie.shifobaxshasaluz.CustomDialog;
import uz.techie.shifobaxshasaluz.Utils;
import uz.techie.shifobaxshasaluz.adapter.ProductAdapter;
import uz.techie.shifobaxshasaluz.models.Cart;
import uz.techie.shifobaxshasaluz.models.Product;
import uz.techie.shifobaxshasaluz.room.HoneyViewModel;


public class HomeFragment extends Fragment implements ProductAdapter.ProductInterface, CustomDialog.CustomDialogInterface {
    KProgressHUD progressHUD;
    HoneyViewModel viewModel;
    RecyclerView recyclerView;
    ProductAdapter adapter;
    ProgressBar progressBar;
    CompositeDisposable disposable;
    CustomDialog dialog;
    private static final String TAG = "HomeFragment";
    List<Product> products = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressHUD = new KProgressHUD(requireContext());
        viewModel = ViewModelProviders.of(this).get(HoneyViewModel.class);
        disposable = new CompositeDisposable();

        dialog = new CustomDialog(requireActivity(), this);

        progressBar = view.findViewById(R.id.home_progressbar);
        recyclerView = view.findViewById(R.id.home_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));

        adapter = new ProductAdapter(requireContext(), this, products);
        recyclerView.setAdapter(adapter);


        viewModel.getProducts().observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                    progressBar.setVisibility(View.GONE);
                    Log.d(TAG, "onChanged: "+products.size());
                    adapter.submitList(products);

            }
        });




    }


    @Override
    public void onStart() {
        super.onStart();
        checkInternet();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.loadProducts();
    }

    @Override
    public void onItemClick(Product product) {
        Navigation.findNavController(requireView())
                .navigate(HomeFragmentDirections.actionHomeFragmentToProductDetailsFragment(product.getId(), product.getName()));
    }

    @Override
    public void addItem(Product product) {
//        if (!Utils.doesUserLogin() || viewModel.getUser()==null){
//            Navigation.findNavController(requireView())
//                    .navigate(HomeFragmentDirections.actionGlobalPhoneInputFragment());
//            return;
//        }
        insertCart(product);

    }

    private void checkInternet(){
        if (!Utils.hasInternetConnection(requireContext())){
            dialog.title(getString(R.string.internet));
            dialog.message(getString(R.string.internetga_ulanmagan));
            dialog.isSuccess(false);
            dialog.status(Constants.GO_BACK);
            dialog.show();

        }
    }

    private void insertCart(Product product) {
        Cart cart = new Cart();
        cart.setId(product.getId());
        cart.setProduct(product.getId());
        cart.setAff_bonus(product.getAfilate_bonus());
        cart.setBonus(product.getBonus_amount());
        cart.setPrice(product.getPrice());
        cart.setQuantity(1);
        cart.setProduct_name(product.getName());
        cart.setDesc(product.getDesc());
        cart.setPhoto(product.getPhoto());

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
                        Snackbar.make(requireView(), getString(R.string.mahsulot_savatchaga_qoshildi), Snackbar.LENGTH_SHORT)
                                .show();
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Log.e("TAG", "onError: insertCart ", e);
                    }
                });
    }


    @Override
    public void onCustomDialogBtnClick(int status) {
        if (status == Constants.GO_BACK){
            dialog.hide();
        }
    }
}