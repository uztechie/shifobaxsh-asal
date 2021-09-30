package uz.techie.shifobaxshasaluz.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kaopiz.kprogresshud.KProgressHUD;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import uz.nisd.asalsavdosi.R;
import uz.techie.shifobaxshasaluz.Constants;
import uz.techie.shifobaxshasaluz.CustomDialog;
import uz.techie.shifobaxshasaluz.adapter.FriendAdapter;
import uz.techie.shifobaxshasaluz.adapter.HistoryAdapter;
import uz.techie.shifobaxshasaluz.models.Friend;
import uz.techie.shifobaxshasaluz.models.History;
import uz.techie.shifobaxshasaluz.models.Order;
import uz.techie.shifobaxshasaluz.network.ApiClient;
import uz.techie.shifobaxshasaluz.network.ApiInterface;
import uz.techie.shifobaxshasaluz.room.HoneyViewModel;


public class FriendFragment extends Fragment implements CustomDialog.CustomDialogInterface {
    HoneyViewModel viewModel;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    List<Friend> friends = new ArrayList<>();
    FriendAdapter adapter;

    private String token;

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
        return inflater.inflate(R.layout.fragment_friend, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dialog = new CustomDialog(requireActivity(), FriendFragment.this);

        viewModel = ViewModelProviders.of(this).get(HoneyViewModel.class);
        adapter = new FriendAdapter(friends);
        progressBar = view.findViewById(R.id.friend_progressbar);
        recyclerView = view.findViewById(R.id.friend_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);



        if (viewModel.getUser()!=null){
            token = viewModel.getUser().getToken();
        }
        token = "token "+token;

        loadFriends();



    }

    private void loadFriends() {
        ApiClient.getApiInterface().loadFriends(token)
                .toObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Friend>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull List<Friend> friends) {
                        progressBar.setVisibility(View.GONE);
                        if (!friends.isEmpty()){
                            adapter.submitList(friends);
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        dialog.title(getString(R.string.profil));
                        dialog.message(e.getMessage());
                        dialog.isSuccess(false);
                        dialog.status(Constants.GO_BACK);
                        dialog.show();

                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onCustomDialogBtnClick(int status) {

    }

    @Override
    public void onStop() {
        super.onStop();
        disposable.clear();
    }

}