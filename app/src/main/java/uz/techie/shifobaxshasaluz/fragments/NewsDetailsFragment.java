package uz.techie.shifobaxshasaluz.fragments;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import uz.nisd.asalsavdosi.R;
import uz.techie.shifobaxshasaluz.fragments.NewsDetailsFragmentArgs;
import uz.techie.shifobaxshasaluz.models.News;
import uz.techie.shifobaxshasaluz.room.HoneyViewModel;


public class NewsDetailsFragment extends Fragment {
    KProgressHUD progressHUD;
    HoneyViewModel viewModel;
    ImageView imageView;
    TextView tvDate, tvTitle, tvDesc;
    int id = -1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(HoneyViewModel.class);
        progressHUD = new KProgressHUD(requireContext());
        tvDate = view.findViewById(R.id.news_details_date);
        tvTitle = view.findViewById(R.id.news_details_title);
        tvDesc = view.findViewById(R.id.news_details_desc);
        imageView = view.findViewById(R.id.news_details_image);

        if (getArguments() != null){
            id = NewsDetailsFragmentArgs.fromBundle(getArguments()).getId();
        }

        viewModel.getSingleNews(id).observe(getViewLifecycleOwner(), new Observer<News>() {
            @Override
            public void onChanged(News news) {
                if (news != null){
                    tvDate.setText(news.getPublished_date());
                    tvTitle.setText(Html.fromHtml(news.getName()));
                    tvDesc.setText(Html.fromHtml(news.getFull_desc()));

                    Picasso.get()
                            .load(news.getPhoto())
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .into(imageView);
                }
            }
        });
    }
}