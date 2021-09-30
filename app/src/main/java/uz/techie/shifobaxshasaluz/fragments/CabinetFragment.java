package uz.techie.shifobaxshasaluz.fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.google.android.material.card.MaterialCardView;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import uz.nisd.asalsavdosi.R;
import uz.techie.shifobaxshasaluz.BonusDialog;
import uz.techie.shifobaxshasaluz.fragments.CabinetFragmentDirections;
import uz.techie.shifobaxshasaluz.Constants;
import uz.techie.shifobaxshasaluz.CustomDialog;
import uz.techie.shifobaxshasaluz.Utils;
import uz.techie.shifobaxshasaluz.models.Bonus;
import uz.techie.shifobaxshasaluz.models.User;
import uz.techie.shifobaxshasaluz.network.ApiClient;
import uz.techie.shifobaxshasaluz.room.HoneyViewModel;


public class CabinetFragment extends Fragment implements CustomDialog.CustomDialogInterface {
    private static final String TAG = "CabinetFragment";
    KProgressHUD progressHUD;
    TextView tvName, tvPhone, tvGender, tvBirthdate, tvAffiliate, tvHistory, tvBonus, tvFriends, tvMoreAboutBonus;
    ImageView btnCopy;
    HoneyViewModel viewModel;
    MaterialCardView cardHistory;
    MaterialCardView cardLogout;
    MaterialCardView cardLogin;
    MaterialCardView cardFriend;
    String token = "";

    BonusDialog bonusDialog;

    CompositeDisposable disposable;
    CustomDialog dialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        disposable = new CompositeDisposable();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cabinet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        dialog = new CustomDialog(requireActivity(), CabinetFragment.this);

        progressHUD = new KProgressHUD(requireContext());
        tvMoreAboutBonus = view.findViewById(R.id.cabinet_more_about_bonus);
        tvFriends = view.findViewById(R.id.cabinet_friends);
        tvName = view.findViewById(R.id.cabinet_name);
        tvPhone = view.findViewById(R.id.cabinet_phone);
        tvGender = view.findViewById(R.id.cabinet_gender);
        tvBirthdate = view.findViewById(R.id.cabinet_birthdate);
        tvAffiliate = view.findViewById(R.id.cabinet_affiliate_link);
        tvHistory = view.findViewById(R.id.cabinet_history);
        tvBonus = view.findViewById(R.id.cabinet_bonus);
        btnCopy = view.findViewById(R.id.cabinet_affiliate_copy_btn);
        cardHistory = view.findViewById(R.id.cabinet_card_history);
        cardLogout = view.findViewById(R.id.cabinet_card_logout);
        cardLogin = view.findViewById(R.id.cabinet_card_login);
        cardFriend = view.findViewById(R.id.cabinet_card_friends);

        viewModel = ViewModelProviders.of(this).get(HoneyViewModel.class);

        userState();

        tvBonus.setPaintFlags(tvBonus.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvBonus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(requireView())
                        .navigate(CabinetFragmentDirections.actionCabinetFragmentToBonusFragment());
            }
        });

        tvMoreAboutBonus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bonusDialog = new BonusDialog(requireActivity());
                bonusDialog.init();
                bonusDialog.showProgress();
                loadBonusInfo();
            }
        });

        btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = tvAffiliate.getText().toString();
//                copyClipboard(text);
                shareAffiliateLink(text);
            }
        });

        cardFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(requireView())
                        .navigate(CabinetFragmentDirections.actionCabinetFragmentToFriendFragment());
            }
        });

        cardHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(requireView())
                        .navigate(CabinetFragmentDirections.actionCabinetFragmentToHistoryFragment());
            }
        });

        cardLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setTitle(R.string.chiqish);
                builder.setMessage(R.string.siz_rostdan_chiqmoqchimisiz);
                builder.setPositiveButton(getString(R.string.ha), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Navigation.findNavController(requireView())
                                .navigate(CabinetFragmentDirections.actionGlobalPhoneInputFragment());
                        Utils.setUserAsSeller(false);
                        viewModel.deleteUser();
                        viewModel.deleteAllHistories();
                        Utils.setUserLogin(false);
                    }
                }).setNegativeButton(getString(R.string.yoq), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create().show();





            }
        });

        cardLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(requireView())
                        .navigate(CabinetFragmentDirections.actionGlobalPhoneInputFragment());
            }
        });


    }

    private void userState() {
        if (viewModel.getUser() != null){
            token = viewModel.getUser().getToken();
            cardLogout.setVisibility(View.VISIBLE);
            cardHistory.setVisibility(View.VISIBLE);
            cardLogin.setVisibility(View.GONE);

            User user = viewModel.getUser();
            tvName.setText(user.getFirst_name());
            tvBonus.setText(Utils.moneyToDecimal(user.getBonus()));
            tvBirthdate.setText(user.getBirthday());
            tvPhone.setText(user.getUser());
            tvFriends.setText(user.getFriend()+"");
            tvAffiliate.setText("https://shifobaxshasal.uz/"+user.getId()+"/login");
            if (user.getGender() == 1)
                tvGender.setText(getString(R.string.erkak));
            else
                tvGender.setText(getString(R.string.ayol));
        }
        else {
            cardLogout.setVisibility(View.GONE);
            cardHistory.setVisibility(View.GONE);
            cardLogin.setVisibility(View.VISIBLE);
        }

    }

    public void loadBonusInfo(){
        ApiClient.getApiInterface().loadBonusInfo()
                .toObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.rxjava3.core.Observer<List<Bonus>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull List<Bonus> bonusList) {
                        bonusDialog.hideProgress();
                        bonusDialog.show(bonusList);
                        Log.d(TAG, "onNext: bonusList "+bonusList.size());
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Log.e(TAG, "onError: historyList ", e);
                        bonusDialog.hideProgress();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    @Override
    public void onStart() {
        super.onStart();
        checkInternet();

    }

    private void checkInternet() {
        if (!Utils.hasInternetConnection(requireContext())){
            dialog.title(getString(R.string.internet));
            dialog.message(getString(R.string.internetga_ulanmagan));
            dialog.isSuccess(false);
            dialog.status(Constants.GO_BACK);
            dialog.show();

        }
        else if (viewModel.getUser() != null){
            loadUserProfile();
        }
    }


    public void loadUserProfile() {
        token = "token "+token;
        Log.d("TAG", "loadUserProfile: token: "+token);
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
                        dialog.title(getString(R.string.profil));
                        dialog.message(e.getMessage());
                        dialog.isSuccess(false);
                        dialog.status(Constants.GO_BACK);
                        dialog.show();
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
                        userState();
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Utils.hideProgressBar(progressHUD);
                    }
                });
    }

    private void copyClipboard(String text){
        ClipboardManager clipboardManager = (ClipboardManager) requireActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText(getString(R.string.taklif_qiluvchi_link), text);
        clipboardManager.setPrimaryClip(clipData);
    }

    private void shareAffiliateLink(String link){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.taklif_qiluvchi_link));
        intent.putExtra(Intent.EXTRA_TEXT, link);
        startActivity(Intent.createChooser(intent, getString(R.string.ulashing)));
    }



    @Override
    public void onCustomDialogBtnClick(int status) {
        if (status == Constants.GO_BACK){
            Navigation.findNavController(requireView())
                    .popBackStack();
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        disposable.clear();
    }
}