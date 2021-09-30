package uz.techie.shifobaxshasaluz.fragments;

import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.chaos.view.PinView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.uzairiqbal.circulartimerview.CircularTimerListener;
import com.uzairiqbal.circulartimerview.CircularTimerView;
import com.uzairiqbal.circulartimerview.TimeFormatEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uz.nisd.asalsavdosi.R;
import uz.techie.shifobaxshasaluz.fragments.ConfirmOrderFragmentArgs;
import uz.techie.shifobaxshasaluz.fragments.ConfirmOrderFragmentDirections;
import uz.techie.shifobaxshasaluz.Constants;
import uz.techie.shifobaxshasaluz.CustomDialog;
import uz.techie.shifobaxshasaluz.Utils;
import uz.techie.shifobaxshasaluz.models.Cart;
import uz.techie.shifobaxshasaluz.models.Order;
import uz.techie.shifobaxshasaluz.models.OrderResponse;
import uz.techie.shifobaxshasaluz.models.RequestOrder;
import uz.techie.shifobaxshasaluz.network.ApiClient;
import uz.techie.shifobaxshasaluz.room.HoneyViewModel;


public class ConfirmOrderFragment extends Fragment implements CustomDialog.CustomDialogInterface {

    String TAG = "CodeVerificationFragment";
    HoneyViewModel viewModel;

    CustomDialog customDialog;

    PinView pinView;
    String codebySystem = "";
    TextView tvDetails;
    TextView tvDidnotReceiveCode;
    KProgressHUD progressHUD;
    int responseStatus = 200;
    MaterialButton btnConfirm;

    int sellerId = -1;
    int bonusPay = 0;
    int code = 0;
    int userId = 0;
    int customerId = 0;
    int affiliate = 0;
    String sellerName;
    String sellerPhone;
    String token;


    CompositeDisposable disposable;


    CircularTimerView progressBar;

    private CountDownTimer timer;
    private static final long START_TIME = 120000;
    private long leftTime = START_TIME;
    private boolean isTimerRunning;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_confirm_order, container, false);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(HoneyViewModel.class);
        customDialog = new CustomDialog(requireActivity(), ConfirmOrderFragment.this);

        if (getArguments() != null){
            sellerId = ConfirmOrderFragmentArgs.fromBundle(getArguments()).getSellerId();
            code = ConfirmOrderFragmentArgs.fromBundle(getArguments()).getCode();
            bonusPay = ConfirmOrderFragmentArgs.fromBundle(getArguments()).getBonusPay();
            sellerName = ConfirmOrderFragmentArgs.fromBundle(getArguments()).getSellerName();
            sellerPhone = ConfirmOrderFragmentArgs.fromBundle(getArguments()).getSellerPhone();
        }

        if (viewModel.getUser() != null){
            affiliate = viewModel.getUser().getAffilate_id();
            userId = viewModel.getUser().getId();
            token = viewModel.getUser().getToken();
        }


        progressHUD = KProgressHUD.create(requireContext());
        progressBar = view.findViewById(R.id.confirm_progress_circular);
        disposable = new CompositeDisposable();


        btnConfirm = view.findViewById(R.id.confirm_order_btn_conf);
        pinView = view.findViewById(R.id.pinView);
//        tvTimer = view.findViewById(R.id.confirm_tv_time);
        tvDetails = view.findViewById(R.id.confirm_detail_tv);
        tvDidnotReceiveCode = view.findViewById(R.id.confirm_didnt_receive_code);
        tvDidnotReceiveCode.setPaintFlags(tvDidnotReceiveCode.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        tvDetails.setText(getString(R.string.tasdiqlash_kodi_ushbu_raqamga)+" "+sellerName+"\n"+sellerPhone);


        if (!Utils.isUserSeller()){
            customerId = userId;
            sellerId = sellerId;

        }
        else {
            customerId = sellerId;
            sellerId = userId;
        }


        timeProgress();


        tvDidnotReceiveCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestSendCode();
                tvDidnotReceiveCode.setVisibility(View.GONE);
            }
        });


        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: status: "+responseStatus);
                String codeFromUser = pinView.getText().toString();
                if (codeFromUser.length()>=6){
                    if (String.valueOf(code).equals(codeFromUser)){
                        requestSendOrder();
                    }
                    else {
                        Snackbar.make(requireView(), getString(R.string.sms_kod_xato_kiritldi), Snackbar.LENGTH_SHORT)
                                .show();
                    }
                }
            }
        });





    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    private void requestSendOrder() {

        Utils.showProgressBar(progressHUD);
        List<Order> orders = new ArrayList<>();
        for (Cart cart:viewModel.getCarts()){
            Order order = new Order();
            order.setProduct_name(cart.getProduct_name());
            order.setQuantity(cart.getQuantity());
            order.setProduct(cart.getProduct());
            order.setBonus(cart.getBonus());
            order.setPrice(cart.getPrice());
            order.setAff_bonus(cart.getAff_bonus());
            orders.add(order);
        }


        Log.d(TAG, "requestSendOrder: bonus: "+bonusPay);

        RequestOrder ordersItem = new RequestOrder();
        ordersItem.setOperations_item(orders);
        ordersItem.setPay_bonus(bonusPay);
        ordersItem.setStatus(4);
        ordersItem.setOp_type(2);
        ordersItem.setSeller_id(sellerId);
        ordersItem.setCustomer(customerId);
        ordersItem.setBonus_type(4);
        ordersItem.setAffilate_id(affiliate);


        Log.d("TAG", "requestSendOrder: token "+token);
        token = "token "+token;

        Call<OrderResponse> call = ApiClient.getApiInterface().sendOrderList(token, Constants.HEADER_KEY_ORDER_SMS, ordersItem);
        call.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if (response.isSuccessful() && response.body() != null){
                    if (response.body().getStatus() == 200){
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

                Log.d("TAG", "onResponse: send order "+ response.toString());
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




    private void startTimer(){
        timer = new CountDownTimer(leftTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                leftTime = millisUntilFinished;
                updateTimerText();
            }

            @Override
            public void onFinish() {
                tvDidnotReceiveCode.setVisibility(View.VISIBLE);
                leftTime = START_TIME;

            }
        }.start();

        isTimerRunning = true;
    }

    private void updateTimerText() {
        int minute = (int) (leftTime/1000)/60;
        int second = (int) (leftTime/1000)%60;
        String timeLeftFormatted = String.format(Locale.ENGLISH, "%02d:%02d", minute, second);
//        tvTimer.setText(timeLeftFormatted);
    }

    private void pauseTimer(){
        timer.cancel();
    }

    private void timeProgress(){
        progressBar.setProgress(0);
        progressBar.setCircularTimerListener(new CircularTimerListener() {
            @Override
            public String updateDataOnTick(long remainingTimeInMs) {
                return String.valueOf((int)Math.ceil((remainingTimeInMs / 1000.f)));
            }

            @Override
            public void onTimerFinished() {
                progressBar.setPrefix("");
                progressBar.setSuffix("");
                progressBar.setText("Time over");

                tvDidnotReceiveCode.setVisibility(View.VISIBLE);
            }
        }, 5, TimeFormatEnum.MINUTES, 5);



// To start timer

        progressBar.startTimer();
    }




    private void requestSendCode() {
        Utils.showProgressBar(progressHUD);
        code = generateCode();

        Call<OrderResponse> call = ApiClient.getApiInterface().sendSmsCodeForOrder(Constants.HEADER_KEY_ORDER_SMS, sellerId, code);
        call.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 200) {
                        Utils.hideProgressBar(progressHUD);
                        timeProgress();
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
                    .navigate(ConfirmOrderFragmentDirections.actionGlobalHomeFragment());
        }
    }
}
