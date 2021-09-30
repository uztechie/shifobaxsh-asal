package uz.techie.shifobaxshasaluz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import uz.nisd.asalsavdosi.R;
import uz.techie.shifobaxshasaluz.models.Seller;


public class SellerDropDownAdapter extends ArrayAdapter<Seller> {
    List<Seller> sellersFullList = new ArrayList<>();

    public SellerDropDownAdapter(@NonNull Context context, List<Seller>  sellers) {
        super(context, R.layout.dropdown_adapter, sellers);
        sellersFullList = new ArrayList<>(sellers);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

//    @Override
//    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return initView(position, convertView, parent);
//    }


    private View initView(int position, View convertView, ViewGroup parent){
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.dropdown_adapter, parent, false);
        }
        TextView tvTitle = convertView.findViewById(R.id.dropdown_adapter_title);
        TextView tvPhone = convertView.findViewById(R.id.dropdown_adapter_phone);
        String name = getItem(position).getName();
        String phone = getItem(position).getPhone();


        tvTitle.setText(name);
        tvPhone.setText(phone);
        return convertView;

    }

    @NonNull
    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            List<Seller> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(sellersFullList);
            }
            else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Seller seller:sellersFullList){
                    String name = seller.getName().toString().toLowerCase().trim();
                    String phone = seller.getPhone().toString().toLowerCase().trim();

                    if (name.contains(filterPattern) || phone.contains(filterPattern)){
                        filteredList.add(seller);
                    }
                }
            }
            filterResults.values = filteredList;
            filterResults.count = filteredList.size();
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List) results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String name = ((Seller)resultValue).getName();
            String phone = ((Seller)resultValue).getPhone();
            return name;
        }
    };

}
