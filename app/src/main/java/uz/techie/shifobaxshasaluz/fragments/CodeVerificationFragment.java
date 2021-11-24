package uz.techie.shifobaxshasaluz.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.chaos.view.PinView;
import com.github.pinball83.maskededittext.MaskedEditText;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.uzairiqbal.circulartimerview.CircularTimerListener;
import com.uzairiqbal.circulartimerview.CircularTimerView;
import com.uzairiqbal.circulartimerview.TimeFormatEnum;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import uz.nisd.asalsavdosi.R;
import uz.techie.shifobaxshasaluz.fragments.CodeVerificationFragmentArgs;
import uz.techie.shifobaxshasaluz.fragments.CodeVerificationFragmentDirections;
import uz.techie.shifobaxshasaluz.Utils;
import uz.techie.shifobaxshasaluz.models.HoneyResponse;
import uz.techie.shifobaxshasaluz.network.ApiClient;
import uz.techie.shifobaxshasaluz.room.HoneyViewModel;


public class CodeVerificationFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    String TAG = "CodeVerificationFragment";
    HoneyViewModel viewModel;



    PinView pinView;
    FloatingActionButton actionButton;
    String codebySystem = "";
    String phoneNumber;
    TextView tvDetails;
    TextView tvDidnotReceiveCode;
    TextView tvTimer;
    KProgressHUD progressHUD;
    int responseStatus = 200;

    NestedScrollView scrollView;
    TextInputLayout layoutName;
    AppCompatEditText etName;
    RadioGroup radioGroup;
    MaterialButton btnDate;
    String sDate = "";

    CheckBox checkBox;
    TextInputLayout phoneInputLayout;
    MaskedEditText phoneEt;

    CompositeDisposable disposable;


    CircularTimerView progressBar;

    private CountDownTimer timer;
    private static final long START_TIME = 120000;
    private long leftTime = START_TIME;
    private boolean isTimerRunning;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_code_verification, container, false);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(HoneyViewModel.class);

        progressHUD = KProgressHUD.create(requireContext());
        progressBar = view.findViewById(R.id.confirm_progress_circular);
        disposable = new CompositeDisposable();

        scrollView = view.findViewById(R.id.confirm_scrollview);
        layoutName = view.findViewById(R.id.register_name_layout);
        etName = view.findViewById(R.id.register_name);
        radioGroup = view.findViewById(R.id.register_gender_radioGroup);
        btnDate = view.findViewById(R.id.register_date);

        checkBox = view.findViewById(R.id.register_checkbox);
        phoneInputLayout = view.findViewById(R.id.register_phone_inputlayout);
        phoneEt = view.findViewById(R.id.register_phone_edittext);


        pinView = view.findViewById(R.id.pinView);
//        tvTimer = view.findViewById(R.id.confirm_tv_time);
        tvDetails = view.findViewById(R.id.confirm_detail_tv);
        actionButton = view.findViewById(R.id.confirm_next_btn);
        tvDidnotReceiveCode = view.findViewById(R.id.confirm_didnt_receive_code);
        tvDidnotReceiveCode.setPaintFlags(tvDidnotReceiveCode.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        if (getArguments() != null){
            phoneNumber =  CodeVerificationFragmentArgs.fromBundle(getArguments()).getPhoneNumber();
        }
        tvDetails.setText(getString(R.string.tasdiqlash_kodi_ushbu_raqamga)+" "+phoneNumber);
        phoneNumber = phoneNumber.replace("+","");

        checkPhone();



        tvDidnotReceiveCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPhone();
                tvDidnotReceiveCode.setVisibility(View.GONE);
            }
        });


        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: status: "+responseStatus);
                String code = pinView.getText().toString();
                if (code.length()>=6){
                    if (responseStatus == 200){
                        registerUser();
                    }
                    else {
                        requestCheckPhone(code);
                    }
                }
            }
        });



        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDate();
            }
        });


        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    phoneInputLayout.setVisibility(View.VISIBLE);
                }
                else {
                    phoneInputLayout.setVisibility(View.GONE);
                }
            }
        });



    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    private void registerUser() {
        String code = Objects.requireNonNull(pinView.getText()).toString();
        String name = Objects.requireNonNull(etName.getText()).toString();
        String invitationPhone = "998"+ phoneEt.getUnmaskedText();
        int gender = 1;
        if (radioGroup.getCheckedRadioButtonId() == R.id.radio_male){
            gender = 1;
        }
        else {
            gender = 2;
        }

        if (name.isEmpty()){
            layoutName.setError(getString(R.string.iltimos_ism_va_familiya));
            return;
        }
        else {
            layoutName.setError(null);
        }

        if (sDate.isEmpty()){
            Log.d(TAG, "registerUser: "+sDate);
            btnDate.setStrokeColorResource(R.color.red);
            btnDate.setIconResource(R.drawable.ic_baseline_error_24);
            btnDate.setIconTintResource(R.color.red);
            btnDate.setIconGravity(MaterialButton.ICON_GRAVITY_END);
            btnDate.setStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            return;
        }
        else {
            btnDate.setText(sDate);
            btnDate.setIcon(null);
            btnDate.setStrokeColorResource(R.color.black);
        }

        if (!checkBox.isChecked()){
            invitationPhone = "0";
        }

        if (checkBox.isChecked() && invitationPhone.length()<12){
            phoneInputLayout.setError(getString(R.string.mavjud_telefon_raqami));
            return;
        }
        else {
            phoneInputLayout.setError(null);
        }


        requestRegisterUser(code, name, gender, sDate, invitationPhone);



    }

    private void getDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                AlertDialog.THEME_HOLO_LIGHT,
                this,
                year,
                month,
                day
        );
        DatePicker datePicker = datePickerDialog.getDatePicker();
        datePicker.setMaxDate(System.currentTimeMillis());

        datePickerDialog.show();



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
        }, 2, TimeFormatEnum.MINUTES, 2);



// To start timer

        progressBar.startTimer();
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        Date date = calendar.getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        sDate = dateFormat.format(date);
        btnDate.setText(sDate);

        Log.d(TAG, "onDateSet: "+sDate);

    }


    private void checkPhone(){
        timeProgress();
        ApiClient.getApiInterface().checkPhoneNumber(phoneNumber)
                .toObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HoneyResponse>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull HoneyResponse honeyResponse) {
                        Log.d(TAG, "onNext: "+honeyResponse.getMsg());
                        Log.d(TAG, "onNext: "+honeyResponse.getStatus());
                        responseStatus = honeyResponse.getStatus();
                        if (honeyResponse.getStatus() == 200){
                            scrollView.setVisibility(View.VISIBLE);
                        }
                        else {
                            scrollView.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Log.e(TAG, "onError: ", e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }


    private void requestCheckPhone(String code) {
        ApiClient.getApiInterface().sendOtpCode(phoneNumber, code)
                .toObservable()
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<HoneyResponse>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull HoneyResponse honeyResponse) {
                        Log.d(TAG, "onNext: login "+honeyResponse.getMsg());
                        if (honeyResponse.getStatus() == 200){
                            insertUser(honeyResponse);
                        }
                        else if (honeyResponse.getStatus() == 404){
                            Snackbar.make(requireView(), getString(R.string.sms_kod_xato_kiritldi), Snackbar.LENGTH_SHORT)
                                    .show();
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Log.e(TAG, "onError: login ", e);
                        Utils.hideProgressBar(progressHUD);
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void requestRegisterUser(String code, String name, int gender, String date, String invitationPhone){
        Log.d(TAG, "requestRegisterUser: name: "+ name);
        Log.d(TAG, "requestRegisterUser: code: "+ code);
        Log.d(TAG, "requestRegisterUser: gender: "+ gender);
        Log.d(TAG, "requestRegisterUser: date: "+ date);
        Log.d(TAG, "requestRegisterUser: phoneNumber: "+ phoneNumber);
        Log.d(TAG, "requestRegisterUser: invitationPhone: "+ invitationPhone);


        Utils.showProgressBar(progressHUD);
        ApiClient.getApiInterface().registerUser(phoneNumber, code, name, date, gender, invitationPhone)
                .toObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HoneyResponse>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull HoneyResponse honeyResponse) {
                        Log.d(TAG, "onNext: register: "+honeyResponse.getMsg());
                        if (honeyResponse.getStatus() == 200){
                            insertUser(honeyResponse);

                        }
                        else if (honeyResponse.getStatus() == 404){
                            Utils.hideProgressBar(progressHUD);
                            Snackbar.make(requireView(), getString(R.string.sms_kod_xato_kiritldi), Snackbar.LENGTH_SHORT)
                                    .show();
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Utils.hideProgressBar(progressHUD);
                        Log.e(TAG, "onError: register: ", e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void insertUser(HoneyResponse honeyResponse) {
        viewModel.insertUser(honeyResponse.getUser())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onComplete() {

                        if (honeyResponse.getUser().getKim().equals("True")){
                            Utils.setUserAsSeller(true);
                            Log.d(TAG, "onNext: Kim: True");
                        }
                        else {
                            Log.d(TAG, "onNext: Kim: False");
                            Utils.setUserAsSeller(false);
                        }



                        Utils.setUserLogin(true);
                        Utils.hideProgressBar(progressHUD);
                        Navigation.findNavController(requireView())
                                .navigate(CodeVerificationFragmentDirections.actionCodeVerificationFragmentToHomeFragment());
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Log.e(TAG, "onError: insertuser ", e);
                        Utils.hideProgressBar(progressHUD);
                    }
                });
    }






    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }




}
