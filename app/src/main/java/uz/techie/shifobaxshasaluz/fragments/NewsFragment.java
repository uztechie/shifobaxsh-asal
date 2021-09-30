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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kaopiz.kprogresshud.KProgressHUD;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import uz.nisd.asalsavdosi.R;
import uz.techie.shifobaxshasaluz.fragments.NewsFragmentDirections;
import uz.techie.shifobaxshasaluz.adapter.NewsAdapter;
import uz.techie.shifobaxshasaluz.models.News;
import uz.techie.shifobaxshasaluz.room.HoneyViewModel;


public class NewsFragment extends Fragment implements NewsAdapter.NewsInterface {
    KProgressHUD progressHUD;
    RecyclerView recyclerView;
    NewsAdapter adapter;
    HoneyViewModel viewModel;
    ProgressBar progressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(HoneyViewModel.class);
        progressHUD = new KProgressHUD(requireContext());
        progressBar = view.findViewById(R.id.news_progressbar);
        recyclerView = view.findViewById(R.id.news_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));


        viewModel.getNewsLive().observe(getViewLifecycleOwner(), new Observer<List<News>>() {
            @Override
            public void onChanged(List<News> news) {
                if (news.isEmpty()){
                    progressBar.setVisibility(View.VISIBLE);
                }
                else {
                    progressBar.setVisibility(View.GONE);
                    adapter = new NewsAdapter(news, NewsFragment.this);
                    recyclerView.setAdapter(adapter);
                }
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.loadNews();
    }

    @Override
    public void onItemClick(News news) {
        Navigation.findNavController(requireView())
                .navigate(NewsFragmentDirections.actionNewsFragmentToNewsDetailsFragment(news.getId(), news.getName()));
    }
}