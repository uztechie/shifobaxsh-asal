package uz.techie.shifobaxshasaluz.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kaopiz.kprogresshud.KProgressHUD;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import uz.nisd.asalsavdosi.R;
import uz.techie.shifobaxshasaluz.adapter.HistoryAdapter;
import uz.techie.shifobaxshasaluz.models.History;
import uz.techie.shifobaxshasaluz.models.Order;
import uz.techie.shifobaxshasaluz.room.HoneyViewModel;


public class HistoryFragment extends Fragment implements HistoryAdapter.HistoryInterface {
    KProgressHUD progressHUD;
    HoneyViewModel viewModel;
    RecyclerView recyclerView;
    HistoryAdapter adapter;
    List<History> histories = new ArrayList<>();
    List<Order> orders = new ArrayList<>();

    ProgressBar progressBar;
    private String token;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressHUD = new KProgressHUD(requireContext());
        viewModel = ViewModelProviders.of(this).get(HoneyViewModel.class);

        progressBar = view.findViewById(R.id.history_progressbar);
        recyclerView = view.findViewById(R.id.history_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new HistoryAdapter(histories, orders, this);

        if (viewModel.getUser()!=null){
            token = viewModel.getUser().getToken();
        }

        orders = viewModel.getHistoryItems();

        token = "token "+token;

        viewModel.getHistory().observe(getViewLifecycleOwner(), new Observer<List<History>>() {
            @Override
            public void onChanged(List<History> histories) {
                progressBar.setVisibility(View.GONE);
                adapter = new HistoryAdapter(histories, orders, HistoryFragment.this);
                recyclerView.setAdapter(adapter);
            }
        });

        viewModel.getHistoryItemsLive().observe(getViewLifecycleOwner(), new Observer<List<Order>>() {
            @Override
            public void onChanged(List<Order> orders) {
                adapter.setOrdersList(orders);
            }
        });

    }



    @Override
    public void onStart() {
        super.onStart();
        viewModel.loadHistory(token);
    }

    @Override
    public void onItemClick(History history) {

    }
}