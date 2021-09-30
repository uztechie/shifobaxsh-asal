package uz.techie.shifobaxshasaluz.adapter;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import uz.nisd.asalsavdosi.R;
import uz.techie.shifobaxshasaluz.models.News;


public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    List<News> newsList = new ArrayList<>();
    NewsInterface newsInterface;

    public NewsAdapter(List<News> newsList, NewsInterface newsInterface) {
        this.newsList = newsList;
        this.newsInterface = newsInterface;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        holder.tvTitle.setText(Html.fromHtml(newsList.get(position).getName()));
        holder.tvDesc.setText(Html.fromHtml(newsList.get(position).getFull_desc()));
        holder.tvDate.setText(newsList.get(position).getPublished_date());

        Picasso.get()
                .load(newsList.get(position).getPhoto())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvTitle, tvDate, tvDesc;
        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.adapter_news_image);
            tvTitle = itemView.findViewById(R.id.adapter_news_title);
            tvDesc = itemView.findViewById(R.id.adapter_news_desc);
            tvDate = itemView.findViewById(R.id.adapter_news_date);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    newsInterface.onItemClick(newsList.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface NewsInterface{
        void onItemClick(News news);
    }


}
