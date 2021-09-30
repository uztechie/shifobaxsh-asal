package uz.techie.shifobaxshasaluz.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputLayout;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uz.nisd.asalsavdosi.R;
import uz.techie.shifobaxshasaluz.fragments.CartSelectCustomerFragmentDirections;
import uz.techie.shifobaxshasaluz.fragments.CartSelectSellerFragmentArgs;
import uz.techie.shifobaxshasaluz.Constants;
import uz.techie.shifobaxshasaluz.CustomDialog;
import uz.techie.shifobaxshasaluz.Utils;
import uz.techie.shifobaxshasaluz.adapter.SellerDropDownAdapter;
import uz.techie.shifobaxshasaluz.models.Cart;
import uz.techie.shifobaxshasaluz.models.Order;
import uz.techie.shifobaxshasaluz.models.OrderResponse;
import uz.techie.shifobaxshasaluz.models.RequestOrder;
import uz.techie.shifobaxshasaluz.models.Seller;
import uz.techie.shifobaxshasaluz.models.User;
import uz.techie.shifobaxshasaluz.network.ApiClient;
import uz.techie.shifobaxshasaluz.room.HoneyViewModel;


public class CartSelectCustomerFragment extends Fragment implements CustomDialog.CustomDialogInterface {
    KProgressHUD progressHUD;
    TextView tvTotalPrice, tvTotalPricePay, tvTotalBonus, tvTotalBonusPay, tvIncomingBonus;
    EditText etBonusInput;
    CheckBox checkBox;

    CustomDialog customDialog;

    Button btnOrder;
    AutoCompleteTextView tvSellers;
    TextInputLayout bonusLayout, sellersLayout;
    SellerDropDownAdapter adapter;

    HoneyViewModel viewModel;
    String token = "";
    CompositeDisposable disposable;

    private boolean isInternetBuy = false;
    private int customerId = 0;
    private int mPosition = -1;


    int totalPrice = 0;
    int bonus = 0;
    int bonusPay = 0;
    int code = 0;
    String customerName = "";
    String phone = "";
    int affiliate = -1;
    int userId = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_cart_select_seller, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        disposable = new CompositeDisposable();
        progressHUD = new KProgressHUD(requireContext());
        viewModel = ViewModelProviders.of(this).get(HoneyViewModel.class);

        customDialog = new CustomDialog(requireActivity(), CartSelectCustomerFragment.this);

        Utils.showProgressBar(progressHUD);

        checkBox = view.findViewById(R.id.cart_select_buy_via_internet);
        tvIncomingBonus = view.findViewById(R.id.cart_select_incoming_bonus);
        tvTotalPrice = view.findViewById(R.id.cart_select_totalPrice);
        tvTotalPricePay = view.findViewById(R.id.cart_select_totalPrice_needed_pay);
        tvTotalBonus = view.findViewById(R.id.cart_select_totalBonus);
        tvTotalBonusPay = view.findViewById(R.id.cart_select_totalBonus_needed_pay);
        etBonusInput = view.findViewById(R.id.cart_select_bonus_et);
        btnOrder = view.findViewById(R.id.cart_select_order_btn);
        tvSellers = view.findViewById(R.id.cart_select_seller);
        bonusLayout = view.findViewById(R.id.cart_select_bonus_tv_layout);
        sellersLayout = view.findViewById(R.id.cart_select_seller_layout);

        sellersLayout.setHint(R.string.haridorni_tanlang);

        checkBox.setVisibility(View.GONE);

        int incomingBonus = viewModel.getIncomingBonus();
        tvIncomingBonus.setText("+"+ Utils.moneyToDecimal(incomingBonus)+getString(R.string.som));

        if (viewModel.getUser() != null) {
            token = viewModel.getUser().getToken();
            affiliate = viewModel.getUser().getAffilate_id();
//            bonus = viewModel.getUser().getBonus();
            userId = viewModel.getUser().getId();
        }


        if (getArguments() != null) {
            totalPrice = CartSelectSellerFragmentArgs.fromBundle(getArguments()).getTotalPrice();
        }
        tvTotalPrice.setText(Utils.moneyToDecimal(totalPrice)+getString(R.string.som));
        tvTotalBonus.setText(Utils.moneyToDecimal(bonus)+getString(R.string.som));

        bonusInputStatus();

        etBonusInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    int bonusFromInput = Integer.parseInt(String.valueOf(s));
                    Log.d("TAG", "onTextChanged: "+s);

                    if (bonus<totalPrice/2){
                        if (bonus<bonusFromInput){
                            Toast.makeText(requireContext(), getString(R.string.siz_mavjud_bonusdan_koproq_kiritdingiz), Toast.LENGTH_SHORT).show();
                            etBonusInput.setText(String.valueOf(bonus));
                            bonusFromInput = bonus;
                        }
                        bonusPay = bonusFromInput;
                        tvTotalPricePay.setText(Utils.moneyToDecimal(totalPrice - bonusPay)+getString(R.string.som));
                        tvTotalBonusPay.setText(Utils.moneyToDecimal(bonusPay)+getString(R.string.som));

                    }
                    else {
                        if (totalPrice / 2 < bonusFromInput) {
                            bonusFromInput = totalPrice/2;
                            etBonusInput.setText(String.valueOf(bonusFromInput));
                            Toast.makeText(requireContext(), getString(R.string.iltimos_bonus_miqdorini_50_kam), Toast.LENGTH_SHORT).show();
                        }
                        else {
                            bonusPay = bonusFromInput;
                            tvTotalPricePay.setText(Utils.moneyToDecimal(totalPrice - bonusPay)+getString(R.string.som));
                            tvTotalBonusPay.setText(Utils.moneyToDecimal(bonusPay)+getString(R.string.som));
                        }
                    }






                } else {
                    bonusPay = 0;
                    tvTotalPricePay.setText(Utils.moneyToDecimal(totalPrice)+getString(R.string.som));
                    tvTotalBonusPay.setText(Utils.moneyToDecimal(bonusPay)+getString(R.string.som));
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        setSellerDropDown();



        tvSellers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                customerId = ((Seller) (tvSellers.getAdapter().getItem(position))).getId();
                customerName = ((Seller) (tvSellers.getAdapter().getItem(position))).getName();
                phone = ((Seller) (tvSellers.getAdapter().getItem(position))).getPhone();
                bonus = ((Seller) (tvSellers.getAdapter().getItem(position))).getBonus();
                tvTotalBonus.setText(Utils.moneyToDecimal(bonus)+getString(R.string.som));
                bonusInputStatus();
                etBonusInput.setText("");

                mPosition = position;
                Log.d("TAG", "onItemClick: " + customerId);

                sellersLayout.setError(null);

//                InputMethodManager in = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);

            }
        });


        tvSellers.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("TAG", "onTextChanged: " + s);
                customerId = -1;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("TAG", "onClick: " + generateCode());
                if (customerId == 0) {
                    sellersLayout.setError(getString(R.string.iltimos_haridorni_tanlang));
                } else {
                    sellersLayout.setError(null);
                    requestSendCode();
                }


            }
        });


        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Log.d("TAG", "handleOnBackPressed: ");
                Utils.hideProgressBar(progressHUD);
                Navigation.findNavController(view)
                        .popBackStack();
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);


    }


    @Override
    public void onStart() {
        super.onStart();
        checkInternet();
    }


    private void checkInternet() {
        if (!Utils.hasInternetConnection(requireContext())){
            customDialog.title(getString(R.string.internet));
            customDialog.message(getString(R.string.internetga_ulanmagan));
            customDialog.isSuccess(false);
            customDialog.status(Constants.GO_BACK);
            customDialog.show();

        }
        else {
            loadUserProfile();
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    private void bonusInputStatus() {
        if (bonus <= 0) {
            etBonusInput.setEnabled(false);
        } else {
            etBonusInput.setEnabled(true);
        }
    }

    private void requestSendOrder() {

        Utils.showProgressBar(progressHUD);
        List<Order> orders = new ArrayList<>();
        for (Cart cart : viewModel.getCarts()) {
            Order order = new Order();
            order.setProduct_name(cart.getProduct_name());
            order.setQuantity(cart.getQuantity());
            order.setProduct(cart.getProduct());
            order.setBonus(cart.getBonus());
            order.setPrice(cart.getPrice());
            order.setAff_bonus(cart.getAff_bonus());
            orders.add(order);
        }

        RequestOrder ordersItem = new RequestOrder();
        ordersItem.setOperations_item(orders);
        ordersItem.setPay_bonus(bonusPay);
        ordersItem.setStatus(1);
        ordersItem.setOp_type(2);
        ordersItem.setSeller_id(userId);
        ordersItem.setCustomer(customerId);
        ordersItem.setBonus_type(4);
        ordersItem.setAffilate_id(affiliate);


        Log.d("TAG", "requestSendOrder: token " + token);

        Call<OrderResponse> call = ApiClient.getApiInterface().sendOrderList(token, Constants.HEADER_KEY_ORDER_SMS, ordersItem);
        call.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 200) {
                        deleteAllCarts();
                        customDialog.title(getString(R.string.buyurtma));
                        customDialog.message(getString(R.string.sizing_buyurtma_qab_qilindi));
                        customDialog.isSuccess(true);
                        customDialog.status(Constants.GO_HOME);
                        customDialog.show();

                    }
                    else {
                        customDialog.title(getString(R.string.buyurtma));
                        customDialog.message(response.body().getMessage());
                        customDialog.isSuccess(false);
                        customDialog.status(Constants.GO_NOWHERE);
                        customDialog.show();
                    }
                }

                Log.d("TAG", "onResponse: send order " + response.toString());
                Utils.hideProgressBar(progressHUD);
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                Utils.hideProgressBar(progressHUD);
                Log.e("TAG", "onFailure: send order ", t);

                customDialog.title(getString(R.string.buyurtma));
                customDialog.message(t.getMessage());
                customDialog.isSuccess(false);
                customDialog.status(Constants.GO_NOWHERE);
                customDialog.show();
            }
        });


    }

    private void deleteAllCarts() {
        viewModel.deleteAllCarts()
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    private void requestSendCode() {
        Utils.showProgressBar(progressHUD);
        code = generateCode();

        Call<OrderResponse> call = ApiClient.getApiInterface().sendSmsCodeForOrder(Constants.HEADER_KEY_ORDER_SMS, customerId, code);
        call.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 200) {
                        Utils.hideProgressBar(progressHUD);
                        Navigation.findNavController(requireView())
                                .navigate(CartSelectCustomerFragmentDirections.actionCartSelectCustomerFragmentToConfirmOrderFragment(bonusPay, code, customerId, customerName, phone));


                    }
                    else {
                        Utils.hideProgressBar(progressHUD);
                        customDialog.title(getString(R.string.buyurtma));
                        customDialog.message(response.body().getMessage());
                        customDialog.isSuccess(false);
                        customDialog.status(Constants.GO_NOWHERE);
                        customDialog.show();
                    }
                }
                else {
                    Utils.hideProgressBar(progressHUD);
                }

                Log.d("TAG", "onResponse: code " + response.toString());
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                Utils.hideProgressBar(progressHUD);
                Log.e("TAG", "onFailure: code ", t);

                customDialog.title(getString(R.string.buyurtma));
                customDialog.message(t.getMessage());
                customDialog.isSuccess(false);
                customDialog.status(Constants.GO_NOWHERE);
                customDialog.show();
            }
        });
    }

    private int generateCode() {
        return ThreadLocalRandom.current().nextInt(100000, 999999);
    }

    private void setSellerDropDown() {
        token = "token " + token;
        viewModel.getCustomersLive().observe(getViewLifecycleOwner(), new Observer<List<Seller>>() {
            @Override
            public void onChanged(List<Seller> sellers) {
                if (sellers.isEmpty()) {
                    viewModel.loadSellers(token);
                } else {
                    adapter = new SellerDropDownAdapter(requireContext(), sellers);
                    adapter.notifyDataSetChanged();
                    tvSellers.setAdapter(adapter);
                }
            }
        });
    }


    public void loadUserProfile() {
        Utils.showProgressBar(progressHUD);

        ApiClient.getApiInterface().loadUserProfile(token)
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new io.reactivex.rxjava3.core.Observer<User>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull User user) {
                        insertUserData(user);
                        Log.d("TAG", "onNext: profile " + user.getFirst_name());
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Log.e("TAG", "onError: profile ", e);
                        Utils.hideProgressBar(progressHUD);

                        customDialog.title(getString(R.string.profil));
                        customDialog.message(e.getMessage());
                        customDialog.isSuccess(false);
                        customDialog.status(Constants.GO_BACK);
                        customDialog.show();

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void insertUserData(User user) {
        viewModel.insertUser(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onComplete() {
                        Utils.hideProgressBar(progressHUD);
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Utils.hideProgressBar(progressHUD);

                        customDialog.title(getString(R.string.profil));
                        customDialog.message(e.getMessage());
                        customDialog.isSuccess(false);
                        customDialog.status(Constants.GO_BACK);
                        customDialog.show();
                    }
                });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }


    @Override
    public void onCustomDialogBtnClick(int status) {

        if (status == Constants.GO_BACK){
            Navigation.findNavController(requireView()).popBackStack();
        }
        else if (status == Constants.GO_HOME){
            Navigation.findNavController(requireView())
                .navigate(CartSelectCustomerFragmentDirections.actionGlobalHomeFragment());
        }
    }
}